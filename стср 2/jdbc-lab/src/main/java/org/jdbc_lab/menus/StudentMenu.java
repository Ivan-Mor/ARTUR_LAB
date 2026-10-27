package org.jdbc_lab.menus;

import org.jdbc_lab.prepared_statement.action.PreparedStatementAction;
import org.jdbc_lab.utils.InputManager;
import org.jdbc_lab.utils.MenuUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class StudentMenu {
    private static final Logger logger = Logger.getLogger(StudentMenu.class.getName());

    private final PreparedStatementAction preparedStatementAction = new PreparedStatementAction();

    public void print() {
        List<String> options = new ArrayList<>();
        options.add("Добавить студента");
        options.add("Обновить студента");
        options.add("Удалить студента");
        options.add("Найти по id");
        options.add("Найти по имени");
        options.add("Найти по фамилии");
        options.add("Найти по городу");
        options.add("Найти по email");
        options.add("Найти по возрасту");
        options.add("Отобразить всех студентов");

        final String header = MenuUtils.getHeader("Меню студентов", options);

        while (true) {
            System.out.print(header);

            try {
                switch (InputManager.getNextInt()) {
                    case 1: {
                        preparedStatementAction.insertStudent();
                        break;
                    }
                    case 2: {
                        preparedStatementAction.updateStudent();
                        break;
                    }
                    case 3: {
                        preparedStatementAction.deleteStudent();
                        break;
                    }
                    case 4: {
                        preparedStatementAction.getStudentById();
                        break;
                    }
                    case 5: {
                        preparedStatementAction.getStudentsByFirstName();
                        break;
                    }
                    case 6: {
                        preparedStatementAction.getStudentsByLastName();
                        break;
                    }
                    case 7: {
                        preparedStatementAction.getStudentsByCity();
                        break;
                    }
                    case 8: {
                        preparedStatementAction.getStudentsByEmail();
                        break;
                    }
                    case 9: {
                        preparedStatementAction.getStudentsByAge();
                        break;
                    }
                    case 10: {
                        preparedStatementAction.getAllStudents();
                        break;
                    }
                    case 0: {
                        return;
                    }
                    default: {
                        System.out.println("Такого варианта нет!");
                        break;
                    }
                }
            } catch (Exception e) {
                logger.severe("Ошибка при обработке меню студентов: " + e.getMessage());
            }
        }
    }
}
