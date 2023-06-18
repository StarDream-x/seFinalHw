package com.whu.tomado.ui.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.os.Vibrator;
import android.app.Service;
import android.net.Uri;
import android.media.Ringtone;
import android.media.RingtoneManager;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.whu.tomado.R;
import com.whu.tomado.pojo.Todo;
import com.whu.tomado.ui.utils.TodoTaskViewUtils;

public class ClockFragment extends Fragment {

    private TextView timerTimesUp;
    private TextView timerTextView;
    private Button startButton;
    private Button resetButton;
    private CountDownTimer countDownTimer;
    private boolean isTimerRunning;
    private long timeLeftInMillis = 1500000;
    private static long START_TIME_IN_MILLIS = 1500000; // 25 minutes

    private static EditText timeHours;
    private static EditText timeMinutes;
    private static EditText timeSeconds;
    private Context context;

    public ClockFragment(){}

    public ClockFragment(Context context) {
        this.context = context;
    }

    // 重置计时器
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 为fragment加载布局
        View view = inflater.inflate(R.layout.clock, container, false);

        // 获取布局中的控件
        timerTextView = view.findViewById(R.id.timerTextView);
        // 设置开始按钮
        startButton = view.findViewById(R.id.startButton);
        resetButton = view.findViewById(R.id.resetButton);

        // 设置开始按钮的点击事件
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isTimerRunning) {
                    pauseTimer();
                } else {
                    startTimer();
                }
            }
        });

        timerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isTimerRunning)
                {
                    // 创建一个edit_time对话框
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("修改时间");

                    // 添加edit_time布局文件
                    View dialogView = getLayoutInflater().inflate(R.layout.edit_time, null);
                    // 设置对话框显示的View对象
                    builder.setView(dialogView);

                    timeHours = dialogView.findViewById(R.id.timeHours);
                    timeMinutes = dialogView.findViewById(R.id.timeMinutes);
                    timeSeconds = dialogView.findViewById(R.id.timeSeconds);
                    timeHours.setText(String.format("%02d",timeLeftInMillis / 1000 / 3600));
                    timeMinutes.setText(String.format("%02d",timeLeftInMillis / 1000 / 60 % 60));
                    timeSeconds.setText(String.format("%02d",timeLeftInMillis / 1000 % 60));

                    // 添加按钮及点击事件
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            timeHours = dialogView.findViewById(R.id.timeHours);
                            timeMinutes = dialogView.findViewById(R.id.timeMinutes);
                            timeSeconds = dialogView.findViewById(R.id.timeSeconds);
                            long Hours = toInt(timeHours.getText().toString());
                            long Minutes = toInt(timeMinutes.getText().toString());
                            long Seconds = toInt(timeSeconds.getText().toString());
                            if(Hours == -1 || Minutes == -1 || Seconds == -1 || Minutes > 59 || Seconds > 59)
                            {
                                Warning("时间格式错误",context);
                                return;
                            }
                            START_TIME_IN_MILLIS = (Hours* 3600L +Minutes*60L+Seconds)*1000L;
                            resetTimer();
                        }
                    });
                    builder.setNegativeButton("取消", null);

                    // 创建并显示AlertDialog
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                    alertDialog.getButton(0);
                }
            }
        });

        // 设置重置按钮
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isTimerRunning)
                    pauseTimer();
                resetTimer();
            }
        });
        return view;
    }

    // 开始计时器
    private void startTimer() {
        // 创建一个新的倒计时计时器
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateTimerText();
            }

            @Override
            public void onFinish() {
                isTimerRunning = false;
                startButton.setText("Start");
                // 播放铃声或执行其他操作以指示番茄钟完成
                playVibrate(context);
//                playRing(context);
            }
        }.start();

        // 计时器正在运行
        isTimerRunning = true;
        startButton.setText("Pause");
    }

    private void pauseTimer() {
        countDownTimer.cancel();
        isTimerRunning = false;
        startButton.setText("Start");
    }

    // 重置计时器
    private void resetTimer() {
        timeLeftInMillis = START_TIME_IN_MILLIS;
        updateTimerText();
        isTimerRunning = false;
        startButton.setText("Start");
    }

    // 将剩余时间转换为格式化的字符串，并将其设置为TextView的文本
    private void updateTimerText() {
        int hours   = (int) (timeLeftInMillis / 1000) / 3600;
        int minutes = (int) (timeLeftInMillis / 1000) / 60 % 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;

        String timeLeftFormatted;
        timeLeftFormatted = String.format("%02d:%02d:%02d",hours, minutes, seconds);

        timerTextView.setText(timeLeftFormatted);
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

    private static int toInt(String s) {
        int biaoji = 0, ret = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) >= '0' && s.charAt(i) <= '9')
                ret = ret * 10 + s.charAt(i) - '0';
            else {
                biaoji = 1;
                break;
            }
        }
        if (biaoji != 0)
            return -1;
        return ret;
    }

    private static void playRing(Context context) {
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone rt = RingtoneManager.getRingtone(context, uri);
        rt.play();
//方法可行
    }

    private static void playVibrate(Context context) {
        Vibrator vibrator = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
        long[] vibrationPattern = new long[]{0, 1500, 500, 1500};
        vibrator.vibrate(vibrationPattern, -1);
    }
}
