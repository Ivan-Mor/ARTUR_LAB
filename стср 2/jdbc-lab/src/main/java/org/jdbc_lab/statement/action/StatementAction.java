package org.jdbc_lab.statement.action;

import org.jdbc_lab.statement.dao.AuthorDao;
import org.jdbc_lab.statement.dao.BookDao;
import org.jdbc_lab.statement.dao.ShopBookDao;
import org.jdbc_lab.statement.dao.ShopDao;
import org.jdbc_lab.statement.model.*;
import org.jdbc_lab.utils.InputManager;

import java.util.*;
import java.util.logging.Logger;

public class StatementAction {
    private static final Logger logger = Logger.getLogger(StatementAction.class.getName());

    AuthorDao authorDao = new AuthorDao();
    BookDao bookDao = new BookDao();
    ShopDao shopDao = new ShopDao();
    ShopBookDao shopBookDao = new ShopBookDao();

    public void deleteAllInfo() {
        System.out.print("Подтвердите удаление всей информации (y / Y): ");

        final String choice = InputManager.getNextLine();
        if (!choice.equals("Y") && !choice.equals("y")) {
            System.out.println("Удаление было отменено!");
            return;
        }

        try {
            shopBookDao.clearTable();
            bookDao.clearTable();
            authorDao.clearTable();
            shopDao.clearTable();

            System.out.println("Информация из таблиц была успешно удалена!");
        } catch (RuntimeException e) {
            logger.severe("При удалении информации из таблиц ошибка: " + e.getMessage());
        }
    }

    public void addDefaultInfo() {
        try {
            if (authorDao.getCount() != 0 || bookDao.getCount() != 0 ||
                    shopDao.getCount() != 0 || shopBookDao.getCount() != 0) {
                System.out.println("Таблицы не пусты. Вставка информации по умолчанию невозможна!");
                return;
            }

            List<Author> authors = getDefaultAuthors();
            for (Author author: authors) {
                authorDao.insert(author);
            }
            authors = authorDao.getAll();

            List<Book> books = getDefaultBooks(authors);
            for (Book book: books) {
                bookDao.insert(book);
            }
            books = bookDao.getAll();

            List<Shop> shops = getDefaultShops();
            for (Shop shop: shops) {
                shopDao.insert(shop);
            }
            shops = shopDao.getAll();

            List<ShopBook> shopBooks = getDefaultShopBooks(shops, books);
            for (ShopBook shopBook: shopBooks) {
                shopBookDao.insert(shopBook);
            }

            System.out.println("Информация по умолчанию вставлена успешно!");
        } catch (RuntimeException e) {
            logger.severe("При добавлении информации в таблицы возникла ошибка: " + e.getMessage());
        }
    }

    // Задание 1: выборка по всем таблицам
    public void getAllInfo() {
        try {
            List<Author> authors = authorDao.getAll();

            if (authors.isEmpty()) {
                System.out.println("Таблица авторов пуста!");
            } else {
                System.out.println("[Авторы]");
                for (Author author: authors) {
                    System.out.println(author);
                }
            }

            List<Book> books = bookDao.getAll();

            if (books.isEmpty()) {
                System.out.println("Таблица книг пуста!");
            } else {
                System.out.println("\n[Книги]");
                for (Book book: books) {
                    System.out.println(book);
                }
            }

            List<Shop> shops = shopDao.getAll();

            if (shops.isEmpty()) {
                System.out.println("Таблица магазинов пуста!");
            } else {
                System.out.println("\n[Магазины]");
                for (Shop shop: shops) {
                    System.out.println(shop);
                }
            }

            List<ShopBook> shopBooks = shopBookDao.getAll();

            if (shopBooks.isEmpty()) {
                System.out.println("Таблица связей магазинов и книг пуста!");
            } else {
                System.out.println("\n[Книги в магазинах]");
                for (ShopBook shopBook: shopBooks) {
                    System.out.println(shopBook);
                }
            }
        } catch (RuntimeException e) {
            logger.severe("При просмотре информации возникла ошибка: " + e.getMessage());
        }
    }

    // Задание 2: выборка авторов, отсортированных по имени и по фамилии
    public void getAuthorsSortedByFirstNameAndLastName() {
        try {
            List<Author> authors = authorDao.getAllSortedByFirstNameAndLastName();

            if (authors.isEmpty()) {
                System.out.println("Таблица авторов пуста!");
                return;
            }

            System.out.println("[Авторы, отсортированные по имени и фамилии]");
            for (Author author: authors) {
                System.out.println(author);
            }
        } catch (RuntimeException e) {
            logger.severe("При выборке авторов возникла ошибка: " + e.getMessage());
        }
    }

    // Задание 3: выборка книг, изданных после определённого года, отсортированных по году публикации
    public void getBooksPublishedAfterYear() {
        try {
            System.out.print("Введи год публикации: ");
            final int year = InputManager.getNextInt();

            List<Book> books = bookDao.getPublishedAfter(year);

            if (books.isEmpty()) {
                System.out.println("Не найдено ни одной книги, изданной после " + year + " года!");
                return;
            }

            System.out.println("[Книги, изданные после " + year + " года, по году публикации]");
            for (Book book: books) {
                System.out.println(book);
            }
        } catch (RuntimeException e) {
            logger.severe("При выборке книг возникла ошибка: " + e.getMessage());
        }
    }

    // Задание 4: обновление названия случайной книги, находящейся в определённом магазине
    public void updateRandomBookTitleInShop() {
        try {
            System.out.print("Введи название магазина: ");
            final String shopName = InputManager.getNextLine();

            System.out.print("Введи новое название книги: ");
            final String newTitle = InputManager.getNextLine();

            final int updatedRows = bookDao.updateRandomBookTitleInShop(shopName, newTitle);

            if (updatedRows == 0) {
                System.out.println("В магазине \"" + shopName + "\" не найдено ни одной книги!");
            } else {
                System.out.println("Название случайной книги из магазина \"" + shopName +
                        "\" успешно обновлено!");
            }
        } catch (RuntimeException e) {
            logger.severe("При обновлении книги возникла ошибка: " + e.getMessage());
        }
    }

    // Задание 5: выборка всех книг, авторы которых имеют фамилию, начинающуюся с определённой буквы
    public void getBooksByAuthorLastNameLetter() {
        try {
            System.out.print("Введи букву, с которой начинается фамилия автора: ");
            final String line = InputManager.getNextLine();

            if (line.length() != 1) {
                System.out.println("Необходимо ввести ровно одну букву!");
                return;
            }

            List<Book> books = bookDao.getByAuthorLastNameLetter(line.charAt(0));

            if (books.isEmpty()) {
                System.out.println("Не найдено ни одной книги автора с фамилией на букву \"" + line + "\"!");
                return;
            }

            System.out.println("[Книги авторов с фамилией на букву \"" + line + "\"]");
            for (Book book: books) {
                System.out.println(book);
            }
        } catch (RuntimeException e) {
            logger.severe("При выборке книг возникла ошибка: " + e.getMessage());
        }
    }

    // Задание 6: обновление имени всех авторов, чьи книги находятся в определённом магазине (подзапрос)
    public void updateFirstNameOfAuthorsWithBooksInShop() {
        try {
            System.out.print("Введи название магазина: ");
            final String shopName = InputManager.getNextLine();

            System.out.print("Введи новое имя авторов: ");
            final String newFirstName = InputManager.getNextLine();

            final int updatedRows = authorDao.updateFirstNameOfAuthorsWithBooksInShop(shopName, newFirstName);

            if (updatedRows == 0) {
                System.out.println("Не найдено ни одного автора, чьи книги находятся в магазине \"" + shopName + "\"!");
            } else {
                System.out.println("Имена авторов (затронуто записей: " + updatedRows +
                        ") успешно обновлены!");
            }
        } catch (RuntimeException e) {
            logger.severe("При обновлении авторов возникла ошибка: " + e.getMessage());
        }
    }

    // Задание 7: количество книг каждого автора (подзапрос)
    public void getBooksCountByAuthors() {
        try {
            List<AuthorBooksCount> counts = authorDao.getBookCounts();

            if (counts.isEmpty()) {
                System.out.println("Таблица авторов пуста!");
                return;
            }

            System.out.println("[Количество книг по авторам]");
            for (AuthorBooksCount count: counts) {
                System.out.println(count);
            }
        } catch (RuntimeException e) {
            logger.severe("При подсчёте книг возникла ошибка: " + e.getMessage());
        }
    }

    private List<Author> getDefaultAuthors() {
        List<Author> authors = new ArrayList<>();

        authors.add(new Author("Лев", "Толстой"));
        authors.add(new Author("Фёдор", "Достоевский"));
        authors.add(new Author("Александр", "Пушкин"));
        authors.add(new Author("Антон", "Чехов"));

        return authors;
    }

    private List<Book> getDefaultBooks(List<Author> authors) {
        List<Book> books = new ArrayList<>();
        books.add(new Book(
                "Война и мир",
                authors.stream().filter(x -> x.getLastName().equals("Толстой")).findFirst().orElseThrow().getId(),
                1869
        ));
        books.add(new Book(
                "Анна Каренина",
                authors.stream().filter(x -> x.getLastName().equals("Толстой")).findFirst().orElseThrow().getId(),
                1877
        ));
        books.add(new Book(
                "Преступление и наказание",
                authors.stream().filter(x -> x.getLastName().equals("Достоевский")).findFirst().orElseThrow().getId(),
                1866
        ));
        books.add(new Book(
                "Идиот",
                authors.stream().filter(x -> x.getLastName().equals("Достоевский")).findFirst().orElseThrow().getId(),
                1869
        ));
        books.add(new Book(
                "Евгений Онегин",
                authors.stream().filter(x -> x.getLastName().equals("Пушкин")).findFirst().orElseThrow().getId(),
                1833
        ));
        books.add(new Book(
                "Капитанская дочка",
                authors.stream().filter(x -> x.getLastName().equals("Пушкин")).findFirst().orElseThrow().getId(),
                1836
        ));
        books.add(new Book(
                "Чайка",
                authors.stream().filter(x -> x.getLastName().equals("Чехов")).findFirst().orElseThrow().getId(),
                1896
        ));
        books.add(new Book(
                "Вишнёвый сад",
                authors.stream().filter(x -> x.getLastName().equals("Чехов")).findFirst().orElseThrow().getId(),
                1904
        ));

        return books;
    }

    private List<Shop> getDefaultShops() {
        List<Shop> shops = new ArrayList<>();

        shops.add(new Shop(
                "Белкнига",
                "проспект Независимости, 14"
        ));
        shops.add(new Shop(
                "Букинист",
                "проспект Независимости, 53"
        ));
        shops.add(new Shop(
                "Oz",
                "улица Сурганова, 21"
        ));

        return shops;
    }

    private List<ShopBook> getDefaultShopBooks(List<Shop> shops, List<Book> books) {
        List<ShopBook> shopBooks = new ArrayList<>();

        shopBooks.add(new ShopBook(
                shops.stream().filter(x -> x.getName().equals("Белкнига")).findFirst().orElseThrow().getId(),
                books.stream().filter(x -> x.getTitle().equals("Война и мир")).findFirst().orElseThrow().getId()
        ));
        shopBooks.add(new ShopBook(
                shops.stream().filter(x -> x.getName().equals("Белкнига")).findFirst().orElseThrow().getId(),
                books.stream().filter(x -> x.getTitle().equals("Чайка")).findFirst().orElseThrow().getId()
        ));
        shopBooks.add(new ShopBook(
                shops.stream().filter(x -> x.getName().equals("Белкнига")).findFirst().orElseThrow().getId(),
                books.stream().filter(x -> x.getTitle().equals("Капитанская дочка")).findFirst().orElseThrow().getId()
        ));
        shopBooks.add(new ShopBook(
                shops.stream().filter(x -> x.getName().equals("Букинист")).findFirst().orElseThrow().getId(),
                books.stream().filter(x -> x.getTitle().equals("Анна Каренина")).findFirst().orElseThrow().getId()
        ));
        shopBooks.add(new ShopBook(
                shops.stream().filter(x -> x.getName().equals("Букинист")).findFirst().orElseThrow().getId(),
                books.stream().filter(x -> x.getTitle().equals("Идиот")).findFirst().orElseThrow().getId()
        ));
        shopBooks.add(new ShopBook(
                shops.stream().filter(x -> x.getName().equals("Букинист")).findFirst().orElseThrow().getId(),
                books.stream().filter(x -> x.getTitle().equals("Евгений Онегин")).findFirst().orElseThrow().getId()
        ));
        shopBooks.add(new ShopBook(
                shops.stream().filter(x -> x.getName().equals("Oz")).findFirst().orElseThrow().getId(),
                books.stream().filter(x -> x.getTitle().equals("Преступление и наказание")).findFirst().orElseThrow().getId()
        ));
        shopBooks.add(new ShopBook(
                shops.stream().filter(x -> x.getName().equals("Oz")).findFirst().orElseThrow().getId(),
                books.stream().filter(x -> x.getTitle().equals("Вишнёвый сад")).findFirst().orElseThrow().getId()
        ));

        return shopBooks;
    }
}
