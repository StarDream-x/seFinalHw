package com.whu.tomado.ui.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
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

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.whu.tomado.R;

//import com.whu.tomado.Task.LoginTask;
//import com.whu.tomado.Task.RegisterTask;
import com.whu.tomado.pojo.Team;
import com.whu.tomado.ui.utils.MySingleton;
import com.whu.tomado.utils.Global;

import com.whu.tomado.network.Task.LoginTask;
import com.whu.tomado.network.Task.RegisterTask;

import org.json.JSONException;
import org.json.JSONObject;


public class ProfileFragment extends Fragment implements LoginTask.OnTaskCompleted , RegisterTask.OnTaskCompleted{

    private TextView usernameTextView;
    private Button settingsButton;
    private Button aboutButton;

    private Button loginButton;
    private Button logoutButton;
    private Button addTeamButton;
    private Button createTeamButton;


    private Button dataStatisticButton;


    private Button testButton;
    private Context context;
    // 用于标记用户是否已登录
//    private boolean isLoggedIn = false;

    public long addTeamId;
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile, container, false);
        usernameTextView = view.findViewById(R.id.usernameTextView);
        settingsButton = view.findViewById(R.id.settingsButton);
        aboutButton = view.findViewById(R.id.aboutButton);
        loginButton = view.findViewById(R.id.loginButton);
        logoutButton=view.findViewById(R.id.logoutButton);

        addTeamButton = view.findViewById(R.id.addTeamButton);
        createTeamButton = view.findViewById(R.id.createTeamButton);
        dataStatisticButton=view.findViewById(R.id.dataStatiscs);

//        testButton = view.findViewById(R.id.testButton);
        // 设置用户名
//        setUsername("未登录");
        setUsername(Global.userName);

//        settingsButton.setVisibility(View.GONE);
//        addTeamButton.setVisibility(View.GONE);
//        createTeamButton.setVisibility(View.GONE);
//        if(Global.isLogin)
//        {
//            loginButton.setVisibility(View.GONE);
//            logoutButton.setVisibility(View.VISIBLE);
//            dataStatisticButton.setVisibility(View.VISIBLE);
//        }else{
//            loginButton.setVisibility(View.VISIBLE);
//            logoutButton.setVisibility(View.GONE);
//            dataStatisticButton.setVisibility(View.GONE);
//        }

        // 设置设置按钮点击事件
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 处理设置按钮点击事件
                Toast.makeText(getActivity(), "敬请期待", Toast.LENGTH_SHORT).show();
                openSettings();
            }
        });
        addTeamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 处理创建团队按钮点击事件
                if(Global.isLogin)
                    openAddTeam();
                else
                    Toast.makeText(getActivity(),"您还未登录",Toast.LENGTH_SHORT).show();
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
        //创建团队按钮点击事件
        createTeamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 处理创建团队按钮点击事件
                if(Global.isLogin)
                    openCreateTeam();
                else
                    Toast.makeText(getActivity(),"您还未登录",Toast.LENGTH_SHORT).show();
            }
        });

//         设置登录按钮点击事件
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 处理登录按钮点击事件
                if(!Global.isLogin)
                    openLogin();
                else
                    Toast.makeText(getActivity(),"您已登录",Toast.LENGTH_SHORT).show();
            }
        });
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 处理登出按钮点击事件
                if(Global.isLogin)
                    openLogout();
                else
                    Toast.makeText(getActivity(),"您还未登录",Toast.LENGTH_SHORT).show();

            }
        });
        dataStatisticButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Global.isLogin)
                    opendataStatistic();
                else
                    opendataStatistic();
//                    Toast.makeText(getActivity(),"您还未登录",Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void openAddTeam() {
        //提示创建成功
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("加入团队");

        // Create the layout for the dialog
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.addteam_dialog, null);
        builder.setView(dialogView);

        // Find the input fields in the dialog layout
        EditText teamnameEditText = dialogView.findViewById(R.id.teamnameEditText);
        EditText teamPasswordEditText = dialogView.findViewById(R.id.teamPasswordEditText);
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Retrieve the entered username
                String teamName = teamnameEditText.getText().toString();
                String teamPassword = teamPasswordEditText.getText().toString();
                //如果用户名为空，则提示用户名不能为空
                if(teamName.compareTo("") == 0){
                    Toast.makeText(getActivity(), "团队名不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(teamPassword.compareTo("") == 0){
                    Toast.makeText(getActivity(), "团队密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                JSONObject jsonObject = new JSONObject();
                String url = getString(R.string.server_url) + "teams/join";
                try {
                    jsonObject.put("teamName", teamName);
                    jsonObject.put("teamPassword", teamPassword);
                    jsonObject.put("idList", Global.userID);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                        (Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {

                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onResponse(JSONObject response) {
//                                        textView.setText("Response: " + response.toString());
                                Log.d("addTeam", response.toString());
                                String res = response.toString();
                                Toast.makeText(getActivity(), "加入成功", Toast.LENGTH_SHORT).show();
                            }
                        }, new Response.ErrorListener() {

                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("addTeam", error.toString());
                            }
                        });
                jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(10000,0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                MySingleton.getInstance(ProfileFragment.this.context).addToRequestQueue(jsonObjectRequest);
            }
        });
        builder.setNegativeButton("取消", null);
        builder.create().show();
    }

    private void setUsername(String username) {
        usernameTextView.setText(username);
    }

    private void openSettings() {
        // 处理打开设置界面的逻辑
    }

    private void opendataStatistic() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("数据统计");
        View dialogView = getLayoutInflater().inflate(R.layout.data_statistic, null);
        builder.setView(dialogView);

        builder.setNegativeButton("确定", null);

        // 创建并显示AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        alertDialog.getButton(0);

//        builder.show();
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

    private void openLogout(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("退出");
        builder.setMessage("确定要退出登录吗？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 处理登出按钮点击事件
//                isLoggedIn = false;
                Global.isLogin=false;
                setUsername("未登录");
                Global.userName="未登录";
                Global.userID=0;
                Toast.makeText(getActivity(), "退出登录成功", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", null);
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
                        Global.userName=username;
                        setUsername(Global.userName);
//                        isLoggedIn = true;
                        Global.isLogin=true;
                        System.out.println(result);
                        long userId=Long.parseLong(result.split(" ")[0]);
                        Global.userID= userId;
//                        Toast.makeText(getActivity(), "用户ID为"+userId, Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getActivity(), "用户名或密码错误", Toast.LENGTH_SHORT).show();
//                        isLoggedIn = false;
                        Global.isLogin=false;
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

    private void openCreateTeam(){
//提示创建成功
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("创建团队");

        // Create the layout for the dialog
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.addteam_dialog, null);
        builder.setView(dialogView);

        // Find the input fields in the dialog layout
        EditText teamnameEditText = dialogView.findViewById(R.id.teamnameEditText);
        EditText teamPasswordEditText = dialogView.findViewById(R.id.teamPasswordEditText);
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Retrieve the entered username
                String teamname = teamnameEditText.getText().toString();
                String teamPassword = teamPasswordEditText.getText().toString();
                //如果用户名为空，则提示用户名不能为空
                if(teamname.compareTo("") == 0){
                    Toast.makeText(getActivity(), "团队名不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(teamPassword.compareTo("") == 0){
                    Toast.makeText(getActivity(), "团队密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                JSONObject jsonObject = new JSONObject();
                JSONObject jsonObject2 = new JSONObject();
                String url = getString(R.string.server_url) + "teams";

                String idList = Global.userID+",";
                try {
                    jsonObject.put("teamName", teamname);
                    jsonObject.put("teamPassword", teamPassword);
                    jsonObject.put("idList",idList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                        (Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {

                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onResponse(JSONObject response) {
//                                        textView.setText("Response: " + response.toString());
                                Log.d("addTeam", response.toString());
                                try {
                                    addTeamId=response.getLong("id");

                                    String url2 = getString(R.string.server_url)+"/"+Global.userID+"/"+addTeamId;
                                    JsonObjectRequest jsonObjectRequest2 = new JsonObjectRequest
                                            (Request.Method.PUT, url2, jsonObject2, new Response.Listener<JSONObject>() {

                                                @SuppressLint("SetTextI18n")
                                                @Override
                                                public void onResponse(JSONObject response) {
//                                        textView.setText("Response: " + response.toString());
                                                    Log.d("addTeam", response.toString());
                                                    //                                    team.setId(response.getLong("id"));
                                Toast.makeText(getActivity(), "创建成功", Toast.LENGTH_SHORT).show();
                                                }
                                            }, new Response.ErrorListener() {

                                                @SuppressLint("SetTextI18n")
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    Log.e("addTeam", error.toString());
                                                }
                                            });
                                    //重试不请求
                                    jsonObjectRequest2.setRetryPolicy(new DefaultRetryPolicy(10000,0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                    MySingleton.getInstance(ProfileFragment.this.context).addToRequestQueue(jsonObjectRequest2);

                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
//                                Toast.makeText(getActivity(), "创建成功", Toast.LENGTH_SHORT).show();
                            }
                        }, new Response.ErrorListener() {

                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("addTeam", error.toString());
                            }
                        });
                try {
                    jsonObject2.put("userId", Global.userID);
                    jsonObject2.put("tid",addTeamId);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(10000,0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                MySingleton.getInstance(ProfileFragment.this.context).addToRequestQueue(jsonObjectRequest);

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
