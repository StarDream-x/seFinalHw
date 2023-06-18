package com.whu.tomado.ui.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.whu.tomado.R;

//import com.whu.tomado.Task.LoginTask;
//import com.whu.tomado.Task.RegisterTask;
import com.whu.tomado.utils.Global;

import java.util.Locale;

import com.whu.tomado.network.Task.LoginTask;
import com.whu.tomado.network.Task.RegisterTask;


public class ProfileFragment extends Fragment implements LoginTask.OnTaskCompleted , RegisterTask.OnTaskCompleted{

    private TextView usernameTextView;
    private Button settingsButton;
    private Button aboutButton;

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
        aboutButton = view.findViewById(R.id.aboutButton);
        loginButton = view.findViewById(R.id.loginButton);
//        testButton = view.findViewById(R.id.testButton);
        // 设置用户名
        setUsername("未登录");

        // 设置设置按钮点击事件
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 处理设置按钮点击事件
                openSettings();
            }
        });

        // 设置关于按钮点击事件
        aboutButton.setOnClickListener(new View.OnClickListener() {
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
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("关于");
        builder.setMessage("作者：SE7组\n简介：tomado是一个简单而强大的事务管理应用，帮助您管理日常任务和提高工作效率。");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }


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
                //如果用户名为空，则提示用户名不能为空
                if(username.compareTo("") == 0){
                    Toast.makeText(getActivity(), "用户名不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.compareTo("") == 0){
                    Toast.makeText(getActivity(), "密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    LoginTask loginTask = new LoginTask(ProfileFragment.this);
                    loginTask.execute(getString(R.string.server_url)+"login?username="+username+"&password="+password);
//                    loginTask.setParams(username, password);
                    String result = loginTask.get();
                    Log.d("result", result);
                    //如果result为true，则登录成功,否则登录失败
//                    System.out.println(result);

                    //如果result中包含true，则登录成功，否则登录失败
                    if(result.toLowerCase().contains("true")){
                        Toast.makeText(getActivity(), "登录成功", Toast.LENGTH_SHORT).show();
                        setUsername(username);
                        isLoggedIn = true;
                        long userId=Long.parseLong(result.split(" ")[0]);
                        Global.userID= userId;
                    }else{
                        Toast.makeText(getActivity(), "用户名或密码错误", Toast.LENGTH_SHORT).show();
                        isLoggedIn = false;
                    }

//                    Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    e.printStackTrace();
                }



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
                //如果用户名为空，则提示用户名不能为空
                if(username.compareTo("") == 0){
                    Toast.makeText(getActivity(), "用户名不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.compareTo("") == 0){
                    Toast.makeText(getActivity(), "密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                //如果密码不大于6位，则提示密码不能小于6位
                if(password.length() < 6){
                    Toast.makeText(getActivity(), "密码不能小于6位", Toast.LENGTH_SHORT).show();
                    return;
                }
                //如果密码大于16位，则提示密码不能大于16位
                if(password.length() > 16){
                    Toast.makeText(getActivity(), "密码不能大于16位", Toast.LENGTH_SHORT).show();
                    return;
                }
                //如果密码不包含字母大小写和数字，则提示密码必须包含字母大小写和数字
                if(!password.matches("^(?=.*[a-z])(?=.*[A-Z]).+$") || !password.matches(".*[0-9].*")){
                    Toast.makeText(getActivity(), "密码必须包含字母大小写和数字", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    RegisterTask registerTask = new RegisterTask(ProfileFragment.this);
                    registerTask.execute(getString(R.string.server_url)+"register?username="+username+"&password="+password);
//                    loginTask.setParams(username, password);
                    String result = registerTask.get();
                    Log.d("result", result);

                    if(result.compareToIgnoreCase("true\n") == 0){
                        Toast.makeText(getActivity(), "注册成功", Toast.LENGTH_SHORT).show();


                    }else{
                        Toast.makeText(getActivity(), "用户名已存在", Toast.LENGTH_SHORT).show();

                    }

//                    Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    e.printStackTrace();
                }


            }
        });

        builder.setNegativeButton("取消", null);

        builder.create().show();
    }


    @Override
    public void onTaskCompleted(String result) {
        // 在此处处理获取到的结果
        Log.d("MainActivity", "Result: " + result);

    }

}
