package com.example.todoapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterClass extends RecyclerView.Adapter<AdapterClass.ViewHolder> {

    private List<DataModel> todoList;
    private DataBase db;
    private MainActivity activity;
    public AdapterClass(DataBase db, MainActivity activity) {
        this.db = db;
        this.activity = activity;
    }

    @NonNull
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_layout, parent, false);
        return new ViewHolder(itemView);
    }

    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        db.openDatabase();

        final DataModel item = todoList.get(position);
        holder.task.setText(item.getTask());
        holder.task.setChecked(toBoolean(item.getStatus()));
        holder.task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    db.updateStatus(item.getId(), 1);
                } else {
                    db.updateStatus(item.getId(), 0);
                }
            }
        });
    }

    private boolean toBoolean(int n) {
        return n != 0;
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    public Context getContext() {
        return activity;
    }
    @SuppressLint("NotifyDataSetChanged")
    public void setTasks(List<DataModel> todoList) {
        this.todoList = todoList;
        notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        DataModel item = todoList.get(position);
        db.deleteTask(item.getId());
        todoList.remove(position);
        notifyItemRemoved(position);
    }
    public void editItem(int position) {
        DataModel item = todoList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId());
        bundle.putString("task", item.getTask());
        DailyTaskAdd fragment = new DailyTaskAdd();
        fragment.setArguments(bundle);
        fragment.show(activity.getSupportFragmentManager(), DailyTaskAdd.TAG);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox task;

        ViewHolder(View view) {
            super(view);
            task = view.findViewById(R.id.todoCheckBox);
        }
    }
}
