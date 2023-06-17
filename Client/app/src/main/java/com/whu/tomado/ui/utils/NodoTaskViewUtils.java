package com.whu.tomado.ui.utils;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.whu.tomado.R;
import com.whu.tomado.pojo.Nodo;

import java.util.Calendar;
import java.util.List;

public class NodoTaskViewUtils {
    @SuppressLint("StaticFieldLeak")
    private static TextView taskTimeTextView;
    @SuppressLint("StaticFieldLeak")
    private static EditText taskNotesEditText;
    @SuppressLint("StaticFieldLeak")
    private static EditText taskNameEditText;
    @SuppressLint("StaticFieldLeak")
    private static EditText taskCycleTotEditText;
//    @SuppressLint("StaticFieldLeak")
//    private static EditText taskCycleTimeEditText;
    @SuppressLint("StaticFieldLeak")
    private static CheckBox taskRepeatCheckBox;

    private static final View.OnClickListener taskRepeatCheckBoxClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            boolean taskRepeat = ((CheckBox) v).isChecked();
            if (taskRepeat) {
//                taskTimeTextView.setVisibility(View.GONE);
                taskCycleTotEditText.setVisibility(View.VISIBLE);
//                taskCycleTimeEditText.setVisibility(View.VISIBLE);
            } else {
//                taskTimeTextView.setVisibility(View.VISIBLE);
                taskCycleTotEditText.setVisibility(View.GONE);
//                taskCycleTimeEditText.setVisibility(View.GONE);
            }
        }
    };

    @SuppressLint("SetTextI18n")
    public static void setTaskView(View dialogView, int position, List<Nodo> nodoList, Context context) {

        // 获取布局中的控件
        taskNameEditText = dialogView.findViewById(R.id.taskNameEditText);
        taskNameEditText.setText(nodoList.get(position).getTaskName());

        taskTimeTextView = dialogView.findViewById(R.id.taskTimeTextView);
        taskTimeTextView.setText(nodoList.get(position).getTaskTime());

        taskNotesEditText = dialogView.findViewById(R.id.taskNotesEditText);
        taskNotesEditText.setText(nodoList.get(position).getTaskNotes());

        taskCycleTotEditText = dialogView.findViewById(R.id.taskCycleTotEditText);

//        taskCycleTimeEditText = dialogView.findViewById(R.id.taskCycleTimeEditText);

        taskRepeatCheckBox = dialogView.findViewById(R.id.taskRepeat);
        taskRepeatCheckBox.setChecked(nodoList.get(position).getTaskRepeat());
        if (nodoList.get(position).getTaskRepeat()) {
            taskCycleTotEditText.setText(nodoList.get(position).getTaskCycleTot() + "");
//            taskCycleTimeEditText.setText(nodoList.get(position).getTaskCycleTime() + "");

//            taskTimeTextView.setVisibility(View.GONE);
            taskCycleTotEditText.setVisibility(View.VISIBLE);
//            taskCycleTimeEditText.setVisibility(View.VISIBLE);
        }
        taskTimeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(context);
            }
        });
        taskRepeatCheckBox.setOnClickListener(taskRepeatCheckBoxClickListener);
    }

    public static void addTaskVIew(View dialogView, Context context){
        // 获取组件
        taskNameEditText = dialogView.findViewById(R.id.taskNameEditText);
        taskNotesEditText = dialogView.findViewById(R.id.taskNotesEditText);
        taskCycleTotEditText = dialogView.findViewById(R.id.taskCycleTotEditText);
//        taskCycleTimeEditText = dialogView.findViewById(R.id.taskCycleTimeEditText);
        taskRepeatCheckBox = dialogView.findViewById(R.id.taskRepeat);
        taskTimeTextView = dialogView.findViewById(R.id.taskTimeTextView);

        taskTimeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(context);
            }
        });
        taskRepeatCheckBox.setOnClickListener(taskRepeatCheckBoxClickListener);
    }

    // 获取对应Nodo的详细信息
    public static Nodo getAddOrEditNodoInfo(View dialogView, Context context){
        // 处理确定按钮点击事件
        taskNameEditText = dialogView.findViewById(R.id.taskNameEditText);
        taskNotesEditText = dialogView.findViewById(R.id.taskNotesEditText);
        taskCycleTotEditText = dialogView.findViewById(R.id.taskCycleTotEditText);
//        taskCycleTimeEditText = dialogView.findViewById(R.id.taskCycleTimeEditText);
        taskRepeatCheckBox = dialogView.findViewById(R.id.taskRepeat);

        //final boolean[] taskRepeat_onClick = {false};

        String taskName = taskNameEditText.getText().toString();
        String taskTime = taskTimeTextView.getText().toString();
        String taskNotes = taskNotesEditText.getText().toString();

        String taskCycleTot_string = taskCycleTotEditText.getText().toString();
//        String taskCycleTime_string = taskCycleTimeEditText.getText().toString();
        boolean taskRepeat = taskRepeatCheckBox.isChecked();

        // taskName为空
        if (taskName.isEmpty()) {
            Warning("任务名不能为空",context);
            return null;
        }

        // taskCycleTot/taskCycleTime 转为整数或报错
        int taskCycleTot = toInt(taskCycleTot_string, taskRepeat, "重复次数应为整数",context);
        if (taskCycleTot == -1) return null;
//        int taskCycleTime = toInt(taskCycleTime_string, taskRepeat, "每个周期天数应为正整数",context);
//        if (taskCycleTime == -1) return null;
        return new Nodo(taskName, taskTime, taskNotes, taskCycleTot, taskRepeat);
    }


    private static void Warning(String warningInfo,Context context) {
        // 弹出警告框，警告框内容为"任务名不能为空"，警告框标题为"警告"，警告框按钮为"确定"
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("警告");
        builder.setMessage(warningInfo);
        builder.setPositiveButton("确定", null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private static int toInt(String s, boolean taskRepeat, String warningInfo, Context context) {
        int biaoji = 0, ret = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) >= '0' && s.charAt(i) <= '9')
                ret = ret * 10 + s.charAt(i) - '0';
            else {
                biaoji = 1;
                break;
            }
        }
        if ((biaoji == 1 || ret == 0) && taskRepeat) {
            Warning(warningInfo,context);
            return -1;
        }
        return ret;
    }


    // 显示日期选择器
    private static void showDatePicker(Context context){
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
