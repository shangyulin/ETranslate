package com.example.shang.etranslate.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shang.etranslate.R;

import java.util.ArrayList;
import java.util.List;

public class TestActivity extends AppCompatActivity {

    private TextView textView;
    private List<Integer> menuIds = new ArrayList<>();
    private long startTime;

    private Handler handler  = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            int selectionStart = textView.getSelectionStart();
            int selectionEnd = textView.getSelectionEnd();
            String substring = textView.getText().toString().substring(selectionStart, selectionEnd + 1);
            Toast.makeText(TestActivity.this, substring, Toast.LENGTH_SHORT).show();
            if(substring!=null && substring.trim().length()>0){
                Intent intent = new Intent(TestActivity.this, WindowTranslateActivity.class);
                intent.putExtra("clipContent", substring);
                startActivity(intent);
            }
        }
    };
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        sp = getSharedPreferences("config", MODE_PRIVATE);
        textView = (TextView) findViewById(R.id.tv_test);
        textView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_UP:
                        boolean result = sp.getBoolean("auto_query", false);
                        // 有文本被选中且自动查询服务已开启
                        if(textView.hasSelection()&&result){
                            handler.sendEmptyMessageDelayed(0,3000);
                        }
                }
                return false;
            }
        });
    }
}
