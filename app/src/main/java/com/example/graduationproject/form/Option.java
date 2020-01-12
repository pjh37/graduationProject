package com.example.graduationproject.form;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.graduationproject.R;

public class Option extends LinearLayout {
    private static final int MULTIPLECHOICE=2;
    private static final int CHECKBOXES=3;
    private static final int DROPDOWN=4;
    private static final int MULTIPLECHOICEGRID=6;
    private Context mContext;
    private EditText editOption;
    private View customView;
    public Option(Context context, int type){
        super(context);
        this.mContext=context;
        LayoutInflater inflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        customView=inflater.inflate(R.layout.form_option,this,true);
        imgSelect(type);
        ImageButton imageButton=(ImageButton)findViewById(R.id.delete_option);
        editOption=(EditText)findViewById(R.id.editOption);
        imageButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewGroup parentView=(ViewGroup)customView.getParent();
                parentView.removeView(customView);
                //((ViewGroup)view.getParent().getParent()).removeView((ViewGroup)view.getParent());
                //Log.v("테스트","mAddOptionContainer child : "+((ViewGroup)view.getParent().getParent()).getChildCount());
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
            case MULTIPLECHOICEGRID:img_icon.setImageResource(R.drawable.multiplechoice);break;
            case MULTIPLECHOICE: img_icon.setImageResource(R.drawable.multiplechoice);break;
            case CHECKBOXES: img_icon.setImageResource(R.drawable.checkbox);break;
            case DROPDOWN: img_icon.setImageResource(R.drawable.dropdown);break;

        }
    }

}
