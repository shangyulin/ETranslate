package com.example.shang.etranslate.Activity;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.shang.etranslate.JavaBean.Question;
import com.example.shang.etranslate.R;

import java.io.Serializable;

public class QuestionDetailActivity extends AppCompatActivity {

    private LinearLayout root;
    private TextView question;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_detail);
        root = findViewById(R.id.root);
        question = findViewById(R.id.question);

        Intent intent = getIntent();
        Question content = (Question) intent.getSerializableExtra("object");

        question.setText(content.getQuestion());

        for (int i = 0; i < content.getAnswer().size(); i++) {
            LinearLayout linearLayout = new LinearLayout(this);
            linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout.setPadding(5, 5, 5, 5);

            TextView textView = new TextView(this);
            textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            textView.setMaxLines(1);
            textView.setEllipsize(TextUtils.TruncateAt.END);
            textView.setText("答：");

            TextView textView2 = new TextView(this);
            textView2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            textView2.setMaxLines(1);
            textView2.setEllipsize(TextUtils.TruncateAt.END);
            textView2.setText(content.getAnswer().get(i));
            linearLayout.addView(textView);
            linearLayout.addView(textView2);
            root.addView(linearLayout);
        }
    }
}
