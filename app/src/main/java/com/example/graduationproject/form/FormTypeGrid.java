package com.example.graduationproject.form;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;

import com.example.graduationproject.CustomSpinnerAdapter;
import com.example.graduationproject.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class FormTypeGrid extends FormAbstract {
    private Context mContext;
    private int mType;
    private LayoutInflater mInflater;
    private ImageButton mDeleteView;
    private EditText mEditQuestion;
    private Switch mSwitch;
    private Spinner spinner;
    private LinearLayout mAddedRowContainer;
    private LinearLayout mAddedColContainer;
    private Button mBtnAddRow;
    private Button mBtnAddCol;
    private View customView;
    public FormTypeGrid(Context context, int type) {
        super(context, type);
        mContext=context;
        this.mType=type;
        mInflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        customView=mInflater.inflate(R.layout.form_type_multiple_choice_grid,this,true);
        mAddedRowContainer=(LinearLayout)findViewById(R.id.added_row_container);
        mAddedColContainer=(LinearLayout)findViewById(R.id.added_col_container);
        mBtnAddCol=(Button)findViewById(R.id.btnAddCol);
        mBtnAddRow=(Button)findViewById(R.id.btnAddRow);
        mBtnAddCol.setOnClickListener(new ClickListener());
        mBtnAddRow.setOnClickListener(new ClickListener());
        spinner=(Spinner)findViewById(R.id.spinner);
        mDeleteView=(ImageButton)findViewById(R.id.delete_view);
        mDeleteView.setOnClickListener(new ClickListener());
        mEditQuestion=(EditText)findViewById(R.id.editQuestion);
        mSwitch=(Switch)findViewById(R.id.required_switch);
        ArrayList<String> list=new ArrayList<>();
        for(String str : getResources().getStringArray(R.array.formType)){list.add(str);}
        CustomSpinnerAdapter spinnerAdapter=new CustomSpinnerAdapter(mContext,list);
        spinner.setAdapter(spinnerAdapter);
        spinner.setSelection(6);
    }

    @Override
    public JSONObject getJsonObject() {
        JSONObject jsonObject=new JSONObject();
        try{
            jsonObject.put("type",mType);
            jsonObject.put("question",mEditQuestion.getText().toString());
            jsonObject.put("required_switch",mSwitch.isChecked());
            JSONArray jsonColArray=new JSONArray();
            for(int i=0;i<mAddedColContainer.getChildCount();i++){
                jsonColArray.put(i,((Option)mAddedColContainer.getChildAt(i)).getOption());
            }
            jsonObject.put("addedColOption",jsonColArray);
            JSONArray jsonRowArray=new JSONArray();
            for(int i=0;i<mAddedRowContainer.getChildCount();i++){
                jsonRowArray.put(i,((Option)mAddedRowContainer.getChildAt(i)).getOption());
            }
            jsonObject.put("addedRowOption",jsonRowArray);
        }catch (Exception e){
            e.printStackTrace();
        }
        return jsonObject;
    }

    @Override
    public void formComponentSetting(FormComponentVO vo) {
        mEditQuestion.setText(vo.getQuestion());
        mSwitch.setChecked(vo.isRequired_switch());
        for(int i=0;i<vo.getAddedColOption().size();i++){
            Option option=new Option(mContext,mType);
            option.setOption(vo.getAddedColOption().get(i));
            mAddedColContainer.addView(option);
        }
        for(int i=0;i<vo.getAddedRowOption().size();i++){
            Option option=new Option(mContext,mType);
            option.setOption(vo.getAddedRowOption().get(i));
            mAddedRowContainer.addView(option);
        }

    }

    public class ClickListener implements OnClickListener{
        @Override
        public void onClick(View view) {
            if(view==mDeleteView){
                ViewGroup parentView=(ViewGroup)customView.getParent();
                parentView.removeView(customView);
            }else if(view==mBtnAddCol){
                Option option=new Option(mContext,mType);
                mAddedColContainer.addView(option);
            }else if(view==mBtnAddRow){
                Option option=new Option(mContext,mType);
                mAddedRowContainer.addView(option);
            }
        }
    }



}
