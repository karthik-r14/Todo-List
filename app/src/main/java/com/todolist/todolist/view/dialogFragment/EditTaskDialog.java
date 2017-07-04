package com.todolist.todolist.view.dialogFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.todolist.todolist.R;
import com.todolist.todolist.presenter.EditTaskDialogPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.widget.Toast.LENGTH_LONG;


public class EditTaskDialog extends DialogFragment implements EditTaskDialogView {

    public static final String TASK = "task";
    public static final String TASK_POSITION = "task_position";
    @BindView(R.id.edit_task)
    EditText editTask;

    private int taskPosition;
    private EditTaskDialogPresenter presenter;

    public static final String TAG = EditTaskDialog.class.getSimpleName();

    public static EditTaskDialog newInstance() {
        return new EditTaskDialog();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.edit_task_layout, null);
        ButterKnife.bind(this, view);
        presenter = new EditTaskDialogPresenter(this);

        String task = getArguments().getString(TASK);
        taskPosition = getArguments().getInt(TASK_POSITION);
        editTask.setText(task);

        builder.setView(view);
        Dialog dialog = builder.create();
        return dialog;
    }


    @OnClick(R.id.save_button)
    public void onSaveButtonClick() {
        String updatedTask = editTask.getText().toString();
        presenter.validate(updatedTask);
    }

    @Override
    public void showToastMessage() {
        Toast.makeText(getContext(), R.string.empty_task, LENGTH_LONG).show();
    }

    @Override
    public void transferTaskData(String task) {
        hideSoftKeyboard();
        getDialog().dismiss();
        TransferData transferData = (TransferData) getActivity();
        transferData.transfer(task.trim(), taskPosition);
    }

    protected void hideSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) this.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(editTask.getWindowToken(), 0);
    }
}
