package com.todolist.todolist.view.dialogFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.todolist.todolist.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class EditTaskDialog extends DialogFragment {

    public static final String TASK = "task";
    public static final String TASK_POSITION = "task_position";
    @BindView(R.id.edit_task)
    EditText editTask;

    private int taskPosition;

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

        String task = getArguments().getString(TASK);
        taskPosition = getArguments().getInt(TASK_POSITION);
        editTask.setText(task);

        builder.setView(view);
        return builder.create();
    }

    @OnClick(R.id.save_button)
    public void onSaveButtonClick() {
        String updatedTask = editTask.getText().toString();
        getDialog().dismiss();
        TranferData tranferData = (TranferData) getActivity();
        tranferData.transfer(updatedTask, taskPosition);
    }
}
