package by.bsuir.lab5.connection;
import java.sql.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.logging.*;
import by.bsuir.lab5.exception.RepositoryException;

/** One pool per deployed application. Always return a borrowed connection in finally. */
public final class ConnectionPool implements AutoCloseable {
    private static final Logger LOG = Logger.getLogger(ConnectionPool.class.getName());
    private static ConnectionPool instance;
    private static boolean terminated;
    private static final class Slot { Connection connection; }
    private final BlockingQueue<Slot> free = new ArrayBlockingQueue<>(5);
    private final Map<Connection, Slot> borrowed = new IdentityHashMap<>();
    private final String url, user, password;
    private boolean closed;

    private ConnectionPool() throws RepositoryException {
        url = System.getenv("LAB5_DB_URL"); user = System.getenv("LAB5_DB_USER"); password = System.getenv("LAB5_DB_PASSWORD");
        if (url == null || url.isBlank() || user == null || user.isBlank() || password == null)
            throw new RepositoryException("Задайте LAB5_DB_URL, LAB5_DB_USER и LAB5_DB_PASSWORD");
        if (url.matches("(?i).*[?&;](connectTimeout|socketTimeout)=.*"))
            throw new RepositoryException("Не задавайте connectTimeout и socketTimeout в LAB5_DB_URL");
        try { Class.forName("com.mysql.cj.jdbc.Driver"); }
        catch (ClassNotFoundException e) { throw new RepositoryException("Драйвер MySQL не найден", e); }
        for (int i = 0; i < 5; i++) free.add(new Slot());
    }
    private Connection open() throws SQLException {
        Properties properties = new Properties();
        properties.setProperty("user", user);
        properties.setProperty("password", password);
        properties.setProperty("connectTimeout", "5000");
        properties.setProperty("socketTimeout", "5000");
        return DriverManager.getConnection(url, properties);
    }
    private static void closeConnection(Connection c) {
        if (c != null) try { c.close(); } catch (SQLException e) { LOG.warning("Ошибка закрытия соединения"); }
    }
    public static synchronized ConnectionPool getInstance() throws RepositoryException {
        if (terminated) throw new RepositoryException("Пул остановлен");
        if (instance == null) instance = new ConnectionPool();
        return instance;
    }
    public Connection acquire() throws RepositoryException {
        synchronized (this) { if (closed) throw new RepositoryException("Пул закрыт"); }
        Slot slot;
        try {
            slot = free.poll(10, TimeUnit.SECONDS);
            if (slot == null) throw new RepositoryException("Истекло время ожидания свободного слота");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); throw new RepositoryException("Ожидание соединения прервано", e);
        }
        boolean alreadyClosed;
        synchronized (this) { alreadyClosed = closed; }
        if (alreadyClosed) { closeConnection(slot.connection); throw new RepositoryException("Пул закрыт"); }
        try {
            boolean valid;
            try { valid = slot.connection != null && !slot.connection.isClosed() && slot.connection.isValid(2); }
            catch (SQLException e) { valid = false; }
            if (!valid) { closeConnection(slot.connection); slot.connection = null; slot.connection = open(); }
        } catch (SQLException e) {
            synchronized (this) { if (!closed) free.offer(slot); }
            throw new RepositoryException("Не удалось восстановить соединение с базой данных", e);
        }
        synchronized (this) {
            alreadyClosed = closed;
            if (!alreadyClosed) borrowed.put(slot.connection, slot);
        }
        if (alreadyClosed) { closeConnection(slot.connection); throw new RepositoryException("Пул закрыт"); }
        return slot.connection;
    }
    public void release(Connection c) {
        if (c == null) return;
        Slot slot;
        boolean close;
        synchronized (this) {
            slot = borrowed.remove(c);
            if (slot == null) { LOG.warning("Неизвестное или уже возвращённое соединение"); return; }
            close = closed || !free.offer(slot);
        }
        if (close) closeConnection(c);
    }
    private synchronized List<Connection> markClosed() {
        closed = true;
        List<Connection> connections = new ArrayList<>();
        Slot slot;
        while ((slot = free.poll()) != null) connections.add(slot.connection);
        return connections;
    }
    public void close() { for (Connection c : markClosed()) closeConnection(c); }
    public static void shutdown() {
        List<Connection> connections;
        synchronized (ConnectionPool.class) {
            terminated = true;
            connections = instance == null ? Collections.emptyList() : instance.markClosed();
        }
        for (Connection c : connections) closeConnection(c);
    }
}
