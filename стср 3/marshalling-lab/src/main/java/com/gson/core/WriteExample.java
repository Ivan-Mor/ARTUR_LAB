package com.gson.core;

import com.fatboyindustrial.gsonjavatime.Converters;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.gson.model.Lesson;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Задание GSON (часть 1): записать массив из объектов Lesson в JSON-файл.
 */
public class WriteExample {
    public static void main(String[] args) {
        Gson gson = Converters
                .registerLocalTime(new GsonBuilder())
                .setPrettyPrinting()
                .create();
        List<Lesson> lessons = createLessons();
        final String jsonData = gson.toJson(lessons);
        try (FileWriter writer = new FileWriter("lessons.json")) {
            writer.write(jsonData);
            System.out.println("Lessons have been written to lessons.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<Lesson> createLessons() {
        List<Lesson> lessons = new ArrayList<>();
        lessons.add(new Lesson("Математика", LocalTime.of(0, 45),
                List.of("Дискриминант", "Логарифмы", "Теорема Пифагора")));
        lessons.add(new Lesson("Программирование", LocalTime.of(1, 30),
                List.of("Массивы", "Коллекции", "Стримы")));
        lessons.add(new Lesson("Физика", LocalTime.of(1, 0),
                List.of("Кинематика", "Законы Ньютона")));
        return lessons;
    }
}
