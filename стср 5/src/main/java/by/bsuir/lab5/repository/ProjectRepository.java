package by.bsuir.lab5.repository;
import by.bsuir.lab5.exception.RepositoryException;
import by.bsuir.lab5.model.Project;
import by.bsuir.lab5.repository.builder.ProjectBuilder;
import by.bsuir.lab5.repository.specification.Specification;
import java.sql.*;
import java.util.*;
public class ProjectRepository extends Repository {
    private static final String SELECT = "SELECT p.id,p.name,p.description,p.developer_id,d.name AS developer_name FROM projects p JOIN developers d ON d.id=p.developer_id";
    public List<Project> query(Specification spec) throws RepositoryException {return execute(c->{
        List<Project> result=new ArrayList<>();
        try(PreparedStatement s=c.prepareStatement(SELECT+(spec==null?"":spec.where())+" ORDER BY p.id")){
            if(spec!=null) spec.bind(s);
            try(ResultSet r=s.executeQuery()){ProjectBuilder b=new ProjectBuilder();while(r.next()) result.add(b.build(r));}
        } return result;
    });}
    public boolean save(int id,String name,String description,int developerId) throws RepositoryException {return execute(c->{
        String sql=id==0?"INSERT INTO projects(name,description,developer_id) VALUES(?,?,?)":"UPDATE projects SET name=?,description=?,developer_id=? WHERE id=?";
        try(PreparedStatement s=c.prepareStatement(sql)){s.setString(1,name);s.setString(2,description);s.setInt(3,developerId);if(id!=0)s.setInt(4,id);return s.executeUpdate()==1;}
    });}
    public boolean delete(int id) throws RepositoryException {return execute(c->{try(PreparedStatement s=c.prepareStatement("DELETE FROM projects WHERE id=?")){s.setInt(1,id);return s.executeUpdate()==1;}});}
}
