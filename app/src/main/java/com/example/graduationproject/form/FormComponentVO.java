package com.example.graduationproject.form;

import java.io.File;
import java.util.ArrayList;

public class FormComponentVO {
    private int type;
    private String question;
    private String description;
    private boolean required_switch;
    private String media_file;
    private ArrayList<String> addedOption;
    private ArrayList<String> addedRowOption;
    private ArrayList<String> addedColOption;
    private int begin;
    private int end;

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

    public ArrayList<String> getAddedRowOption() {
        return addedRowOption;
    }

    public void setAddedRowOption(ArrayList<String> addedRowOption) {
        this.addedRowOption = addedRowOption;
    }

    public ArrayList<String> getAddedColOption() {
        return addedColOption;
    }

    public void setAddedColOption(ArrayList<String> addedColOption) {
        this.addedColOption = addedColOption;
    }

    public int getBegin() {
        return begin;
    }

    public void setBegin(int begin) {
        this.begin = begin;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }
}
