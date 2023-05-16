package com.whu.tomado.network;

import static androidx.core.content.res.TypedArrayUtils.getString;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.whu.tomado.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Test02 extends AsyncTask<String, Void, String>{
    private static final String TAG = "FetchDataAsyncTask";

//    private AsyncTaskCompleteListener mListener;
    private TextView textView;

    public Test02() {
    }

    public Test02(TextView textView) {
        this.textView = textView;
    }

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
        textView.setText(result);
    }
}
