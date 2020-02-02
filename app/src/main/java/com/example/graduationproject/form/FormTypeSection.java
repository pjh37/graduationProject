package com.example.graduationproject.form;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.graduationproject.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class FormTypeSection extends FormAbstract{
    private Context mContext;
    private int mType;

    private LayoutInflater mInflater;
    private View customView;

    private EditText mEditTitle;
    private ImageButton mDeleteView;
    private EditText mEditDescription;

    public FormTypeSection(Context context, int type){
        super(context,type);
        mContext=context;
        this.mType=type;

        mInflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        customView=mInflater.inflate(R.layout.form_type_section,this,true);

        init();
    }
    public void init(){
        mEditTitle=(EditText)findViewById(R.id.editTitle);
        mDeleteView=(ImageButton)findViewById(R.id.delete_view);
        mEditDescription=(EditText)findViewById(R.id.editDescription);

        mDeleteView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewGroup parentView = (ViewGroup) customView.getParent();
                parentView.removeView(customView);
            }
        });
    }
    @Override
    public JSONObject getJsonObject(){
        JSONObject jsonObject=new JSONObject();
        try{
            jsonObject.put("type",mType);
            jsonObject.put("title",mEditTitle.getText().toString());
            jsonObject.put("description",mEditDescription.getText().toString());
        }catch (Exception e){
            e.printStackTrace();
        }
        return jsonObject;
    }
    @Override
    public void formComponentSetting(FormComponentVO vo) {
        mEditTitle.setText(vo.getQuestion());
        mEditDescription.setText(vo.getDescription());
    }

}
