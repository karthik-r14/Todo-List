package com.todolist.todolist.view.todolist;

import android.animation.Animator;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
    public static final String FADE_IN = "fade_in";
    public static final String FADE_OUT = "fade_out";
    public static final String NO_ANIMATION = "no_animation";
    @BindView(R.id.task)
    EditText task;
    @BindView(R.id.task_list)
    ListView taskList;
    @BindView(R.id.add_task_message)
    RelativeLayout addTaskMessage;
    @BindView(R.id.your_tasks_text)
    TextView yourTasksText;

    private TodoListPresenter presenter;

    private ArrayList<UserTask> userTasksList;

    private UserTaskListAdapter adapter;

    private DatabaseHelper taskDatabase;

    private String animationType;

    private String taskTextAnimationType;

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

        addAnimationListener();

        if (userTasksList.isEmpty()) {
            animationType = FADE_IN;
            addTaskMessage.animate().alpha(1.0f);//fade in animation
        } else {
            animationType = FADE_OUT;
            addTaskMessage.animate().alpha(0.0f);//fade out animation
            taskTextAnimationType = FADE_IN;
            yourTasksText.animate().alpha(1.0f);//fade in animation
        }
    }

    private void addAnimationListener() {
        addTaskMessage.animate().setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (animationType.equals(FADE_IN)) {
                    addTaskMessage.setVisibility(VISIBLE);
                    animationType = NO_ANIMATION;
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (animationType.equals(FADE_OUT)) {
                    addTaskMessage.setVisibility(GONE);
                    animationType = NO_ANIMATION;
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        yourTasksText.animate().setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (taskTextAnimationType.equals(FADE_IN)) {
                    yourTasksText.setVisibility(VISIBLE);
                    taskTextAnimationType = NO_ANIMATION;
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
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
        hideSoftKeyboard();

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
            addTaskMessage.animate().alpha(1.0f);//fade in animation
            yourTasksText.animate().alpha(0.0f);//fade out animation
            animationType = FADE_IN;
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

    public void hideSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager.isActive()) {
            inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
