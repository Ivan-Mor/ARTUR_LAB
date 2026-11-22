package by.bsuir.lab5.repository;
import by.bsuir.lab5.exception.RepositoryException;
import by.bsuir.lab5.model.Developer;
import java.sql.*;
import java.util.*;
public class DeveloperRepository extends Repository {
    public List<Developer> findAll() throws RepositoryException { return execute(c->{
        List<Developer> list=new ArrayList<>();
        try(PreparedStatement s=c.prepareStatement("SELECT d.id,d.name,d.specialty,t.name AS department FROM developers d JOIN departments t ON t.id=d.department_id ORDER BY d.name");ResultSet r=s.executeQuery()){
            while(r.next()) list.add(new Developer(r.getInt("id"),r.getString("name"),r.getString("specialty"),r.getString("department")));
        } return list;
    }); }
}
