package com.whu.tomado.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.graphics.Paint;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.whu.tomado.R;
import com.whu.tomado.pojo.Nodo;
import com.whu.tomado.ui.utils.MySingleton;
import com.whu.tomado.utils.Global;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class NodoAdapter extends ArrayAdapter<Nodo> {
    private LayoutInflater inflater;
    private List<Nodo> nodoList;
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
    public NodoAdapter(Context context, List<Nodo> taskList) {
        super(context, 0, taskList);
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.nodoList = taskList;
    }

    // 重写getView方法
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;
        if (itemView == null) {
            itemView = inflater.inflate(R.layout.one_task, parent, false);
        }
        Nodo currentTask = getItem(position);
        // 设置任务完成情况
        CheckBox taskFinishedBox = itemView.findViewById(R.id.taskFinished);
        taskFinishedBox.setChecked(currentTask.isFailed());
        if(currentTask.isFailed()){
            taskFinishedBox.setButtonDrawable(R.drawable.ic_cross);
        }
        else{
            taskFinishedBox.setButtonDrawable(android.R.drawable.checkbox_off_background);

        }
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
        if(currentTask.isFailed()) {
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
//        taskCycleTotTextView.setText(currentTask.getTaskCycleTot());
//        taskCycleTimeTextView.setText(currentTask.getTaskCycleTime());
//        taskCycleCountTextView.setText(currentTask.getTaskCycleCount());
//        taskLastFinishedTextView.setText(currentTask.getTaskLastFinished());




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

    private void handleCheckBoxClick(Nodo nodo, View v, boolean isChecked, int position) {
        View parent = (View) v.getParent();
        TextView taskNameTextView = parent.findViewById(R.id.taskNameTextView);
        TextView taskNotesTextView = parent.findViewById(R.id.taskNotesTextView);
        TextView taskTimeTextView = parent.findViewById(R.id.taskTimeTextView);
        // 在这里实现 CheckBox 点击事件的处理逻辑
        if (isChecked) {
            // 如果选中，就把任务从任务列表中移除，再添加到任务列表的最后
            int cnt=nodo.getTaskCycleCount();

            int tot=nodo.getTaskCycleTot();
            if(cnt > 0 && nodo.isTaskRepeat())
            {
                cnt--;
                nodo.setTaskCycleCount(cnt);
                if(cnt!=0){
                    ((CheckBox) v).setChecked(false);
                    ((CheckBox) v).setButtonDrawable(android.R.drawable.checkbox_off_background);
                }
                else
                {
                    if(unfinishedTaskCount-1 <= nodoList.size() )
                    {
                        nodo.setFailed(true);
                        ((CheckBox) v).setChecked(true);
                        ((CheckBox) v).setButtonDrawable(R.drawable.ic_cross);

                        taskNameTextView.setTextColor(context.getResources().getColor(R.color.half_black));
                        taskNotesTextView.setTextColor(context.getResources().getColor(R.color.half_black));
                        taskTimeTextView.setTextColor(context.getResources().getColor(R.color.half_black));
                        taskNameTextView.setPaintFlags(taskNameTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        taskNotesTextView.setPaintFlags(taskNotesTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        taskTimeTextView.setPaintFlags(taskTimeTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                        nodoList.remove(nodo);

                        nodoList.add(unfinishedTaskCount-1,nodo);
                        unfinishedTaskCount--;
                    }
                    else {
                        Toast.makeText(getContext(), "服务器错误，请重试", Toast.LENGTH_SHORT).show();
                        ((CheckBox) v).setChecked(false);
                        ((CheckBox) v).setButtonDrawable(android.R.drawable.checkbox_off_background);
                    }
                }
            }
            else
            {
                if(unfinishedTaskCount-1 <= nodoList.size() )
                {
                    nodo.setFailed(true);
                    ((CheckBox) v).setChecked(true);
                    ((CheckBox) v).setButtonDrawable(R.drawable.ic_cross);

                    taskNameTextView.setTextColor(context.getResources().getColor(R.color.half_black));
                    taskNotesTextView.setTextColor(context.getResources().getColor(R.color.half_black));
                    taskTimeTextView.setTextColor(context.getResources().getColor(R.color.half_black));
                    taskNameTextView.setPaintFlags(taskNameTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    taskNotesTextView.setPaintFlags(taskNotesTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    taskTimeTextView.setPaintFlags(taskTimeTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                    nodoList.remove(nodo);

                    nodoList.add(unfinishedTaskCount-1,nodo);
                    unfinishedTaskCount--;
                }
                else {
                    Toast.makeText(getContext(), "服务器错误，请重试", Toast.LENGTH_SHORT).show();
                    ((CheckBox) v).setChecked(false);
                    ((CheckBox) v).setButtonDrawable(android.R.drawable.checkbox_off_background);
                }
            }
        } else {
            // 如果未选中，就把任务从任务列表中移除，再添加到任务列表的unfinishedTaskCount位置
            nodo.setFailed(false);
            ((CheckBox) v).setChecked(false);
            ((CheckBox) v).setButtonDrawable(android.R.drawable.checkbox_off_background);

            taskNameTextView.setTextColor(context.getResources().getColor(R.color.black));
            taskNotesTextView.setTextColor(context.getResources().getColor(R.color.black));
            taskTimeTextView.setTextColor(context.getResources().getColor(R.color.black));
            taskNameTextView.setPaintFlags(taskNameTextView.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
            taskNotesTextView.setPaintFlags(taskNotesTextView.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
            taskTimeTextView.setPaintFlags(taskTimeTextView.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);

            nodoList.remove(nodo);
//            nodoList.add(unfinishedTaskCount, nodo);
            nodoList.add(0, nodo);
            unfinishedTaskCount++;
            int cnt=nodo.getTaskCycleCount();
            if(nodo.isTaskRepeat() && cnt == 0)
            {
                cnt=nodo.getTaskCycleTot();
                nodo.setTaskCycleCount(cnt);
            }
        }
        notifyDataSetChanged();
        JSONObject jsonObject = new JSONObject();
        String url = context.getResources().getString(R.string.server_url) + "nodos/" + nodo.getId();
        try {
            jsonObject.put("id", nodo.getId());
            jsonObject.put("userId", Global.userID + "");
            jsonObject.put("taskName", nodo.getTaskName());
            jsonObject.put("taskNotes", nodo.getTaskNotes());
            jsonObject.put("taskTime", nodo.getTaskTime());
            jsonObject.put("failed", nodo.isFailed());
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
        MySingleton.getInstance(NodoAdapter.this.context).addToRequestQueue(jsonObjectRequest);
    }
}
