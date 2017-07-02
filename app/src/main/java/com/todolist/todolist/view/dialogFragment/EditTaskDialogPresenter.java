package com.todolist.todolist.view.dialogFragment;

class EditTaskDialogPresenter {
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
