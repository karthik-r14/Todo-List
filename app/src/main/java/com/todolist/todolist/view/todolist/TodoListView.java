package com.todolist.todolist.view.todolist;

import com.todolist.todolist.model.UserTask;

public interface TodoListView {
    void showToastMessage();

    void addTaskToListView(String task);

    void showAlertDialog(UserTask userTask, int taskPosition);

    void deleteUserTask(String task, int taskPosition);

    void showDeleteTaskToastMessage();
}
