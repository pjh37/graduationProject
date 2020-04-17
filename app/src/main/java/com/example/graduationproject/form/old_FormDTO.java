package com.example.graduationproject.form;

import java.util.ArrayList;

public class old_FormDTO {

    //    private String title;
//    private String description;
    private ArrayList<FormComponentVO> formComponents; // not working

//    public String getTitle() {
//        return title;
//    }
//    public void setTitle(String title) {
//        this.title = title;
//    }
//
//    public String getDescription() {
//        return description;
//    }
//    public void setDescription(String description) {
//        this.description = description;
//    }

    public ArrayList<FormComponentVO> getFormComponents() {
        return formComponents;
    }
    public void setFormComponents(ArrayList<FormComponentVO> formComponents) {this.formComponents = formComponents;}
}
