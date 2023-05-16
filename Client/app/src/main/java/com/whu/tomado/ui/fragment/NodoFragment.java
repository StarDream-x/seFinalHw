package com.whu.tomado.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import androidx.fragment.app.Fragment;

import com.whu.tomado.R;

import java.util.ArrayList;
import java.util.List;

public class NodoFragment extends Fragment {

    private ListView taskListView;
    private Button addTaskButton;
    private ArrayAdapter<String> taskAdapter;
    private List<String> taskList;

    public NodoFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.todo, container, false);

        taskListView = view.findViewById(R.id.todoListView);
        addTaskButton = view.findViewById(R.id.addTodoButton);

        // 初始化任务列表数据
        taskList = new ArrayList<>();
        taskList.add("任务1");
        taskList.add("任务2");
        taskList.add("任务3");

        // 创建任务列表适配器
        taskAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, taskList);

        // 将适配器设置给ListView
        taskListView.setAdapter(taskAdapter);

        // 设置"添加任务"按钮的点击事件
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 处理添加任务的逻辑
                addNewTask("新任务");
            }
        });

        return view;
    }

    private void addNewTask(String taskName) {
        taskList.add(taskName);
        taskAdapter.notifyDataSetChanged();
    }
}
