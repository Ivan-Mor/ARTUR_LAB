package com.hibernate.xmlbased.menu;

import java.util.Scanner;

import com.hibernate.xmlbased.dao.DepartmentDAO;
import com.hibernate.xmlbased.dao.DeveloperDAO;
import com.hibernate.xmlbased.dao.UserDAO;
import com.hibernate.xmlbased.model.User;

public class Menu {
    User authUser;

    // общий Scanner на весь ввод приложения: несколько Scanner'ов на одном System.in
    // буферизуют данные независимо и "съедают" строки друг друга
    public static final Scanner in = new Scanner(System.in);

    static final DeveloperDAO developerDAO = new DeveloperDAO();
    static final UserDAO userDAO = new UserDAO();
    static final DepartmentDAO departmentDAO = new DepartmentDAO();

    public void menu(User authUser) {
        this.authUser = authUser;
        System.out.printf("\nHi, %s. Your role: %s\n", authUser.getDeveloper().getName(), authUser.getUserRole().getRole());
    }
}
