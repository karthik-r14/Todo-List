package com.todolist.todolist.presenter;

import com.todolist.todolist.view.todolist.TodoListActivity;
import com.todolist.todolist.view.todolist.TodoListView;

public class TodoListPresenter {
    private TodoListView view;

    public TodoListPresenter(TodoListView view) {
        this.view = view;
    }

    public void validate(String task) {
        if(task.trim().isEmpty()) {
            view.showToastMessage();
        } else {
            view.addTaskToListView(task);
        }
    }
}
