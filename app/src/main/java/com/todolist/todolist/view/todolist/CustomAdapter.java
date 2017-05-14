package com.todolist.todolist.view.todolist;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.support.annotation.CheckResult;
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

import java.util.ArrayList;


public class CustomAdapter extends ArrayAdapter {
    private Context context;
    private ArrayList<String> tasks;
    private ArrayList<String> checkboxStates;

    public CustomAdapter(Context context, ArrayList<String> tasks, ArrayList<String> checkboxStates) {
        super(context, R.layout.custom_listview_row, R.id.my_task ,tasks);
        this.context = context;
        this.tasks = tasks;
        this.checkboxStates = checkboxStates;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.custom_listview_row, parent, false);

        final TextView task = (TextView) row.findViewById(R.id.my_task);
        final CheckBox checkBox = (CheckBox) row.findViewById(R.id.state);

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseHelper database = new DatabaseHelper(context);
                System.out.println("checkBox state : " + checkBox.isChecked());
                System.out.println("task : " + task.getText().toString());
                database.storeState(checkBox.isChecked(), task.getText().toString());
                if(checkBox.isChecked()) {
                    Toast.makeText(context, "Task done", Toast.LENGTH_LONG).show();
                    task.setPaintFlags(task.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                } else {
                    task.setPaintFlags( task.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
                }
            }
        });
        task.setText(tasks.get(position));
        if(checkboxStates.get(position).equals("0")) {
            checkBox.setChecked(true);
            task.setPaintFlags(task.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }else {
            checkBox.setChecked(false);
        }
        return row;
    }
}
