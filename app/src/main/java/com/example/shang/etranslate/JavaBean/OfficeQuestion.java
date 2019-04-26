package com.example.shang.etranslate.JavaBean;

import java.util.List;

public class OfficeQuestion {

    private String question;

    private String answer;

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

    public OfficeQuestion(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }
}
