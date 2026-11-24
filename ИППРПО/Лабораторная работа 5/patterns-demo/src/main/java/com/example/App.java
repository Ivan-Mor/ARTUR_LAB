package com.example;

import java.util.ArrayList;
import java.util.List;

public class App {
    public static void main(String[] args) {
        TaskStore store = TaskStore.instance();
        TaskFactory factory = new SimpleTaskFactory();
        store.add(factory.create("Prepare report", 2));
        store.add(factory.create("Review code", 1));
        System.out.println("One store: " + (store == TaskStore.instance()));
        TaskFormatter formatter = new BracketDecorator(new LegacyFormatterAdapter(new LegacyFormatter()));
        printTasks(store.tasks(), new PriorityOrder(), formatter);
        printTasks(store.tasks(), new NameOrder(), formatter);
    }

    private static void printTasks(List<Task> tasks, TaskOrder order, TaskFormatter formatter) {
        List<Task> sorted = new ArrayList<>(tasks);
        sorted.sort(order::compare);
        System.out.println(order.name() + ":");
        for (Task task : sorted) System.out.println(formatter.format(task));
    }
}

record Task(String name, int priority) {}

// Singleton
final class TaskStore {
    private static final TaskStore INSTANCE = new TaskStore();
    private final List<Task> tasks = new ArrayList<>();
    private TaskStore() {}
    static TaskStore instance() { return INSTANCE; }
    void add(Task task) { tasks.add(task); }
    List<Task> tasks() { return List.copyOf(tasks); }
}

// Factory Method
abstract class TaskFactory {
    abstract Task create(String name, int priority);
}
final class SimpleTaskFactory extends TaskFactory {
    @Override Task create(String name, int priority) { return new Task(name, priority); }
}

interface TaskFormatter { String format(Task task); }
final class LegacyFormatter {
    String render(String name, int priority) { return name + " (priority " + priority + ")"; }
}

// Adapter
final class LegacyFormatterAdapter implements TaskFormatter {
    private final LegacyFormatter formatter;
    LegacyFormatterAdapter(LegacyFormatter formatter) { this.formatter = formatter; }
    @Override public String format(Task task) { return formatter.render(task.name(), task.priority()); }
}

// Decorator
final class BracketDecorator implements TaskFormatter {
    private final TaskFormatter inner;
    BracketDecorator(TaskFormatter inner) { this.inner = inner; }
    @Override public String format(Task task) { return "[" + inner.format(task) + "]"; }
}

// Strategy
interface TaskOrder {
    int compare(Task first, Task second);
    String name();
}
final class PriorityOrder implements TaskOrder {
    public int compare(Task first, Task second) { return Integer.compare(first.priority(), second.priority()); }
    public String name() { return "By priority"; }
}
final class NameOrder implements TaskOrder {
    public int compare(Task first, Task second) { return first.name().compareTo(second.name()); }
    public String name() { return "By name"; }
}
