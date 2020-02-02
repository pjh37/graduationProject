package com.example.graduationproject.form;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.graduationproject.R;

import java.util.ArrayList;

public class Option extends LinearLayout {
    private static final int RADIOCHOICE = 2;
    private static final int CHECKBOXES = 3;
    private static final int DROPDOWN = 4;
    private static final int RADIOCHOICEGRID = 6;
    private static final int CHECKBOXGRID = 7;

    private EditText editOption;
    private View customView;

    private int RowOrder;
    ArrayList<Option> OptRowTexts; // row,col 통합 index 조절 문제 때문에,,

    public Option(Context context, int type,final ArrayList<Option> OptRowTexts) {
        super(context);
//        this.mContext = context;
        this.OptRowTexts = OptRowTexts; // 얕은 복사 !

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        customView = inflater.inflate(R.layout.form_option_col, this, true);

        imgSelect(type);

        editOption = (EditText) findViewById(R.id.editOption);

        ImageButton imageButton = (ImageButton) findViewById(R.id.delete_option);
        imageButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewGroup parentView = (ViewGroup) customView.getParent();
                int index = parentView.indexOfChild(customView); // 위치 인덱스
                parentView.removeView(customView);
                OptRowTexts.remove(index);
            }
        });

    }
    public Option(Context context, int type, int RowOrder,final ArrayList<Option> OptRowTexts) { // type 매개변수 제거하면 오버라이딩 걸림
        super(context);
//        this.mContext = context;
        this.RowOrder = RowOrder;
        this.OptRowTexts = OptRowTexts; // 얕은 복사 !

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        customView = inflater.inflate(R.layout.form_option_row, this, true);

        setRowOrder();

        editOption = (EditText) findViewById(R.id.editOption);

        ImageButton imageButton = (ImageButton) findViewById(R.id.delete_option);
        imageButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewGroup parentView = (ViewGroup) customView.getParent(); // row container
                int index = parentView.indexOfChild(customView); // 위치 인덱스
                parentView.removeView(customView);
                OptRowTexts.remove(index);

                // 인덱스 리프레쉬
                for (int i = 0; i < parentView.getChildCount(); i++) {
                    LinearLayout ll = (LinearLayout) parentView.getChildAt(i); // option class..(패키지)
                    // 허허 이럴수가.. 이런식으로 추가된다니..
                    LinearLayout ll2 = (LinearLayout) ll.getChildAt(0);

//                    TextView tmpTv = (TextView)ll2.getChildAt(0);
//                    tmpTv.setText(""+(i+1));
// id로도 가능
                    TextView tmpTv2 = ll2.findViewById(R.id.order);
                    tmpTv2.setText("" + (i + 1));

//                    Log.d("mawang", "ll = " + ll);
//                    Log.d("mawang", "ll2 = " + ll2);
//                    Log.d("mawang", "ll2.getChildAt(0) = " + ll2.getChildAt(0));
                }


            }
        });

    }
    public Option(Context context, int type, boolean etc) { // boolean 오버로딩 구분
        super(context);
//        this.mContext = context;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        customView = inflater.inflate(R.layout.form_option_etc, this, true);

        imgSelect(type);
        editOption = (EditText) findViewById(R.id.editOption);

    }
    public Option(Context context, int type, String text,final ArrayList<Option> OptRowTexts) { // 지저분 하군
        super(context);
//        this.mContext = context;
        this.OptRowTexts = OptRowTexts; // 얕은 복사 !

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        customView = inflater.inflate(R.layout.form_option_col, this, true);

        imgSelect(type);

        editOption = (EditText) findViewById(R.id.editOption);
        editOption.setText(text);

        ImageButton imageButton = (ImageButton) findViewById(R.id.delete_option);
        imageButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewGroup parentView = (ViewGroup) customView.getParent();
                int index = parentView.indexOfChild(customView); // 위치 인덱스
                parentView.removeView(customView);
                OptRowTexts.remove(index);
            }
        });

    }
    public Option(Context context, int type, int RowOrder,String text,final ArrayList<Option> OptRowTexts) { // type 매개변수 제거하면 오버라이딩 걸림
        super(context);
//        this.mContext = context;
        this.RowOrder = RowOrder;
        this.OptRowTexts = OptRowTexts; // 얕은 복사 !

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        customView = inflater.inflate(R.layout.form_option_row, this, true);

        setRowOrder();

        editOption = (EditText) findViewById(R.id.editOption);
        editOption.setText(text);

        ImageButton imageButton = (ImageButton) findViewById(R.id.delete_option);
        imageButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewGroup parentView = (ViewGroup) customView.getParent(); // row container
                int index = parentView.indexOfChild(customView); // 위치 인덱스
                parentView.removeView(customView);
                OptRowTexts.remove(index);

//                Log.d("mawang", "parentView = " + parentView);
                // 인덱스 리프레쉬
                for (int i = 0; i < parentView.getChildCount(); i++) {
                    LinearLayout ll = (LinearLayout) parentView.getChildAt(i); // option class..(패키지)
                    // 허허 이럴수가.. 이런식으로 추가된다니..
                    LinearLayout ll2 = (LinearLayout) ll.getChildAt(0);

                    TextView tmpTv = (TextView)ll2.getChildAt(0);
                    tmpTv.setText(""+(i+1));
// id로도 가능
//                    TextView tmpTv2 = ll2.findViewById(R.id.order);
//                    tmpTv2.setText("" + (i + 1));

//                    Log.d("mawang", "ll = " + ll);
//                    Log.d("mawang", "ll2 = " + ll2);
//                    Log.d("mawang", "ll2.getChildAt(0) = " + ll2.getChildAt(0));
                }
            }
        });

    }

    public String getOption(){
        return editOption.getText().toString();
    }
    public void setOption(String str){
        editOption.setText(str);
    }

    public void imgSelect(int type) {
        ImageView img_icon = findViewById(R.id.img_icon);
        switch (type) {
            case RADIOCHOICE:
            case RADIOCHOICEGRID:
                img_icon.setImageResource(R.drawable.multiplechoice);
                break;
            case CHECKBOXES:
            case CHECKBOXGRID:
                img_icon.setImageResource(R.drawable.checkbox);
                break;
            case DROPDOWN:
                img_icon.setImageResource(R.drawable.dropdown);
                break;
        }
    }

    public void setRowOrder() {
        TextView tv_order = findViewById(R.id.order);
        tv_order.setText("" + RowOrder);
    }

}
