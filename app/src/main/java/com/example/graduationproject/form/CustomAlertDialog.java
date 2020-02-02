package com.example.graduationproject.form;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        // 10 * 10 호출됨 .. 비정상

        ViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(resid, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder); // gettag를 위해서

            ImageView imageView = viewHolder.imageView;
            TextView txtType = viewHolder.textView;
            imageView.setImageResource(datas.get(position).getImgType());
            txtType.setText(datas.get(position).getTxtType());

            if (position == 2 || position == 5 || position == 8 || position == 10) {
                convertView.setBackgroundResource(R.drawable.divider);
            }

        }

        return convertView;
    }

    class ViewHolder {
        public ImageView imageView;
        public TextView textView;

        public ViewHolder(View v) {
            imageView = (ImageView) v.findViewById(R.id.imgType);
            textView = (TextView) v.findViewById(R.id.txtType);
        }
    }

}
