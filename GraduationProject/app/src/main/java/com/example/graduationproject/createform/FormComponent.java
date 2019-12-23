package com.example.graduationproject.createform;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.graduationproject.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class FormComponent extends LinearLayout {
    private static final int SHORTTEXT=0;
    private static final int LONGTEXT=1;
    private static final int MULTIPLECHOICE=2;
    private static final int CHECKBOXES=3;
    private static final int DROPDOWN=4;
    private static final int LINEARSCALE=5;
    private static final int MULTIPLECHOICEGRID=6;
    private static final int DATE=7;
    private static final int TIME=8;
    private static final int ADDSECTION=9;
    private Context mContext;
    private int mType;
    private LayoutInflater inflater;
    private LinearLayout container;
    private EditText editQuestion;
    private ImageButton delete_view;
    private TextView txtDescription;
    private Button btnAddOption;
    private Switch mSwitch;
    private int mOptionCnt=0;
    public FormComponent(Context context,int type){
        super(context);
        this.mContext=context;
        this.mType=type;
        this.inflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        init();
    }
    public void init(){
       layoutCreate();
       viewInit();
       setDescription();
    }

    public JSONObject getJsonObject(){
        JSONObject jsonObject=new JSONObject();
        try{
            jsonObject.put("type",mType);
            jsonObject.put("question",editQuestion.getText().toString());
            if(mType==SHORTTEXT||mType==LONGTEXT||mType==DATE||mType==TIME){
                jsonObject.put("required_switch",mSwitch.isChecked());
            } else if(mType==MULTIPLECHOICE||mType==CHECKBOXES||mType==DROPDOWN){
                jsonObject.put("required_switch",mSwitch.isChecked());
                JSONArray jsonArray=new JSONArray();
                for(int i=0;i<mOptionCnt;i++){
                    jsonArray.put(i,((Option)container.getChildAt(i)).getOption());
                }
                jsonObject.put("addedOption",jsonArray);
            }else if(mType==LINEARSCALE||mType==MULTIPLECHOICEGRID){

            }else if(mType==ADDSECTION){

            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return jsonObject;
    }
    public void setComponent(JSONObject jsonObject){
        if(jsonObject==null)return;
        try{
            editQuestion.setText(jsonObject.getString("question"));
            if(mType==SHORTTEXT||mType==LONGTEXT||mType==DATE||mType==TIME){
                mSwitch.setChecked(jsonObject.getBoolean("required_switch"));
            } else if(mType==MULTIPLECHOICE||mType==CHECKBOXES||mType==DROPDOWN){
                mSwitch.setChecked(jsonObject.getBoolean("required_switch"));
                JSONArray jsonArray=jsonObject.getJSONArray("addedOption");
                for(int i=0;i<jsonArray.length();i++){
                    Option op=new Option(mContext,mType);
                    container.addView(op);
                    ((Option)container.getChildAt(i)).setOption(jsonArray.getString(i));
                }
            }else if(mType==LINEARSCALE||mType==MULTIPLECHOICEGRID){

            }else if(mType==ADDSECTION){

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void layoutCreate(){
        if(mType==SHORTTEXT||mType==LONGTEXT||mType==DATE||mType==TIME){
            noAddOption(mType);
        }else if(mType==MULTIPLECHOICE||mType==CHECKBOXES||mType==DROPDOWN){
            addOption(mType);
        }else if(mType==LINEARSCALE||mType==MULTIPLECHOICEGRID){

        }
        else if(mType==ADDSECTION){
            addSection(mType);
        }
    }
    public void viewInit(){
        container=(LinearLayout)findViewById(R.id.container);
        txtDescription=(TextView)findViewById(R.id.txtDescription);
        delete_view=(ImageButton)findViewById(R.id.delete_view);
        btnAddOption=(Button)findViewById(R.id.btnAddOption);
        editQuestion=(EditText)findViewById(R.id.editQuestion);
        mSwitch=(Switch)findViewById(R.id.required_switch);
        delete_view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ViewGroup)view.getParent().getParent().getParent()).removeView((ViewGroup)view.getParent().getParent());
            }
        });
        if(mType==MULTIPLECHOICE||mType==CHECKBOXES||mType==DROPDOWN){
            btnAddOption.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    Option op=new Option(mContext,mType);
                    container.addView(op);
                    mOptionCnt++;
                    //Log.v("테스트",((Option)container.getChildAt(0)).getOption());
                }
            });
        }

    }
    public void setDescription(){
        switch (mType){
            case SHORTTEXT: txtDescription.setText("짧은 글");break;
            case LONGTEXT: txtDescription.setText("긴 글");break;
            case DATE: txtDescription.setText("날짜");break;
            case TIME: txtDescription.setText("시간");break;
            case MULTIPLECHOICE: txtDescription.setText("multiple choice");break;
            case CHECKBOXES: txtDescription.setText("checkbox");break;
            case DROPDOWN: txtDescription.setText("dropdown");break;
        }
    }
    public void noAddOption(int type){
        inflater.inflate(R.layout.form_no_add_option,this,true);
    }
    public void addOption(int type){
        inflater.inflate(R.layout.form_add_option,this,true);
    }
    public void addSection(int type){
        inflater.inflate(R.layout.form_add_section,this,true);
    }
}
