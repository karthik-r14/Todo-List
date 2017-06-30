package com.todolist.todolist.model;

public class UserTask {
    private String task;
    private String state;

    public UserTask(String task, String  state) {
        this.task = task;
        this.state = state;
    }

    public String getTask() {
        return task;
    }

    public String getState() {
        return state;
    }
}