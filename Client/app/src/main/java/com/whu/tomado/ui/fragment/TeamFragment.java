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
import com.whu.tomado.network.Test;
import com.whu.tomado.network.Test02;

import javax.xml.XMLConstants;

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
        testButton = view.findViewById(R.id.test_button);
        showJsonTextView = view.findViewById(R.id.showJsonTextView);
//        Test test = new Test(this.showJsonTextView);
        Test02 test = new Test02(this.showJsonTextView);

        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 执行异步任务来获取JSON数据
                try {
                    test.execute(mcontext.getString(R.string.server_url));
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        return view;
    }
}
