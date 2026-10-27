package org.jdbc_lab.prepared_statement.dao;

import org.jdbc_lab.connection.ConnectionManager;
import org.jdbc_lab.prepared_statement.model.Reader;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * DAO новой сущности "читатель" (работает через PreparedStatement)
 */

public class ReaderDao implements PreparedStatementDao<Reader> {
    private static final Logger logger = Logger.getLogger(ReaderDao.class.getName());

    @Override
    public Reader getById(Long id) {
        Reader reader = null;

        final String query = "SELECT * FROM readers WHERE id = ? LIMIT 1";

        try (PreparedStatement pStmt = ConnectionManager.getConnection().prepareStatement(query)) {
            pStmt.setLong(1, id);

            ResultSet resultSet = pStmt.executeQuery();

            if (resultSet.next()) {
                reader = new Reader();
                reader.setId(resultSet.getLong("id"));
                reader.setFirstName(resultSet.getString("first_name"));
                reader.setLastName(resultSet.getString("last_name"));
                reader.setPhone(resultSet.getString("phone"));
                reader.setCity(resultSet.getString("city"));
                reader.setBirthYear(resultSet.getInt("birth_year"));
            }
        } catch (SQLException e) {
            logger.severe("Ошибка: " + e.getMessage());

            throw new RuntimeException(e);
        }

        return reader;
    }

    @Override
    public void insert(Reader reader) {
        final String query = "INSERT INTO readers (first_name, last_name, phone, city, birth_year) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement pStmt = ConnectionManager.getConnection().prepareStatement(query)) {
            pStmt.setString(1, reader.getFirstName());
            pStmt.setString(2, reader.getLastName());
            pStmt.setString(3, reader.getPhone());
            pStmt.setString(4, reader.getCity());
            pStmt.setInt(5, reader.getBirthYear());
            pStmt.executeUpdate();
        } catch (SQLException e) {
            logger.severe("Ошибка: " + e.getMessage());

            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Reader reader) {
        final String query = "UPDATE readers " +
                "SET first_name = ?, last_name = ?, phone = ?, city = ?, birth_year = ? " +
                "WHERE id = ?";

        try (PreparedStatement pStmt = ConnectionManager.getConnection().prepareStatement(query)) {
            pStmt.setString(1, reader.getFirstName());
            pStmt.setString(2, reader.getLastName());
            pStmt.setString(3, reader.getPhone());
            pStmt.setString(4, reader.getCity());
            pStmt.setInt(5, reader.getBirthYear());
            pStmt.setLong(6, reader.getId());
            pStmt.executeUpdate();
        } catch (SQLException e) {
            logger.severe("Ошибка: " + e.getMessage());

            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Long id) {
        final String query = "DELETE FROM readers WHERE id = ?";

        try (PreparedStatement pStmt = ConnectionManager.getConnection().prepareStatement(query)) {
            pStmt.setLong(1, id);
            pStmt.executeUpdate();
        } catch (SQLException e) {
            logger.severe("Ошибка: " + e.getMessage());

            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Reader> getAll() {
        List<Reader> readers;

        final String query = "SELECT * FROM readers";

        try (PreparedStatement pStmt = ConnectionManager.getConnection().prepareStatement(query)) {
            ResultSet resultSet = pStmt.executeQuery();

            readers = getReaderListByResultSet(resultSet);
        } catch (SQLException e) {
            logger.severe("Ошибка: " + e.getMessage());

            throw new RuntimeException(e);
        }

        return readers;
    }

    // Поиск читателей по фамилии (или её началу)
    public List<Reader> getByLastName(String lastName) {
        List<Reader> readers;

        final String query = "SELECT * FROM readers WHERE last_name ILIKE ?";

        try (PreparedStatement pStmt = ConnectionManager.getConnection().prepareStatement(query)) {
            pStmt.setString(1, lastName + "%");

            ResultSet resultSet = pStmt.executeQuery();
            readers = getReaderListByResultSet(resultSet);
        } catch (SQLException e) {
            logger.severe("Ошибка: " + e.getMessage());

            throw new RuntimeException(e);
        }

        return readers;
    }

    // Поиск читателей по городу (или его началу)
    public List<Reader> getByCity(String city) {
        List<Reader> readers;

        final String query = "SELECT * FROM readers WHERE city ILIKE ?";

        try (PreparedStatement pStmt = ConnectionManager.getConnection().prepareStatement(query)) {
            pStmt.setString(1, city + "%");

            ResultSet resultSet = pStmt.executeQuery();
            readers = getReaderListByResultSet(resultSet);
        } catch (SQLException e) {
            logger.severe("Ошибка: " + e.getMessage());

            throw new RuntimeException(e);
        }

        return readers;
    }

    private List<Reader> getReaderListByResultSet(ResultSet resultSet) throws SQLException {
        List<Reader> readers = new ArrayList<>();

        while (resultSet.next()) {
            Reader reader = new Reader();
            reader.setId(resultSet.getLong("id"));
            reader.setFirstName(resultSet.getString("first_name"));
            reader.setLastName(resultSet.getString("last_name"));
            reader.setPhone(resultSet.getString("phone"));
            reader.setCity(resultSet.getString("city"));
            reader.setBirthYear(resultSet.getInt("birth_year"));
            readers.add(reader);
        }

        return readers;
    }
}
