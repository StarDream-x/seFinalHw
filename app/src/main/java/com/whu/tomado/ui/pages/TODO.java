package com.whu.tomado.ui.pages;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.whu.tomado.R;
import com.whu.tomado.databinding.FragmentMainBinding;

public class TODO extends Fragment {

    private TextView titleTextView;
    private TextView descriptionTextView;
    private Button startButton;
    private Chronometer chronometer;

    public TODO() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // 加载对应页面的 XML 布局文件
        View rootView = inflater.inflate(R.layout.todo, container, false);
        // 初始化视图和组件
        initializeViews(rootView);
        // 返回根视图
        return rootView;
    }

    private void initializeViews(View rootView) {
        titleTextView = rootView.findViewById(R.id.todo_title);
        descriptionTextView = rootView.findViewById(R.id.todo_description);
        startButton = rootView.findViewById(R.id.start_button);
        chronometer = rootView.findViewById(R.id.chronometer);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimer();
            }
        });
    }

    private void startTimer() {
        // 在此处实现计时器的逻辑
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
    }
}
