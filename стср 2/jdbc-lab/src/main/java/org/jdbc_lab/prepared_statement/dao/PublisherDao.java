package org.jdbc_lab.prepared_statement.dao;

import org.jdbc_lab.connection.ConnectionManager;
import org.jdbc_lab.prepared_statement.model.Publisher;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * DAO новой сущности "издательство" (работает через PreparedStatement)
 */

public class PublisherDao implements PreparedStatementDao<Publisher> {
    private static final Logger logger = Logger.getLogger(PublisherDao.class.getName());

    @Override
    public Publisher getById(Long id) {
        Publisher publisher = null;

        final String query = "SELECT * FROM publishers WHERE id = ? LIMIT 1";

        try (PreparedStatement pStmt = ConnectionManager.getConnection().prepareStatement(query)) {
            pStmt.setLong(1, id);

            ResultSet resultSet = pStmt.executeQuery();

            if (resultSet.next()) {
                publisher = new Publisher();
                publisher.setId(resultSet.getLong("id"));
                publisher.setName(resultSet.getString("name"));
                publisher.setCountry(resultSet.getString("country"));
                publisher.setFoundingYear(resultSet.getInt("founding_year"));
                publisher.setEmail(resultSet.getString("email"));
            }
        } catch (SQLException e) {
            logger.severe("Ошибка: " + e.getMessage());

            throw new RuntimeException(e);
        }

        return publisher;
    }

    @Override
    public void insert(Publisher publisher) {
        final String query = "INSERT INTO publishers (name, country, founding_year, email) VALUES (?, ?, ?, ?)";

        try (PreparedStatement pStmt = ConnectionManager.getConnection().prepareStatement(query)) {
            pStmt.setString(1, publisher.getName());
            pStmt.setString(2, publisher.getCountry());
            pStmt.setInt(3, publisher.getFoundingYear());
            pStmt.setString(4, publisher.getEmail());
            pStmt.executeUpdate();
        } catch (SQLException e) {
            logger.severe("Ошибка: " + e.getMessage());

            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Publisher publisher) {
        final String query = "UPDATE publishers " +
                "SET name = ?, country = ?, founding_year = ?, email = ? " +
                "WHERE id = ?";

        try (PreparedStatement pStmt = ConnectionManager.getConnection().prepareStatement(query)) {
            pStmt.setString(1, publisher.getName());
            pStmt.setString(2, publisher.getCountry());
            pStmt.setInt(3, publisher.getFoundingYear());
            pStmt.setString(4, publisher.getEmail());
            pStmt.setLong(5, publisher.getId());
            pStmt.executeUpdate();
        } catch (SQLException e) {
            logger.severe("Ошибка: " + e.getMessage());

            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Long id) {
        final String query = "DELETE FROM publishers WHERE id = ?";

        try (PreparedStatement pStmt = ConnectionManager.getConnection().prepareStatement(query)) {
            pStmt.setLong(1, id);
            pStmt.executeUpdate();
        } catch (SQLException e) {
            logger.severe("Ошибка: " + e.getMessage());

            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Publisher> getAll() {
        List<Publisher> publishers;

        final String query = "SELECT * FROM publishers";

        try (PreparedStatement pStmt = ConnectionManager.getConnection().prepareStatement(query)) {
            ResultSet resultSet = pStmt.executeQuery();

            publishers = getPublisherListByResultSet(resultSet);
        } catch (SQLException e) {
            logger.severe("Ошибка: " + e.getMessage());

            throw new RuntimeException(e);
        }

        return publishers;
    }

    // Поиск издательств по названию (или его началу)
    public List<Publisher> getByName(String name) {
        List<Publisher> publishers;

        final String query = "SELECT * FROM publishers WHERE name ILIKE ?";

        try (PreparedStatement pStmt = ConnectionManager.getConnection().prepareStatement(query)) {
            pStmt.setString(1, name + "%");

            ResultSet resultSet = pStmt.executeQuery();
            publishers = getPublisherListByResultSet(resultSet);
        } catch (SQLException e) {
            logger.severe("Ошибка: " + e.getMessage());

            throw new RuntimeException(e);
        }

        return publishers;
    }

    // Поиск издательств по стране (или её началу)
    public List<Publisher> getByCountry(String country) {
        List<Publisher> publishers;

        final String query = "SELECT * FROM publishers WHERE country ILIKE ?";

        try (PreparedStatement pStmt = ConnectionManager.getConnection().prepareStatement(query)) {
            pStmt.setString(1, country + "%");

            ResultSet resultSet = pStmt.executeQuery();
            publishers = getPublisherListByResultSet(resultSet);
        } catch (SQLException e) {
            logger.severe("Ошибка: " + e.getMessage());

            throw new RuntimeException(e);
        }

        return publishers;
    }

    private List<Publisher> getPublisherListByResultSet(ResultSet resultSet) throws SQLException {
        List<Publisher> publishers = new ArrayList<>();

        while (resultSet.next()) {
            Publisher publisher = new Publisher();
            publisher.setId(resultSet.getLong("id"));
            publisher.setName(resultSet.getString("name"));
            publisher.setCountry(resultSet.getString("country"));
            publisher.setFoundingYear(resultSet.getInt("founding_year"));
            publisher.setEmail(resultSet.getString("email"));
            publishers.add(publisher);
        }

        return publishers;
    }
}
