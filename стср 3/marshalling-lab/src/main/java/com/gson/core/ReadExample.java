package com.gson.core;

import com.fatboyindustrial.gsonjavatime.Converters;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.gson.model.Lesson;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Задание GSON (часть 2): считать JSON-файл с массивом объектов Lesson
 * и вывести информацию на экран.
 */
public class ReadExample {
    public static void main(String[] args) {
        Gson gson = Converters
                .registerLocalTime(new GsonBuilder())
                .create();
        try (Reader reader = new FileReader("lessons.json")) {
            Type lessonsListType = new TypeToken<List<Lesson>>() {
            }.getType();
            List<Lesson> lessons = gson.fromJson(reader, lessonsListType);
            for (Lesson lesson : lessons) {
                System.out.println(lesson);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
