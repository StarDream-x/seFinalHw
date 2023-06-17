package com.whu.tomado.ui.fragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ListView;
import android.view.View;


import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.whu.tomado.R;
import com.whu.tomado.pojo.Nodo;
import com.whu.tomado.ui.adapter.NodoAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class NodoFragment extends Fragment {

    private ListView nodoListView;
    private Button addNodoButton;
    private NodoAdapter nodoAdapter;
    private List<Nodo> nodoList;

    private TextView taskTimeTextView;
    private EditText taskNotesEditText;
    private EditText taskNameEditText;

    private Context context;

    public NodoFragment(){}

    public NodoFragment(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.nodo, container, false);

        nodoListView = view.findViewById(R.id.nodoListView);

        // 如果nodoListView为空，则显示提示信息，否则显示任务列表
        nodoListView.setEmptyView(view.findViewById(R.id.nodoEmptyView));
        addNodoButton = view.findViewById(R.id.addNodoButton);

        // 初始化任务列表数据
        nodoList = new ArrayList<>();
//        nodoList.add("任务1");
//        nodoList.add("任务2");
//        nodoList.add("任务3");

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
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // position 参数表示用户点击的项的位置

                // 创建一个edit_nodo对话框
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("任务详情");

                // 添加自定义布局
                View dialogView = getLayoutInflater().inflate(R.layout.edit_nodo, null);
                builder.setView(dialogView);
                // 获取布局中的控件
                taskNameEditText = dialogView.findViewById(R.id.taskNameEditText);
                //设置text内容
                taskNameEditText.setText(nodoList.get(position).getTaskName());

                taskTimeTextView = dialogView.findViewById(R.id.taskTimeTextView);
                //设置text内容
                taskTimeTextView.setText(nodoList.get(position).getTaskTime());

                taskNotesEditText = dialogView.findViewById(R.id.taskNotesEditText);
                //设置text内容
                taskNotesEditText.setText(nodoList.get(position).getTaskNotes());

                taskTimeTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDatePicker();
                    }
                });

                // 添加按钮及点击事件
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 处理确定按钮点击事件
                        taskNameEditText = dialogView.findViewById(R.id.taskNameEditText);
                        taskNotesEditText = dialogView.findViewById(R.id.taskNotesEditText);

                        String taskName = taskNameEditText.getText().toString();
                        String taskTime = taskTimeTextView.getText().toString();
                        String taskNotes = taskNotesEditText.getText().toString();

                        setTask(position,taskName,taskTime,taskNotes);
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

                taskTimeTextView = dialogView.findViewById(R.id.taskTimeTextView);
                taskTimeTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDatePicker();
                    }
                });

                // 添加按钮及点击事件
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 处理确定按钮点击事件
                        taskNameEditText = dialogView.findViewById(R.id.taskNameEditText);
                        taskNotesEditText = dialogView.findViewById(R.id.taskNotesEditText);

                        String taskName = taskNameEditText.getText().toString();
                        String taskTime = taskTimeTextView.getText().toString();
                        String taskNotes = taskNotesEditText.getText().toString();

                        // taskName为空
                        if (taskName.isEmpty()) {
                            // 弹出警告框，警告框内容为"任务名不能为空"，警告框标题为"警告"，警告框按钮为"确定"
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setTitle("警告");
                            builder.setMessage("任务名不能为空");
                            builder.setPositiveButton("确定", null);
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                            return;
                        }

                        addNewTask(taskName,taskTime,taskNotes);
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
    public void modifyTask(int position, String taskName, String taskTime, String taskNotes){
        nodoList.get(position).setTaskName(taskName);
        nodoList.get(position).setTaskTime(taskTime);
        nodoList.get(position).setTaskNotes(taskNotes);
        nodoAdapter.notifyDataSetChanged();
    }

    //删除任务
    public void deleteTask(int position){
        nodoList.remove(position);
        nodoAdapter.notifyDataSetChanged();
    }

    // 添加新任务
    private void addNewTask(String taskName, String taskTime, String taskNotes) {
        nodoList.add(new Nodo(taskName, taskTime, taskNotes));
        nodoAdapter.addUnfinishedTaskCount();
        nodoAdapter.notifyDataSetChanged();
    }

    // 设置已有任务
    private void setTask(int pos ,String taskName, String taskTime, String taskNotes) {
        nodoList.set(pos,new Nodo(taskName, taskTime, taskNotes));
        nodoAdapter.notifyDataSetChanged();
    }

    // 显示日期选择器
    private void showDatePicker() {
        // 获取当前日期
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        // 创建并显示DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                // 更新TextView的文本为选定的日期
                String selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
                taskTimeTextView.setText(selectedDate);
            }
        }, year, month, dayOfMonth);
        datePickerDialog.show();
    }
}
