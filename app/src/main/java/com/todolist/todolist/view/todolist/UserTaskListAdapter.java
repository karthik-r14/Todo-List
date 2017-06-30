package com.todolist.todolist.view.todolist;

import android.content.Context;
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


public class UserTaskListAdapter extends ArrayAdapter {
    private Context context;
    private ArrayList<UserTask> userTasks;

    public UserTaskListAdapter(Context context, ArrayList<UserTask> userTasks) {
        super(context, R.layout.custom_listview_row, R.id.my_task, userTasks);
        this.context = context;
        this.userTasks = userTasks;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.custom_listview_row, parent, false);
        row.setLongClickable(true);

        final TextView task = (TextView) row.findViewById(R.id.my_task);
        final CheckBox checkBox = (CheckBox) row.findViewById(R.id.state);

        task.setText(userTasks.get(position).getTask());
        if (userTasks.get(position).getState().equals("0")) {
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
                    Toast.makeText(context, "Task done", Toast.LENGTH_LONG).show();
                    task.setPaintFlags(task.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                } else {
                    task.setPaintFlags(task.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                }
            }
        });

        return row;
    }
}
