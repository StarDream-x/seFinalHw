package com.whu.tomado.ui.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.whu.tomado.R;
import com.whu.tomado.pojo.Team;

import java.util.List;

public class TeamAdapter extends ArrayAdapter<Team> {
    private LayoutInflater inflater;
    private List<Team> teamList;
    private Context context;

    // 构造函数
    public TeamAdapter(Context context, List<Team> teamList) {
        super(context, 0, teamList);
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.teamList = teamList;
    }

    // 重写getView方法
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;
        if (itemView == null) {
            itemView = inflater.inflate(R.layout.one_team, parent, false);
        }
        Team currentTeam = getItem(position);
        TextView teamId = itemView.findViewById(R.id.tid);
        TextView teamDesc = itemView.findViewById(R.id.tdesc);
        Long tid = currentTeam.getId();
        teamId.setText("团队id: "+tid.toString());
        teamDesc.setText(currentTeam.getDesc());
        teamId.setTextColor(context.getResources().getColor(R.color.black));
        teamDesc.setTextColor(context.getResources().getColor(R.color.black));
        teamId.setPaintFlags(teamId.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
        teamDesc.setPaintFlags(teamId.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);

        return itemView;
    }

}
