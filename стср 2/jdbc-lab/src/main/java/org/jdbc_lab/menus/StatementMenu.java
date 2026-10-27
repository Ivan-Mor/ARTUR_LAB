package org.jdbc_lab.menus;

import org.jdbc_lab.statement.action.StatementAction;
import org.jdbc_lab.utils.InputManager;
import org.jdbc_lab.utils.MenuUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class StatementMenu {
    private static final Logger logger = Logger.getLogger(StatementMenu.class.getName());

    private final StatementAction statementAction = new StatementAction();

    public void print() {
        List<String> options = new ArrayList<>();
        options.add("Очистить все таблицы");
        options.add("Просмотреть все записи");                          // задание 1
        options.add("Добавить дефолтную информацию");
        options.add("Авторы, отсортированные по имени и фамилии");       // задание 2
        options.add("Книги, изданные после определённого года");         // задание 3
        options.add("Обновить название случайной книги в магазине");     // задание 4
        options.add("Книги авторов с фамилией на определённую букву");   // задание 5
        options.add("Обновить имя авторов книг из магазина (подзапрос)");// задание 6
        options.add("Количество книг каждого автора (подзапрос)");       // задание 7

        final String header = MenuUtils.getHeader("Меню Statement", options);

        while (true) {
            System.out.print(header);

            try {
                switch (InputManager.getNextInt()) {
                    case 1: {
                        statementAction.deleteAllInfo();
                        break;
                    }
                    case 2: {
                        statementAction.getAllInfo();
                        break;
                    }
                    case 3: {
                        statementAction.addDefaultInfo();
                        break;
                    }
                    case 4: {
                        statementAction.getAuthorsSortedByFirstNameAndLastName();
                        break;
                    }
                    case 5: {
                        statementAction.getBooksPublishedAfterYear();
                        break;
                    }
                    case 6: {
                        statementAction.updateRandomBookTitleInShop();
                        break;
                    }
                    case 7: {
                        statementAction.getBooksByAuthorLastNameLetter();
                        break;
                    }
                    case 8: {
                        statementAction.updateFirstNameOfAuthorsWithBooksInShop();
                        break;
                    }
                    case 9: {
                        statementAction.getBooksCountByAuthors();
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
                logger.severe("Ошибка при обработке меню Statement: " + e.getMessage());
            }
        }
    }
}
