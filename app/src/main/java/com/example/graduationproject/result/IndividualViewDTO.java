package com.example.graduationproject.result;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class IndividualViewDTO implements Serializable {
    private int index;

    private String time;
    private HashMap<Integer,ArrayList<String>> result;
    public IndividualViewDTO(){
        result=new HashMap<>();
    }

    public void setResult(HashMap<Integer, ArrayList<String>> result) {
        this.result = result;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public HashMap<Integer, ArrayList<String>> getResult() {
        return result;
    }

    public void setResult(int key,ArrayList<String> value) {
        this.result.put(key,value);
    }
}
