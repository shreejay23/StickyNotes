package com.example.stickynotes;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    private List<Task> taskList;
    private AddTaskActivity activity;
    private TaskDatabase db;

    public TaskAdapter(AddTaskActivity activity, TaskDatabase db) {
        this.activity = activity;
        this.db = db;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_view, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        db.openDatabase();
        final Task task = taskList.get(position);
        viewHolder.taskCheckBox.setText(task.getTitle());
        viewHolder.taskCheckBox.setChecked(toBoolean(task.getStatus()));
        viewHolder.taskCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    db.updateStatus(task.getID(), 1);
                } else {
                    db.updateStatus(task.getID(), 0);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox taskCheckBox;
        ViewHolder(View view) {
            super(view);
            taskCheckBox = view.findViewById(R.id.taskCheckBox);
        }
    }

    public Context getContext() {
        return activity;
    }

    private boolean toBoolean(int n) {
        return (n != 0);
    }

    public void setTasks(List<Task> taskList) {
        this.taskList = taskList;
        notifyDataSetChanged();
    }

    public void editTask(int position) {
        Task task = taskList.get(position);
        Bundle bundle = new Bundle();
        bundle.putLong("id", task.getID());
        bundle.putString("title", task.getTitle());
        AddTask fragment = new AddTask();
        fragment.setArguments(bundle);
        fragment.show(activity.getSupportFragmentManager(), AddTask.TAG);
    }

    public void deleteTask(int position) {
        Task task = taskList.get(position);
        db.deleteTask(task.getID());
        taskList.remove(position);
        notifyItemRemoved(position);
    }

}
