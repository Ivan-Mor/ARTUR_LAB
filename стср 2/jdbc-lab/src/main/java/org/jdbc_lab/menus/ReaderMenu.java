package org.jdbc_lab.menus;

import org.jdbc_lab.prepared_statement.action.ReaderAction;
import org.jdbc_lab.utils.InputManager;
import org.jdbc_lab.utils.MenuUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Меню работы с новой сущностью "читатель" (пакет prepared_statement)
 */

public class ReaderMenu {
    private static final Logger logger = Logger.getLogger(ReaderMenu.class.getName());

    private final ReaderAction readerAction = new ReaderAction();

    public void print() {
        List<String> options = new ArrayList<>();
        options.add("Добавить читателя");
        options.add("Обновить читателя");
        options.add("Удалить читателя");
        options.add("Найти по id");
        options.add("Найти по фамилии");
        options.add("Найти по городу");
        options.add("Отобразить всех читателей");

        final String header = MenuUtils.getHeader("Меню читателей", options);

        while (true) {
            System.out.print(header);

            try {
                switch (InputManager.getNextInt()) {
                    case 1: {
                        readerAction.insertReader();
                        break;
                    }
                    case 2: {
                        readerAction.updateReader();
                        break;
                    }
                    case 3: {
                        readerAction.deleteReader();
                        break;
                    }
                    case 4: {
                        readerAction.getReaderById();
                        break;
                    }
                    case 5: {
                        readerAction.getReadersByLastName();
                        break;
                    }
                    case 6: {
                        readerAction.getReadersByCity();
                        break;
                    }
                    case 7: {
                        readerAction.getAllReaders();
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
                logger.severe("Ошибка при обработке меню читателей: " + e.getMessage());
            }
        }
    }
}
