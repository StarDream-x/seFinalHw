package com.whu.tomado.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;


import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.whu.tomado.R;
import com.whu.tomado.Task.AddTodoTask;
import com.whu.tomado.Task.LoginTask;
import com.whu.tomado.pojo.Todo;
import com.whu.tomado.ui.adapter.TodoAdapter;
import com.whu.tomado.ui.utils.TodoTaskViewUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TodoFragment extends Fragment implements AddTodoTask.OnTaskCompleted {

    private TodoAdapter todoAdapter;
    private List<Todo> todoList;

    private Context context;

    public TodoFragment() {
    }

    public TodoFragment(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.todo, container, false);

        ListView todoListView = view.findViewById(R.id.todoListView);

        SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 执行刷新操作
                // TODO:在这里执行上传服务器的操作
                Toast.makeText(getActivity(), "上传数据", Toast.LENGTH_LONG).show();
                // 刷新完成后，调用 setRefreshing(false) 结束刷新状态
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        // 如果todoListView为空，则显示提示信息，否则显示任务列表
        todoListView.setEmptyView(view.findViewById(R.id.todoEmptyView));
        Button addTodoButton = view.findViewById(R.id.addTodoButton);

        // 初始化任务列表数据
        todoList = new ArrayList<>();

        // 创建任务列表适配器
        todoAdapter = new TodoAdapter(requireContext(), todoList);

        // 将适配器设置给ListView
        todoListView.setAdapter(todoAdapter);

        // 当长按某个item时，弹出一个对话框，询问用户是否删除该任务
        todoListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {

                // 创建一个delete_todo对话框
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("删除任务");
                builder.setMessage("确定删除该任务吗？");

                // 添加确定按钮及点击事件
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 处理确定按钮点击事件
                        todoList.remove(position);
                        todoAdapter.notifyDataSetChanged();
                    }
                });

                // 添加取消按钮及点击事件
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 处理取消按钮点击事件
                    }
                });

                // 使用builder创建出对话框对象
                AlertDialog dialog = builder.create();
                // 显示对话框
                dialog.show();

                return true;
            }
        });

        // 当点击单个item时，弹出一个对话框，显示任务的详细信息
        todoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // position 参数表示用户点击的项的位置

                // 创建一个edit_todo对话框
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                // 设置对话框标题
                builder.setTitle("任务详情");

                // 添加add_todo布局文件
                View dialogView = getLayoutInflater().inflate(R.layout.add_todo, null);

                // 设置对话框显示的View对象
                builder.setView(dialogView);

                TodoTaskViewUtils.setTaskView(dialogView,position,todoList,context);

                // 添加按钮及点击事件
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Todo todo_old = todoList.get(position);
                        Todo todo = TodoTaskViewUtils.getAddOrEditTodoInfo(dialogView,context);
                        if( !todo_old.getTaskName().equals(todo.getTaskName()) || !todo_old.getTaskNotes().equals(todo.getTaskNotes())
                            || todo_old.getTaskRepeat() != todo.getTaskRepeat() || !todo_old.getTaskTime().equals(todo.getTaskTime())
                            || todo_old.getTaskCycleTot() != todo.getTaskCycleTot()) {
                            setTask(position, todo);
                        }
                    }
                });
                builder.setNegativeButton("取消", null);

                // 创建并显示AlertDialog
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

                alertDialog.getButton(0);
            }
        });


        // 设置"添加任务"按钮的点击事件
        // 在您希望弹出页面的位置，例如点击一个按钮时
        addTodoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("添加TODO");
//                builder.setMessage("这是一个弹出页面的示例");

                // 添加自定义布局
                View dialogView = getLayoutInflater().inflate(R.layout.add_todo, null);
                builder.setView(dialogView);

                TodoTaskViewUtils.addTaskVIew(dialogView,context);

                // 添加按钮及点击事件
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Todo todo = TodoTaskViewUtils.getAddOrEditTodoInfo(dialogView,context);


                        if(todo != null) {
                            addNewTask(todo);
                        }
                        try {
                            AddTodoTask addtodoTask = new AddTodoTask(TodoFragment.this);
//                            addtodoTask.execute(getString(R.string.server_url)+"login?username="+username+"&password="+password);

                            String result = addtodoTask.get();
                            Log.d("result", result);
                            //如果result为true，则登录成功,否则登录失败
//                    System.out.println(result);

//                            if(result.compareToIgnoreCase("true\n") == 0){
//                                Toast.makeText(getActivity(), "登录成功", Toast.LENGTH_SHORT).show();
//                                setUsername(username);
//                                isLoggedIn = true;
//                            }else{
//                                Toast.makeText(getActivity(), "用户名或密码错误", Toast.LENGTH_SHORT).show();
//                                isLoggedIn = false;
//                            }

//                    Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                });
                builder.setNegativeButton("取消", null);

                // 创建并显示AlertDialog
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

                alertDialog.getButton(0);
            }
        });

        //某个CheckBox被选中或取消选中时，修改任务状态
        return view;
    }


    // 当点击某个任务时，弹出详情界面
    public void showTaskDetail(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("任务详情");
    }

    // 修改任务
    public void modifyTask(int position, String taskName, String taskTime, String taskNotes, int taskCycleTot, boolean taskRepeat) {
        todoList.get(position).setTaskName(taskName);
        todoList.get(position).setTaskTime(taskTime);
        todoList.get(position).setTaskNotes(taskNotes);
        todoList.get(position).setTaskCycleTot(taskCycleTot);
//        todoList.get(position).setTaskCycleTime(taskCycleTime);
        todoList.get(position).setTaskRepeat(taskRepeat);
        todoAdapter.notifyDataSetChanged();
    }

    //删除任务
    public void deleteTask(int position) {
        todoList.remove(position);
        todoAdapter.notifyDataSetChanged();
    }

    // 添加新任务
    private void addNewTask(Todo todo) {
//        todoList.add(todoAdapter.getUnfinishedTaskCount(), todo);
        todoList.add(0, todo);
        todoAdapter.addUnfinishedTaskCount();
        todoAdapter.notifyDataSetChanged();
    }

    // 设置已有任务
    private void setTask(int pos, Todo todo) {
        todoList.set(0, todo);
        todoAdapter.notifyDataSetChanged();
    }

    @Override
    public void onTaskCompleted(String result) {
        Log.d("MainActivity", "Result: " + result);
    }
}
