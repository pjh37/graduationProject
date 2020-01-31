package com.example.graduationproject.form;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.graduationproject.R;

import org.json.JSONObject;

import java.util.ArrayList;

public abstract class FormAbstract extends LinearLayout {
    public abstract JSONObject getJsonObject();
    public abstract void formComponentSetting(FormComponentVO vo);
    private Context mContext;
    private int mType;
    public FormAbstract(Context context, int type){
        super(context);
        this.mType=type;
    }

}
