package com.todolist.todolist.model;

public class UserTask {
    private String task;
    private String checkBoxState;

    public UserTask(String task, String checkBoxState) {
        this.task = task;
        this.checkBoxState = checkBoxState;
    }

    public String getTask() {
        return task;
    }

    public String getCheckBoxState() {
        return checkBoxState;
    }
}