package org.jdbc_lab.prepared_statement.action;

import org.jdbc_lab.prepared_statement.dao.PublisherDao;
import org.jdbc_lab.prepared_statement.model.Publisher;
import org.jdbc_lab.utils.InputManager;

import java.util.List;

/**
 * Бизнес-логика работы с издательствами (часть задания пакета prepared_statement)
 */

public class PublisherAction {
    PublisherDao publisherDao = new PublisherDao();

    public void insertPublisher() {
        Publisher publisher = Publisher.getPublisherFromInput();

        publisherDao.insert(publisher);

        System.out.println("Издательство добавлено успешно!");
    }

    public void updatePublisher() {
        System.out.print("Введи id издательства: ");

        Publisher publisher = publisherDao.getById(InputManager.getNextLong());

        if (publisher == null) {
            System.out.println("Издательство с введённым id не найдено!");
        } else {
            Publisher newPublisher = Publisher.getPublisherFromInput();
            newPublisher.setId(publisher.getId());

            publisherDao.update(newPublisher);

            System.out.println("Издательство обновлено успешно!");
        }
    }

    public void deletePublisher() {
        System.out.print("Введи id издательства: ");

        Publisher publisher = publisherDao.getById(InputManager.getNextLong());

        if (publisher == null) {
            System.out.println("Издательство с введённым id не найдено!");
        } else {
            publisherDao.delete(publisher.getId());

            System.out.println("Издательство удалено успешно!");
        }
    }

    public void getPublisherById() {
        System.out.print("Введи id издательства: ");

        Publisher publisher = publisherDao.getById(InputManager.getNextLong());

        if (publisher == null) {
            System.out.println("Издательство с введённым id не найдено!");
        } else {
            System.out.println("[Издательство по id]\n" + publisher);
        }
    }

    public void getPublishersByName() {
        System.out.print("Введи название (или его часть): ");

        List<Publisher> publishers = publisherDao.getByName(InputManager.getNextLine());

        if (publishers.isEmpty()) {
            System.out.println("Не найдено ни одного издательства с похожим названием!");
            return;
        }

        System.out.println("[Издательства по названию]");
        for (Publisher publisher : publishers) {
            System.out.println(publisher);
        }
    }

    public void getPublishersByCountry() {
        System.out.print("Введи страну (или её часть): ");

        List<Publisher> publishers = publisherDao.getByCountry(InputManager.getNextLine());

        if (publishers.isEmpty()) {
            System.out.println("Не найдено ни одного издательства из страны с похожим названием!");
            return;
        }

        System.out.println("[Издательства по стране]");
        for (Publisher publisher : publishers) {
            System.out.println(publisher);
        }
    }

    public void getAllPublishers() {
        List<Publisher> publishers = publisherDao.getAll();

        if (publishers.isEmpty()) {
            System.out.println("Нет ни одного издательства!");
            return;
        }

        System.out.println("[Все издательства]");
        for (Publisher publisher : publishers) {
            System.out.println(publisher);
        }
    }
}
