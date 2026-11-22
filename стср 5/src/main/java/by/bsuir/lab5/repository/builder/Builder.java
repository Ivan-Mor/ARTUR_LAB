package by.bsuir.lab5.repository.builder;
import java.sql.ResultSet;
import java.sql.SQLException;
public interface Builder<T> { T build(ResultSet rs) throws SQLException; }
