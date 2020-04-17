package com.example.graduationproject.form;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.graduationproject.R;
import com.jmedeisis.draglinearlayout.DragLinearLayout;

import org.json.JSONObject;

public class FormTypeVideo extends FormAbstract {
    private Context mContext;
    private int mType;
    private LayoutInflater mInflater;
    private View customView;

    private EditText mEditQuestion;
    private ImageButton mCopyView;
    private ImageButton mDeleteView;

    private EditText mEditYtbURL;

    public FormTypeVideo(Context context, int type) {
        super(context, type);
        mContext = context;
        this.mType = type;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        customView = mInflater.inflate(R.layout.form_type_video, this, true);

        mEditQuestion = findViewById(R.id.editQuestion);
        mCopyView = findViewById(R.id.copy_view);
        mDeleteView = findViewById(R.id.delete_view);
        mEditYtbURL = findViewById(R.id.editURL);


        mCopyView.setOnClickListener(new ClickListener());
        mDeleteView.setOnClickListener(new ClickListener());

    }
    public FormTypeVideo(FormCopyFactory fcf) {
        super(fcf.getmContext(), fcf.getmType());
        mContext = fcf.getmContext();
        this.mType = fcf.getmType();

        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        customView = mInflater.inflate(R.layout.form_type_video, this, true);

        mEditQuestion = findViewById(R.id.editQuestion);
        mCopyView = findViewById(R.id.copy_view);
        mDeleteView = findViewById(R.id.delete_view);
        mEditYtbURL = findViewById(R.id.editURL);

        mEditQuestion.setText(fcf.getEditQuestion_text());
        mEditYtbURL.setText(fcf.getEditYtbURL_text());

        mCopyView.setOnClickListener(new ClickListener());
        mDeleteView.setOnClickListener(new ClickListener());

    }

    @Override
    public JSONObject getJsonObject() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("type", mType);
            jsonObject.put("question", mEditQuestion.getText().toString());
            jsonObject.put("ytburl", mEditYtbURL.getText().toString().replace(" ","").replace("\n",""));
            // trim은 앞과뒤만 공백제거
            // 유튜브 url 엔터,스페이스 전부 제거! 해야함
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    @Override
    public void formComponentSetting(FormComponentVO vo) {
        mEditQuestion.setText(vo.getQuestion());
        mEditYtbURL.setText(vo.getYtburl());
    }

    public class ClickListener implements OnClickListener {
        @Override
        public void onClick(View view) {
            if (view == mDeleteView) {
                ViewGroup parentView = (ViewGroup) customView.getParent();
                parentView.removeView(customView);
            } else if (view == mCopyView) {
//                ViewGroup parentView = (ViewGroup) customView.getParent();
                DragLinearLayout parentView = (DragLinearLayout) customView.getParent();
                int index = parentView.indexOfChild(customView); // 위치 인덱스

                FormAbstract layout = new FormCopyFactory.Builder(mContext, mType)
                        .Question(mEditQuestion.getText().toString())
                        .YtbURL(mEditYtbURL.getText().toString())
                        .build()
                        .createCopyForm();

                ViewGroup customlayout = (ViewGroup) layout.getChildAt(0);
                ImageView dragHandle = (ImageView)customlayout.getChildAt(0);
                parentView.addDragView(layout, dragHandle, index + 1);

            }

        }
    }






}

