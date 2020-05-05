package com.example.graduationproject.form;

import java.io.File;
import java.util.ArrayList;

public class FormComponentVO {
    // json 과 key name이 같아야 하는구나 ! ,, gson에서
    private int type;
    private String question;
    private String description; // section , subtext 활용
    private boolean required_switch;
    private ArrayList<String> addedOption; // option 용
    private ArrayList<String> addedRowOption; // grid 용
    private ArrayList<String> addedColOption; // grid 용
    private String media_file; // uri
    private boolean OptionEtc_switch;

    private int beginIndex; // 직선단계용
    private int endIndex; // 직선단계용
    private String beingLabel;
    private String endLabel;

    private String ytburl;
    private boolean posted;

    public String getQuestion() {return question;}
    public void setQuestion(String question) {this.question = question;}

    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}

    public String getMedia_file() {
        return media_file;
    }
    public void setMedia_file(String media_file) {
        this.media_file = media_file;
    }

    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }

    public boolean isRequired_switch() {
        return required_switch;
    }
    public void setRequired_switch(boolean required_switch) {this.required_switch = required_switch;}

    public ArrayList<String> getAddedOption() {
        return addedOption;
    }
    public void setAddedOption(ArrayList<String> addedOption) {
        this.addedOption = addedOption;
    }

    public ArrayList<String> getAddedRowOption() {
        return addedRowOption;
    }
    public void setAddedRowOption(ArrayList<String> addedRowOption) {this.addedRowOption = addedRowOption;}

    public ArrayList<String> getAddedColOption() {
        return addedColOption;
    }
    public void setAddedColOption(ArrayList<String> addedColOption) {this.addedColOption = addedColOption;}



    public boolean isOptionEtc_switch() {return OptionEtc_switch;}
    public void setOptionEtc_switch(boolean optionEtc_switch) {OptionEtc_switch = optionEtc_switch;}

    public int getBeginIndex() {return beginIndex;}
    public void setBeginIndex(int beginIndex) {this.beginIndex = beginIndex;}
    public int getEndIndex() {return endIndex;}
    public void setEndIndex(int endIndex) {this.endIndex = endIndex;}
    public String getBeingLabel() {return beingLabel;}
    public void setBeingLabel(String beingLabel) {this.beingLabel = beingLabel;}
    public String getEndLabel() {return endLabel;}
    public void setEndLabel(String endLabel) {this.endLabel = endLabel;}

    public String getYtburl() {return ytburl;}
    public void setYtburl(String ytburl) {this.ytburl = ytburl;}
    public boolean isPosted() {return posted;}
    public void setPosted(boolean posted) {this.posted = posted;}
}
