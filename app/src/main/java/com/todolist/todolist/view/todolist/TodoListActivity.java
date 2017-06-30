package com.todolist.todolist.view.todolist;

import android.database.Cursor;
import android.media.browse.MediaBrowser;
import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.todolist.todolist.R;
import com.todolist.todolist.helper.DatabaseHelper;
import com.todolist.todolist.presenter.TodoListPresenter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TodoListActivity extends AppCompatActivity implements TodoListView {
    @BindView(R.id.task)
    EditText task;
    @BindView(R.id.task_list)
    ListView taskList;

    private TodoListPresenter presenter;

    ArrayList<String> list;

    ArrayList<String> states;

    ArrayAdapter<String> adapter;

    DatabaseHelper taskDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);
        ButterKnife.bind(this);
        presenter = new TodoListPresenter(this);
        taskDatabase = new DatabaseHelper(this);

        list = new ArrayList<>();
        states = new ArrayList<>();
        //adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        populateTaskList();
        populateCheckboxes();
        adapter = new CustomAdapter(this, list, states);
        taskList.setAdapter(adapter);
        registerForContextMenu(taskList);
    }

    private void populateTaskList() {
        Cursor tasks = taskDatabase.getAllData();

        list.removeAll(list);

        while (tasks.moveToNext()) {
            list.add(tasks.getString(1));
        }
    }

    private void populateCheckboxes() {
        Cursor checkboxState = taskDatabase.getAllData();
        states.removeAll(states);

        while (checkboxState.moveToNext()) {
            states.add(checkboxState.getString(2));
            System.out.println("State = " + checkboxState.getString(2));
        }
    }

    @OnClick(R.id.add_task_button)
    public void onAddTaskClick() {
        presenter.validate(task.getText().toString());
    }

    @Override
    public void showToastMessage() {
        Toast.makeText(getApplicationContext(), R.string.empty_task, Toast.LENGTH_SHORT).show();
    }

        @Override
    public void addTaskToListView(String task) {
        this.task.setText("");
        taskDatabase.insertData(task, "false");
        populateTaskList();
        populateCheckboxes();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, v.getId(), 0, "DELETE");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
                .getMenuInfo();
        if (item.getTitle() == "DELETE") {
            Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_SHORT).show();
            Log.d("Value", list.get(info.position));
            taskDatabase.deleteTask(list.get(info.position));
            list.remove(info.position);
            adapter.notifyDataSetChanged();
        }

        return true;
    }
}
