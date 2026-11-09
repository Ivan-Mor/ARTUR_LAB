package com.gson.model;

import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

/**
 * POJO-класс «Занятие» (GSON).
 */
public class Lesson {
    private String subject;
    private LocalTime duration;
    private List<String> topics;

    public Lesson() {
    }

    public Lesson(String subject, LocalTime duration, List<String> topics) {
        this.subject = subject;
        this.duration = duration;
        this.topics = topics;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public LocalTime getDuration() {
        return duration;
    }

    public void setDuration(LocalTime duration) {
        this.duration = duration;
    }

    public List<String> getTopics() {
        return topics;
    }

    public void setTopics(List<String> topics) {
        this.topics = topics;
    }

    @Override
    public String toString() {
        return "Lesson{" +
                "subject='" + subject + '\'' +
                ", duration=" + duration +
                ", topics=" + topics +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(subject, duration, topics);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lesson lesson = (Lesson) o;
        return Objects.equals(subject, lesson.subject) &&
                Objects.equals(duration, lesson.duration) &&
                Objects.equals(topics, lesson.topics);
    }
}
