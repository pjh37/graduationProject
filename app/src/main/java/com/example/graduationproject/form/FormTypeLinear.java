package com.example.graduationproject.form;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Switch;

import com.example.graduationproject.CustomSpinnerAdapter;
import com.example.graduationproject.R;

import org.json.JSONObject;

import java.util.ArrayList;

public class FormTypeLinear extends FormAbstract{
    private Context mContext;
    private int mType;
    private LayoutInflater mInflater;
    private View customView;
    private ImageButton mDeleteView;
    private EditText mEditQuestion;
    private Switch mSwitch;
    private Spinner beginSpinner;
    private Spinner endSpinner;
    private ArrayList<String> beginList;
    private ArrayList<String> endList;
    public FormTypeLinear(Context context, int type){
        super(context,type);
        mContext=context;
        this.mType=type;
        mInflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        customView=mInflater.inflate(R.layout.form_type_linear,this,true);
        mEditQuestion=(EditText)findViewById(R.id.editQuestion);
        mSwitch=(Switch)findViewById(R.id.required_switch);
        beginSpinner=(Spinner)findViewById(R.id.begin_spinner);
        endSpinner=(Spinner)findViewById(R.id.end_spinner);
        mDeleteView=(ImageButton)findViewById(R.id.delete_view);
        mDeleteView.setOnClickListener(new ClickListener());
        beginList=new ArrayList<>();
        beginList.add("0");
        beginList.add("1");
        endList=new ArrayList<>();
        for(int i=3;i<=10;i++){
            endList.add(String.valueOf(i));
        }
        CustomSpinnerAdapter beginSpinnerAdapter=new CustomSpinnerAdapter(mContext,beginList);
        CustomSpinnerAdapter endSpinnerAdapter=new CustomSpinnerAdapter(mContext,endList);
        beginSpinner.setAdapter(beginSpinnerAdapter);
        endSpinner.setAdapter(endSpinnerAdapter);
    }

    @Override
    public JSONObject getJsonObject() {
        JSONObject jsonObject=new JSONObject();
        try{
            jsonObject.put("type",mType);
            jsonObject.put("question",mEditQuestion.getText().toString());
            jsonObject.put("required_switch",mSwitch.isChecked());
            jsonObject.put("begin",beginSpinner.getSelectedItemPosition());
            jsonObject.put("end",endSpinner.getSelectedItemPosition());
            Log.v("테스트",beginSpinner.getSelectedItemPosition()+"  "+endSpinner.getSelectedItemPosition());
        }catch (Exception e){
            e.printStackTrace();
        }

        return jsonObject;
    }

    @Override
    public void formComponentSetting(FormComponentVO vo) {
        mEditQuestion.setText(vo.getQuestion());
        mSwitch.setChecked(vo.isRequired_switch());
        beginSpinner.setSelection(vo.getBegin());
        endSpinner.setSelection(vo.getEnd());
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

}
