package com.example.graduationproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomSpinnerAdapter extends BaseAdapter {
    private ArrayList<String> datas;
    private Context mContext;
    public CustomSpinnerAdapter(Context mContext,ArrayList<String> datas){
        this.mContext=mContext;
        this.datas=datas;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int i) {
        return datas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view==null){
            view= LayoutInflater.from(mContext).inflate(R.layout.custom_spinner_item,null);
        }
        TextView tv=(TextView)view.findViewById(R.id.spinner_item);
        tv.setText(datas.get(i));

        return view;
    }
}
