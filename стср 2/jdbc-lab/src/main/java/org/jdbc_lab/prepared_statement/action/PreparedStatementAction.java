package org.jdbc_lab.prepared_statement.action;

import org.jdbc_lab.prepared_statement.dao.StudentDao;
import org.jdbc_lab.prepared_statement.model.Student;
import org.jdbc_lab.utils.InputManager;

import java.util.List;

public class PreparedStatementAction {
    StudentDao studentDao = new StudentDao();

    public void insertStudent() {
        Student student = Student.getStudentFromInput();

        studentDao.insert(student); // Если произойдёт исключение, отработает блок catch из меню

        System.out.println("Студент добавлен успешно!");
    }

    public void updateStudent() {
        System.out.print("Введи id студента: ");

        Student student = studentDao.getById(InputManager.getNextLong());

        if (student == null) {
            System.out.println("Студент с введённым id не найден!");
        } else {
            Student newStudent = Student.getStudentFromInput();
            newStudent.setId(student.getId());

            studentDao.update(newStudent);

            System.out.println("Студент обновлён успешно!");
        }
    }

    public void deleteStudent() {
        System.out.print("Введи id студента: ");

        Student student = studentDao.getById(InputManager.getNextLong());

        if (student == null) {
            System.out.println("Студент с введённым id не найден!");
        } else {
            studentDao.delete(student.getId());

            System.out.println("Студент удалён успешно!");
        }
    }

    public void getStudentById() {
        System.out.print("Введи id студента: ");

        Student student = studentDao.getById(InputManager.getNextLong());

        if (student == null) {
            System.out.println("Студент с введённым id не найден!");
        } else {
            System.out.println("[Студент по id]\n" + student);
        }
    }

    public void getStudentsByFirstName() {
        System.out.print("Введи имя (или его часть): ");

        List<Student> students = studentDao.getByFirstName(InputManager.getNextLine());

        if (students.isEmpty()) {
            System.out.println("Не найдено ни одного студента с похожим именем!");
            return;
        }

        System.out.println("[Студенты по имени]");
        for (Student student : students) {
            System.out.println(student);
        }
    }

    public void getStudentsByLastName() {
        System.out.print("Введи фамилию (или её часть): ");

        List<Student> students = studentDao.getByLastName(InputManager.getNextLine());

        if (students.isEmpty()) {
            System.out.println("Не найдено ни одного студента с похожей фамилией!");
            return;
        }

        System.out.println("[Студенты по фамилии]");
        for (Student student : students) {
            System.out.println(student);
        }
    }

    public void getStudentsByCity() {
        System.out.print("Введи название города (или его часть): ");

        List<Student> students = studentDao.getByCity(InputManager.getNextLine());

        if (students.isEmpty()) {
            System.out.println("Не найдено ни одного студента из города с похожим наименованием!");
            return;
        }

        System.out.println("[Студенты по городу]");
        for (Student student : students) {
            System.out.println(student);
        }
    }

    public void getStudentsByEmail() {
        System.out.print("Введи электронную почту (или её часть): ");

        List<Student> students = studentDao.getByEmail(InputManager.getNextLine());

        if (students.isEmpty()) {
            System.out.println("Не найдено ни одного студента с похожей электронной почтой!");
            return;
        }

        System.out.println("[Студенты по электронной почте]");
        for (Student student : students) {
            System.out.println(student);
        }
    }

    public void getStudentsByAge() {
        int start;
        int end;

        while (true) {
            System.out.print("Введи начальный возраст для поиска: ");
            start = InputManager.getNextIntInRange(17, 71);
            System.out.print("Введи конечный возраст для поиска: ");
            end = InputManager.getNextIntInRange(17, 71);

            if (start > end) {
                System.out.println("Начальный возраст не может быть > конечного!");
            } else {
                break;
            }
        }

        List<Student> students = studentDao.getByAge(start, end);

        if (students.isEmpty()) {
            System.out.println("Не найдено ни одного студента в выбранном возрастном диапазоне!");
            return;
        }

        System.out.println("[Студенты по возрасту]");
        for (Student student : students) {
            System.out.println(student);
        }
    }

    public void getAllStudents () {
        List<Student> students = studentDao.getAll();

        if (students.isEmpty()) {
            System.out.println("Нет ни одного студента!");
            return;
        }

        System.out.println("[Все студенты]");
        for (Student student : students) {
            System.out.println(student);
        }
    }
}
