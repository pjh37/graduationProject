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

    private EditText mEditQuestion;
    private Spinner spinner;
    private Spinner beginSpinner;
    private Spinner endSpinner;

    private EditText beginLabel;
    private EditText endLabel;

    private ImageButton mCopyView;
    private ImageButton mDeleteView;
    private Switch mSwitch;

    public FormTypeLinear(Context context, int type){
        super(context,type);
        mContext=context;
        this.mType=type; // 위에 super 와 중복 아니야?

        mInflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        customView=mInflater.inflate(R.layout.form_type_linear,this,true);

        mEditQuestion = (EditText) findViewById(R.id.editQuestion);
        spinner = (Spinner) findViewById(R.id.spinner);
        beginSpinner = (Spinner) findViewById(R.id.begin_spinner);
        endSpinner = (Spinner) findViewById(R.id.end_spinner);

        beginLabel =  findViewById(R.id.begin_label);
        endLabel = findViewById(R.id.end_label);

        mCopyView = findViewById(R.id.copy_view);
        mDeleteView = (ImageButton) findViewById(R.id.delete_view);
        mSwitch = (Switch) findViewById(R.id.required_switch);

        String[] spinnerItems =getResources().getStringArray(R.array.templateItemSpinner);
        CustomSpinnerAdapter spinnerAdapter=new CustomSpinnerAdapter(mContext,spinnerItems);
        spinner.setAdapter(spinnerAdapter);
        spinner.setSelection(mType);
        spinner.setOnItemSelectedListener(new ItemSelectListener());

        mCopyView.setOnClickListener(new ClickListener());
        mDeleteView.setOnClickListener(new ClickListener());

    }
    public FormTypeLinear(FormCopyFactory fcf) {
        super(fcf.getmContext(), fcf.getmType());
        mContext = fcf.getmContext();
        this.mType = fcf.getmType();

        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        customView = mInflater.inflate(R.layout.form_type_linear, this, true); // this 는 어디고 true 면 어디에 붙는거야?

        mEditQuestion = (EditText) findViewById(R.id.editQuestion);
        spinner = (Spinner) findViewById(R.id.spinner);
        beginSpinner = (Spinner) findViewById(R.id.begin_spinner);
        endSpinner = (Spinner) findViewById(R.id.end_spinner);

//        labelLeft=  findViewById(R.id.labelLeft);
//        labelRight=  findViewById(R.id.labelRight);
        beginLabel =  findViewById(R.id.begin_label);
        endLabel = findViewById(R.id.end_label);

        mCopyView = findViewById(R.id.copy_view);
        mDeleteView = (ImageButton) findViewById(R.id.delete_view);
        mSwitch = (Switch) findViewById(R.id.required_switch);

        mEditQuestion.setText(fcf.getEditQuestion_text());
        String[] spinnerItems = getResources().getStringArray(R.array.templateItemSpinner);
        CustomSpinnerAdapter spinnerAdapter = new CustomSpinnerAdapter(mContext, spinnerItems);
        spinner.setAdapter(spinnerAdapter);
        spinner.setSelection(fcf.getmType()); // 처음에 한 번 호출
        spinner.setOnItemSelectedListener(new ItemSelectListener());

        beginSpinner.setSelection(fcf.getBegin_spinner_position(), false);
        endSpinner.setSelection(fcf.getEnd_spinner_position(), false);

//        labelLeft.setText(fcf.getLabelLeft_text());
//        labelRight.setText(fcf.getLabelRight_text());
        beginLabel.setText(fcf.getLabelLeft_text());
        endLabel.setText(fcf.getLabelRight_text());

        mCopyView.setOnClickListener(new ClickListener());
        mDeleteView.setOnClickListener(new ClickListener());
        mSwitch.setChecked(fcf.isRequired_switch_bool());

    }

    @Override
    public JSONObject getJsonObject() {
        JSONObject jsonObject=new JSONObject();
        try{
            jsonObject.put("type",mType);
            jsonObject.put("question",mEditQuestion.getText().toString());

            jsonObject.put("begin",beginSpinner.getSelectedItemPosition());
            jsonObject.put("end",endSpinner.getSelectedItemPosition());

//            if (labelLeft.getText().toString().trim().length() > 0) {
//                jsonObject.put("begin_label",beginLabel.getText().toString());
//            }
//            if (labelRight.getText().toString().trim().length() > 0) {
//                jsonObject.put("end_label",endLabel.getText().toString());
//            }

            jsonObject.put("begin_label",beginLabel.getText().toString());
            jsonObject.put("end_label",endLabel.getText().toString());

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
        beginSpinner.setSelection(vo.getBegin());
        endSpinner.setSelection(vo.getEnd());
    }
    public class ClickListener implements OnClickListener{
        @Override
        public void onClick(View view) {
            if (view == mDeleteView) {
                ViewGroup parentView = (ViewGroup) customView.getParent();
                parentView.removeView(customView);
            }else if(view == mCopyView){
                //copyItem(customView); // customView 를 바꿔주면 되는군

                ViewGroup parentView = (ViewGroup) customView.getParent();
                int index = parentView.indexOfChild(customView); // 위치 인덱스

                FormAbstract layout = new FormCopyFactory.Builder(mContext,mType)
                        .Question(mEditQuestion.getText().toString())
                        .LinearSpinnerPosition(beginSpinner.getSelectedItemPosition(),endSpinner.getSelectedItemPosition())
                        .LabelText(beginLabel.getText().toString(),endLabel.getText().toString())
                        .RequiredSwitchBool(mSwitch.isChecked())
                        .build()
                        .createCopyForm();

                // FormAbstract 로 cast 되도 데이터만 전해진다면
                parentView.addView(layout, index+1);

//                remove_BaseFormActivity.AddcopiedLayouts(index, layout);
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
            Log.d("mawang", "spinner nothing ");
        }
    }
}
