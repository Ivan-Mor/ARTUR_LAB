package by.bsuir.lab5.repository.builder;
import by.bsuir.lab5.model.Project;
import java.sql.*;
public class ProjectBuilder implements Builder<Project> {
    public Project build(ResultSet r) throws SQLException { return new Project(r.getInt("id"),r.getString("name"),r.getString("description"),r.getInt("developer_id"),r.getString("developer_name")); }
}
