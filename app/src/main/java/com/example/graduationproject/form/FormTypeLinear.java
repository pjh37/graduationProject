package com.example.graduationproject.form;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;

import com.example.graduationproject.R;

import org.json.JSONObject;

public class FormTypeLinear extends FormAbstract{
    private Context mContext;
    private int mType;
    private LayoutInflater mInflater;
    private View customView;
    public FormTypeLinear(Context context, int type){
        super(context,type);
        mContext=context;
        this.mType=type;
        mInflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        customView=mInflater.inflate(R.layout.form_type_text,this,true);
    }

    @Override
    public JSONObject getJsonObject() {
        return null;
    }

    @Override
    public void formComponentSetting(FormComponentVO vo) {

    }

    @Override
    public void onClickListener(OnClickListener listener) {

    }

    @Override
    public void onItemSelectedListener(AdapterView.OnItemSelectedListener listener) {

    }
}
