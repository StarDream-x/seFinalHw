package com.whu.tomado.network;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ProfileInterface extends AsyncTask<String, Void, String> {
    private static final String TAG = "FetchDataAsyncTask";

    public ProfileInterface() {
    }

    // 重写doInBackground方法，处理后台执行的任务
    @Override
    protected String doInBackground(String... params) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String url_str = params[0];
        try {
            // 创建URL对象
            URL url = new URL(url_str);

            // 建立HTTP连接
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // 读取输入流
            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder buffer = new StringBuilder();

            if (inputStream == null) {
                // 处理空输入流的情况
                return null;
            }

            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;

            while ((line = reader.readLine()) != null) {
                buffer.append(line).append("\n");
            }

            if (buffer.length() == 0) {
                // 处理空响应的情况
                return null;
            }
            System.out.println(buffer.toString());
            return buffer.toString();

        } catch (Exception e) {
            Log.e(TAG, "Error", e);
            return null;

        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }

            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(TAG, "Error closing stream", e);
                }
            }
        }
    }

    @Override
    protected void onPostExecute(String result) {
        return;
    }
}

