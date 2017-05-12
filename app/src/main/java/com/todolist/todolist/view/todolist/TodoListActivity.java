package com.todolist.todolist.view.todolist;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import butterknife.OnItemLongClick;

public class TodoListActivity extends AppCompatActivity implements TodoListView {
    @BindView(R.id.task)
    EditText task;
    @BindView(R.id.task_list)
    ListView taskList;

    private TodoListPresenter presenter;

    ArrayList<String> list;

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
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        taskList.setAdapter(adapter);
        populateTaskList();
        registerForContextMenu(taskList);
    }

    private void populateTaskList() {
        Cursor tasks = taskDatabase.getAllData();
        while (tasks.moveToNext()) {
            list.add(tasks.getString(1));
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
        taskDatabase.insertData(task);
        populateTaskList();
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
            taskDatabase.deleteTask(list.get(info.position));
            list.remove(info.position);
            adapter.notifyDataSetChanged();
        }

        return true;
    }
}
