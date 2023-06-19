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
import com.whu.tomado.pojo.Nodo;
import com.whu.tomado.ui.adapter.NodoAdapter;
import com.whu.tomado.ui.utils.MySingleton;
import com.whu.tomado.ui.utils.NodoTaskViewUtils;
import com.whu.tomado.utils.Global;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class NodoFragment extends Fragment {

    private NodoAdapter nodoAdapter;
    private List<Nodo> nodoList= new ArrayList<>();

    private Context context;

    private ListView nodoListView;

    public NodoFragment() {
    }

    public NodoFragment(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.nodo, container, false);

        nodoListView = view.findViewById(R.id.nodoListView);

        // 如果nodoListView为空，则显示提示信息，否则显示任务列表
        nodoListView.setEmptyView(view.findViewById(R.id.nodoEmptyView));
        Button addNodoButton = view.findViewById(R.id.addNodoButton);

        // 初始化任务列表数据
        // nodoList = new ArrayList<>();
        getNodoListById(Global.userID);
        // 创建任务列表适配器
        // nodoAdapter = new NodoAdapter(requireContext(), nodoList);

        // 将适配器设置给ListView
        // nodoListView.setAdapter(nodoAdapter);

        // 当长按某个item时，弹出一个对话框，询问用户是否删除该任务
        nodoListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {

                // 创建一个delete_nodo对话框
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("删除任务");
                builder.setMessage("确定删除该任务吗？");

                // 添加确定按钮及点击事件
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 处理确定按钮点击事件

                         String url = getString(R.string.server_url) + "nodos/" + nodoList.get(position).getId();
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
                        MySingleton.getInstance(NodoFragment.this.context).addToRequestQueue(jsonArrayRequest);
                        nodoList.remove(position);
                        nodoAdapter.notifyDataSetChanged();
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
        nodoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // position 参数表示用户点击的项的位置

                // 创建一个edit_nodo对话框
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                // 设置对话框标题
                builder.setTitle("任务详情");

                // 添加add_nodo布局文件
                View dialogView = getLayoutInflater().inflate(R.layout.add_nodo, null);

                // 设置对话框显示的View对象
                builder.setView(dialogView);

                NodoTaskViewUtils.setTaskView(dialogView,position,nodoList,context);

                // 添加按钮及点击事件
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Nodo nodo = nodoList.get(position);
                        nodo = NodoTaskViewUtils.getAddOrEditNodoInfo(dialogView,context,nodo);
                        setTask(position, nodo);
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
        addNodoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("添加NODO");
//                builder.setMessage("这是一个弹出页面的示例");

                // 添加自定义布局
                View dialogView = getLayoutInflater().inflate(R.layout.add_nodo, null);
                builder.setView(dialogView);

                NodoTaskViewUtils.addTaskVIew(dialogView,context);

                // 添加按钮及点击事件

                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Nodo nodo = NodoTaskViewUtils.getAddOrEditNodoInfo(dialogView,context,null);
                        if(nodo != null) {
                            addNewTask(nodo);
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

    private void getNodoListById(long userID) {
        String url = getString(R.string.server_url) + "nodos/user/" + userID;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(JSONArray response) {
//                                        textView.setText("Response: " + response.toString());
                        Log.d("editNodo", response.toString());
                        nodoList = new ArrayList<>();
                        // 创建任务列表适配器
                        nodoAdapter = new NodoAdapter(requireContext(), nodoList);
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                Nodo nodo = new Nodo();
                                nodo.setId(jsonObject.getLong("id"));
                                nodo.setTaskName(jsonObject.getString("taskName"));
                                nodo.setTaskTime(jsonObject.getString("taskTime"));
                                nodo.setTaskNotes(jsonObject.getString("taskNotes"));
                                nodo.setTaskRepeat(jsonObject.getBoolean("taskRepeat"));
                                nodo.setTaskCycleTot(jsonObject.getInt("taskCycleTot"));
                                nodo.setTaskCycleCount(jsonObject.getInt("taskCycleCount"));
                                nodo.setDone(jsonObject.getBoolean("done"));
                                nodoList.add(nodo);
                                if (!nodo.isDone()) {
                                    nodoAdapter.addUnfinishedTaskCount();
                                }
                            }
                            // 将适配器设置给ListView
                            nodoListView.setAdapter(nodoAdapter);
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
        MySingleton.getInstance(NodoFragment.this.context).addToRequestQueue(jsonArrayRequest);
    }
    

    // 当点击某个任务时，弹出详情界面
    public void showTaskDetail(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("任务详情");
    }

    // 修改任务
    public void modifyTask(int position, String taskName, String taskTime, String taskNotes, int taskCycleTot, boolean taskRepeat) {
        nodoList.get(position).setTaskName(taskName);
        nodoList.get(position).setTaskTime(taskTime);
        nodoList.get(position).setTaskNotes(taskNotes);
        nodoList.get(position).setTaskCycleTot(taskCycleTot);
//        nodoList.get(position).setTaskCycleTime(taskCycleTime);
        nodoList.get(position).setTaskRepeat(taskRepeat);
        nodoAdapter.notifyDataSetChanged();
    }

    //删除任务
    public void deleteTask(int position) {
        nodoList.remove(position);
        nodoAdapter.notifyDataSetChanged();
    }

    // 添加新任务
    private void addNewTask(Nodo nodo) {
//        nodoList.add(nodoAdapter.getUnfinishedTaskCount(), nodo);
        nodoList.add(0, nodo);
        nodoAdapter.addUnfinishedTaskCount();
        nodoAdapter.notifyDataSetChanged();
        JSONObject jsonObject = new JSONObject();
        String url = getString(R.string.server_url) + "nodos";
        try {
            jsonObject.put("userId", Global.userID + "");
            jsonObject.put("isDone", nodo.isDone());
            jsonObject.put("taskName", nodo.getTaskName());
            jsonObject.put("taskTime", nodo.getTaskTime());
            jsonObject.put("taskNotes", nodo.getTaskNotes());
            jsonObject.put("taskRepeat", nodo.isTaskRepeat());
            jsonObject.put("taskCycleTot", nodo.getTaskCycleTot());
            jsonObject.put("taskCycleCount", nodo.getTaskCycleCount());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(JSONObject response) {
//                                        textView.setText("Response: " + response.toString());
                        Log.d("addNodo", response.toString());
                        try {
                            nodo.setId(response.getLong("id"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("addNodo", error.toString());
                    }
                });

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(NodoFragment.this.context).addToRequestQueue(jsonObjectRequest);
    }

    // 设置已有任务
    private void setTask(int pos, Nodo nodo) {
        nodoList.set(pos, nodo);
        nodoAdapter.notifyDataSetChanged();
        JSONObject jsonObject = new JSONObject();
        String url = getString(R.string.server_url) + "nodos/" + nodo.getId();
        try {
            jsonObject.put("id", nodo.getId());
            jsonObject.put("userId", Global.userID + "");
            jsonObject.put("isDone", nodo.isDone());
            jsonObject.put("taskName", nodo.getTaskName());
            jsonObject.put("taskTime",nodo.getTaskTime());
            jsonObject.put("taskNotes", nodo.getTaskNotes());
            jsonObject.put("taskRepeat", nodo.isTaskRepeat());
            jsonObject.put("taskCycleTot", nodo.getTaskCycleTot());
            jsonObject.put("taskCycleCount", nodo.getTaskCycleCount());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.PUT, url, jsonObject, new Response.Listener<JSONObject>() {

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(JSONObject response) {
//                                        textView.setText("Response: " + response.toString());
                        Log.d("editNodo", response.toString());
                    }
                }, new Response.ErrorListener() {

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
//                                        textView.setText("Error:"+error.toString());
                        Log.e("editNodo", error.toString());
                    }
                });

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(NodoFragment.this.context).addToRequestQueue(jsonObjectRequest);
    }
    

}
