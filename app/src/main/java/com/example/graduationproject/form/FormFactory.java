package com.example.graduationproject.form;

import android.content.Context;
import android.widget.LinearLayout;

public class FormFactory {
    private static FormFactory formFactory=null;
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
        if(mType==FormType.SHORTTEXT||mType==FormType.LONGTEXT||mType==FormType.DATE||mType==FormType.TIME){
            return new FormTypeText(mContext,mType);
        }else if(mType==FormType.MULTIPLECHOICE||mType==FormType.CHECKBOXES||mType==FormType.DROPDOWN){
            return new FormTypeOption(mContext,mType);
        }else if(mType==FormType.LINEARSCALE){
            return new FormTypeLinear(mContext,mType);
        }else if(mType==FormType.MULTIPLECHOICEGRID){
            return new FormTypeGrid(mContext,mType);
        }else if(mType==FormType.ADDSECTION){
            return new FormTypeSection(mContext,mType);
        }else if(mType==FormType.IMAGE){
            return new FormTypeImage(mContext,mType);
        }
        return null;
    }

}
