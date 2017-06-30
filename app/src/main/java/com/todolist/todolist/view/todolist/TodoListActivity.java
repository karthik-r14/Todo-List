package com.todolist.todolist.view.todolist;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.todolist.todolist.R;
import com.todolist.todolist.helper.DatabaseHelper;
import com.todolist.todolist.model.UserTask;
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

    private ArrayList<UserTask> userTasksList;

    private UserTaskListAdapter adapter;

    private DatabaseHelper taskDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);
        ButterKnife.bind(this);
        presenter = new TodoListPresenter(this);
        taskDatabase = new DatabaseHelper(this);
        userTasksList = new ArrayList<>();
        //adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        populateUserTasks();
        adapter = new UserTaskListAdapter(this, userTasksList);
        taskList.setAdapter(adapter);
        registerForContextMenu(taskList);
    }

    private void populateUserTasks() {
        Cursor userTasks = taskDatabase.getAllUserTasks();

        userTasksList.removeAll(userTasksList);

        while (userTasks.moveToNext()) {
            String task = userTasks.getString(1);
            String state = userTasks.getString(2);
            userTasksList.add(new UserTask(task, state));
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

        taskDatabase.insertTask(new UserTask(task, "false"));
        populateUserTasks();
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
            taskDatabase.deleteTask(userTasksList.get(info.position).getTask());
            userTasksList.remove(info.position);
            adapter.notifyDataSetChanged();
        }

        return true;
    }
}
