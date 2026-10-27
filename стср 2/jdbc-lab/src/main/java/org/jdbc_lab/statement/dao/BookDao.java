package org.jdbc_lab.statement.dao;

import org.jdbc_lab.connection.ConnectionManager;
import org.jdbc_lab.statement.model.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class BookDao implements StatementDao<Book> {
    private static final Logger logger = Logger.getLogger(BookDao.class.getName());

    @Override
    public void clearTable() {
        try (Statement stmt = ConnectionManager.getConnection().createStatement()) {
            stmt.executeUpdate("DELETE FROM books");
        } catch (SQLException e) {
            logger.severe("Ошибка: " + e.getMessage());

            throw new RuntimeException(e);
        }
    }

    @Override
    public void insert(Book book) {
        try (Statement stmt = ConnectionManager.getConnection().createStatement()) {
            stmt.executeUpdate("INSERT INTO books (title, author_id, year_of_publication) " +
                    "VALUES ('" + book.getTitle() + "', '" + book.getAuthorId() + "', '" + book.getYearOfPublication() + "')");
        } catch (SQLException e) {
            logger.severe("Ошибка: " + e.getMessage());

            throw new RuntimeException(e);
        }
    }

    @Override
    public int getCount() {
        int count = 0;

        try (Statement stmt = ConnectionManager.getConnection().createStatement()) {
            ResultSet resultSet = stmt.executeQuery("SELECT COUNT(*) FROM books");

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
    public List<Book> getAll() {
        List<Book> books = new ArrayList<>();

        try (Statement stmt = ConnectionManager.getConnection().createStatement()) {
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM books");

            while (resultSet.next()) {
                Book book = new Book();
                book.setId(resultSet.getLong("id"));
                book.setTitle(resultSet.getString("title"));
                book.setAuthorId(resultSet.getLong("author_id"));
                book.setYearOfPublication(resultSet.getInt("year_of_publication"));
                books.add(book);
            }
        } catch (SQLException e) {
            logger.severe("Ошибка: " + e.getMessage());

            throw new RuntimeException(e);
        }

        return books;
    }

    @Override
    public Book getById(Long id) {
        Book book = null;

        try (Statement stmt = ConnectionManager.getConnection().createStatement()) {
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM books WHERE id = " + id);

            if (resultSet.next()) {
                book = new Book();
                book.setId(resultSet.getLong("id"));
                book.setTitle(resultSet.getString("title"));
                book.setAuthorId(resultSet.getLong("author_id"));
                book.setYearOfPublication(resultSet.getInt("year_of_publication"));
            }
        } catch (SQLException e) {
            logger.severe("Ошибка: " + e.getMessage());

            throw new RuntimeException(e);
        }

        return book;
    }

    // Задание 3: книги, изданные после определённого года, отсортированные по году публикации
    public List<Book> getPublishedAfter(final int year) {
        List<Book> books = new ArrayList<>();

        try (Statement stmt = ConnectionManager.getConnection().createStatement()) {
            ResultSet resultSet = stmt.executeQuery(
                    "SELECT * FROM books WHERE year_of_publication > " + year +
                            " ORDER BY year_of_publication");

            while (resultSet.next()) {
                Book book = new Book();
                book.setId(resultSet.getLong("id"));
                book.setTitle(resultSet.getString("title"));
                book.setAuthorId(resultSet.getLong("author_id"));
                book.setYearOfPublication(resultSet.getInt("year_of_publication"));
                books.add(book);
            }
        } catch (SQLException e) {
            logger.severe("Ошибка: " + e.getMessage());

            throw new RuntimeException(e);
        }

        return books;
    }

    // Задание 4: обновление названия случайной книги, находящейся в определённом магазине.
    // Подзапрос выбирает случайную книгу магазина (ORDER BY RANDOM() LIMIT 1),
    // внешняя часть запроса обновляет её название. Возвращает число затронутых строк
    public int updateRandomBookTitleInShop(final String shopName, final String newTitle) {
        int updatedRows;

        try (Statement stmt = ConnectionManager.getConnection().createStatement()) {
            updatedRows = stmt.executeUpdate("UPDATE books SET title = '" + newTitle + "' " +
                    "WHERE id = (" +
                    "SELECT sb.book_id FROM shops_books sb " +
                    "JOIN shops s ON s.id = sb.shop_id " +
                    "WHERE s.name = '" + shopName + "' " +
                    "ORDER BY RANDOM() LIMIT 1)");
        } catch (SQLException e) {
            logger.severe("Ошибка: " + e.getMessage());

            throw new RuntimeException(e);
        }

        return updatedRows;
    }

    // Задание 5: все книги, чьи авторы имеют фамилию, начинающуюся с определённой буквы
    // (ILIKE — регистронезависимый вариант LIKE в PostgreSQL)
    public List<Book> getByAuthorLastNameLetter(final char letter) {
        List<Book> books = new ArrayList<>();

        try (Statement stmt = ConnectionManager.getConnection().createStatement()) {
            ResultSet resultSet = stmt.executeQuery(
                    "SELECT b.* FROM books b " +
                            "JOIN authors a ON a.id = b.author_id " +
                            "WHERE a.last_name ILIKE '" + letter + "%' " +
                            "ORDER BY b.id");

            while (resultSet.next()) {
                Book book = new Book();
                book.setId(resultSet.getLong("id"));
                book.setTitle(resultSet.getString("title"));
                book.setAuthorId(resultSet.getLong("author_id"));
                book.setYearOfPublication(resultSet.getInt("year_of_publication"));
                books.add(book);
            }
        } catch (SQLException e) {
            logger.severe("Ошибка: " + e.getMessage());

            throw new RuntimeException(e);
        }

        return books;
    }
}
