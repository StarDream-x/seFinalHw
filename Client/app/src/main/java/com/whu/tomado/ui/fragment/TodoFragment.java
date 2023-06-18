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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.whu.tomado.R;
import com.whu.tomado.network.Task.AddTodoTask;
import com.whu.tomado.network.Task.LoginTask;

import com.whu.tomado.network.VolleyCallback;
import com.whu.tomado.pojo.Todo;
import com.whu.tomado.ui.adapter.TodoAdapter;
import com.whu.tomado.ui.utils.MySingleton;
import com.whu.tomado.ui.utils.TodoTaskViewUtils;
import com.whu.tomado.utils.Global;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TodoFragment extends Fragment implements AddTodoTask.OnTaskCompleted {

    private TodoAdapter todoAdapter;
    private List<Todo> todoList = new ArrayList<>();

    private Context context;

    private ListView todoListView;

    public TodoFragment() {
    }

    public TodoFragment(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.todo, container, false);

        todoListView = view.findViewById(R.id.todoListView);

//        SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                getTodoListById(Global.userID);
//                // 执行刷新操作
//                Toast.makeText(getActivity(), "刷新数据成功", Toast.LENGTH_LONG).show();
//                // 刷新完成后，调用 setRefreshing(false) 结束刷新状态
//                swipeRefreshLayout.setRefreshing(false);
//            }
//        });

        // 如果todoListView为空，则显示提示信息，否则显示任务列表
        todoListView.setEmptyView(view.findViewById(R.id.todoEmptyView));
        Button addTodoButton = view.findViewById(R.id.addTodoButton);

        // 初始化任务列表数据
//        todoList = new ArrayList<>();

        getTodoListById(Global.userID);

        // 删除！！！
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

                        String url = getString(R.string.server_url) + "todos/" + todoList.get(position).getId();
                        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                                (Request.Method.DELETE, url, null, new Response.Listener<JSONArray>() {

                                    @SuppressLint("SetTextI18n")
                                    @Override
                                    public void onResponse(JSONArray response) {
//                                        textView.setText("Response: " + response.toString());
                                        Log.d("删除成功！", response.toString());

                                    }
                                }, new Response.ErrorListener() {
                                    @SuppressLint("SetTextI18n")
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Log.e("删除失败！", error.toString());
                                    }
                                });

                        // Access the RequestQueue through your singleton class.
                        MySingleton.getInstance(TodoFragment.this.context).addToRequestQueue(jsonArrayRequest);

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

        // 编辑！！！
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

                TodoTaskViewUtils.setTaskView(dialogView, position, todoList, context);

                // 添加按钮及点击事件
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Todo todo = todoList.get(position);
                        todo = TodoTaskViewUtils.getAddOrEditTodoInfo(dialogView, context, todo);
                        setTask(position, todo);
                    }
                });
                builder.setNegativeButton("取消", null);

                // 创建并显示AlertDialog
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

                alertDialog.getButton(0);
            }
        });


        // 添加任务！！！
        addTodoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("添加TODO");
//                builder.setMessage("这是一个弹出页面的示例");

                // 添加自定义布局
                View dialogView = getLayoutInflater().inflate(R.layout.add_todo, null);
                builder.setView(dialogView);

                TodoTaskViewUtils.addTaskVIew(dialogView, context);

                // 添加按钮及点击事件

                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Todo todo = TodoTaskViewUtils.getAddOrEditTodoInfo(dialogView, context, new Todo());

                        if (todo != null) {
                            addNewTask(todo);
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

    private void getTodoListById(long userID) {
        String url = getString(R.string.server_url) + "todos/user/" + userID;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(JSONArray response) {
//                                        textView.setText("Response: " + response.toString());
                        Log.d("editTodo", response.toString());
                        todoList = new ArrayList<>();
                        // 创建任务列表适配器
                        todoAdapter = new TodoAdapter(requireContext(), todoList);
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                Todo todo = new Todo();
                                todo.setId(jsonObject.getLong("id"));
                                todo.setTaskName(jsonObject.getString("taskName"));
                                todo.setTaskTime(jsonObject.getString("taskTime"));
                                todo.setTaskNotes(jsonObject.getString("taskNotes"));
                                todo.setTaskRepeat(jsonObject.getBoolean("taskRepeat"));
                                todo.setTaskCycleTot(jsonObject.getInt("taskCycleTot"));
                                todo.setTaskCycleCount(jsonObject.getInt("taskCycleCount"));
                                todo.setDone(jsonObject.getBoolean("done"));
                                todoList.add(todo);
                                if (!todo.isDone()) {
                                    todoAdapter.addUnfinishedTaskCount();
                                }
                            }
                            // 将适配器设置给ListView
                            todoListView.setAdapter(todoAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("show all", error.toString());
                    }
                });
        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(TodoFragment.this.context).addToRequestQueue(jsonArrayRequest);
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
        JSONObject jsonObject = new JSONObject();
        String url = getString(R.string.server_url) + "todos";
        try {
            jsonObject.put("userId", Global.userID + "");
            jsonObject.put("isDone", todo.isDone());
            jsonObject.put("taskName", todo.getTaskName());
            jsonObject.put("taskTime", todo.getTaskTime());
            jsonObject.put("taskNotes", todo.getTaskNotes());
            jsonObject.put("taskRepeat", todo.isTaskRepeat());
            jsonObject.put("taskCycleTot", todo.getTaskCycleTot());
            jsonObject.put("taskCycleCount", todo.getTaskCycleCount());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(JSONObject response) {
//                                        textView.setText("Response: " + response.toString());
                        Log.d("addTodo", response.toString());
                        try {
                            todo.setId(response.getLong("id"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("addTodo", error.toString());
                    }
                });

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(TodoFragment.this.context).addToRequestQueue(jsonObjectRequest);
    }

    // 设置已有任务
    private void setTask(int pos, Todo todo) {
        todoList.set(pos, todo);
        todoAdapter.notifyDataSetChanged();
        JSONObject jsonObject = new JSONObject();
        String url = getString(R.string.server_url) + "todos/" + todo.getId();
        try {
            jsonObject.put("id", todo.getId());
            jsonObject.put("userId", Global.userID + "");
            jsonObject.put("isDone", todo.isDone());
            jsonObject.put("taskName", todo.getTaskName());
            jsonObject.put("taskTime", todo.getTaskTime());
            jsonObject.put("taskNotes", todo.getTaskNotes());
            jsonObject.put("taskRepeat", todo.isTaskRepeat());
            jsonObject.put("taskCycleTot", todo.getTaskCycleTot());
            jsonObject.put("taskCycleCount", todo.getTaskCycleCount());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.PUT, url, jsonObject, new Response.Listener<JSONObject>() {

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(JSONObject response) {
//                                        textView.setText("Response: " + response.toString());
                        Log.d("editTodo", response.toString());
                    }
                }, new Response.ErrorListener() {

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
//                                        textView.setText("Error:"+error.toString());
                        Log.e("editTodo", error.toString());
                    }
                });

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(TodoFragment.this.context).addToRequestQueue(jsonObjectRequest);
    }

    @Override
    public void onTaskCompleted(String result) {
        Log.d("MainActivity", "Result: " + result);
    }
}
