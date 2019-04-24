package com.example.shang.etranslate.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.example.shang.etranslate.Dao.CodeDao;
import com.example.shang.etranslate.R;
import com.example.shang.etranslate.Tools.BaseApplication;
import com.example.shang.etranslate.Tools.FileUtils;
import com.example.shang.etranslate.Tools.ToolUtils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class WindowTranslateActivity extends Activity {

    private EditText et_result;
    private EditText et_content;
    private Button clear;
    private Button translate;
    private FrameLayout fl;
    private String clipContent;
    private SharedPreferences sp;

    private String db_path = FileUtils.getDatabasePath() + "/database.db";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.ap2, R.anim.ap1);
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_window_translate);
        Intent intent = getIntent();
        clipContent = intent.getStringExtra("clipContent");
        initView();
    }

    private void initView() {
        sp = getSharedPreferences("config", MODE_PRIVATE);
        fl = (FrameLayout) findViewById(R.id.fl);
        et_content = (EditText) findViewById(R.id.content);
        et_result = (EditText) findViewById(R.id.result);
        clear = (Button) findViewById(R.id.clear);
        translate = (Button) findViewById(R.id.translate);

        // 查看有没有开启自动查询功能
        if(sp.getBoolean("auto_query", false)){
            et_content.setText(clipContent);
            translate(clipContent);
        }

        translate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = et_content.getText().toString();
                // 根据内容进行翻译
                translate(content);
            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_content.getText() != null){
                    et_content.setText("");
                    et_result.setVisibility(View.GONE);
                }
            }
        });
        fl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.ap2, R.anim.ap1);
            }
        });
    }


    public void translate(String content){
        if (content != null && content.trim().length() > 0) {
            // 显示结果框
            et_result.setVisibility(View.VISIBLE);

            /**
             * 计算拼音首字母
             */
            HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
            char[] chars = content.toCharArray();
            StringBuilder builder = new StringBuilder();
            for(int i = 0; i < chars.length; i++){
                String s = "";
                String newCode = "";
                // 判断字符是否为汉字
                if(!ToolUtils.isChinese(chars[i])){
                    builder.append(chars[i]);
                }else{
                    try {
                        s = PinyinHelper.toHanyuPinyinStringArray(chars[i],format)[0];
                        newCode = CodeDao.getPinyinFromWord(db_path, chars[i]+"", s.charAt(0)+"");
                        builder.append(newCode);
                        builder.append(" ");
                    } catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
                        badHanyuPinyinOutputFormatCombination.printStackTrace();
                    }
                }
            }
            // 翻译结果
            String result = builder.toString();

            /**
             * 将翻译结果显示在屏幕上
             */
            et_result.setText(result);
            InputMethodManager manager = (InputMethodManager) BaseApplication.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        } else {
            ToolUtils.showToast("请输入文本！！！");
        }
    }

    public boolean isChinese(char ch){
        if(ch<0)    return true;
        return false;
    }
}
