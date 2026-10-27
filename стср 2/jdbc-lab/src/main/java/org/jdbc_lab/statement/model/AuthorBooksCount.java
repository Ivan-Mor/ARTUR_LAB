package org.jdbc_lab.statement.model;

import lombok.Data;

/**
 * Модель для 7-го задания: автор и количество его книг
 * (количество считается подзапросом в AuthorDao.getBookCounts())
 */

@Data
public class AuthorBooksCount {
    private Long id;
    private String firstName;
    private String lastName;
    private Integer booksCount;

    public AuthorBooksCount() {}

    public AuthorBooksCount(Long id, String firstName, String lastName, Integer booksCount) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.booksCount = booksCount;
    }

    @Override
    public String toString() {
        return firstName + " " + lastName + " — книг: " + booksCount;
    }
}
