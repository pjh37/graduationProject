package com.example.graduationproject.form;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.example.graduationproject.CustomSpinnerAdapter;
import com.example.graduationproject.R;
import com.jmedeisis.draglinearlayout.DragLinearLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class FormTypeOption extends FormAbstract {
    private Context mContext;
    private int mType;

    private LayoutInflater mInflater;
    private View customView;

    private EditText mEditQuestion;
    private Spinner spinner;
    private Button mBtnAddOption;
    private Switch mSwitchEtc;
    private ImageButton mCopyView;
    private ImageButton mDeleteView;
    private Switch mSwitch;  // 필수응답 유무

    private LinearLayout mAddOptionContainer;
    private LinearLayout mEtcOptionContainer;

    private ArrayList<Option> rowTexts; // option text 저장할 것
    boolean EtcIsFisrt = true; //etc 한개만 생성

    public FormTypeOption(Context context, int type) {
        super(context, type);
        mContext = context;
        this.mType = type;

        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        customView = mInflater.inflate(R.layout.form_type_option, this, true);

        mEditQuestion = (EditText) findViewById(R.id.editQuestion);
        spinner = (Spinner) findViewById(R.id.spinner);
        mAddOptionContainer = (LinearLayout) findViewById(R.id.add_option_container);
        mEtcOptionContainer = (LinearLayout) findViewById(R.id.etc_option_container);
        mBtnAddOption = findViewById(R.id.btnAddOption);
        mSwitchEtc = findViewById(R.id.etc_switch);
        mCopyView = (ImageButton) findViewById(R.id.copy_view);
        mDeleteView = (ImageButton) findViewById(R.id.delete_view);
        mSwitch = (Switch) findViewById(R.id.required_switch);

        String[] spinnerItems = getResources().getStringArray(R.array.templateItemSpinner);
        CustomSpinnerAdapter spinnerAdapter = new CustomSpinnerAdapter(mContext, spinnerItems);
        spinner.setAdapter(spinnerAdapter);
        spinner.setSelection(mType);
        spinner.setOnItemSelectedListener(new ItemSelectListener());

        mBtnAddOption.setOnClickListener(new ClickListener());

        if (mType == FormType.DROPDOWN) { // 드롭다운은 필요없음
            TextView mTextEtc = findViewById(R.id.etc_text);
            mTextEtc.setVisibility(GONE);
            mSwitchEtc.setVisibility(GONE);
        } else {
            mSwitchEtc.setOnClickListener(new View.OnClickListener() {
                //boolean IsFisrt = true; // 한개만 생성
                public void onClick(View view) {
                    boolean on = mSwitchEtc.isChecked();
                    if (on) {
//                    Log.d("mawang", "Switch on = " + on);
                        mEtcOptionContainer.setVisibility(VISIBLE);
                        if (EtcIsFisrt) {
                            Option option = new Option(mContext, mType, true);
                            mEtcOptionContainer.addView(option);
                            EtcIsFisrt = false;
                        }
                    } else {
//                    Log.d("mawang", "Switch off = " + on);
//                    mEtcOptionContainer.removeViewAt(0);
                        mEtcOptionContainer.setVisibility(GONE);
                    }
                }
            });
        }

        mCopyView.setOnClickListener(new ClickListener());
        mDeleteView.setOnClickListener(new ClickListener());

        rowTexts = new ArrayList<>();

    }
    public FormTypeOption(FormCopyFactory fcf) {
        super(fcf.getmContext(), fcf.getmType());
        mContext = fcf.getmContext();
        this.mType = fcf.getmType();

        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        customView = mInflater.inflate(R.layout.form_type_option, this, true);

        mEditQuestion = (EditText) findViewById(R.id.editQuestion);
        spinner = (Spinner) findViewById(R.id.spinner);
        mAddOptionContainer = (LinearLayout) findViewById(R.id.add_option_container);
        mEtcOptionContainer = (LinearLayout) findViewById(R.id.etc_option_container);
        mBtnAddOption = findViewById(R.id.btnAddOption);
        mSwitchEtc = findViewById(R.id.etc_switch);
        mCopyView = (ImageButton) findViewById(R.id.copy_view);
        mDeleteView = (ImageButton) findViewById(R.id.delete_view);
        mSwitch = (Switch) findViewById(R.id.required_switch);

        mEditQuestion.setText(fcf.getEditQuestion_text());
        String[] spinnerItems = getResources().getStringArray(R.array.templateItemSpinner);
        CustomSpinnerAdapter spinnerAdapter = new CustomSpinnerAdapter(mContext, spinnerItems);
        spinner.setAdapter(spinnerAdapter);
        spinner.setSelection(fcf.getmType()); // 처음에 한 번 호출
        spinner.setOnItemSelectedListener(new ItemSelectListener());

        mBtnAddOption.setOnClickListener(new ClickListener());

        rowTexts = new ArrayList<>();
        rowTexts.addAll(fcf.getOptRowTexts());  // 깊은 복사
        int i = 0;
        for(Option o : rowTexts)
        {
            Option option = new Option(mContext, mType,o.getOption(),rowTexts); // need
            mAddOptionContainer.addView(option);
            rowTexts.set(i,option); // 안하면 수정된 텍스트들이 반영이 안됨
            i++;
        }

        if (mType == FormType.DROPDOWN) { // 드롭다운은 필요없음
            TextView mTextEtc = findViewById(R.id.etc_text);
            mTextEtc.setVisibility(GONE);
            mSwitchEtc.setVisibility(GONE);
        } else { // 하지만 객관식,체크박스 는 필요함
            mSwitchEtc.setChecked(fcf.isEtc_switch_bool());

            if(mSwitchEtc.isChecked()){ // 기타옵션 추가되어있다면 바로 부착
                mEtcOptionContainer.setVisibility(VISIBLE);
                Option option = new Option(mContext, mType, true);
                mEtcOptionContainer.addView(option);
            }

            mSwitchEtc.setOnClickListener(new View.OnClickListener() {
                boolean IsFisrt = !mSwitchEtc.isChecked(); // 기타옵션이 추가되어 복사된건지 확인
                public void onClick(View view) {
                    boolean on = mSwitchEtc.isChecked();
                    if (on) {
                        mEtcOptionContainer.setVisibility(VISIBLE);
                        if (IsFisrt) { // 제대로 안되면 etc 두 개 됨
                            Option option = new Option(mContext, mType, true);
                            mEtcOptionContainer.addView(option);
                            IsFisrt = false;
                        }
                    } else {
//                    mEtcOptionContainer.removeViewAt(0);
                        mEtcOptionContainer.setVisibility(GONE);
                    }
                }
            });
        }

        mCopyView.setOnClickListener(new ClickListener());
        mDeleteView.setOnClickListener(new ClickListener());
        mSwitch.setChecked(fcf.isRequired_switch_bool());

    }

    @Override
    public JSONObject getJsonObject(){
        Log.v("테스트","mAddOptionContainer child : "+mAddOptionContainer.getChildCount());
        JSONObject jsonObject=new JSONObject();
        try{
            jsonObject.put("type", mType);
            jsonObject.put("question", mEditQuestion.getText().toString());

            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < mAddOptionContainer.getChildCount(); i++) {
//                jsonArray.put(i, ((Option) mAddOptionContainer.getChildAt(i)).getOption());
                jsonArray.put(((Option) mAddOptionContainer.getChildAt(i)).getOption()); // text 들
            }
            jsonObject.put("addedOption", jsonArray);

            jsonObject.put("OptionEtc_switch", mSwitchEtc.isChecked());
            // 기타 text는 웹에서만 입력가능이기 때문에 보낼필요 없다.
//            jsonObject.put("OptionEtc_text", ((Option) mEtcOptionContainer.getChildAt(0)).getOption());

            jsonObject.put("required_switch", mSwitch.isChecked());
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
            Option option=new Option(mContext,mType,rowTexts);
            mAddOptionContainer.addView(option);
            option.setOption(vo.getAddedOption().get(i));
        }

        mSwitchEtc.setChecked(vo.isOptionEtc_switch());

        if(mSwitchEtc.isChecked()){
            mEtcOptionContainer.setVisibility(VISIBLE);
            Option option = new Option(mContext, mType, true); // boolean 아무거나 상관없음
            //option.setOption("etc test"); //etc 입력은 웹서버에서만 가능
            mEtcOptionContainer.addView(option);
            EtcIsFisrt = false;
        }
    }

    public class ClickListener implements OnClickListener{
        @Override
        public void onClick(View view) {
            if (view == mDeleteView) {
                ViewGroup parentView = (ViewGroup) customView.getParent();
                parentView.removeView(customView);
            }else if(view == mCopyView){
                DragLinearLayout parentView = (DragLinearLayout) customView.getParent();
                int index = parentView.indexOfChild(customView); // 위치 인덱스

                FormAbstract layout = new FormCopyFactory.Builder(mContext,mType)
                        .Question(mEditQuestion.getText().toString())
                        .OptRowTexts(rowTexts) //  option class
                        .EtcSwitchBool(mSwitchEtc.isChecked())
                        .RequiredSwitchBool(mSwitch.isChecked())
                        .build()
                        .createCopyForm();

                ViewGroup customlayout = (ViewGroup) layout.getChildAt(0);
                ImageView dragHandle = (ImageView)customlayout.getChildAt(0);
                parentView.addDragView(layout, dragHandle, index + 1);
            } else if (view == mBtnAddOption)
            {
                Option option = new Option(mContext, mType,rowTexts);
                mAddOptionContainer.addView(option);
                rowTexts.add(option);

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
                DragLinearLayout parentView = (DragLinearLayout) customView.getParent();
                int index = parentView.indexOfChild(customView);

                FormAbstract layout = FormFactory.getInstance(mContext, position).createForm(); // position is formType
                ViewGroup customlayout = (ViewGroup) layout.getChildAt(0); // xml 얻어옴
                ImageView dragHandle = (ImageView)customlayout.getChildAt(0);
                parentView.addDragView(layout, dragHandle, index);
                parentView.removeDragView(customView);
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
