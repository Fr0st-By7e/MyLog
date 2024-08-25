package com.example.test;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "ToDoListPrefs";
    private static final String KEY_TASKS = "tasks";

    private EditText etTask;
    private EditText etDueDate;
    private Button btnAddTask;
    private ListView lvTasks;
    private ArrayAdapter<Task> adapter;
    private ArrayList<Task> tasks;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etTask = findViewById(R.id.etTask);
        etDueDate = findViewById(R.id.etDueDate);
        btnAddTask = findViewById(R.id.btnAddTask);
        lvTasks = findViewById(R.id.lvTasks);

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        tasks = loadTasks();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, tasks);
        lvTasks.setAdapter(adapter);

        btnAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewTask();
            }
        });

        // Handle single tap to delete
        lvTasks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tasks.remove(position);
                adapter.notifyDataSetChanged();
                saveTasks();
            }
        });

        // Handle long press to edit
        lvTasks.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showEditDialog(position);
                return true; // Return true to indicate the event was handled
            }
        });
    }

    private void addNewTask() {
        String description = etTask.getText().toString().trim();
        String dueDate = etDueDate.getText().toString().trim();
        if (!description.isEmpty()) {
            String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Calendar.getInstance().getTime());
            Task task = new Task(description, timestamp, dueDate);
            tasks.add(task);
            adapter.notifyDataSetChanged();
            saveTasks();
            etTask.setText("");
            etDueDate.setText("");
        }
    }

    private void showEditDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Task");

        // Inflate the custom dialog layout
        View view = getLayoutInflater().inflate(R.layout.edit_task_dialog, null);
        final EditText etEditTask = view.findViewById(R.id.etEditTask);
        final EditText etEditDueDate = view.findViewById(R.id.etEditDueDate);

        Task task = tasks.get(position);
        etEditTask.setText(task.getDescription());
        etEditDueDate.setText(task.getDueDate());

        builder.setView(view);
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String updatedDescription = etEditTask.getText().toString().trim();
                String updatedDueDate = etEditDueDate.getText().toString().trim();
                if (!updatedDescription.isEmpty()) {
                    Task task = tasks.get(position);
                    task.setDescription(updatedDescription);
                    task.setDueDate(updatedDueDate);
                    adapter.notifyDataSetChanged();
                    saveTasks();
                }
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void saveTasks() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        ArrayList<String> taskStrings = new ArrayList<>();
        for (Task task : tasks) {
            taskStrings.add(task.getDescription() + "||" + task.getTimestamp() + "||" + task.getDueDate());
        }
        editor.putString(KEY_TASKS, String.join(",", taskStrings));
        editor.apply();
    }

    private ArrayList<Task> loadTasks() {
        String savedTasks = sharedPreferences.getString(KEY_TASKS, "");
        ArrayList<Task> loadedTasks = new ArrayList<>();
        if (!savedTasks.isEmpty()) {
            String[] taskArray = savedTasks.split(",");
            for (String taskString : taskArray) {
                String[] parts = taskString.split("\\|\\|");
                if (parts.length == 3) {
                    loadedTasks.add(new Task(parts[0], parts[1], parts[2]));
                }
            }
        }
        return loadedTasks;
    }
}








