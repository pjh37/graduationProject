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
    private Spinner spinner;
    private Spinner beginSpinner;
    private Spinner endSpinner;
    private EditText beginLabel;
    private EditText endLabel;
    private ArrayList<String> beginList;
    private ArrayList<String> endList;
    private boolean selected;
    private ViewGroup parentView;
    public FormTypeLinear(Context context, int type){
        super(context,type);
        mContext=context;
        this.mType=type;
        mInflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        customView=mInflater.inflate(R.layout.form_type_linear,this,true);
        spinner=(Spinner)findViewById(R.id.spinner);
        selected=true;
        mEditQuestion=(EditText)findViewById(R.id.editQuestion);
        mSwitch=(Switch)findViewById(R.id.required_switch);
        beginSpinner=(Spinner)findViewById(R.id.begin_spinner);
        endSpinner=(Spinner)findViewById(R.id.end_spinner);
        beginLabel=(EditText)findViewById(R.id.begin_label);
        endLabel=(EditText)findViewById(R.id.end_label);
        mDeleteView=(ImageButton)findViewById(R.id.delete_view);
        mDeleteView.setOnClickListener(new ClickListener());
        beginList=new ArrayList<>();
        beginList.add("0");
        beginList.add("1");
        endList=new ArrayList<>();
        for(int i=3;i<=10;i++){
            endList.add(String.valueOf(i));
        }
        ArrayList<String> list=new ArrayList<>();
        for(String str : getResources().getStringArray(R.array.formType)){list.add(str);}
        CustomSpinnerAdapter spinnerAdapter=new CustomSpinnerAdapter(mContext,list);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new ItemSelectListener());
        spinner.setSelection(5);
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
            jsonObject.put("begin_label",beginLabel.getText().toString());
            jsonObject.put("end_label",endLabel.getText().toString());
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
    public class ItemSelectListener implements AdapterView.OnItemSelectedListener{
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
            if(selected){
                selected=false;
            }else{
                FormAbstract layout=FormFactory.getInstance(mContext,position).createForm();
                parentView=(ViewGroup)customView.getParent();
                int indexOfChild=parentView.indexOfChild(customView);
                parentView.addView(layout,indexOfChild);
                parentView.removeView(customView);
            }
        }
        @Override
        public void onNothingSelected(AdapterView<?> adapterView) { }
    }
}
