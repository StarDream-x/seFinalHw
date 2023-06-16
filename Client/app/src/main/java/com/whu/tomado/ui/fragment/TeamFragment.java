package com.whu.tomado.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.whu.tomado.R;

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
        return view;
    }
}
