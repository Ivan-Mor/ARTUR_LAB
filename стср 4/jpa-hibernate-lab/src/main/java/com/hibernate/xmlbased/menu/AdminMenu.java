package com.hibernate.xmlbased.menu;

import java.util.List;
import java.util.Scanner;

import com.hibernate.xmlbased.model.Department;
import com.hibernate.xmlbased.model.Developer;
import com.hibernate.xmlbased.model.User;

public class AdminMenu extends Menu {
    public void menu(User authUser) {
        super.menu(authUser);
        while (true) {
            String options;
            System.out.println("0. Выход");
            System.out.println("1. Посмотреть все департаменты и их сотрудников");
            System.out.println("2. Найти департамент по ID");
            System.out.println("3. Добавить департамент");
            System.out.println("4. Перевести сотрудника в новый департамент");
            System.out.println("5. Удалить департамент (Внимание! Удаляет также связанных сотрудников)");
            System.out.println("6. Найти сотрудника по ID");
            System.out.println("7. Добавить сотрудника");
            System.out.println("8. Удалить сотрудника");
            System.out.println("9. Получить список юзеров");
            System.out.println("10. Найти юзера по никнейму");
            System.out.println("11. Добавить аккаунт сотруднику");
            System.out.println("12. Удалить юзера");
            System.out.println("13. Найти департаменты по первому символу ID (SELECT 1, HQL)");
            System.out.println("14. Найти сотрудников по опыту работы (SELECT 2, HQL)");
            System.out.println("15. Найти сотрудников по опыту работы (SELECT 3, Criteria)");
            try {
                System.out.print("Ввод: ");
                options = in.nextLine();
                switch (options) {
                    case "0":
                        return;
                    case "1":
                        seeAllWorkers();
                        break;
                    case "2":
                        findDepartmentByID();
                        break;
                    case "3":
                        addDepartament();
                        break;
                    case "4":
                        relocateDeveloper();
                        break;
                    case "5":
                        deleteDepartment();
                        break;
                    case "6":
                        findDeveloperByID();
                        break;
                    case "7":
                        addDeveloper();
                        break;
                    case "8":
                        deleteDeveloper();
                        break;
                    case "9":
                        getUsers();
                        break;
                    case "10":
                        getUserByUsername();
                        break;
                    case "11":
                        addUser();
                        break;
                    case "12":
                        deleteUser();
                        break;
                    case "13":
                        findDepartmentByLetterInID();
                        break;
                    case "14":
                        findByExperienceEqual();
                        break;
                    case "15":
                        findByExperienceEqualCriteria();
                        break;
                    default:
                        System.out.println("\nНеверный ввод.");
                }
            } catch (Exception ex) {
                if (ex instanceof java.util.NoSuchElementException) {
                    // ввод закончился (например, Ctrl+D) - корректно завершаем сессию
                    System.out.println("\nВвод завершен.");
                    return;
                }
                System.out.println(ex.getMessage());
            }
        }
    }

    public void seeAllWorkers() {
        List<Department> departments = departmentDAO.getDepartmentWithWorkers();
        for (Department department : departments) {
            System.out.println(department);
            List<Developer> devs = department.getDevelopers();
            for (Developer dev : devs) {
                System.out.println("\t" + dev);
            }
        }
    }

    public void findDepartmentByID() {
        System.out.print("Введите ID департамента: ");
        String depID = in.nextLine();
        Department dep = departmentDAO.findDepartmentByID(depID);
        if (dep == null) {
            throw new NullPointerException("Департамент не найден");
        }
        System.out.println(dep);
    }

    public void addDepartament() {
        System.out.print("Введите ID департамента: ");
        String depID = in.nextLine();
        if (depID.length() != 3) {
            throw new IllegalArgumentException("ID департамента должен быть длиной в три символа.");
        }
        System.out.print("Введите название департамента: ");
        String departmentName = in.nextLine();
        System.out.print("Введите адрес департамента: ");
        String location = in.nextLine();
        Department dep = new Department(depID, departmentName, location);
        if (departmentDAO.addDepartament(dep) == dep) {
            System.out.println("Департамент успешно добавлен.");
        } else {
            System.out.println("Возникла ошибка добавления.");
        }
    }

    public void relocateDeveloper() {
        System.out.print("Введите ID сотрудника: ");
        Integer devID = Integer.valueOf(in.nextLine());
        Developer dev = developerDAO.getDeveloperById(devID);
        if (dev == null) {
            throw new NullPointerException("Сотрудник не найден");
        }
        System.out.print("Введите ID департамента: ");
        String depID = in.nextLine();
        Department department = departmentDAO.findDepartmentByID(depID);
        if (department == null) {
            throw new NullPointerException("Департамент не найден");
        }
        developerDAO.updateDevelopersDepartment(devID, department);
        System.out.println("Сотрудник переведен в новый департамент");
    }

    private void deleteDepartment() {
        System.out.print("Введите ID департамента: ");
        String depID = in.nextLine();
        departmentDAO.deleteDepartment(depID);
        System.out.println("Департамент удален.");
    }

    private void findDeveloperByID() {
        System.out.print("Введите ID работника: ");
        Integer devID = Integer.valueOf(in.nextLine());
        Developer developer = developerDAO.getDeveloperById(devID);
        if (developer == null) {
            throw new NullPointerException("Сотрудник не найден");
        }
        System.out.println(developer);
    }

    private void addDeveloper() {
        System.out.print("Введите имя работника: ");
        String name = in.nextLine();
        System.out.print("Введите специализацию работника: ");
        String specialty = in.nextLine();
        System.out.print("Введите опыт работника: ");
        Integer experience = Integer.valueOf(in.nextLine());
        for (Department department : departmentDAO.getDepartments()) {
            System.out.println(department);
        }
        System.out.print("Введите ID департамента работника: ");
        String depID = in.nextLine();
        Department department = departmentDAO.findDepartmentByID(depID);
        if (department == null) {
            throw new NullPointerException("Департамент не найден");
        }
        Developer dev = new Developer(name, specialty, experience, department);
        developerDAO.addDeveloper(dev);
        System.out.println("Сотрудник успешно добавлен");
    }

    private void deleteDeveloper() {
        System.out.print("Введите ID сотрудника: ");
        Integer devID = Integer.valueOf(in.nextLine());
        Developer dev = developerDAO.getDeveloperById(devID);
        if (dev == null) {
            throw new NullPointerException("Сотрудник не найден");
        }
        developerDAO.removeDeveloper(devID);
        System.out.println("Разработчик успешно удален");
    }

    private void getUsers() {
        List<User> users = userDAO.getUsers();
        for (User user : users) {
            System.out.println(user);
        }
    }

    private void getUserByUsername() {
        System.out.print("Введите username: ");
        String username = in.nextLine();
        System.out.println(userDAO.getUserByUsername(username));
    }

    private void addUser() {
        seeAllWorkers();
        System.out.print("Введите ID сотрудника, для которого мы добавляем аккаунт: ");
        Integer devID = Integer.valueOf(in.nextLine());
        if (developerDAO.getDeveloperById(devID) == null) {
            throw new NullPointerException("Сотрудник не найден");
        }
        System.out.print("Введите username: ");
        String username = in.nextLine();
        System.out.print("Введите password: ");
        String password = in.nextLine();
        System.out.println("Юзер " + userDAO.addUser(new User(devID, username, password)) + " успешно добавлен");
    }

    private void deleteUser() {
        System.out.print("Введите ID аккаунта: ");
        Integer userID = Integer.valueOf(in.nextLine());
        if (userDAO.getUserByID(userID) == null) {
            throw new NullPointerException("Юзер не найден");
        }
        developerDAO.removeDeveloper(userID);
        System.out.println("Разработчик успешно удален");
    }

    private void findDepartmentByLetterInID() {
        System.out.print("Введите первый символ из ID департамента: ");
        String charID = in.nextLine();
        if (charID.length() != 1) {
            throw new NullPointerException("Это не один символ");
        }
        List<Department> deps = departmentDAO.findDepartmentByLetterInID(charID.charAt(0));
        if (deps.isEmpty()) {
            throw new NullPointerException("Департаментов с таким началом ID не найдено");
        }
        for (Department dep : deps) {
            System.out.println(dep);
        }
    }

    private void findByExperienceEqual() {
        System.out.print("Введите опыт работы: ");
        Integer experience = Integer.valueOf(in.nextLine());
        List<Developer> devs = developerDAO.findByExperienceEqual(experience);
        if (devs.isEmpty()) {
            throw new NullPointerException("Сотрудников с таким опытом работы не найдено");
        } else {
            for (Developer dev : devs) {
                System.out.println(dev);
            }
        }
    }

    private void findByExperienceEqualCriteria() {
        System.out.print("Введите опыт работы: ");
        Integer experience = Integer.valueOf(in.nextLine());
        List<Developer> devs = developerDAO.findByExperienceEqualCriteria(experience);
        if (devs.isEmpty()) {
            throw new NullPointerException("Сотрудников с таким опытом работы не найдено");
        } else {
            for (Developer dev : devs) {
                System.out.println(dev);
            }
        }
    }
}
