package com.example.graduationproject.form;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.example.graduationproject.CustomSpinnerAdapter;
import com.example.graduationproject.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class FormTypeText extends FormAbstract{
    private Context mContext;
    private int mType;
    private LayoutInflater mInflater;
    private EditText mEditQuestion;
    private ImageButton mDeleteView;
    private TextView mTxtDescription;
    private Switch mSwitch;
    private Spinner spinner;
    private LinearLayout mParentContainer;
    private LinearLayout mContainer;
    private ArrayList<FormAbstract> layouts;
    private View customView;
    public FormTypeText(Context context, int type){
        super(context,type);
        mContext=context;
        this.mType=type;
        mInflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        customView=mInflater.inflate(R.layout.form_type_text,this,true);


        init();

    }
    public void init(){

        mEditQuestion=(EditText)findViewById(R.id.editQuestion);
        mSwitch=(Switch)findViewById(R.id.required_switch);
        mTxtDescription=(TextView)findViewById(R.id.txtDescription);
        mDeleteView=(ImageButton)findViewById(R.id.delete_view);
        spinner=(Spinner)findViewById(R.id.spinner);
        ArrayList<String> list=new ArrayList<>();

        list.add("단답형");
        list.add("장문형");
        list.add("다중선택");
        list.add("체크박스");
        list.add("드롭다운");
        list.add("범위질문");
        list.add("그리드");
        list.add("날짜");
        list.add("시간");
        list.add("구획분할");
        list.add("이미지");
        list.add("동영상");
        CustomSpinnerAdapter spinnerAdapter=new CustomSpinnerAdapter(mContext,list);
        spinner.setAdapter(spinnerAdapter);

        mContainer=(LinearLayout)mEditQuestion.getParent().getParent();
        mParentContainer=(LinearLayout)mEditQuestion.getParent().getParent().getParent();

        mDeleteView.setOnClickListener(new ClickListener());

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
    public class ClickListener implements OnClickListener{
        @Override
        public void onClick(View view) {
            if(view==mDeleteView){
                ViewGroup parentView=(ViewGroup)customView.getParent();
                parentView.removeView(customView);
            }
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
