package org.jdbc_lab.statement.dao;

import org.jdbc_lab.connection.ConnectionManager;
import org.jdbc_lab.statement.model.Shop;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ShopDao implements StatementDao<Shop> {
    private static final Logger logger = Logger.getLogger(ShopDao.class.getName());

    @Override
    public void clearTable() {
        try (Statement stmt = ConnectionManager.getConnection().createStatement()) {
            stmt.executeUpdate("DELETE FROM shops");
        } catch (SQLException e) {
            logger.severe("Ошибка: " + e.getMessage());

            throw new RuntimeException(e);
        }
    }

    @Override
    public void insert(Shop shop) {
        try (Statement stmt = ConnectionManager.getConnection().createStatement()) {
            stmt.executeUpdate("INSERT INTO shops (name, address) " +
                    "VALUES ('" + shop.getName() + "', '" + shop.getAddress() + "')");
        } catch (SQLException e) {
            logger.severe("Ошибка: " + e.getMessage());

            throw new RuntimeException(e);
        }
    }

    @Override
    public int getCount() {
        int count = 0;

        try (Statement stmt = ConnectionManager.getConnection().createStatement()) {
            ResultSet resultSet = stmt.executeQuery("SELECT COUNT(*) FROM shops");

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
    public List<Shop> getAll() {
        List<Shop> shops = new ArrayList<>();

        try (Statement stmt = ConnectionManager.getConnection().createStatement()) {
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM shops");

            while (resultSet.next()) {
                Shop shop = new Shop();
                shop.setId(resultSet.getLong("id"));
                shop.setName(resultSet.getString("name"));
                shop.setAddress(resultSet.getString("address"));
                shops.add(shop);
            }
        } catch (SQLException e) {
            logger.severe("Ошибка: " + e.getMessage());

            throw new RuntimeException(e);
        }

        return shops;
    }

    @Override
    public Shop getById(Long id) {
        Shop shop = null;

        try (Statement stmt = ConnectionManager.getConnection().createStatement()) {
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM shops WHERE id = " + id);

            if (resultSet.next()) {
                shop = new Shop();
                shop.setId(resultSet.getLong("id"));
                shop.setName(resultSet.getString("name"));
                shop.setAddress(resultSet.getString("address"));
            }
        } catch (SQLException e) {
            logger.severe("Ошибка: " + e.getMessage());

            throw new RuntimeException(e);
        }

        return shop;
    }
}
