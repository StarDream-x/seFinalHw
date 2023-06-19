package com.whu.tomado.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import com.whu.tomado.R;

import android.os.Bundle;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start);
        Thread myThread = new Thread() {//创建子线程
            @Override
            public void run() {
                try {
                    sleep(1500);//休眠1.5秒
                    Intent it = new Intent(getApplicationContext(), MainActivity.class);//跳转到你想要在启动之后出现的页面Activity
                    startActivity(it);
                    finish();//关闭当前活动
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        myThread.start();//启动线程

    }

}
