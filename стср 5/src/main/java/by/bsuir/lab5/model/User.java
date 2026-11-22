package by.bsuir.lab5.model;
import java.io.Serializable;
public class User implements Serializable {
    private final int id; private final String username, passwordHash, role;
    public User(int id,String username,String passwordHash,String role){this.id=id;this.username=username;this.passwordHash=passwordHash;this.role=role;}
    public int getId(){return id;} public String getUsername(){return username;} public String getPasswordHash(){return passwordHash;} public String getRole(){return role;}
}
