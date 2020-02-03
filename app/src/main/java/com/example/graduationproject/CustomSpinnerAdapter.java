package com.example.graduationproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomSpinnerAdapter extends BaseAdapter {
    private Context mContext;
    private String[] spinnerTexts;

    private int[] spinnerImages={R.drawable.shortanswer,R.drawable.longanswer
            ,R.drawable.multiplechoice,R.drawable.checkbox,R.drawable.dropdown,
            R.drawable.linear_scale,R.drawable.radiogrid,R.drawable.checkboxgrid,
            R.drawable.date,R.drawable.time
    };

    public CustomSpinnerAdapter(Context mContext, String[] texts) {
        this.mContext = mContext;
        this.spinnerTexts = texts;
    }

    @Override
    public int getCount() {
        return spinnerTexts.length;
    }

    @Override
    public Object getItem(int i) {
        return spinnerTexts[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    // 스피너 화면에 적용
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    // 드롭다운 팝업에 적용
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.custom_spinner_item_imgadded, null);

        // 텍스트 넣기
        TextView tv = (TextView) view.findViewById(R.id.spinner_item_text);
        tv.setText(spinnerTexts[position]);

        // 이미지 넣기
        ImageView img = view.findViewById(R.id.spinner_item_img);
        img.setImageResource(spinnerImages[position]);

        // 꼼수 바운드리 넣기
        if (position == 2 || position == 5 || position == 8) { // mType
            view.setBackgroundResource(R.drawable.divider);
        }

        return view;
    }




}
