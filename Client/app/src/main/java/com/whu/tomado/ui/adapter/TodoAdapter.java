package com.whu.tomado.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.whu.tomado.R;
import com.whu.tomado.pojo.Todo;

import java.util.List;

public class TodoAdapter extends ArrayAdapter<Todo> {
    private LayoutInflater inflater;

    public TodoAdapter(Context context, List<Todo> taskList) {
        super(context, 0, taskList);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;
        if (itemView == null) {
            itemView = inflater.inflate(R.layout.one_task, parent, false);
        }

        Todo currentTask = getItem(position);

        TextView taskNameTextView = itemView.findViewById(R.id.taskNameTextView);
        TextView taskTimeTextView = itemView.findViewById(R.id.taskTimeTextView);
        TextView taskNotesTextView = itemView.findViewById(R.id.taskNotesTextView);

        taskNameTextView.setText(currentTask.getTaskName());
        taskTimeTextView.setText(currentTask.getTaskTime());
        taskNotesTextView.setText(currentTask.getTaskNotes());

        return itemView;
    }
}
