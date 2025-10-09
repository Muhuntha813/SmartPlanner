package com.smartplanner;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class TaskStore {
    private static final TaskStore INSTANCE = new TaskStore();
    private final ObservableList<Task> tasks = FXCollections.observableArrayList();

    private TaskStore() {}

    public static TaskStore get() { return INSTANCE; }
    public ObservableList<Task> tasks() { return tasks; }
    public void add(Task t) { tasks.add(t); }
    public void remove(Task t) { tasks.remove(t); }
    public void clear() { tasks.clear(); }
}
