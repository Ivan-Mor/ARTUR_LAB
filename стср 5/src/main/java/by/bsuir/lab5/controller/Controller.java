package by.bsuir.lab5.controller;
import by.bsuir.lab5.exception.*;
import by.bsuir.lab5.model.*;
import by.bsuir.lab5.service.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.*;
import java.util.logging.*;

public class Controller extends HttpServlet {
    private static final Logger LOG=Logger.getLogger(Controller.class.getName());
    private final UserService users=new UserService(); private final ProjectService projects=new ProjectService();
    private final Map<String,Command> commands=new HashMap<>();
    @Override public void init() {
        commands.put("login",(r,s)->{var found=users.login(r.getParameter("username"),r.getParameter("password"));if(found.isEmpty())throw new IncorrectDataException("Неверный логин или пароль.");
            HttpSession old=r.getSession(false);if(old!=null)old.invalidate(); HttpSession session=r.getSession(true);User u=found.get();session.setAttribute("username",u.getUsername());session.setAttribute("role",u.getRole());byte[] token=new byte[32];new SecureRandom().nextBytes(token);session.setAttribute("csrf",Base64.getUrlEncoder().withoutPadding().encodeToString(token));return "redirect:projects";});
        commands.put("register",(r,s)->{if(!users.register(r.getParameter("username"),r.getParameter("password")))throw new IncorrectDataException("Логин уже занят.");r.setAttribute("message","Регистрация выполнена. Войдите в систему.");return "login";});
        commands.put("logout",(r,s)->{r.getSession().invalidate();return "redirect:login";});
        commands.put("save",(r,s)->{int id=number(r.getParameter("id"),true), developer=number(r.getParameter("developerId"),false);if(!projects.save(id,r.getParameter("name"),r.getParameter("description"),developer))throw new IncorrectDataException("Проект не найден.");return "redirect:projects";});
        commands.put("delete",(r,s)->{if(!projects.delete(number(r.getParameter("id"),false)))throw new IncorrectDataException("Проект не найден.");return "redirect:projects";});
    }
    private static int number(String s,boolean zero) throws IncorrectDataException {try{int n=Integer.parseInt(s);if(n<0 || (!zero && n==0))throw new NumberFormatException();return n;}catch(NumberFormatException e){throw new IncorrectDataException("Некорректный идентификатор.",e);}}
    private static boolean loggedIn(HttpSession session){return session!=null && session.getAttribute("username")!=null;}
    private static boolean admin(HttpSession session){return loggedIn(session) && "ADMIN".equals(session.getAttribute("role"));}
    private void view(HttpServletRequest r,HttpServletResponse s,String page) throws ServletException,IOException {r.getRequestDispatcher("/WEB-INF/views/"+page+".jsp").forward(r,s);}
    @Override protected void doGet(HttpServletRequest r,HttpServletResponse s) throws ServletException,IOException {
        r.setCharacterEncoding("UTF-8");String action=Objects.toString(r.getParameter("action"),"projects");HttpSession session=r.getSession(false);
        if("login".equals(action)||"register".equals(action)){view(r,s,action);return;}
        if(!loggedIn(session)){s.sendRedirect(r.getContextPath()+"/controller?action=login");return;}
        try {
            if("projects".equals(action)){r.setAttribute("projects",projects.all());view(r,s,"projects");}
            else if("developers".equals(action)){r.setAttribute("developers",projects.developers());view(r,s,"developers");}
            else if("edit".equals(action) && admin(session)){String raw=r.getParameter("id");if(raw!=null){Optional<Project> item=projects.one(number(raw,false));if(item.isEmpty()){s.sendError(404);return;}r.setAttribute("project",item.get());}r.setAttribute("developers",projects.developers());view(r,s,"edit");}
            else s.sendError(404);
        } catch(ServiceException e){LOG.log(Level.SEVERE,"Ошибка загрузки данных",e);s.sendError(500,"Не удалось загрузить данные.");}
          catch(IncorrectDataException e){s.sendError(400,e.getMessage());}
    }
    @Override protected void doPost(HttpServletRequest r,HttpServletResponse s) throws ServletException,IOException {
        r.setCharacterEncoding("UTF-8");String action=Objects.toString(r.getParameter("action"),"");HttpSession session=r.getSession(false);
        if(!action.equals("login")&&!action.equals("register")){
            if(!loggedIn(session)){s.sendError(401);return;}
            if((action.equals("save")||action.equals("delete"))&&!admin(session)){s.sendError(403);return;}
            Object token=session.getAttribute("csrf");if(token==null||!token.equals(r.getParameter("csrf"))){s.sendError(403);return;}
        }
        Command cmd=commands.get(action);if(cmd==null){s.sendError(404);return;}
        try {String result=cmd.execute(r,s);if(result.startsWith("redirect:"))s.sendRedirect(r.getContextPath()+"/controller?action="+result.substring(9));else view(r,s,result);}
        catch(IncorrectDataException e){r.setAttribute("error",e.getMessage());if(action.equals("login")||action.equals("register"))view(r,s,action);else{s.setStatus(400);view(r,s,"error");}}
        catch(ServiceException e){LOG.log(Level.SEVERE,"Ошибка обработки запроса",e);s.setStatus(500);r.setAttribute("error","Ошибка работы с данными. Обратитесь к администратору.");view(r,s,"error");}
    }
}
