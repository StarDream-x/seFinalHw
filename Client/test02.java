        // 设置"添加任务"按钮的点击事件
        // 在您希望弹出页面的位置，例如点击一个按钮时
        addTodoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("添加TODO");
                //builder.setMessage("这是一个弹出页面的示例");

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