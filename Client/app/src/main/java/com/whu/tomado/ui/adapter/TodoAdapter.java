package com.whu.tomado.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.whu.tomado.R;
import com.whu.tomado.pojo.Todo;

import java.util.List;

public class TodoAdapter extends ArrayAdapter<Todo> {
    private LayoutInflater inflater;
    private List<Todo> todoList;
    private Context context;

    private int unfinishedTaskCount = 0;

    public void addUnfinishedTaskCount() {
        unfinishedTaskCount++;
    }

    // 构造函数
    public TodoAdapter(Context context, List<Todo> taskList) {
        super(context, 0, taskList);
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.todoList = taskList;
    }

    // 重写getView方法
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;
        if (itemView == null) {
            itemView = inflater.inflate(R.layout.one_task, parent, false);
        }
        Todo currentTask = getItem(position);
        // 设置任务完成情况
        CheckBox taskFinishedBox = itemView.findViewById(R.id.taskFinished);
        taskFinishedBox.setChecked(currentTask.getTaskStatus());
        TextView taskNameTextView = itemView.findViewById(R.id.taskNameTextView);
        TextView taskTimeTextView = itemView.findViewById(R.id.taskTimeTextView);
        TextView taskNotesTextView = itemView.findViewById(R.id.taskNotesTextView);

        taskFinishedBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isChecked = ((CheckBox) v).isChecked();
                // 调用处理选中事件的函数
                handleCheckBoxClick(currentTask, isChecked, position);
            }
        });

        taskNameTextView.setText(currentTask.getTaskName());
        // 如果时间为空，不显示（不占用空间）。否则显示
        if (currentTask.getTaskTime().isEmpty()) {
            taskTimeTextView.setVisibility(View.GONE);
        } else {
            taskTimeTextView.setText(currentTask.getTaskTime());
        }
        // 如果备注为空，不显示（不占用空间）。否则显示
        if (currentTask.getTaskNotes().isEmpty()) {
            taskNotesTextView.setVisibility(View.GONE);
        } else {
            taskNotesTextView.setText(currentTask.getTaskNotes());
        }

        return itemView;
    }

    private void handleCheckBoxClick(Todo todo, boolean isChecked, int position) {
        // 在这里实现 CheckBox 点击事件的处理逻辑
        if (isChecked) {
            // 如果选中，就把任务从任务列表中移除，再添加到任务列表的最后
            todo.setDone(true);
            todoList.remove(todo);
            todoList.add(unfinishedTaskCount-1,todo);
            unfinishedTaskCount--;
        } else {
            // 如果未选中，就把任务从任务列表中移除，再添加到任务列表的unfinishedTaskCount位置
            todo.setDone(false);
            todoList.remove(todo);
            todoList.add(unfinishedTaskCount, todo);
            unfinishedTaskCount++;
        }
        notifyDataSetChanged();
    }
}
