package com.example.graduationproject.form;

import java.util.ArrayList;

public class FormComponentVO {
    private int type;
    private String question;
    private String description;
    private boolean required_switch;
    private ArrayList<String> addedOption;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public boolean isRequired_switch() {
        return required_switch;
    }

    public void setRequired_switch(boolean required_switch) {
        this.required_switch = required_switch;
    }

    public ArrayList<String> getAddedOption() {
        return addedOption;
    }

    public void setAddedOption(ArrayList<String> addedOption) {
        this.addedOption = addedOption;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
