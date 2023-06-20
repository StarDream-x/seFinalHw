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
import android.widget.TextView;
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
import com.whu.tomado.network.VolleyCallback;
import com.whu.tomado.network.VolleyExample;
import com.whu.tomado.pojo.Team;
import com.whu.tomado.pojo.Todo;
import com.whu.tomado.ui.adapter.TeamAdapter;
import com.whu.tomado.ui.adapter.TodoAdapter;
import com.whu.tomado.ui.utils.MySingleton;
import com.whu.tomado.ui.utils.TodoTaskViewUtils;
import com.whu.tomado.utils.Global;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TeamFragment extends Fragment implements AddTodoTask.OnTaskCompleted {

    private TodoAdapter todoAdapter;
    private TeamAdapter teamAdapter;
    private List<Todo> todoList;
    private List<Team> teamList;

    private int mode = 1;
    private long tmid = 0;
    private int tmType = 0;

    ListView todoListView;

    TextView todoEmptyView;

    private View view;
    private Context context;

    public TeamFragment() {
    }

    public TeamFragment(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.team, container, false);

        todoListView = view.findViewById(R.id.todoListView);

        switchMode(1);

        //Added By Hong Weijun 可删 upd: 现在别删了
        todoEmptyView = view.findViewById(R.id.todoEmptyView);
        //todoEmptyView.setText("敬请期待");
        todoListView.setEmptyView(todoEmptyView);

        //某个CheckBox被选中或取消选中时，修改任务状态
        return view;
    }

    private boolean checkTodoExist(long ntid){
        for(Todo todo: todoList){
            if(todo.getId() == ntid) return true;
        }
        return false;
    }
    private void getTodoListById(long userID) {
        JSONArray jsonArray = new JSONArray();

        String url = getString(R.string.server_url)+"todos/user/"+((Long)(userID)).toString();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, url, jsonArray, new Response.Listener<JSONArray>() {

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("editTodo",response.toString());
                        try {
                            int sz = response.length();
                            for(int i = 0; i < sz; i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                if(!checkTodoExist(jsonObject.getLong("id"))){
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
                            }
                            todoListView.setAdapter(todoAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
//                                        textView.setText("Error:"+error.toString());
                        Log.e("addTodo",error.toString());
                    }
                });

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(TeamFragment.this.context).addToRequestQueue(jsonArrayRequest);
        RequestQueue requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        requestQueue.add(jsonArrayRequest);
        return;
    }

    private boolean checkTeamExist(long ntid){
        for(Team team: teamList){
            if(team.getId() == ntid) return true;
        }
        return false;
    }

    private void getTeamListById(long userID) {
        JSONObject jsonObject = new JSONObject();

        String url = getString(R.string.server_url)+"manageTeam?uid="+Global.userID;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, jsonObject, new Response.Listener<JSONObject>() {

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(response!=null){
                                String mTeams = response.getString("manageTeams");
                                char ch;int x=0;boolean flag = false;
                                for(int i = 0; i < mTeams.length(); ++i){
                                    ch = mTeams.charAt(i);
                                    if(ch>=48&&ch<=57) {x=(x<<3)+(x<<1)+ch-48;flag=true;}
                                    else{
                                        if(flag&&(!checkTeamExist(x))){
                                            Team team = new Team();
                                            team.setId(x);team.setType(1);
                                            team.setDesc("我管理的");
                                            teamList.add(team);
                                        }
                                        x=0;flag=false;
                                    }
                                }
                                if(flag&&(!checkTeamExist(x))){
                                    Team team = new Team();
                                    team.setId(x);team.setType(1);
                                    team.setDesc("我管理的");
                                    teamList.add(team);
                                }

                                mTeams = response.getString("memTeams");
                                x=0;flag = false;
                                for(int i = 0; i < mTeams.length(); ++i){
                                    ch = mTeams.charAt(i);
                                    if(ch>=48&&ch<=57) {x=(x<<3)+(x<<1)+ch-48;flag=true;}
                                    else{
                                        if(flag&&(!checkTeamExist(x))){
                                            Team team = new Team();
                                            team.setId(x);team.setType(2);
                                            team.setDesc("我加入的");
                                            teamList.add(team);
                                        }
                                        x=0;flag=false;
                                    }
                                }
                                if(flag&&(!checkTeamExist(x))){
                                    Team team = new Team();
                                    team.setId(x);team.setType(2);
                                    team.setDesc("我加入的");
                                    teamList.add(team);
                                }
                            }
                            todoListView.setAdapter(teamAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
//                                        textView.setText("Error:"+error.toString());
                        Log.e("addTodo",error.toString());
                    }
                });

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(TeamFragment.this.context).addToRequestQueue(jsonObjectRequest);
        RequestQueue requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        requestQueue.add(jsonObjectRequest);
        return;
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
        String url = getString(R.string.server_url)+"todos";
        try{
            jsonObject.put("userId", (tmid + Global.userUpLimit)+"");
            jsonObject.put("done",todo.isDone());
            jsonObject.put("taskName",todo.getTaskName());
            jsonObject.put("taskTime",todo.getTaskTime());
            jsonObject.put("taskNotes",todo.getTaskNotes());
            jsonObject.put("taskCycleTot",todo.getTaskCycleTot());
            jsonObject.put("taskCycleCount",todo.getTaskCycleCount());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(JSONObject response) {
//                                        textView.setText("Response: " + response.toString());
                        Log.d("addTodo",response.toString());
                    }
                }, new Response.ErrorListener() {

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
//                                        textView.setText("Error:"+error.toString());
                        Log.e("addTodo",error.toString());
                    }
                });

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(TeamFragment.this.context).addToRequestQueue(jsonObjectRequest);
        RequestQueue requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        requestQueue.add(jsonObjectRequest);
    }

    // 设置已有任务
    private void setTask(int pos, Todo todo) {
        todoList.set(0, todo);
        todoAdapter.notifyDataSetChanged();
        JSONObject jsonObject = new JSONObject();
        String url = getString(R.string.server_url)+"todos";
        try{
            jsonObject.put("userId", (tmid+Global.userUpLimit)+"");
            jsonObject.put("done",todo.isDone());
            jsonObject.put("taskName",todo.getTaskName());
            jsonObject.put("taskTime",todo.getTaskTime());
            jsonObject.put("taskNotes",todo.getTaskNotes());
            jsonObject.put("taskCycleTot",todo.getTaskCycleTot());
            jsonObject.put("taskCycleCount",todo.getTaskCycleCount());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(JSONObject response) {
//                                        textView.setText("Response: " + response.toString());
                        Log.d("addTodo",response.toString());
                    }
                }, new Response.ErrorListener() {

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
//                                        textView.setText("Error:"+error.toString());
                        Log.e("addTodo",error.toString());
                    }
                });

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(TeamFragment.this.context).addToRequestQueue(jsonObjectRequest);
        RequestQueue requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        requestQueue.add(jsonObjectRequest);
    }

    private void switchMode(int mode){
        this.mode=mode;
        if(mode == 1){
            view.findViewById(R.id.todoEmptyView).setVisibility(View.INVISIBLE);
            view.findViewById(R.id.addTodoButton).setVisibility(View.INVISIBLE);
            view.findViewById(R.id.backButton).setVisibility(View.INVISIBLE);

            teamList = new ArrayList<>();
            getTeamListById(Global.userID);
            teamAdapter = new TeamAdapter(requireContext(), teamList);

            todoListView.setAdapter(teamAdapter);

            todoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    tmid = teamList.get(position).getId();
                    tmType = teamList.get(position).getType();
                    switchMode(2);
                }
            });

        }
        else if(mode == 2){
            // 如果todoListView为空，则显示提示信息，否则显示任务列表
            todoListView.setEmptyView(view.findViewById(R.id.todoEmptyView));
            Button addTodoButton = view.findViewById(R.id.addTodoButton);
            Button backButton = view.findViewById(R.id.backButton);
            view.findViewById(R.id.todoEmptyView).setVisibility(View.VISIBLE);
            view.findViewById(R.id.addTodoButton).setVisibility(View.VISIBLE);
            view.findViewById(R.id.backButton).setVisibility(View.VISIBLE);

            // 初始化任务列表数据
            todoList = new ArrayList<>();
            getTodoListById(tmid+Global.userUpLimit);
            todoAdapter = new TodoAdapter(requireContext(), todoList);
            // 将适配器设置给ListView
            todoListView.setAdapter(todoAdapter);
            todoAdapter.setTmType(tmType);
            todoAdapter.setTmid(tmid);

            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switchMode(1);
                }
            });

            if(tmType == 1){
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
                                Todo todo = TodoTaskViewUtils.getAddOrEditTodoInfo(dialogView,context, todo_old);
                                if( !todo_old.getTaskName().equals(todo.getTaskName()) || !todo_old.getTaskNotes().equals(todo.getTaskNotes())
                                        || !todo_old.getTaskTime().equals(todo.getTaskTime())
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
                                Todo todo = TodoTaskViewUtils.getAddOrEditTodoInfo(dialogView,context, new Todo());

                                if(todo != null) {
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
            }
            else{
                // 当长按某个item时，弹出一个对话框，询问用户是否删除该任务
                todoListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view,
                                                   int position, long id) {
                        return true;
                    }
                });

                // 当点击单个item时，弹出一个对话框，显示任务的详细信息
                todoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    }
                });


                // 设置"添加任务"按钮的点击事件
                // 在您希望弹出页面的位置，例如点击一个按钮时
                addTodoButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                });
            }
        }
    }

    @Override
    public void onTaskCompleted(String result) {
        Log.d("MainActivity", "Result: " + result);
    }
}
