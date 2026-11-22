package by.bsuir.lab5.repository.specification;
import java.sql.*;
public interface Specification { String where(); void bind(PreparedStatement statement) throws SQLException; }
