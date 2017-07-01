package com.todolist.todolist.presenter;

import com.todolist.todolist.model.UserTask;
import com.todolist.todolist.view.todolist.TodoListView;

public class TodoListPresenter {
    private TodoListView view;

    public TodoListPresenter(TodoListView view) {
        this.view = view;
    }

    public void validate(String task) {
        if (task.trim().isEmpty()) {
            view.showToastMessage();
        } else {
            view.addTaskToListView(task);
        }
    }

    public void editUserTask(UserTask userTask, int taskPosition) {
        view.showAlertDialog(userTask, taskPosition);
    }

    public void deleteUserTask(String task, int taskPosition) {
        view.showDeleteTaskToastMessage();
        view.deleteUserTask(task, taskPosition);
    }
}