package com.example.graduationproject.form;

import android.content.Context;
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

import com.example.graduationproject.CustomSpinnerAdapter;
import com.example.graduationproject.R;
import com.jmedeisis.draglinearlayout.DragLinearLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class FormTypeGrid extends FormAbstract {
    private Context mContext;
    private int mType;

    private LayoutInflater mInflater;
    private View customView;

    private EditText mEditQuestion;
    private Spinner spinner;
    private LinearLayout mAddedRowContainer;
    private Button mBtnAddRow;
    private LinearLayout mAddedColContainer;
    private Button mBtnAddCol;
    private ImageButton mCopyView;
    private ImageButton mDeleteView;
    private Switch mSwitch;

    private ArrayList<Option> rowTexts; // option text 저장할 것
    private ArrayList<Option> colTexts; // option text 저장할 것

    public FormTypeGrid(Context context, int type) {
        super(context, type);
        mContext=context;
        this.mType=type;

        mInflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        customView=mInflater.inflate(R.layout.form_type_multiple_choice_grid,this,true);

        mEditQuestion = (EditText) findViewById(R.id.editQuestion);
        spinner = (Spinner) findViewById(R.id.spinner);

        mAddedRowContainer = (LinearLayout) findViewById(R.id.added_row_container);
        mBtnAddRow = (Button) findViewById(R.id.btnAddRow);
        mAddedColContainer = (LinearLayout) findViewById(R.id.added_col_container);
        mBtnAddCol = (Button) findViewById(R.id.btnAddCol);
        mCopyView = (ImageButton) findViewById(R.id.copy_view);
        mDeleteView = (ImageButton) findViewById(R.id.delete_view);
        mSwitch = (Switch) findViewById(R.id.required_switch);

        String[] spinnerItems = getResources().getStringArray(R.array.templateItemSpinner);
        CustomSpinnerAdapter spinnerAdapter = new CustomSpinnerAdapter(mContext, spinnerItems);
        spinner.setAdapter(spinnerAdapter);
        spinner.setSelection(mType); // 스피너 선택된 것 표시
        spinner.setOnItemSelectedListener(new ItemSelectListener());

        mBtnAddCol.setOnClickListener(new ClickListener());
        mBtnAddRow.setOnClickListener(new ClickListener());
        mCopyView.setOnClickListener(new ClickListener());
        mDeleteView.setOnClickListener(new ClickListener());

        rowTexts = new ArrayList<>();
        colTexts = new ArrayList<>();

    }
    public FormTypeGrid(FormCopyFactory fcf) {
        super(fcf.getmContext(), fcf.getmType());
        mContext = fcf.getmContext();
        this.mType = fcf.getmType();

        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        customView = mInflater.inflate(R.layout.form_type_multiple_choice_grid, this, true);
        mEditQuestion = (EditText) findViewById(R.id.editQuestion);
        spinner = (Spinner) findViewById(R.id.spinner);
        mAddedRowContainer = (LinearLayout) findViewById(R.id.added_row_container);
        mBtnAddRow = (Button) findViewById(R.id.btnAddRow);
        mAddedColContainer = (LinearLayout) findViewById(R.id.added_col_container);
        mBtnAddCol = (Button) findViewById(R.id.btnAddCol);
        mCopyView = (ImageButton) findViewById(R.id.copy_view);
        mDeleteView = (ImageButton) findViewById(R.id.delete_view);
        mSwitch = (Switch) findViewById(R.id.required_switch);

        mEditQuestion.setText(fcf.getEditQuestion_text());

        String[] spinnerItems = getResources().getStringArray(R.array.templateItemSpinner);
        CustomSpinnerAdapter spinnerAdapter = new CustomSpinnerAdapter(mContext, spinnerItems);
        spinner.setAdapter(spinnerAdapter);
        spinner.setSelection(fcf.getmType()); // 처음에 한 번 호출
        spinner.setOnItemSelectedListener(new ItemSelectListener());

        mBtnAddCol.setOnClickListener(new ClickListener());
        mBtnAddRow.setOnClickListener(new ClickListener());
        mCopyView.setOnClickListener(new ClickListener());
        mDeleteView.setOnClickListener(new ClickListener());

        mSwitch.setChecked(fcf.isRequired_switch_bool());

        rowTexts = new ArrayList<>();
        colTexts = new ArrayList<>();
        rowTexts.addAll(fcf.getOptRowTexts());  // 깊은 복사
        colTexts.addAll(fcf.getOptColTexts());  // 깊은 복사
        int i = 0;
        for(Option o : rowTexts)
        {
            Option option = new Option(mContext, mType,mAddedRowContainer.getChildCount() + 1,o.getOption(),rowTexts); // need
            mAddedRowContainer.addView(option);
            rowTexts.set(i,option); // 안하면 수정된 텍스트들이 반영이 안됨
            i++;
        }
        i = 0;
        for(Option o : colTexts)
        {
            Option option = new Option(mContext, mType,o.getOption(),colTexts); // need
            mAddedColContainer.addView(option);
            colTexts.set(i,option); // 안하면 수정된 텍스트들이 반영이 안됨
            i++;
        }

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
//                jsonColArray.put(i,((Option)mAddedColContainer.getChildAt(i)).getOption());
                jsonColArray.put(((Option)mAddedColContainer.getChildAt(i)).getOption());
            }
            jsonObject.put("addedColOption",jsonColArray);

            JSONArray jsonRowArray=new JSONArray();
            for(int i=0;i<mAddedRowContainer.getChildCount();i++){
                //jsonRowArray.put(i,((Option)mAddedRowContainer.getChildAt(i)).getOption());
                jsonRowArray.put(((Option)mAddedRowContainer.getChildAt(i)).getOption());
            }
            jsonObject.put("addedRowOption",jsonRowArray);

        }catch (Exception e){
            e.printStackTrace();
        }
        return jsonObject;
    }

    @Override
    public void formComponentSetting(FormComponentVO vo) { // 무슨 용도지?
        mEditQuestion.setText(vo.getQuestion());
        mSwitch.setChecked(vo.isRequired_switch());

        for(int i=0;i<vo.getAddedRowOption().size();i++){

            Option option = new Option(mContext, mType, mAddedRowContainer.getChildCount() + 1,rowTexts);
            option.setOption(vo.getAddedRowOption().get(i)); // string 가져오기
            mAddedRowContainer.addView(option);
            rowTexts.add(option);
        }
        for(int i=0;i<vo.getAddedColOption().size();i++){
            Option option = new Option(mContext, mType,colTexts);
            option.setOption(vo.getAddedColOption().get(i));
            mAddedColContainer.addView(option);
            colTexts.add(option);
        }
    }

    public class ClickListener implements OnClickListener {
        @Override
        public void onClick(View view) {
            if (view == mDeleteView) {
                ViewGroup parentView = (ViewGroup) customView.getParent();
                parentView.removeView(customView);
            }
            else if(view == mCopyView){
                DragLinearLayout parentView = (DragLinearLayout) customView.getParent();
                int index = parentView.indexOfChild(customView);

                FormAbstract layout = new FormCopyFactory.Builder(mContext,mType)
                        .Question(mEditQuestion.getText().toString())
                        .OptRowTexts(rowTexts) //  option class
                        .OptColTexts(colTexts) //  option class
                        .RequiredSwitchBool(mSwitch.isChecked())
                        .build()
                        .createCopyForm();

                ViewGroup customlayout = (ViewGroup) layout.getChildAt(0);
                ImageView dragHandle = (ImageView)customlayout.getChildAt(0);
                parentView.addDragView(layout, dragHandle, index + 1);
            }
            else if (view == mBtnAddRow)
            {
                Option option = new Option(mContext, mType, mAddedRowContainer.getChildCount() + 1,rowTexts);
                mAddedRowContainer.addView(option);
                rowTexts.add(option);

            } else if (view == mBtnAddCol) {
                Option option = new Option(mContext, mType,colTexts);
                mAddedColContainer.addView(option);
                colTexts.add(option);
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
