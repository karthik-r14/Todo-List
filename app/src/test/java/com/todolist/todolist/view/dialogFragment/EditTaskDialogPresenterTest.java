package com.todolist.todolist.view.dialogFragment;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class EditTaskDialogPresenterTest {
    @Mock
    private EditTaskDialogView view;

    private EditTaskDialogPresenter presenter;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        presenter = new EditTaskDialogPresenter(view);
    }

    @Test
    public void shouldShowToastMessageIfTaskIsEmpty() throws Exception {
        String updatedTask = " ";

        presenter.validate(updatedTask);

        verify(view).showToastMessage();
    }

    @Test
    public void shouldTransferTaskDataToTodoListActivityIfTaskExists() throws Exception {
        String updatedTask = "Task";

        presenter.validate(updatedTask);

        verify(view).transferTaskData("Task");
    }
}