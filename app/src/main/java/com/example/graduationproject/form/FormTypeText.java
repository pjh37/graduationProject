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
    private View customView;

    private EditText mEditQuestion;
    private Spinner spinner;
    private ImageButton mCopyView;
    private ImageButton mDeleteView;
    private Switch mSwitch;

    public FormTypeText(Context context, int type) {
        super(context, type);
        mContext = context;
        this.mType = type;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        customView = mInflater.inflate(R.layout.form_type_text, this, true);

        mEditQuestion = findViewById(R.id.editQuestion);
        spinner = findViewById(R.id.spinner);
        mCopyView =  findViewById(R.id.copy_view);
        mDeleteView =  findViewById(R.id.delete_view);
        mSwitch = findViewById(R.id.required_switch);

        String[] spinnerItems = getResources().getStringArray(R.array.templateItemSpinner);
        CustomSpinnerAdapter spinnerAdapter = new CustomSpinnerAdapter(mContext, spinnerItems);
        spinner.setAdapter(spinnerAdapter);
        spinner.setSelection(mType); // success
        spinner.setOnItemSelectedListener(new ItemSelectListener());

        mCopyView.setOnClickListener(new ClickListener());
        mDeleteView.setOnClickListener(new ClickListener());

    }
    public FormTypeText(FormCopyFactory fcf) {
        super(fcf.getmContext(), fcf.getmType());
        mContext = fcf.getmContext();
        this.mType = fcf.getmType();

        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        customView = mInflater.inflate(R.layout.form_type_text, this, true);

        mEditQuestion = findViewById(R.id.editQuestion);
        spinner = findViewById(R.id.spinner);
        mCopyView =  findViewById(R.id.copy_view);
        mDeleteView =  findViewById(R.id.delete_view);
        mSwitch = findViewById(R.id.required_switch);

        mEditQuestion.setText(fcf.getEditQuestion_text());

        String[] spinnerItems = getResources().getStringArray(R.array.templateItemSpinner);
        CustomSpinnerAdapter spinnerAdapter = new CustomSpinnerAdapter(mContext, spinnerItems);
        spinner.setAdapter(spinnerAdapter);
        spinner.setSelection(fcf.getmType()); // 처음에 한 번 호출
        spinner.setOnItemSelectedListener(new ItemSelectListener());

        mCopyView.setOnClickListener(new ClickListener());
        mDeleteView.setOnClickListener(new ClickListener());
        mSwitch.setChecked(fcf.isRequired_switch_bool());

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
    }

    public class ClickListener implements OnClickListener{
        @Override
        public void onClick(View view) {
            if (view == mDeleteView) {
                ViewGroup parentView = (ViewGroup) customView.getParent();
                parentView.removeView(customView);
            }else if(view == mCopyView){
                ViewGroup parentView = (ViewGroup) customView.getParent();
                int index = parentView.indexOfChild(customView); // 위치 인덱스

                FormAbstract layout = new FormCopyFactory.Builder(mContext,mType)
                        .Question(mEditQuestion.getText().toString())
                        .RequiredSwitchBool(mSwitch.isChecked())
                        .build()
                        .createCopyForm();

                parentView.addView(layout, index+1);
            }
        }
    }
    public class ItemSelectListener implements AdapterView.OnItemSelectedListener {
        private boolean spinner1steventprevent = false;
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
            if (position == 2 || position == 5 || position == 8) {
                view.setBackgroundResource(0);
            }
            if (spinner1steventprevent) {
                //changeItemTypeDynamically(position); // 스피너를 통한 항목 바꾸기
                FormAbstract layout=FormFactory.getInstance(mContext,position).createForm();
                ViewGroup parentView=(ViewGroup)customView.getParent();
                int indexOfChild=parentView.indexOfChild(customView);
                parentView.addView(layout,indexOfChild);
                parentView.removeView(customView);
            }else{
                spinner1steventprevent = true;
            }
        }
        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
            // when happen? but must override
//            Log.d("mawang", "spinner nothing ");
        }
    }

}
