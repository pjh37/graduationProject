package com.example.graduationproject.form;

import android.content.Context;
import android.widget.LinearLayout;

public class FormFactory {
    private int mType;
    private Context mContext;

    private FormFactory(Context context,int type){
        this.mContext=context;
        this.mType=type;
    }
    public static FormFactory getInstance(Context context,int type){
        return new FormFactory(context,type);
    }

    public FormAbstract createForm(){
        // date . time 분리?
        if(mType==FormType.SHORTTEXT||mType==FormType.LONGTEXT||mType==FormType.DATE||mType==FormType.TIME){
            return new FormTypeText(mContext,mType);
        }
        else if(mType==FormType.RADIOCHOICE||mType==FormType.CHECKBOXES||mType==FormType.DROPDOWN){
            return new FormTypeOption(mContext,mType);
        }
        else if(mType==FormType.LINEARSCALE){
            return new FormTypeLinear(mContext,mType);
        }
        else if(mType==FormType.RADIOCHOICEGRID || mType==FormType.CHECKBOXGRID){
            return new FormTypeGrid(mContext,mType);
        }
        else if(mType==FormType.ADDSECTION){
            return new FormTypeSection(mContext,mType);
        }
        else if(mType==FormType.SUBTEXT){
            return new FormTypeSubText(mContext,mType);
        }
        else if(mType==FormType.IMAGE){
            return new FormTypeImage(mContext,mType);
        }
        else if(mType==FormType.VIDEO){ // 변경해야되 fab 로
            return new FormTypeVideo(mContext,mType);
        }
        return null;
    }

}
