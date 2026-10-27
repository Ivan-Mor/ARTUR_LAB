package org.jdbc_lab.prepared_statement.model;

import lombok.Data;
import org.jdbc_lab.utils.InputManager;

/**
 * Новая сущность №1 пакета prepared_statement: издательство
 */

@Data
public class Publisher {
    private Long id;
    private String name;
    private String country;
    private Integer foundingYear;
    private String email;

    public Publisher() {}

    public Publisher(String name, String country, Integer foundingYear, String email) {
        this.name = name;
        this.country = country;
        this.foundingYear = foundingYear;
        this.email = email;
    }

    public static Publisher getPublisherFromInput() {
        Publisher publisher = new Publisher();

        System.out.print("Введи название издательства: ");
        publisher.setName(InputManager.getNextLine());
        System.out.print("Введи страну (-, чтобы пропустить): ");
        publisher.setCountry(InputManager.getNextLineWithSkip());
        System.out.print("Введи год основания (1400 - 2026): ");
        publisher.setFoundingYear(InputManager.getNextIntInRange(1400, 2026));
        System.out.print("Введи email (-, чтобы пропустить): ");
        publisher.setEmail(InputManager.getNextLineWithSkip());

        return publisher;
    }
}
