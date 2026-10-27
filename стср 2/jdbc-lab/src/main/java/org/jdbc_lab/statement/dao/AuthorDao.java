package org.jdbc_lab.statement.dao;

import org.jdbc_lab.connection.ConnectionManager;
import org.jdbc_lab.statement.model.Author;
import org.jdbc_lab.statement.model.AuthorBooksCount;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class AuthorDao implements StatementDao<Author> {
    private static final Logger logger = Logger.getLogger(AuthorDao.class.getName());

    @Override
    public void clearTable() {
        try (Statement stmt = ConnectionManager.getConnection().createStatement()) {
            stmt.executeUpdate("DELETE FROM authors");
        } catch (SQLException e) {
            logger.severe("Ошибка: " + e.getMessage());

            throw new RuntimeException(e);
        }
    }

    @Override
    public void insert(Author author) {
        try (Statement stmt = ConnectionManager.getConnection().createStatement()) {
            stmt.executeUpdate("INSERT INTO authors (first_name, last_name) " +
                    "VALUES ('" + author.getFirstName() + "', '" + author.getLastName() + "')");
        } catch (SQLException e) {
            logger.severe("Ошибка: " + e.getMessage());

            throw new RuntimeException(e);
        }
    }

    @Override
    public int getCount() {
        int count = 0;

        try (Statement stmt = ConnectionManager.getConnection().createStatement()) {
            ResultSet resultSet = stmt.executeQuery("SELECT COUNT(*) FROM authors");

            if (resultSet.next()) {
                count = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            logger.severe("Ошибка: " + e.getMessage());

            throw new RuntimeException(e);
        }

        return count;
    }

    @Override
    public List<Author> getAll() {
        List<Author> authors = new ArrayList<>();

        try (Statement stmt = ConnectionManager.getConnection().createStatement()) {
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM authors");

            while (resultSet.next()) {
                Author author = new Author();
                author.setId(resultSet.getLong("id"));
                author.setFirstName(resultSet.getString("first_name"));
                author.setLastName(resultSet.getString("last_name"));
                authors.add(author);
            }
        } catch (SQLException e) {
            logger.severe("Ошибка: " + e.getMessage());

            throw new RuntimeException(e);
        }

        return authors;
    }

    @Override
    public Author getById(Long id) {
        Author author = null;

        try (Statement stmt = ConnectionManager.getConnection().createStatement()) {
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM authors WHERE id = " + id);

            if (resultSet.next()) {
                author = new Author();
                author.setId(resultSet.getLong("id"));
                author.setFirstName(resultSet.getString("first_name"));
                author.setLastName(resultSet.getString("last_name"));
            }
        } catch (SQLException e) {
            logger.severe("Ошибка: " + e.getMessage());

            throw new RuntimeException(e);
        }

        return author;
    }

    // Задание 2: выборка авторов, отсортированных по имени, затем по фамилии
    public List<Author> getAllSortedByFirstNameAndLastName() {
        List<Author> authors = new ArrayList<>();

        try (Statement stmt = ConnectionManager.getConnection().createStatement()) {
            ResultSet resultSet = stmt.executeQuery(
                    "SELECT * FROM authors ORDER BY first_name, last_name");

            while (resultSet.next()) {
                Author author = new Author();
                author.setId(resultSet.getLong("id"));
                author.setFirstName(resultSet.getString("first_name"));
                author.setLastName(resultSet.getString("last_name"));
                authors.add(author);
            }
        } catch (SQLException e) {
            logger.severe("Ошибка: " + e.getMessage());

            throw new RuntimeException(e);
        }

        return authors;
    }

    // Задание 6: обновление имени всех авторов, чьи книги находятся в определённом магазине.
    // Подзапрос находит id таких авторов по цепочке shops -> shops_books -> books
    public int updateFirstNameOfAuthorsWithBooksInShop(final String shopName, final String newFirstName) {
        int updatedRows;

        try (Statement stmt = ConnectionManager.getConnection().createStatement()) {
            updatedRows = stmt.executeUpdate("UPDATE authors SET first_name = '" + newFirstName + "' " +
                    "WHERE id IN (" +
                    "SELECT b.author_id FROM books b " +
                    "JOIN shops_books sb ON sb.book_id = b.id " +
                    "JOIN shops s ON s.id = sb.shop_id " +
                    "WHERE s.name = '" + shopName + "')");
        } catch (SQLException e) {
            logger.severe("Ошибка: " + e.getMessage());

            throw new RuntimeException(e);
        }

        return updatedRows;
    }

    // Задание 7: количество книг каждого автора.
    // Для каждого автора коррелированный подзапрос считает его книги
    public List<AuthorBooksCount> getBookCounts() {
        List<AuthorBooksCount> result = new ArrayList<>();

        try (Statement stmt = ConnectionManager.getConnection().createStatement()) {
            ResultSet resultSet = stmt.executeQuery(
                    "SELECT a.id, a.first_name, a.last_name, " +
                            "(SELECT COUNT(*) FROM books b WHERE b.author_id = a.id) AS books_count " +
                            "FROM authors a ORDER BY a.id");

            while (resultSet.next()) {
                result.add(new AuthorBooksCount(
                        resultSet.getLong("id"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getInt("books_count")
                ));
            }
        } catch (SQLException e) {
            logger.severe("Ошибка: " + e.getMessage());

            throw new RuntimeException(e);
        }

        return result;
    }
}
