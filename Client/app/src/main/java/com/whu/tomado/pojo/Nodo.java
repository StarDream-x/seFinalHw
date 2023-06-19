package com.whu.tomado.pojo;

public class Nodo {

    private long id;

    private boolean failed = false;

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

public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Nodo(String taskName, String taskTime, String taskNotes, int taskCycleTot, boolean taskRepeat) {
        this.taskName = taskName;
        this.taskTime = taskTime;
        this.taskNotes = taskNotes;
        this.taskCycleTot = taskCycleTot;
//        this.taskCycleTime = taskCycleTime;
        this.taskRepeat = taskRepeat;
    }

    public Nodo() {
    }
    
    public boolean isFailed() {
        return failed;
    }

    public void setFailed(boolean failed) {
        this.failed = failed;
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

    public int getTaskCycleCount() {
        return taskCycleCount;
    }

    public void setTaskCycleCount(int taskCycleCount) {
        this.taskCycleCount = taskCycleCount;
    }

    public int getTaskCycleTot() {
        return taskCycleTot;
    }

       public void setTaskCycleTot(int taskCycleTot) {
        this.taskCycleTot = taskCycleTot;
    }

    public void setTaskRepeat(boolean taskRepeat) {
        this.taskRepeat = taskRepeat;
    }
}
