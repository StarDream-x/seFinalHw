package com.whu.tomado.pojo;

public class Nodo {
    private boolean isDone = false;
    private String taskName;
    private String taskTime;
    private String taskNotes;


    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskTime() {
        return taskTime;
    }

    public void setTaskTime(String taskTime) {
        this.taskTime = taskTime;
    }

    public String getTaskNotes() {
        return taskNotes;
    }

    public void setTaskNotes(String taskNotes) {
        this.taskNotes = taskNotes;
    }

    public Nodo(String taskName, String taskTime, String taskNotes) {
        this.taskName = taskName;
        this.taskTime = taskTime;
        this.taskNotes = taskNotes;
    }

    //修改任务状态
    public void setTaskStatus(boolean newstatus) {
        isDone = newstatus;
    }

    //获取任务状态，已完成为true
    public boolean getTaskStatus() {
        return isDone;
    }

    // 示例方法
    @Override
    public String toString() {
        return taskName;
    }
}
