package by.bsuir.lab5.repository.specification;
import java.sql.*;
public class ProjectById implements Specification {
    private final int id;
    public ProjectById(int id) {this.id=id;}
    public String where(){return " WHERE p.id = ?";}
    public void bind(PreparedStatement statement) throws SQLException {statement.setInt(1,id);}
}
