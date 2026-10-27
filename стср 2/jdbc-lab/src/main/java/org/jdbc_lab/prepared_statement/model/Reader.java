package org.jdbc_lab.prepared_statement.model;

import lombok.Data;
import org.jdbc_lab.utils.InputManager;

/**
 * Новая сущность №2 пакета prepared_statement: читатель
 */

@Data
public class Reader {
    private Long id;
    private String firstName;
    private String lastName;
    private String phone;
    private String city;
    private Integer birthYear;

    public Reader() {}

    public Reader(String firstName, String lastName, String phone, String city, Integer birthYear) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.city = city;
        this.birthYear = birthYear;
    }

    public static Reader getReaderFromInput() {
        Reader reader = new Reader();

        System.out.print("Введи имя: ");
        reader.setFirstName(InputManager.getNextLine());
        System.out.print("Введи фамилию (-, чтобы пропустить): ");
        reader.setLastName(InputManager.getNextLineWithSkip());
        System.out.print("Введи телефон (-, чтобы пропустить): ");
        reader.setPhone(InputManager.getNextLineWithSkip());
        System.out.print("Введи город (-, чтобы пропустить): ");
        reader.setCity(InputManager.getNextLineWithSkip());
        System.out.print("Введи год рождения (1900 - 2015): ");
        reader.setBirthYear(InputManager.getNextIntInRange(1900, 2015));

        return reader;
    }
}
