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
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.todolist.todolist.R;
import com.todolist.todolist.helper.DatabaseHelper;
import com.todolist.todolist.model.UserTask;
import com.todolist.todolist.presenter.TodoListPresenter;
import com.todolist.todolist.view.dialogFragment.EditTaskDialog;
import com.todolist.todolist.view.dialogFragment.TransferData;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class TodoListActivity extends AppCompatActivity implements TodoListView, TransferData {
    @BindView(R.id.task)
    EditText task;
    @BindView(R.id.task_list)
    ListView taskList;
    @BindView(R.id.add_task_message)
    RelativeLayout addTaskMessage;

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
        ArrayList<UserTask> checkedUserTasks = new ArrayList<>();
        ArrayList<UserTask> uncheckedUserTasks = new ArrayList<>();

        while (userTasks.moveToNext()) {
            String task = userTasks.getString(1);
            String state = userTasks.getString(2);
            UserTask userTask = new UserTask(task, state);
            if (state.equals("0")) {
                checkedUserTasks.add(userTask);
            } else {
                uncheckedUserTasks.add(userTask);
            }
        }
        userTasksList.addAll(checkedUserTasks);
        userTasksList.addAll(uncheckedUserTasks);

        if (userTasksList.isEmpty()) {
            addTaskMessage.setVisibility(VISIBLE);
        } else {
            addTaskMessage.setVisibility(GONE);
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
        this.task.getText().clear();

        taskDatabase.insertTask(new UserTask(task, "false"));
        populateUserTasks();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showAlertDialog(UserTask userTask, int taskPosition) {
        EditTaskDialog editTaskDialog = EditTaskDialog.newInstance();
        Bundle bundle = new Bundle();
        bundle.putString(EditTaskDialog.TASK, userTask.getTask());
        bundle.putInt(EditTaskDialog.TASK_POSITION, taskPosition);
        editTaskDialog.setArguments(bundle);
        editTaskDialog.show(getSupportFragmentManager(), EditTaskDialog.TAG);
    }

    @Override
    public void deleteUserTask(String task, int taskPosition) {
        taskDatabase.deleteTask(task);
        userTasksList.remove(taskPosition);

        if (userTasksList.isEmpty()) {
            addTaskMessage.setVisibility(VISIBLE);
        }

        adapter.notifyDataSetChanged();
    }

    @Override
    public void showDeleteTaskToastMessage() {
        Toast.makeText(getApplicationContext(), R.string.delete_msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, v.getId(), 0, "EDIT");
        menu.add(0, v.getId(), 0, "DELETE");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
                .getMenuInfo();

        if (item.getTitle() == "DELETE") {
            presenter.deleteUserTask(userTasksList.get(info.position).getTask(), info.position);
        } else if (item.getTitle() == "EDIT") {
            presenter.editUserTask(userTasksList.get(info.position), info.position);
        }
        return true;
    }

    @Override
    public void transfer(String updatedTask, int position) {
        String taskState = userTasksList.get(position).getCheckBoxState();
        String oldTask = userTasksList.get(position).getTask();
        userTasksList.remove(position);
        userTasksList.add(position, new UserTask(updatedTask, taskState));
        taskDatabase.updateTask(oldTask, updatedTask);
        adapter.notifyDataSetChanged();
    }
}
