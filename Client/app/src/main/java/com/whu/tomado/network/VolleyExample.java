package com.whu.tomado.network;
import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class VolleyExample {
    private static final String TAG = VolleyExample.class.getSimpleName();

    private Context context;
    private RequestQueue requestQueue;

    public VolleyExample(Context context) {
        this.context = context;
        this.requestQueue = Volley.newRequestQueue(context);
    }

    public void makeRequest(String url, final VolleyCallback callback) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // 请求成功时的处理逻辑
                        callback.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // 请求失败时的处理逻辑
                        callback.onError(error.getMessage());
                    }
                });

        requestQueue.add(jsonObjectRequest);
    }
}
