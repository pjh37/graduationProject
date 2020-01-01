package com.example.graduationproject.form;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.graduationproject.R;

import org.json.JSONArray;
import org.json.JSONObject;

public class FormTypeSection extends FormAbstract{
    private Context mContext;
    private int mType;
    private LayoutInflater mInflater;
    private EditText mEditTitle;
    private EditText mEditDescription;
    private ImageButton mDeleteView;
    private LinearLayout mParentContainer;
    private LinearLayout mContainer;
    public FormTypeSection(Context context, int type){
        super(context,type);
        mContext=context;
        this.mType=type;
        mInflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mInflater.inflate(R.layout.form_type_section,this,true);
        init();
    }
    public void init(){
        mEditTitle=(EditText)findViewById(R.id.editTitle);
        mEditDescription=(EditText)findViewById(R.id.editDescription);
        mDeleteView=(ImageButton)findViewById(R.id.delete_view);
        mContainer=(LinearLayout)mEditTitle.getParent();
        mParentContainer=(LinearLayout)mEditTitle.getParent().getParent();
        mDeleteView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mParentContainer.removeView((LinearLayout)mEditTitle.getParent());
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
        //mParentContainer.addView(mContainer);
    }
}
