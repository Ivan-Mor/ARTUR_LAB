package com.hibernate.xmlbased;

import java.util.Scanner;

import com.hibernate.xmlbased.config.SessionConfig;
import com.hibernate.xmlbased.dao.UserDAO;
import com.hibernate.xmlbased.menu.AdminMenu;
import com.hibernate.xmlbased.menu.Menu;
import com.hibernate.xmlbased.menu.UserMenu;
import com.hibernate.xmlbased.model.User;
import jakarta.persistence.NoResultException;

public class MainClass {
    static final UserDAO userDAO = new UserDAO();

    public static void main(String[] args) {
        Menu menu;
        while (true) {
            System.out.print("Username:");
            String username = Menu.in.nextLine();
            System.out.print("Password:");
            String password = Menu.in.nextLine();
            User user;
            try {
                user = userDAO.AuthUser(username, password);
            } catch (NoResultException ex) {
                System.out.println("Bad credentials");
                continue;
            }
            switch (user.getUserRole()) {
                case ROLE_ADMIN:
                    menu = new AdminMenu();
                    break;
                case ROLE_USER:
                    menu = new UserMenu();
                    break;
                default:
                    return;
            }
            menu.menu(user);
            break;
        }
        // освобождаем пул соединений, чтобы приложение корректно завершилось
        SessionConfig.getInstanceOfSeccionFactory().getSessionFactory().close();
    }
}
