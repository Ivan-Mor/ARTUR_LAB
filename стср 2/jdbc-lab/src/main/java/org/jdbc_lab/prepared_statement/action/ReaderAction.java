package org.jdbc_lab.prepared_statement.action;

import org.jdbc_lab.prepared_statement.dao.ReaderDao;
import org.jdbc_lab.prepared_statement.model.Reader;
import org.jdbc_lab.utils.InputManager;

import java.util.List;

/**
 * Бизнес-логика работы с читателями (часть задания пакета prepared_statement)
 */

public class ReaderAction {
    ReaderDao readerDao = new ReaderDao();

    public void insertReader() {
        Reader reader = Reader.getReaderFromInput();

        readerDao.insert(reader);

        System.out.println("Читатель добавлен успешно!");
    }

    public void updateReader() {
        System.out.print("Введи id читателя: ");

        Reader reader = readerDao.getById(InputManager.getNextLong());

        if (reader == null) {
            System.out.println("Читатель с введённым id не найден!");
        } else {
            Reader newReader = Reader.getReaderFromInput();
            newReader.setId(reader.getId());

            readerDao.update(newReader);

            System.out.println("Читатель обновлён успешно!");
        }
    }

    public void deleteReader() {
        System.out.print("Введи id читателя: ");

        Reader reader = readerDao.getById(InputManager.getNextLong());

        if (reader == null) {
            System.out.println("Читатель с введённым id не найден!");
        } else {
            readerDao.delete(reader.getId());

            System.out.println("Читатель удалён успешно!");
        }
    }

    public void getReaderById() {
        System.out.print("Введи id читателя: ");

        Reader reader = readerDao.getById(InputManager.getNextLong());

        if (reader == null) {
            System.out.println("Читатель с введённым id не найден!");
        } else {
            System.out.println("[Читатель по id]\n" + reader);
        }
    }

    public void getReadersByLastName() {
        System.out.print("Введи фамилию (или её часть): ");

        List<Reader> readers = readerDao.getByLastName(InputManager.getNextLine());

        if (readers.isEmpty()) {
            System.out.println("Не найдено ни одного читателя с похожей фамилией!");
            return;
        }

        System.out.println("[Читатели по фамилии]");
        for (Reader reader : readers) {
            System.out.println(reader);
        }
    }

    public void getReadersByCity() {
        System.out.print("Введи город (или его часть): ");

        List<Reader> readers = readerDao.getByCity(InputManager.getNextLine());

        if (readers.isEmpty()) {
            System.out.println("Не найдено ни одного читателя из города с похожим названием!");
            return;
        }

        System.out.println("[Читатели по городу]");
        for (Reader reader : readers) {
            System.out.println(reader);
        }
    }

    public void getAllReaders() {
        List<Reader> readers = readerDao.getAll();

        if (readers.isEmpty()) {
            System.out.println("Нет ни одного читателя!");
            return;
        }

        System.out.println("[Все читатели]");
        for (Reader reader : readers) {
            System.out.println(reader);
        }
    }
}
