package com.example.shang.etranslate.JavaBean;

import java.io.Serializable;
import java.util.List;

public class Question implements Serializable {

    private String question;

    private List<String> answer;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<String> getAnswer() {
        return answer;
    }

    public void setAnswer(List<String> answer) {
        this.answer = answer;
    }

    public Question(String question, List<String> answer) {
        this.question = question;
        this.answer = answer;
    }

    @Override
    public String toString() {
        return "Question{" +
                "question='" + question + '\'' +
                ", answer=" + answer +
                '}';
    }
}
