package com.whu.tomado.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.whu.tomado.R;
import com.whu.tomado.pojo.Todo;
import com.whu.tomado.ui.fragment.TodoFragment;
import com.whu.tomado.ui.utils.MySingleton;
import com.whu.tomado.utils.Global;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class TodoAdapter extends ArrayAdapter<Todo> {
    private LayoutInflater inflater;
    private List<Todo> todoList;
    private Context context;

    private int unfinishedTaskCount = 0;

    public int getUnfinishedTaskCount() {
        return unfinishedTaskCount;
    }

    public void setUnfinishedTaskCount(int unfinishedTaskCount) {
        this.unfinishedTaskCount = unfinishedTaskCount;
    }

    public void addUnfinishedTaskCount() {
        unfinishedTaskCount++;
    }

    // 构造函数
    public TodoAdapter(Context context, List<Todo> taskList) {
        super(context, 0, taskList);
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.todoList = taskList;
    }

    // 重写getView方法
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;
        if (itemView == null) {
            itemView = inflater.inflate(R.layout.one_task, parent, false);
        }
        Todo currentTask = getItem(position);
        // 设置任务完成情况
        CheckBox taskFinishedBox = itemView.findViewById(R.id.taskFinished);
        taskFinishedBox.setChecked(currentTask.isDone());
        TextView taskNameTextView = itemView.findViewById(R.id.taskNameTextView);
        TextView taskTimeTextView = itemView.findViewById(R.id.taskTimeTextView);
        TextView taskNotesTextView = itemView.findViewById(R.id.taskNotesTextView);
        ProgressBar taskProgressBar = itemView.findViewById(R.id.taskPrgressBar);
//        TextView taskCycleTotTextView = itemView.findViewById(R.id.taskCycleTotTextView);
//        TextView taskCycleTimeTextView = itemView.findViewById(R.id.taskCycleTimeTextView);
//        TextView taskCycleCountTextView = itemView.findViewById(R.id.taskCycleCountTextView);
//        TextView taskLastFinishedTextView = itemView.findViewById(R.id.taskLastFinishedTextView);
        taskNameTextView.setText(currentTask.getTaskName());
        taskTimeTextView.setText(currentTask.getTaskTime());
        taskNotesTextView.setText(currentTask.getTaskNotes());
        taskProgressBar.setProgress(currentTask.getTaskCycleCount());
//        taskCycleTotTextView.setText(currentTask.getTaskCycleTot());
//        taskCycleTimeTextView.setText(currentTask.getTaskCycleTime());
//        taskCycleCountTextView.setText(currentTask.getTaskCycleCount());
//        taskLastFinishedTextView.setText(currentTask.getTaskLastFinished());
        if(currentTask.isDone()) {
            taskNameTextView.setTextColor(context.getResources().getColor(R.color.half_black));
            taskNotesTextView.setTextColor(context.getResources().getColor(R.color.half_black));
            taskTimeTextView.setTextColor(context.getResources().getColor(R.color.half_black));
            taskNameTextView.setPaintFlags(taskNameTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            taskNotesTextView.setPaintFlags(taskNotesTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            taskTimeTextView.setPaintFlags(taskTimeTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }else{
            taskNameTextView.setTextColor(context.getResources().getColor(R.color.black));
            taskNotesTextView.setTextColor(context.getResources().getColor(R.color.black));
            taskTimeTextView.setTextColor(context.getResources().getColor(R.color.black));
            taskNameTextView.setPaintFlags(taskNameTextView.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
            taskNotesTextView.setPaintFlags(taskNotesTextView.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
            taskTimeTextView.setPaintFlags(taskTimeTextView.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
        }




        taskFinishedBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isChecked = ((CheckBox) v).isChecked();
                // 调用处理选中事件的函数
                handleCheckBoxClick(currentTask, v, isChecked, position);
            }
        });

        int cnt=currentTask.getTaskCycleCount();
        int tot=currentTask.getTaskCycleTot();

        taskNameTextView.setText(currentTask.getTaskName());
        // 如果时间为空，不显示（不占用空间）。否则显示
        if (currentTask.getTaskTime().isEmpty()) {
            taskTimeTextView.setVisibility(View.GONE);
        } else {
            taskTimeTextView.setVisibility(View.VISIBLE);
            taskTimeTextView.setText(currentTask.getTaskTime());
        }
        // 如果备注为空，不显示（不占用空间）。否则显示
        if (currentTask.getTaskNotes().isEmpty()) {
            taskNotesTextView.setVisibility(View.GONE);
        } else {
            taskNotesTextView.setVisibility(View.VISIBLE);
            taskNotesTextView.setText(currentTask.getTaskNotes());
        }

        // 显示其他内容
        if(currentTask.isTaskRepeat() == false){
            taskProgressBar.setVisibility(View.GONE);
        } else {
            taskProgressBar.setVisibility(View.VISIBLE);
            taskProgressBar.setProgress(cnt);
            taskProgressBar.setMax(tot);
        }

        return itemView;
    }

    private void handleCheckBoxClick(Todo todo, View v, boolean isChecked, int position) {
        View parent = (View) v.getParent();
        TextView taskNameTextView = parent.findViewById(R.id.taskNameTextView);
        TextView taskNotesTextView = parent.findViewById(R.id.taskNotesTextView);
        TextView taskTimeTextView = parent.findViewById(R.id.taskTimeTextView);
        // 在这里实现 CheckBox 点击事件的处理逻辑
        if (isChecked) {
            // 如果选中，就把任务从任务列表中移除，再添加到任务列表的最后
            int cnt=todo.getTaskCycleCount();
            int tot=todo.getTaskCycleTot();
            if(cnt<tot && todo.isTaskRepeat())
            {
                cnt++;
                todo.setTaskCycleCount(cnt);
                if(cnt!=tot)
                    ((CheckBox) v).setChecked(false);
                else
                {
                    todo.setDone(true);

                    taskNameTextView.setTextColor(context.getResources().getColor(R.color.half_black));
                    taskNotesTextView.setTextColor(context.getResources().getColor(R.color.half_black));
                    taskTimeTextView.setTextColor(context.getResources().getColor(R.color.half_black));
                    taskNameTextView.setPaintFlags(taskNameTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    taskNotesTextView.setPaintFlags(taskNotesTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    taskTimeTextView.setPaintFlags(taskTimeTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                    todoList.remove(todo);
                    todoList.add(unfinishedTaskCount-1,todo);
//                    todoList.add(0,todo);
                    unfinishedTaskCount--;
                }
            }
            else
            {
                todo.setDone(true);

                taskNameTextView.setTextColor(context.getResources().getColor(R.color.half_black));
                taskNotesTextView.setTextColor(context.getResources().getColor(R.color.half_black));
                taskTimeTextView.setTextColor(context.getResources().getColor(R.color.half_black));
                taskNameTextView.setPaintFlags(taskNameTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                taskNotesTextView.setPaintFlags(taskNotesTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                taskTimeTextView.setPaintFlags(taskTimeTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                todoList.remove(todo);
                todoList.add(unfinishedTaskCount-1,todo);
//                todoList.add(0,todo);
                unfinishedTaskCount--;
            }
        } else {
            // 如果未选中，就把任务从任务列表中移除，再添加到任务列表的unfinishedTaskCount位置
            todo.setDone(false);

            taskNameTextView.setTextColor(context.getResources().getColor(R.color.black));
            taskNotesTextView.setTextColor(context.getResources().getColor(R.color.black));
            taskTimeTextView.setTextColor(context.getResources().getColor(R.color.black));
            taskNameTextView.setPaintFlags(taskNameTextView.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
            taskNotesTextView.setPaintFlags(taskNotesTextView.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
            taskTimeTextView.setPaintFlags(taskTimeTextView.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);

            todoList.remove(todo);
//            todoList.add(unfinishedTaskCount, todo);
            todoList.add(0, todo);
            unfinishedTaskCount++;
            int cnt=todo.getTaskCycleCount();
            if(todo.isTaskRepeat())
            {
                cnt=0;
                todo.setTaskCycleCount(cnt);
            }
        }
        notifyDataSetChanged();
        JSONObject jsonObject = new JSONObject();

        //转为string
        String url = context.getResources().getString(R.string.server_url) + "todos/" + todo.getId();
        try {
            jsonObject.put("id", todo.getId());
            jsonObject.put("userId", Global.userID + "");
            jsonObject.put("taskName", todo.getTaskName());
            jsonObject.put("taskTime", todo.getTaskTime());
            jsonObject.put("taskNotes", todo.getTaskNotes());
            jsonObject.put("isDone", todo.isDone());
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
        MySingleton.getInstance(TodoAdapter.this.context).addToRequestQueue(jsonObjectRequest);

    }
}
