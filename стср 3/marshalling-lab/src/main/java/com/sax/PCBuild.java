package com.sax;

import java.util.Objects;

/**
 * POJO-класс «Сборка ПК» (вариант 15).
 * Представляет запись XML-файла pc_builds.xml.
 */
public class PCBuild {
    private String id;
    private String processor;
    private String graphics;
    private Integer memory;
    private String storage;
    private Double price;

    // Конструктор по умолчанию
    public PCBuild() {
    }

    // Конструктор с параметрами
    public PCBuild(String id, String processor, String graphics, Integer memory, String storage, Double price) {
        this.id = id;
        this.processor = processor;
        this.graphics = graphics;
        this.memory = memory;
        this.storage = storage;
        this.price = price;
    }

    // Геттеры и сеттеры
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProcessor() {
        return processor;
    }

    public void setProcessor(String processor) {
        this.processor = processor;
    }

    public String getGraphics() {
        return graphics;
    }

    public void setGraphics(String graphics) {
        this.graphics = graphics;
    }

    public Integer getMemory() {
        return memory;
    }

    public void setMemory(Integer memory) {
        this.memory = memory;
    }

    public String getStorage() {
        return storage;
    }

    public void setStorage(String storage) {
        this.storage = storage;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    // Переопределение equals() для корректного сравнения объектов
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PCBuild pcBuild = (PCBuild) o;
        return Objects.equals(id, pcBuild.id) &&
                Objects.equals(processor, pcBuild.processor) &&
                Objects.equals(graphics, pcBuild.graphics) &&
                Objects.equals(memory, pcBuild.memory) &&
                Objects.equals(storage, pcBuild.storage) &&
                Objects.equals(price, pcBuild.price);
    }

    // Переопределение hashCode() для использования в коллекциях
    @Override
    public int hashCode() {
        return Objects.hash(id, processor, graphics, memory, storage, price);
    }

    // Переопределение toString() для удобного отображения объекта
    @Override
    public String toString() {
        return """
                   ┌───────────────────────────────────────────────┐
                   │            Информация о сборке ПК             │
                   └───────────────────────────────────────────────┘
                   ID:               %s
                   Процессор:        %s
                   Видеокарта:       %s
                   ОЗУ:              %s
                   Накопитель:       %s
                   Цена:             %s
                   """.formatted(
                id != null ? id : "N/A",
                processor != null ? processor : "N/A",
                graphics != null ? graphics : "N/A",
                memory != null ? memory + " GB" : "N/A",
                storage != null ? storage : "N/A",
                price != null ? String.format("$%.2f", price) : "N/A"
        );
    }
}
