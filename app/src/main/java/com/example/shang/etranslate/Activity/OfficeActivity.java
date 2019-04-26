package com.example.shang.etranslate.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.shang.etranslate.Adapter.HaHaAdapter;
import com.example.shang.etranslate.JavaBean.OfficeQuestion;
import com.example.shang.etranslate.JavaBean.Question;
import com.example.shang.etranslate.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class OfficeActivity extends AppCompatActivity {

    private RecyclerView list;
    private List<Question> array;// 普通问答
    private List<OfficeQuestion> oArray;// 官方问答

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_office);
        initData();
        initOfficeData();
        list = findViewById(R.id.recycler_view);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(new HaHaAdapter(this, array, oArray));
    }

    private void initOfficeData() {
        oArray = new ArrayList<>();
        for (int j = 0; j < 5; j++) {
            OfficeQuestion question = new OfficeQuestion("这是问题" + j + ",请问你知道吗？", "我知道，这道题的答案就是" + j);
            oArray.add(question);
        }
    }

    private void initData() {
        array = new ArrayList<>();
        for (int i = 0; i < 10; i++){
            String q = "这是问题" + i + "请问你知道吗？";
            List<String> answerList = new ArrayList<>();
            Random random = new Random();
            for (int j = 0; j < random.nextInt(3) + 1; j++) {
                answerList.add("我知道，这道题的答案就是" + j);
            }
            Question question = new Question(q, answerList);
            array.add(question);
        }
    }
}
