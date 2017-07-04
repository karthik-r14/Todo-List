package com.todolist.todolist.presenter;

import com.todolist.todolist.view.dialogFragment.EditTaskDialogView;

public class EditTaskDialogPresenter {
    private EditTaskDialogView view;

    public EditTaskDialogPresenter(EditTaskDialogView editTaskDialogView) {
        this.view = editTaskDialogView;
    }

    public void validate(String updatedTask) {
        if (updatedTask.trim().isEmpty()) {
            view.showToastMessage();
        } else {
            view.transferTaskData(updatedTask);
        }
    }
}
