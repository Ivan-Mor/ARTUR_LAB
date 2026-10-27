package org.jdbc_lab.menus;

import org.jdbc_lab.utils.InputManager;
import org.jdbc_lab.utils.MenuUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Меню пакета prepared_statement: сначала выбирается сущность,
 * затем открывается меню операций над ней
 */

public class PreparedStatementMenu {
    private static final Logger logger = Logger.getLogger(PreparedStatementMenu.class.getName());

    private final StudentMenu studentMenu = new StudentMenu();
    private final PublisherMenu publisherMenu = new PublisherMenu();
    private final ReaderMenu readerMenu = new ReaderMenu();

    public void print() {
        List<String> options = new ArrayList<>();
        options.add("Работать со студентами");
        options.add("Работать с издательствами");
        options.add("Работать с читателями");

        final String header = MenuUtils.getHeader("Меню PreparedStatement", options);

        while (true) {
            System.out.print(header);

            try {
                switch (InputManager.getNextInt()) {
                    case 1: {
                        studentMenu.print();
                        break;
                    }
                    case 2: {
                        publisherMenu.print();
                        break;
                    }
                    case 3: {
                        readerMenu.print();
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
                logger.severe("Ошибка при обработке меню PreparedStatement: " + e.getMessage());
            }
        }
    }
}
