package com.example.graduationproject.createform;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.graduationproject.R;

public class Option extends LinearLayout {
    private static final int MULTIPLECHOICE=2;
    private static final int CHECKBOXES=3;
    private static final int DROPDOWN=4;
    private Context mContext;
    private EditText editOption;
    public Option(Context context, int type){
        super(context);
        this.mContext=context;
        LayoutInflater inflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.form_option,this,true);
        imgSelect(type);
        ImageButton imageButton=(ImageButton)findViewById(R.id.delete_option);
        editOption=(EditText)findViewById(R.id.editOption);
        imageButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ViewGroup)view.getParent().getParent()).removeView((ViewGroup)view.getParent());
            }
        });
    }
    public String getOption(){
        return editOption.getText().toString();
    }
    public void setOption(String str){
        editOption.setText(str);
    }
    public void imgSelect(int type){
        ImageView img_icon=(ImageView)findViewById(R.id.img_icon);
        switch (type){
            case MULTIPLECHOICE: img_icon.setImageResource(R.drawable.multiplechoice);break;
            case CHECKBOXES: img_icon.setImageResource(R.drawable.checkbox);break;
            case DROPDOWN: img_icon.setImageResource(R.drawable.dropdown);break;
        }
    }

}
