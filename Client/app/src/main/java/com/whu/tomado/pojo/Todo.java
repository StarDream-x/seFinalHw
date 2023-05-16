package com.whu.tomado.pojo;

public class Todo {
    private String taskName;
    private String taskTime;
    private String taskNotes;

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

    public Todo(String taskName, String taskTime, String taskNotes) {
        this.taskName = taskName;
        this.taskTime = taskTime;
        this.taskNotes = taskNotes;
    }

    // 示例方法
    @Override
    public String toString() {
        return taskName;
    }
}
