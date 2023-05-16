package com.whu.tomado.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

import com.whu.tomado.R;

public class ProfileFragment extends Fragment {

    private TextView usernameTextView;
    private Button settingsButton;
    private Button helpButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile, container, false);

        usernameTextView = view.findViewById(R.id.usernameTextView);
        settingsButton = view.findViewById(R.id.settingsButton);
        helpButton = view.findViewById(R.id.helpButton);

        // 设置用户名
        setUsername("John Doe");

        // 设置设置按钮点击事件
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 处理设置按钮点击事件
                openSettings();
            }
        });

        // 设置帮助按钮点击事件
        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 处理帮助按钮点击事件
                openHelp();
            }
        });

        return view;
    }

    private void setUsername(String username) {
        usernameTextView.setText(username);
    }

    private void openSettings() {
        // 处理打开设置界面的逻辑
    }

    private void openHelp() {
        // 处理打开帮助界面的逻辑
    }
}
