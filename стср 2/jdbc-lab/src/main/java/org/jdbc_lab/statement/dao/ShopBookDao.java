package org.jdbc_lab.statement.dao;

import org.jdbc_lab.connection.ConnectionManager;
import org.jdbc_lab.statement.model.ShopBook;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ShopBookDao implements StatementDao<ShopBook> {
    private static final Logger logger = Logger.getLogger(ShopBookDao.class.getName());

    @Override
    public void clearTable() {
        try (Statement stmt = ConnectionManager.getConnection().createStatement()) {
            stmt.executeUpdate("DELETE FROM shops_books");
        } catch (SQLException e) {
            logger.severe("Ошибка: " + e.getMessage());

            throw new RuntimeException(e);
        }
    }

    @Override
    public void insert(ShopBook shopBook) {
        try (Statement stmt = ConnectionManager.getConnection().createStatement()) {
            stmt.executeUpdate("INSERT INTO shops_books (shop_id, book_id) " +
                    "VALUES (" + shopBook.getShopId() + ", " + shopBook.getBookId() + ")");
        } catch (SQLException e) {
            logger.severe("Ошибка: " + e.getMessage());

            throw new RuntimeException(e);
        }
    }

    @Override
    public int getCount() {
        int count = 0;

        try (Statement stmt = ConnectionManager.getConnection().createStatement()) {
            ResultSet resultSet = stmt.executeQuery("SELECT COUNT(*) FROM shops_books");

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
    public List<ShopBook> getAll() {
        List<ShopBook> shopBooks = new ArrayList<>();

        try (Statement stmt = ConnectionManager.getConnection().createStatement()) {
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM shops_books");

            while (resultSet.next()) {
                ShopBook shopBook = new ShopBook();
                shopBook.setId(resultSet.getLong("id"));
                shopBook.setShopId(resultSet.getLong("shop_id"));
                shopBook.setBookId(resultSet.getLong("book_id"));
                shopBooks.add(shopBook);
            }
        } catch (SQLException e) {
            logger.severe("Ошибка: " + e.getMessage());

            throw new RuntimeException(e);
        }

        return shopBooks;
    }

    @Override
    public ShopBook getById(Long id) {
        ShopBook shopBook = null;

        try (Statement stmt = ConnectionManager.getConnection().createStatement()) {
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM shops_books WHERE id = " + id);

            if (resultSet.next()) {
                shopBook = new ShopBook();
                shopBook.setId(resultSet.getLong("id"));
                shopBook.setShopId(resultSet.getLong("shop_id"));
                shopBook.setBookId(resultSet.getLong("book_id"));
            }
        } catch (SQLException e) {
            logger.severe("Ошибка: " + e.getMessage());

            throw new RuntimeException(e);
        }

        return shopBook;
    }
}
