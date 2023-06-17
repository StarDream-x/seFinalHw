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
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ListView;
import android.view.View;


import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.whu.tomado.R;
import com.whu.tomado.pojo.Todo;
import com.whu.tomado.ui.adapter.TodoAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TodoFragment extends Fragment {

    private ListView todoListView;
    private Button addTodoButton;
    private TodoAdapter todoAdapter;
    private List<Todo> todoList;

    private TextView taskTimeTextView;
    private EditText taskNotesEditText;
    private EditText taskNameEditText;
    private EditText taskCycleTotEditText;
    private EditText taskCycleTimeEditText;
    private CheckBox taskRepeatCheckBox;

    private Context context;

    public TodoFragment(){}

    public TodoFragment(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.todo, container, false);

        todoListView = view.findViewById(R.id.todoListView);

        // 如果todoListView为空，则显示提示信息，否则显示任务列表
        todoListView.setEmptyView(view.findViewById(R.id.todoEmptyView));
        addTodoButton = view.findViewById(R.id.addTodoButton);

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
                builder.setTitle("任务详情");

                // 添加自定义布局
                View dialogView = getLayoutInflater().inflate(R.layout.add_todo, null);
                builder.setView(dialogView);

                // 获取布局中的控件
                taskNameEditText = dialogView.findViewById(R.id.taskNameEditText);
                //设置text内容
                taskNameEditText.setText(todoList.get(position).getTaskName());

                taskTimeTextView = dialogView.findViewById(R.id.taskTimeTextView);
                taskTimeTextView.setText(todoList.get(position).getTaskTime());

                taskNotesEditText = dialogView.findViewById(R.id.taskNotesEditText);
                taskNotesEditText.setText(todoList.get(position).getTaskNotes());

                taskCycleTotEditText = dialogView.findViewById(R.id.taskCycleTotEditText);

                taskCycleTimeEditText = dialogView.findViewById(R.id.taskCycleTimeEditText);

                taskRepeatCheckBox = dialogView.findViewById(R.id.taskRepeat);
                taskRepeatCheckBox.setChecked(todoList.get(position).getTaskRepeat());
                if(todoList.get(position).getTaskRepeat()){
                    taskCycleTotEditText.setText(todoList.get(position).getTaskCycleTot()+"");
                    taskCycleTimeEditText.setText(todoList.get(position).getTaskCycleTime()+"");

                    taskTimeTextView.setVisibility(View.GONE);
                    taskCycleTotEditText.setVisibility(View.VISIBLE);
                    taskCycleTimeEditText.setVisibility(View.VISIBLE);
                }
                taskTimeTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDatePicker();
                    }
                });
                taskRepeatCheckBox.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        boolean taskRepeat = ((CheckBox)v).isChecked();
                        if(taskRepeat){
                            taskTimeTextView.setVisibility(View.GONE);
                            taskCycleTotEditText.setVisibility(View.VISIBLE);
                            taskCycleTimeEditText.setVisibility(View.VISIBLE);
                        }else{
                            taskTimeTextView.setVisibility(View.VISIBLE);
                            taskCycleTotEditText.setVisibility(View.GONE);
                            taskCycleTimeEditText.setVisibility(View.GONE);
                        }
                    }
                });

                // 添加按钮及点击事件
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 处理确定按钮点击事件
                        taskNameEditText = dialogView.findViewById(R.id.taskNameEditText);
                        taskNotesEditText = dialogView.findViewById(R.id.taskNotesEditText);
                        taskCycleTotEditText = dialogView.findViewById(R.id.taskCycleTotEditText);
                        taskCycleTimeEditText = dialogView.findViewById(R.id.taskCycleTimeEditText);
                        taskRepeatCheckBox = dialogView.findViewById(R.id.taskRepeat);

                        //final boolean[] taskRepeat_onClick = {false};

                        String taskName = taskNameEditText.getText().toString();
                        String taskTime = taskTimeTextView.getText().toString();
                        String taskNotes = taskNotesEditText.getText().toString();

                        String taskCycleTot_string = taskCycleTotEditText.getText().toString();
                        String taskCycleTime_string = taskCycleTimeEditText.getText().toString();
                        boolean taskRepeat=taskRepeatCheckBox.isChecked();

                        // taskName为空
                        if (taskName.isEmpty()) {
                            Warning("任务名不能为空");
                            return;
                        }

                        // taskCycleTot/taskCycleTime 转为整数或报错
                        int taskCycleTot=toInt(taskCycleTot_string, taskRepeat, "完成的周期数应为整数");
                        if(taskCycleTot == -1)  return;
                        int taskCycleTime=toInt(taskCycleTime_string, taskRepeat, "每个周期天数应为正整数");
                        if(taskCycleTime == -1)  return;

                        setTask(position, taskName, taskTime, taskNotes, taskCycleTot, taskCycleTime, taskRepeat);
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

                // 获取组件
                taskNameEditText = dialogView.findViewById(R.id.taskNameEditText);
                taskNotesEditText = dialogView.findViewById(R.id.taskNotesEditText);
                taskCycleTotEditText = dialogView.findViewById(R.id.taskCycleTotEditText);
                taskCycleTimeEditText = dialogView.findViewById(R.id.taskCycleTimeEditText);
                taskRepeatCheckBox = dialogView.findViewById(R.id.taskRepeat);
                taskTimeTextView = dialogView.findViewById(R.id.taskTimeTextView);

                taskTimeTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDatePicker();
                    }
                });
                taskRepeatCheckBox.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        boolean taskRepeat = ((CheckBox)v).isChecked();

                        if(taskRepeat){
                            taskTimeTextView.setVisibility(View.GONE);
                            taskCycleTotEditText.setVisibility(View.VISIBLE);
                            taskCycleTimeEditText.setVisibility(View.VISIBLE);
                        }else{
                            taskTimeTextView.setVisibility(View.VISIBLE);
                            taskCycleTotEditText.setVisibility(View.GONE);
                            taskCycleTimeEditText.setVisibility(View.GONE);
                        }
                    }
                });

                // 添加按钮及点击事件
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 处理确定按钮点击事件
                        taskNameEditText = dialogView.findViewById(R.id.taskNameEditText);
                        taskNotesEditText = dialogView.findViewById(R.id.taskNotesEditText);
                        taskCycleTotEditText = dialogView.findViewById(R.id.taskCycleTotEditText);
                        taskCycleTimeEditText = dialogView.findViewById(R.id.taskCycleTimeEditText);
                        taskRepeatCheckBox = dialogView.findViewById(R.id.taskRepeat);

                        //final boolean[] taskRepeat_onClick = {false};

                        String taskName = taskNameEditText.getText().toString();
                        String taskTime = taskTimeTextView.getText().toString();
                        String taskNotes = taskNotesEditText.getText().toString();

                        String taskCycleTot_string = taskCycleTotEditText.getText().toString();
                        String taskCycleTime_string = taskCycleTimeEditText.getText().toString();
                        boolean taskRepeat=taskRepeatCheckBox.isChecked();

                        // taskName为空
                        if (taskName.isEmpty()) {
                            Warning("任务名不能为空");
                            return;
                        }

                        // taskCycleTot/taskCycleTime 转为整数或报错
                        int taskCycleTot=toInt(taskCycleTot_string, taskRepeat, "完成的周期数应为整数");
                        if(taskCycleTot == -1)  return;
                        int taskCycleTime=toInt(taskCycleTime_string, taskRepeat, "每个周期天数应为正整数");
                        if(taskCycleTime == -1)  return;

                        addNewTask(taskName,taskTime,taskNotes,taskCycleTot,taskCycleTime,taskRepeat);
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
    public void modifyTask(int position, String taskName, String taskTime, String taskNotes, int taskCycleTot, int taskCycleTime, boolean taskRepeat){
        todoList.get(position).setTaskName(taskName);
        todoList.get(position).setTaskTime(taskTime);
        todoList.get(position).setTaskNotes(taskNotes);
        todoList.get(position).setTaskCycleTot(taskCycleTot);
        todoList.get(position).setTaskCycleTime(taskCycleTime);
        todoList.get(position).setTaskRepeat(taskRepeat);
        todoAdapter.notifyDataSetChanged();
    }

    //删除任务
    public void deleteTask(int position){
        todoList.remove(position);
        todoAdapter.notifyDataSetChanged();
    }

    // 添加新任务
    private void addNewTask(String taskName, String taskTime, String taskNotes, int taskCycleTot, int taskCycleTime,boolean taskRepeat) {
        todoList.add(todoAdapter.getUnfinishedTaskCount(),new Todo(taskName, taskTime, taskNotes, taskCycleTot, taskCycleTime, taskRepeat));
        todoAdapter.addUnfinishedTaskCount();
        todoAdapter.notifyDataSetChanged();
    }

    // 设置已有任务
    private void setTask(int pos ,String taskName, String taskTime, String taskNotes, int taskCycleTotEditText, int taskCycleTimeEditText,boolean taskRepeat) {
        todoList.set(pos,new Todo(taskName, taskTime, taskNotes, taskCycleTotEditText, taskCycleTimeEditText, taskRepeat));
        todoAdapter.notifyDataSetChanged();
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

    private void Warning(String warningInfo)
    {
        // 弹出警告框，警告框内容为"任务名不能为空"，警告框标题为"警告"，警告框按钮为"确定"
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("警告");
        builder.setMessage(warningInfo);
        builder.setPositiveButton("确定", null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        return;
    }

    private int toInt(String s, boolean taskRepeat, String warningInfo)
    {
        int biaoji=0,ret=0;
        for(int i = 0; i < s.length(); i++)
        {
            if(s.charAt(i) >= '0' && s.charAt(i) <= '9')
                ret = ret * 10 + s.charAt(i) - '0';
            else
            {
                biaoji = 1;
                break;
            }
        }
        if((biaoji == 1 || ret == 0) && taskRepeat)
        {
            Warning(warningInfo);
            return -1;
        }
        return ret;
    }
}
