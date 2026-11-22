package by.bsuir.lab5.repository;
import by.bsuir.lab5.connection.ConnectionPool;
import by.bsuir.lab5.exception.RepositoryException;
import java.sql.*;
public abstract class Repository {
    @FunctionalInterface protected interface Work<T> { T run(Connection connection) throws SQLException; }
    protected <T> T execute(Work<T> work) throws RepositoryException {
        ConnectionPool pool=ConnectionPool.getInstance(); Connection c=pool.acquire();
        try { return work.run(c); } catch(SQLException e) { throw new RepositoryException("Ошибка обращения к базе данных",e); } finally { pool.release(c); }
    }
}
