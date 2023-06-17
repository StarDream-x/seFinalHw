package com.whu.tomado.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.whu.tomado.R;
import com.whu.tomado.pojo.Nodo;

import java.util.List;

public class NodoAdapter extends ArrayAdapter<Nodo> {
    private LayoutInflater inflater;
    private List<Nodo> nodoList;
    private Context context;

    private int unfinishedTaskCount = 0;

    public void addUnfinishedTaskCount() {
        unfinishedTaskCount++;
    }

    // 构造函数
    public NodoAdapter(Context context, List<Nodo> taskList) {
        super(context, 0, taskList);
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.nodoList = taskList;
    }

    // 重写getView方法
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;
        if (itemView == null) {
            itemView = inflater.inflate(R.layout.one_task, parent, false);
        }
        Nodo currentTask = getItem(position);
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

    private void handleCheckBoxClick(Nodo nodo, boolean isChecked, int position) {
        // 在这里实现 CheckBox 点击事件的处理逻辑
        if (isChecked) {
            // 如果选中，就把任务从任务列表中移除，再添加到任务列表的最后
            nodo.setDone(true);
            nodoList.remove(nodo);
            nodoList.add(unfinishedTaskCount-1,nodo);
            unfinishedTaskCount--;
        } else {
            // 如果未选中，就把任务从任务列表中移除，再添加到任务列表的unfinishedTaskCount位置
            nodo.setDone(false);
            nodoList.remove(nodo);
            nodoList.add(unfinishedTaskCount, nodo);
            unfinishedTaskCount++;
        }
        notifyDataSetChanged();
    }
}
