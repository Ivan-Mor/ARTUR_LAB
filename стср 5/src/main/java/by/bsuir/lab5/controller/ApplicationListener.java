package by.bsuir.lab5.controller;
import by.bsuir.lab5.connection.ConnectionPool;
import jakarta.servlet.*;
public class ApplicationListener implements ServletContextListener {
    @Override public void contextDestroyed(ServletContextEvent event){ConnectionPool.shutdown();}
}
