package com.todolist.todolist.test;

import com.todolist.todolist.model.UserTask;
import com.todolist.todolist.presenter.TodoListPresenter;
import com.todolist.todolist.view.todolist.TodoListView;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class TodoListPresenterTest {
    @Mock
    private TodoListView view;

    private TodoListPresenter presenter;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        presenter = new TodoListPresenter(view);
    }

    @Test
    public void shouldShowToastMessageWhenTaskIsEmpty() throws Exception {
        String task = "";

        presenter.validate(task);

        verify(view).showToastMessage();
    }

    @Test
    public void shouldAddTaskToListViewIfTaskIsNotEmpty() throws Exception {
        String task = "Have to do computer science homework";

        presenter.validate(task);

        verify(view).addTaskToListView(task);
    }

    @Test
    public void shouldDeleteUserTaskWhenDeleteMenuItemIsClicked() throws Exception {
        presenter.deleteUserTask("Task", 1);

        verify(view).deleteUserTask("Task", 1);
        verify(view).showDeleteTaskToastMessage();
    }

    @Test
    public void shouldShowAlertDialogWithEditTextWhenEditOptionInMenuIsSelected() throws Exception {
        UserTask userTask = new UserTask("Task", "0");

        presenter.editUserTask(userTask, 1);

        verify(view).showAlertDialog(userTask, 1);
    }
}