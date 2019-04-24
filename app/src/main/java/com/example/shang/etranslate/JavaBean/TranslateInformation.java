package com.example.shang.etranslate.JavaBean;

/**
 * Created by Shang on 2017/4/2.
 */
public class TranslateInformation {

    private String content;

    private String result;

    public TranslateInformation() {

    }

    public TranslateInformation(String content, String result) {
        this.content = content;
        this.result = result;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "TranslateInformation{" +
                "content='" + content + '\'' +
                ", result='" + result + '\'' +
                '}';
    }
}
