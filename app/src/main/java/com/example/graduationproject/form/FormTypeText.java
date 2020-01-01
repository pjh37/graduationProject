package com.example.graduationproject.form;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.example.graduationproject.R;

import org.json.JSONArray;
import org.json.JSONObject;

public class FormTypeText extends FormAbstract{
    private Context mContext;
    private int mType;
    private LayoutInflater mInflater;
    private EditText mEditQuestion;
    private ImageButton mDeleteView;
    private TextView mTxtDescription;
    private Switch mSwitch;
    private LinearLayout mParentContainer;
    private LinearLayout mContainer;

    public FormTypeText(Context context, int type){
        super(context,type);
        mContext=context;
        this.mType=type;
        mInflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mInflater.inflate(R.layout.form_type_text,this,true);
        init();
    }
    public void init(){
        mEditQuestion=(EditText)findViewById(R.id.editQuestion);
        mSwitch=(Switch)findViewById(R.id.required_switch);
        mTxtDescription=(TextView)findViewById(R.id.txtDescription);
        mDeleteView=(ImageButton)findViewById(R.id.delete_view);
        mContainer=(LinearLayout)mEditQuestion.getParent();
        mParentContainer=(LinearLayout)mEditQuestion.getParent().getParent();
        mDeleteView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mParentContainer.removeView((LinearLayout)mEditQuestion.getParent());
            }
        });
        if(mType==FormType.SHORTTEXT){
            mTxtDescription.setText("짧은 글");
        }else if(mType==FormType.LONGTEXT){
            mTxtDescription.setText("긴 글");
        }else if(mType==FormType.DATE){
            mTxtDescription.setText("날짜");
        }else if(mType==FormType.TIME){
            mTxtDescription.setText("시간");
        }
    }
    @Override
    public JSONObject getJsonObject(){
        JSONObject jsonObject=new JSONObject();
        try{
            jsonObject.put("type",mType);
            jsonObject.put("question",mEditQuestion.getText().toString());
            jsonObject.put("required_switch",mSwitch.isChecked());
        }catch (Exception e){
            e.printStackTrace();
        }

        return jsonObject;
    }
    @Override
    public void formComponentSetting(FormComponentVO vo) {
        mEditQuestion.setText(vo.getQuestion());
        mSwitch.setChecked(vo.isRequired_switch());
        //mParentContainer.addView(mContainer);
    }
}
