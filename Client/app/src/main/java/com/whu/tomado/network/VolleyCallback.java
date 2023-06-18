package com.whu.tomado.network;

import org.json.JSONObject;

public interface VolleyCallback {
    void onSuccess(JSONObject response);
    void onError(String errorMessage);
}
