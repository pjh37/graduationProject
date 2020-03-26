package com.example.graduationproject.form;

import android.content.Context;
import android.net.Uri;

import java.util.ArrayList;

public class FormCopyFactory {

    private Context mContext;
    private int mType;

    private String editQuestion_text;
//    private int spinner_position; // mType 로 커버가능

    private String editExplanation_text;

    private int begin_spinner_position;
    private int end_spinner_position;

    private String labelLeft_text;
    private String labelRight_text;

    private boolean required_switch_bool;
    private boolean etc_switch_bool;

    //    private ArrayList<String> rowTexts;
    private ArrayList<Option> OptRowTexts;
    private ArrayList<Option> OptColTexts;

    private Uri fileUri;
    private int formComponent_id; // for image

    public Context getmContext() {
        return mContext;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    public int getmType() {
        return mType;
    }

    public String getEditQuestion_text() {
        return editQuestion_text;
    }

//    public int getSpinner_position() {return spinner_position;}

    public String getEditExplanation_text() {return editExplanation_text;}

    public int getBegin_spinner_position() {
        return begin_spinner_position;
    }

    public int getEnd_spinner_position() {
        return end_spinner_position;
    }

    public String getLabelLeft_text() {
        return labelLeft_text;
    }

    public String getLabelRight_text() {
        return labelRight_text;
    }

    public boolean isRequired_switch_bool() {
        return required_switch_bool;
    }

    public boolean isEtc_switch_bool() {
        return etc_switch_bool;
    }

    public ArrayList<Option> getOptRowTexts() {
        return OptRowTexts;
    }

    public ArrayList<Option> getOptColTexts() {
        return OptColTexts;
    } // 2개 바꿔야되나

    public Uri getFileUri() {return fileUri;}
    public int getFormComponent_id() {return formComponent_id;}

    private FormCopyFactory(Builder builder) {
        this.mContext = builder.mContext;
        this.mType = builder.mType;
        this.editQuestion_text = builder.editQuestion_text;
//        this.spinner_position = builder.spinner_position;
        this.editExplanation_text = builder.editExplanation_text;

        this.begin_spinner_position = builder.begin_spinner_position;
        this.end_spinner_position = builder.end_spinner_position;
        this.labelLeft_text = builder.labelLeft_text;
        this.labelRight_text = builder.labelRight_text;
        this.required_switch_bool = builder.required_switch_bool;
        this.etc_switch_bool = builder.etc_switch_bool;

        this.OptRowTexts = builder.OptRowTexts;
        this.OptColTexts = builder.OptColTexts;

        this.fileUri = builder.fileUri;
        this.formComponent_id = builder.formComponent_id;
    }

    public FormAbstract createCopyForm() {
        switch (mType) {
            case FormType.SHORTTEXT:
            case FormType.LONGTEXT:
            case FormType.DATE:
            case FormType.TIME:
                return new FormTypeText(this);

            case FormType.RADIOCHOICE:
            case FormType.CHECKBOXES:
            case FormType.DROPDOWN:
                return new FormTypeOption(this);

            case FormType.LINEARSCALE:
                return new FormTypeLinear(this);


            case FormType.RADIOCHOICEGRID:
            case FormType.CHECKBOXGRID:
                return new FormTypeGrid(this);

            case FormType.ADDSECTION:
                return new FormTypeSection(this);

            case FormType.SUBTEXT:
                return new FormTypeSubText(this);

            case FormType.IMAGE:
                return new FormTypeImage(this);
            default:
                return null;

        }
    }

    public static class Builder {
        Context mContext;
        int mType;

        String editQuestion_text;
//        int spinner_position;

        String editExplanation_text;

        int begin_spinner_position;
        int end_spinner_position;

        String labelLeft_text;
        String labelRight_text;

        boolean required_switch_bool;
        boolean etc_switch_bool;

        ArrayList<Option> OptRowTexts;
        ArrayList<Option> OptColTexts;

        Uri fileUri;
        int formComponent_id;

        public Builder(Context context, int type) {
            this.mContext = context;
            this.mType = type;
        }

        public Builder Question(String editQuestion_text) {
            this.editQuestion_text = editQuestion_text;
            return this;
        }

        //        public Builder SpinnerPosition(int spinner_position) {
//            this.spinner_position = spinner_position;
//            return this;
//        }

        public Builder Explanation(String editExplanation_text) {
            this.editExplanation_text = editExplanation_text;
            return this;
        }

        public Builder LinearSpinnerPosition(int begin_spinner_position, int end_spinner_position) {
            this.begin_spinner_position = begin_spinner_position;
            this.end_spinner_position = end_spinner_position;
            return this;
        }

        public Builder LabelText(String labelLeft_text, String labelRight_text) {
            this.labelLeft_text = labelLeft_text;
            this.labelRight_text = labelRight_text;
            return this;
        }

        public Builder RequiredSwitchBool(boolean required_switch_bool) {
            this.required_switch_bool = required_switch_bool;
            return this;
        }

        public Builder EtcSwitchBool(boolean etc_switch_bool) {
            this.etc_switch_bool = etc_switch_bool;
            return this;
        }

        public Builder OptRowTexts(ArrayList<Option> OptRowTexts) {
            this.OptRowTexts = OptRowTexts;  // shallow copy

//            this.OptRowTexts = new ArrayList<>(); // deep copy ?
//            this.OptRowTexts.addAll(OptRowTexts); // 사용쪽에서 depp copy 하는걸로 알고 여기서는 안함
            // 2중 딥카피는 별로...

            return this;
        }

        public Builder OptColTexts(ArrayList<Option> OptColTexts) {
            this.OptColTexts = OptColTexts;  // shallow copy
            return this;
        }

        public Builder FileUri(Uri fileUri) { // 사진 URI
            this.fileUri = fileUri;
            return this;
        }
        public Builder FormComponentId(int formComponent_id) { // 사진 URI
            this.formComponent_id = formComponent_id;
            return this;
        }

        public FormCopyFactory build() {
            return new FormCopyFactory(this);
        }


    }


}
