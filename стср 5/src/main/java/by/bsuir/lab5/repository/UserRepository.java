package by.bsuir.lab5.repository;
import by.bsuir.lab5.exception.RepositoryException;
import by.bsuir.lab5.model.User;
import java.sql.*;
import java.util.Optional;
public class UserRepository extends Repository {
    public Optional<User> find(String name) throws RepositoryException { return execute(c->{
        try(PreparedStatement s=c.prepareStatement("SELECT id,username,password_hash,role FROM users WHERE username=?")){s.setString(1,name);try(ResultSet r=s.executeQuery()){return r.next()?Optional.of(new User(r.getInt(1),r.getString(2),r.getString(3),r.getString(4))):Optional.empty();}}
    }); }
    public boolean add(String name,String hash) throws RepositoryException { return execute(c->{
        try(PreparedStatement s=c.prepareStatement("INSERT INTO users(username,password_hash,role) VALUES(?,?,'USER')")){s.setString(1,name);s.setString(2,hash);s.executeUpdate();return true;}
        catch(SQLIntegrityConstraintViolationException e){return false;}
    }); }
}
