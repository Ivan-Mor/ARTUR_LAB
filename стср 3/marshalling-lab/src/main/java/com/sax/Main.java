package com.sax;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

/**
 * Задание (вариант 15): SAX-парсер для XML-документа с данными о сборке ПК
 * (процессор, видеокарта, оперативная память, накопитель, цена).
 * Вывести все сборки, не превышающие заданный бюджет.
 */
public class Main {
    public static void main(String[] args) {
        String path = new File("src/main/resources/pc_builds.xml").getAbsolutePath();
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            PCBuildHandler handler = new PCBuildHandler();
            parser.parse(new File(path), handler);

            // Вывод каталога после завершения парсинга
            System.out.println("Done with catalog");
            List<PCBuild> catalog = handler.getCatalog();
            for (PCBuild pcBuild : catalog) {
                System.out.println(pcBuild);
            }

            // Фильтрация по бюджету
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter your budget: ");
            double budget = Double.parseDouble(scanner.nextLine());
            System.out.println();
            System.out.println("PC builds within budget $" + String.format("%.2f", budget) + ":");
            boolean found = false;
            for (PCBuild pcBuild : catalog) {
                if (pcBuild.getPrice() != null && pcBuild.getPrice() <= budget) {
                    System.out.println(pcBuild);
                    found = true;
                }
            }
            if (!found) {
                System.out.println("No PC builds found within this budget.");
            }
        } catch (IOException x) {
            System.err.println(x);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }
}
