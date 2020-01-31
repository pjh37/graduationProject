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
import android.widget.ImageView;
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
    private View customView;
    private ViewGroup parentView;
    private boolean selected;
    public FormTypeText(Context context, int type){
        super(context,type);
        mContext=context;
        this.mType=type;
        mInflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        customView=mInflater.inflate(R.layout.form_type_text,this,true);
        selected=true;


        init();

    }
    public void init(){

        mEditQuestion=(EditText)findViewById(R.id.editQuestion);
        mSwitch=(Switch)findViewById(R.id.required_switch);
        mTxtDescription=(TextView)findViewById(R.id.txtDescription);
        mDeleteView=(ImageButton)findViewById(R.id.delete_view);
        spinner=(Spinner)findViewById(R.id.spinner);
        ArrayList<String> list=new ArrayList<>();
        for(String str : getResources().getStringArray(R.array.formType)){list.add(str);}
        CustomSpinnerAdapter spinnerAdapter=new CustomSpinnerAdapter(mContext,list);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new ItemSelectListener());
        spinner.setSelection(mType);
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
                parentView=(ViewGroup)customView.getParent();
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
