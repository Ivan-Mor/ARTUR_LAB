package org.jdbc_lab.menus;

import org.jdbc_lab.prepared_statement.action.PublisherAction;
import org.jdbc_lab.utils.InputManager;
import org.jdbc_lab.utils.MenuUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Меню работы с новой сущностью "издательство" (пакет prepared_statement)
 */

public class PublisherMenu {
    private static final Logger logger = Logger.getLogger(PublisherMenu.class.getName());

    private final PublisherAction publisherAction = new PublisherAction();

    public void print() {
        List<String> options = new ArrayList<>();
        options.add("Добавить издательство");
        options.add("Обновить издательство");
        options.add("Удалить издательство");
        options.add("Найти по id");
        options.add("Найти по названию");
        options.add("Найти по стране");
        options.add("Отобразить все издательства");

        final String header = MenuUtils.getHeader("Меню издательств", options);

        while (true) {
            System.out.print(header);

            try {
                switch (InputManager.getNextInt()) {
                    case 1: {
                        publisherAction.insertPublisher();
                        break;
                    }
                    case 2: {
                        publisherAction.updatePublisher();
                        break;
                    }
                    case 3: {
                        publisherAction.deletePublisher();
                        break;
                    }
                    case 4: {
                        publisherAction.getPublisherById();
                        break;
                    }
                    case 5: {
                        publisherAction.getPublishersByName();
                        break;
                    }
                    case 6: {
                        publisherAction.getPublishersByCountry();
                        break;
                    }
                    case 7: {
                        publisherAction.getAllPublishers();
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
                logger.severe("Ошибка при обработке меню издательств: " + e.getMessage());
            }
        }
    }
}
