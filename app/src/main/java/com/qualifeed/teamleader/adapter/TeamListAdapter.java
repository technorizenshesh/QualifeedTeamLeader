package com.qualifeed.teamleader.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qualifeed.teamleader.R;
import com.qualifeed.teamleader.model.ProductTypeModel;
import com.qualifeed.teamleader.model.TeamListModel;

import java.util.ArrayList;

public class TeamListAdapter extends BaseAdapter {
    Context context;
    ArrayList<TeamListModel.Result> arrayList;
    LayoutInflater inflater;
    // onPosListener listener;

    public TeamListAdapter(Context context, ArrayList<TeamListModel.Result> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        //this.listener = listener;

        inflater = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.item_type, null);
        TextView names = convertView.findViewById(R.id.item);
        RelativeLayout mainView = convertView.findViewById(R.id.mainView);
        names.setText(arrayList.get(position).getTeamName());

/*
        mainView.setOnClickListener(v -> listener.onPos(position,""));
*/

        return convertView;
    }
}
