package com.whu.tomado.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.whu.tomado.R;

public class ClockFragment extends Fragment {

    private TextView timerTextView;
    private Button startButton;
    private CountDownTimer countDownTimer;
    private boolean isTimerRunning;
    private long timeLeftInMillis = 0;
    private static final long START_TIME_IN_MILLIS = 1500000; // 25 minutes

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

        // 设置重置按钮
        resetTimer();
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
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;

        String timeLeftFormatted = String.format("%02d:%02d", minutes, seconds);

        timerTextView.setText(timeLeftFormatted);
    }
}
