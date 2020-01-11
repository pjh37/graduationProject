package com.example.graduationproject.form;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;

import com.example.graduationproject.R;

import org.json.JSONObject;

public class FormTypeGrid extends FormAbstract {
    private Context mContext;
    private int mType;
    private LayoutInflater mInflater;
    private ImageButton mDeleteView;
    private EditText mEditQuestion;
    private Switch mSwitch;
    private View customView;
    public FormTypeGrid(Context context, int type) {
        super(context, type);
        mContext=context;
        this.mType=type;
        mInflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        customView=mInflater.inflate(R.layout.form_type_multiple_choice_grid,this,true);
        mDeleteView=(ImageButton)findViewById(R.id.delete_view);
        mDeleteView.setOnClickListener(new ClickListener());
        mEditQuestion=(EditText)findViewById(R.id.editQuestion);
        mSwitch=(Switch)findViewById(R.id.required_switch);
    }

    @Override
    public JSONObject getJsonObject() {
        JSONObject jsonObject=new JSONObject();
        try{
            jsonObject.put("type",mType);
            jsonObject.put("question",mEditQuestion.getText().toString());
            jsonObject.put("required_switch",mSwitch.isChecked());
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void formComponentSetting(FormComponentVO vo) {

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
