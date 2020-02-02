package com.example.graduationproject.form;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.graduationproject.R;

import org.json.JSONObject;

public class FormTypeSubText extends FormAbstract {
    private Context mContext;
    private int mType;

    private LayoutInflater mInflater;
    private View customView;

    private EditText mEditTitle;
    private ImageButton mCopyView;
    private ImageButton mDeleteView;
    private EditText mEditDescription;


    public FormTypeSubText(Context context, int type) {
        super(context, type);
        mContext = context;
        this.mType = type;

        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        customView = mInflater.inflate(R.layout.form_type_subtext, this, true);

        mEditTitle = (EditText) findViewById(R.id.editTitle);
        mCopyView = (ImageButton) findViewById(R.id.copy_view);
        mDeleteView = (ImageButton) findViewById(R.id.delete_view);
        mEditDescription = (EditText) findViewById(R.id.editDescription);


        mCopyView.setOnClickListener(new ClickListener());
        mDeleteView.setOnClickListener(new ClickListener());
    }
    public FormTypeSubText(FormCopyFactory fcf) {
        super(fcf.getmContext(), fcf.getmType());
        mContext = fcf.getmContext();
        this.mType = fcf.getmType();

        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        customView = mInflater.inflate(R.layout.form_type_subtext, this, true);

        mEditTitle = (EditText) findViewById(R.id.editTitle);
        mCopyView = (ImageButton) findViewById(R.id.copy_view);
        mDeleteView = (ImageButton) findViewById(R.id.delete_view);
        mEditDescription = (EditText) findViewById(R.id.editDescription);

        mEditTitle.setText(fcf.getEditQuestion_text());
        mEditDescription.setText(fcf.getEditExplanation_text());

        mCopyView.setOnClickListener(new ClickListener());
        mDeleteView.setOnClickListener(new ClickListener());
    }



    @Override
    public JSONObject getJsonObject() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("type", mType);
            jsonObject.put("title", mEditTitle.getText().toString());
            jsonObject.put("description", mEditDescription.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    @Override
    public void formComponentSetting(FormComponentVO vo) {
        mEditTitle.setText(vo.getQuestion());
        mEditDescription.setText(vo.getDescription());
    }

    public class ClickListener implements OnClickListener {
        @Override
        public void onClick(View view) {
            if (view == mDeleteView) {
                ViewGroup parentView = (ViewGroup) customView.getParent();
                parentView.removeView(customView);
            } else if (view == mCopyView) {
                ViewGroup parentView = (ViewGroup) customView.getParent();
                int index = parentView.indexOfChild(customView); // 위치 인덱스

                FormAbstract layout = new FormCopyFactory.Builder(mContext, mType)
                        .Question(mEditTitle.getText().toString())
                        .Explanation(mEditDescription.getText().toString())
                        .build()
                        .createCopyForm();

                parentView.addView(layout, index + 1);
            }
        }
    }


}
