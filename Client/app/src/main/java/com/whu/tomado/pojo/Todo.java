package com.whu.tomado.pojo;

public class Todo {
    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Todo() {
    }

    private boolean isDone = false;
    private String taskName;
    private String taskTime;
    private String taskNotes;

    public boolean isTaskRepeat() {
        return taskRepeat;
    }

    private int taskCycleCount = 0;
    private int taskCycleTot;
//    private String taskLastFinished = "";
//    private int taskCycleTime;
    private boolean taskRepeat;

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

    public int getTaskCycleTot() {
        return taskCycleTot;
    }

    public void setTaskCycleTot(int taskCycleTot) {
        this.taskCycleTot = taskCycleTot;
    }

//    public int getTaskCycleTime() {
//        return taskCycleTime;
//    }
//
//    public void setTaskCycleTime(int taskCycleTime) {
//        this.taskCycleTime = taskCycleTime;
//    }

    public int getTaskCycleCount() {
        return taskCycleCount;
    }

    public void setTaskCycleCount(int taskCycleCount) {
        this.taskCycleCount = taskCycleCount;
    }

//    public String getTaskLastFinished() {
//        return taskLastFinished;
//    }

    public void setTaskRepeat(boolean taskRepeat) {
        this.taskRepeat = taskRepeat;
    }

    public boolean getTaskRepeat() {
        return taskRepeat;
    }

//    public void setTaskLastFinished(String taskLastFinished) {
//        this.taskLastFinished = taskLastFinished;
//    }

    public Todo(String taskName, String taskTime, String taskNotes, int taskCycleTot, boolean taskRepeat) {
        this.taskName = taskName;
        this.taskTime = taskTime;
        this.taskNotes = taskNotes;
        this.taskCycleTot = taskCycleTot;
//        this.taskCycleTime = taskCycleTime;
        this.taskRepeat = taskRepeat;
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
