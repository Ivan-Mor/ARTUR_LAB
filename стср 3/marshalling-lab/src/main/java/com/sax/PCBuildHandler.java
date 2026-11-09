package com.sax;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Обработчик событий SAX-парсера для XML-файла со сборками ПК.
 * Реагирует на события начала/конца элемента и встречи текста.
 */
public class PCBuildHandler extends DefaultHandler {
    private List<PCBuild> catalog = null;
    private PCBuild pcBuild = null;
    private StringBuilder currentCharacters = new StringBuilder();

    public List<PCBuild> getCatalog() {
        return catalog;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        currentCharacters.setLength(0);
        if (qName.equalsIgnoreCase("catalog")) {
            catalog = new ArrayList<>();
        } else if (qName.equalsIgnoreCase("build")) {
            pcBuild = new PCBuild();
            String id = attributes.getValue("id");
            if (id != null) {
                pcBuild.setId(id);
            }
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        String content = currentCharacters.toString().trim();
        if (qName.equalsIgnoreCase("build")) {
            catalog.add(pcBuild);
        } else if (qName.equalsIgnoreCase("processor")) {
            pcBuild.setProcessor(content);
        } else if (qName.equalsIgnoreCase("graphics")) {
            pcBuild.setGraphics(content);
        } else if (qName.equalsIgnoreCase("memory")) {
            pcBuild.setMemory(Integer.parseInt(content));
        } else if (qName.equalsIgnoreCase("storage")) {
            pcBuild.setStorage(content);
        } else if (qName.equalsIgnoreCase("price")) {
            pcBuild.setPrice(Double.parseDouble(content));
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) {
        currentCharacters.append(ch, start, length);
    }
}
