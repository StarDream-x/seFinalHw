package com.whu.tomado.ui.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.whu.tomado.R;
import com.whu.tomado.network.ProfileInterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class ProfileFragment extends Fragment {

    private TextView usernameTextView;
    private Button settingsButton;
    private Button helpButton;

    private Button loginButton;

    private Button testButton;
    // 用于标记用户是否已登录
    private boolean isLoggedIn = false;


    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile, container, false);

        usernameTextView = view.findViewById(R.id.usernameTextView);
        settingsButton = view.findViewById(R.id.settingsButton);
        helpButton = view.findViewById(R.id.helpButton);
        loginButton = view.findViewById(R.id.loginButton);
        testButton = view.findViewById(R.id.testButton);
        // 设置用户名
        setUsername("未登录");

        //设置测试按钮点击事件
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url_str = String.valueOf(R.string.server_url);
                System.out.println(url_str);
                URL url = null;
                try {
                    url = new URL(url_str);
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                }

                // 建立HTTP连接
                HttpURLConnection urlConnection = null;
                try {
                    urlConnection = (HttpURLConnection) url.openConnection();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                try {
                    urlConnection.setRequestMethod("GET");
                } catch (ProtocolException e) {
                    throw new RuntimeException(e);
                }
                try {
                    urlConnection.connect();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                // 读取输入流
                InputStream inputStream = null;
                try {
                    inputStream = urlConnection.getInputStream();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                StringBuilder buffer = new StringBuilder();


                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;

                while (true) {
                    try {
                        if ((line = reader.readLine()) == null) break;
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    buffer.append(line).append("\n");
                }


                System.out.println(buffer.toString());
            }
        });

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
//         设置登录按钮点击事件
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 处理登录按钮点击事件
                openLogin();
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

    //    private void openLogin() {
//        // 处理打开登录界面的逻辑
//        // 可以使用对话框或者启动一个新的登录Activity进行登录
//        // 这里只是示例，具体实现取决于你的项目需求
//
//        // 例如，可以使用对话框进行登录
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        builder.setTitle("登录");
//        builder.setMessage("Please log in to access your profile.");
//        builder.setPositiveButton("Log In", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                // 处理登录按钮点击事件，这里可以编写实际的登录逻辑
//
//                // 假设登录成功后，设置用户名为"John Doe"
//                setUsername("John Doe");
//            }
//        });
//        builder.setNegativeButton("Cancel", null);
//        builder.create().show();
//    }
    private void openLogin() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("登录");

        // Create the layout for the dialog
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.login_dialog, null);
        builder.setView(dialogView);

        // Find the input fields and register button in the dialog layout
        EditText usernameEditText = dialogView.findViewById(R.id.usernameEditText);
        EditText passwordEditText = dialogView.findViewById(R.id.passwordEditText);
        Button registerButton = dialogView.findViewById(R.id.registerButton);

        builder.setPositiveButton("登录", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Retrieve the entered username and password
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                // Perform login logic here, e.g., validating credentials

                // Assume login is successful for demonstration purposes
                setUsername(username);
            }
        });

        builder.setNegativeButton("取消", null);

        // Set up the register button click listener
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRegistration();
            }
        });

        builder.create().show();
    }

    private void openRegistration() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("注册");

        // Create the layout for the dialog
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.registration_dialog, null);
        builder.setView(dialogView);

        // Find the input fields in the dialog layout
        EditText usernameEditText = dialogView.findViewById(R.id.usernameEditText);
        EditText passwordEditText = dialogView.findViewById(R.id.passwordEditText);

        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Retrieve the entered username and password
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                // Perform registration logic here, e.g., creating a new account

                // Assume registration is successful for demonstration purposes
                setUsername(username);
            }
        });

        builder.setNegativeButton("取消", null);

        builder.create().show();
    }


}
