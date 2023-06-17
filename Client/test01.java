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