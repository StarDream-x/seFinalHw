package com.whu.tomado.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.whu.tomado.R;
import com.whu.tomado.network.VolleyCallback;
import com.whu.tomado.network.VolleyExample;
import com.whu.tomado.ui.utils.MySingleton;

import org.json.JSONObject;

public class TeamFragment extends Fragment {
    private Button testButton;
    private TextView showJsonTextView;
    private Context mcontext;

    public TeamFragment() {
    }

    public TeamFragment(Context mcontext) {
        this.mcontext = mcontext;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.team, container, false);
        TextView textView = view.findViewById(R.id.showJsonTextView);
        Button button = view.findViewById(R.id.test_button);
        String url = getString(R.string.server_url)+"test";

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                        (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onResponse(JSONObject response) {
                                textView.setText("Response: " + response.toString());
                            }
                        }, new Response.ErrorListener() {

                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // TODO: Handle error
                                textView.setText("Error:"+error.toString());
                            }
                        });

                // Access the RequestQueue through your singleton class.
                MySingleton.getInstance(TeamFragment.this.mcontext).addToRequestQueue(jsonObjectRequest);
            }
        });

        return view;
    }
}
