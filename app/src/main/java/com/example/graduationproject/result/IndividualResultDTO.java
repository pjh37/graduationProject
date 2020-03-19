package com.example.graduationproject.result;

public class IndividualResultDTO {
    private String question;
    private String answer;

    private int type;


    public String getQuestion() {
        return question;
    }
    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }
    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public int getType() {return type;}
    public void setType(int type) {this.type = type;}
}
