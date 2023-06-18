package com.whu.tomado.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;


import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.whu.tomado.R;
import com.whu.tomado.pojo.Nodo;
import com.whu.tomado.pojo.Todo;
import com.whu.tomado.ui.adapter.NodoAdapter;
import com.whu.tomado.ui.utils.NodoTaskViewUtils;

import java.util.ArrayList;
import java.util.List;

public class NodoFragment extends Fragment {

    private NodoAdapter nodoAdapter;
    private List<Nodo> nodoList;

    private Context context;

    public NodoFragment() {
    }

    public NodoFragment(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.nodo, container, false);

        ListView nodoListView = view.findViewById(R.id.nodoListView);

        // 如果nodoListView为空，则显示提示信息，否则显示任务列表
        nodoListView.setEmptyView(view.findViewById(R.id.nodoEmptyView));
        Button addNodoButton = view.findViewById(R.id.addNodoButton);

        // 初始化任务列表数据
        nodoList = new ArrayList<>();

        // 创建任务列表适配器
        nodoAdapter = new NodoAdapter(requireContext(), nodoList);

        // 将适配器设置给ListView
        nodoListView.setAdapter(nodoAdapter);

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
                        Nodo nodo_old = nodoList.get(position);
                        Nodo nodo = NodoTaskViewUtils.getAddOrEditNodoInfo(dialogView,context);
                        if( !nodo_old.getTaskName().equals(nodo.getTaskName()) || !nodo_old.getTaskNotes().equals(nodo.getTaskNotes())
                                || nodo_old.getTaskRepeat() != nodo.getTaskRepeat() || !nodo_old.getTaskTime().equals(nodo.getTaskTime())
                                || nodo_old.getTaskCycleTot() != nodo.getTaskCycleTot()) {
                            setTask(position, nodo);
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
                        Nodo nodo = NodoTaskViewUtils.getAddOrEditNodoInfo(dialogView,context);
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
        nodo.setTaskCycleCount(nodo.getTaskCycleTot());
        nodoList.add(0, nodo);
        nodoAdapter.addUnfinishedTaskCount();
        nodoAdapter.notifyDataSetChanged();
    }

    // 设置已有任务
    private void setTask(int pos, Nodo nodo) {
        nodo.setTaskCycleCount(nodo.getTaskCycleTot());
        nodoList.set(0, nodo);
        nodoAdapter.notifyDataSetChanged();
    }

}
