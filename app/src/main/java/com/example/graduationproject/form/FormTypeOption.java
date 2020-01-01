package com.example.graduationproject.form;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.example.graduationproject.R;

import org.json.JSONArray;
import org.json.JSONObject;

public class FormTypeOption extends FormAbstract {
    private Context mContext;
    private int mType;
    private LayoutInflater mInflater;
    private EditText mEditQuestion;
    private ImageButton mDeleteView;
    private TextView mTxtDescription;
    private Button mBtnAddOption;
    private ImageButton mBtnDeleteOption;
    private Switch mSwitch;
    private LinearLayout mParentContainer;
    private LinearLayout mAddOptionContainer;
    private LinearLayout mContainer;

    public FormTypeOption(Context context, int type){
        super(context,type);
        mContext=context;
        this.mType=type;
        mInflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mInflater.inflate(R.layout.form_type_option,this,true);
        init();
    }
    public void init(){
        mEditQuestion=(EditText)findViewById(R.id.editQuestion);
        mSwitch=(Switch)findViewById(R.id.required_switch);
        mTxtDescription=(TextView)findViewById(R.id.txtDescription);
        mDeleteView=(ImageButton)findViewById(R.id.delete_view);
        mBtnAddOption=(Button)findViewById(R.id.btnAddOption);
        mBtnDeleteOption=(ImageButton)findViewById(R.id.delete_option);

        mAddOptionContainer=(LinearLayout)findViewById(R.id.add_option_container);
        mParentContainer =(LinearLayout)mAddOptionContainer.getParent().getParent();
        mContainer=(LinearLayout)mAddOptionContainer.getParent();
        mBtnAddOption.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Option option=new Option(mContext,mType);
                mAddOptionContainer.addView(option);
            }
        });
        mDeleteView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mParentContainer.removeView(mContainer);
            }
        });
        if(mType==FormType.MULTIPLECHOICE){
            mTxtDescription.setText("multiple choice");
        }else if(mType==FormType.CHECKBOXES){
            mTxtDescription.setText("checkbox");
        }else if(mType==FormType.DROPDOWN){
            mTxtDescription.setText("dropdown");
        }
    }
    @Override
    public JSONObject getJsonObject(){
        JSONObject jsonObject=new JSONObject();
        try{
            jsonObject.put("type",mType);
            jsonObject.put("question",mEditQuestion.getText().toString());
            jsonObject.put("required_switch",mSwitch.isChecked());
            JSONArray jsonArray=new JSONArray();
            for(int i=0;i<mAddOptionContainer.getChildCount();i++){
                jsonArray.put(i,((Option)mAddOptionContainer.getChildAt(i)).getOption());
            }
            jsonObject.put("addedOption",jsonArray);
        }catch (Exception e){
            e.printStackTrace();
        }

        return jsonObject;
    }

    @Override
    public void formComponentSetting(FormComponentVO vo) {
        mEditQuestion.setText(vo.getQuestion());
        mSwitch.setChecked(vo.isRequired_switch());
        for(int i=0;i<vo.getAddedOption().size();i++){
            Option option=new Option(mContext,mType);
            mAddOptionContainer.addView(option);
            option.setOption(vo.getAddedOption().get(i));
        }
        //mParentContainer.addView(mContainer);
    }
}
