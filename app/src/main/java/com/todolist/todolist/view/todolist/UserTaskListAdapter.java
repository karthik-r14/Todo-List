package com.todolist.todolist.view.todolist;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.todolist.todolist.R;
import com.todolist.todolist.helper.DatabaseHelper;
import com.todolist.todolist.model.UserTask;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;


public class UserTaskListAdapter extends ArrayAdapter {
    private Context context;
    private ArrayList<UserTask> userTasksList;

    public UserTaskListAdapter(Context context, ArrayList<UserTask> userTasksList) {
        super(context, R.layout.custom_listview_row, R.id.my_task, userTasksList);
        this.context = context;
        this.userTasksList = userTasksList;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.custom_listview_row, parent, false);
        row.setLongClickable(true);

        final TextView task = (TextView) row.findViewById(R.id.my_task);
        final CheckBox checkBox = (CheckBox) row.findViewById(R.id.checkBoxState);

        task.setText(userTasksList.get(position).getTask());
        if (userTasksList.get(position).getCheckBoxState().equals("0")) {
            checkBox.setChecked(true);
            task.setPaintFlags(task.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            checkBox.setChecked(false);
        }


        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseHelper database = new DatabaseHelper(context);

                database.storeState(checkBox.isChecked(), task.getText().toString());
                if (checkBox.isChecked()) {
                    Toast.makeText(context, R.string.task_done_msg, Toast.LENGTH_LONG).show();
                    task.setPaintFlags(task.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                } else {
                    task.setPaintFlags(task.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                }

                sortUserTaskListAccordingToCheckBoxState();
            }
        });

        return row;
    }

    private void sortUserTaskListAccordingToCheckBoxState() {

        DatabaseHelper taskDatabase = new DatabaseHelper(context);

        Cursor userTasks = taskDatabase.getAllUserTasks();

        userTasksList.removeAll(userTasksList);

        ArrayList<UserTask> checkedUserTasks = new ArrayList<>();
        ArrayList<UserTask> uncheckedUserTask = new ArrayList<>();

        while (userTasks.moveToNext()) {
            String task = userTasks.getString(1);
            String state = userTasks.getString(2);
            UserTask userTask = new UserTask(task, state);

            if (state.equals("0"))
                checkedUserTasks.add(userTask);
            else {
                uncheckedUserTask.add(userTask);
            }
        }

        userTasksList.addAll(checkedUserTasks);
        userTasksList.addAll(uncheckedUserTask);
        this.notifyDataSetChanged();
    }
}

