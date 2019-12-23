package com.example.graduationproject.createform;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.graduationproject.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CustomAlertDialog extends ArrayAdapter<DialogVO> {
    private Context context;
    private ArrayList<DialogVO> datas;
    private int resid;
    public CustomAlertDialog(Context context, int resid, ArrayList<DialogVO> datas){
        super(context,resid);
        this.context=context;
        this.resid=resid;
        this.datas=datas;
    }
    @Override
    public int getCount() {
        return datas.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView==null){
            LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(resid,null);
            ViewHolder viewHolder=new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        ViewHolder viewHolder=(ViewHolder)convertView.getTag();
        ImageView imageView=viewHolder.imageView;
        TextView txtType=viewHolder.textView;
        imageView.setImageResource(datas.get(position).getImgType());
        txtType.setText(datas.get(position).getTxtType());
        return convertView;
    }
        class ViewHolder {
            public ImageView imageView;
            public TextView textView;
            public ViewHolder(View v){
                imageView=(ImageView)v.findViewById(R.id.imgType);
                textView=(TextView)v.findViewById(R.id.txtType);
            }
        }

}
