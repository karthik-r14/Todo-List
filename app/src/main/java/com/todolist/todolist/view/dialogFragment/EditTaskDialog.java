package com.todolist.todolist.view.dialogFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_task_layout, container, false);
        ButterKnife.bind(this, view);
        String task = getArguments().getString(TASK);
        taskPosition = getArguments().getInt(TASK_POSITION);
        editTask.setText(task);
        return view;
    }

    @OnClick(R.id.save_button)
    public void onSaveButtonClick() {
        String updatedTask = editTask.getText().toString();
        getDialog().dismiss();
        TranferData tranferData = (TranferData) getActivity();
        tranferData.transfer(updatedTask, taskPosition);
    }
}
