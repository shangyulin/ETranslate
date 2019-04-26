package com.example.shang.etranslate.Fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shang.etranslate.Dao.CodeDao;
import com.example.shang.etranslate.JavaBean.TranslateInformation;
import com.example.shang.etranslate.R;
import com.example.shang.etranslate.Tools.BaseApplication;
import com.example.shang.etranslate.Tools.FileUtils;
import com.example.shang.etranslate.Tools.ToolUtils;
import com.example.shang.etranslate.Translate.HttpCallBack;
import com.example.shang.etranslate.Translate.RequestUtils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.io.File;
import java.util.Locale;


public class TranslateFragment extends Fragment {

    private View view;
    private Button read;// 朗读
    private EditText et_content;// 翻译内容
    private SharedPreferences sp;
    private SharedPreferences history;

    // 获取保存历史纪录的文件地址
    private File desFile = FileUtils.getHistoryDir();
    private File file = new File(desFile, "history.xml");// 历史文件
    private String db_path = FileUtils.getDatabasePath() + "/database.db";

    private ImageView qc;
    private ImageView translate;
    private TextView result_en;
    private TextView result_xpy;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        sp = BaseApplication.getContext().getSharedPreferences("config", Context.MODE_PRIVATE);
        history = BaseApplication.getContext().getSharedPreferences("config", Context.MODE_PRIVATE);
        view = View.inflate(BaseApplication.getContext(), R.layout.fragment_translate, null);
        initView();
        return view;
    }

    private void initView() {
        qc = view.findViewById(R.id.qingchu);// 清除
        translate = view.findViewById(R.id.do_translate);// 翻译
        et_content = view.findViewById(R.id.content);// 内容
//        result_en = view.findViewById(R.id.result_en);
//        result_xpy = view.findViewById(R.id.result_xpy);
        translate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String content = et_content.getText().toString();// 待翻译内容
                // 判断是否为空
                if (content != null && content.trim().length() > 0) {
                    // 获取拼音首字母
                    HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
                    char[] chars = content.toCharArray();
                    StringBuilder builder = new StringBuilder();
                    for (int i = 0; i < chars.length; i++) {
                        String s;
                        String newCode;
                        // 判断字符是否为汉字
                        System.out.println(chars[i]);
                        if (!ToolUtils.isChinese(chars[i])) {
                            builder.append(chars[i]);
                        } else {
                            try {
                                s = PinyinHelper.toHanyuPinyinStringArray(chars[i], format)[0];
                                System.out.println(s);
                                newCode = CodeDao.getPinyinFromWord(db_path, chars[i] + "", s.charAt(0) + "");
                                System.out.println(newCode);
                                builder.append(newCode);
                                builder.append(" ");
                            } catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
                                badHanyuPinyinOutputFormatCombination.printStackTrace();
                            }
                        }
                    }
                    // 翻译结果
                    String result = builder.toString();
                    result_xpy.setText(builder.toString());
                    // 保存查询记录
                    TranslateInformation information = new TranslateInformation(content, result);
                    if (file.exists()) {
                        ToolUtils.addData(file, information);// 追加数据
                    } else {
                        ToolUtils.writeXml(file, information);// 添加数据
                    }
//                    if (!history.contains(content)){
//                        history.edit().putString(content, result).commit();
//                    }

                    RequestUtils.translate(content, "zh", "en", new HttpCallBack() {
                        @Override
                        public void onSuccess(String result) {
                            result_en.setText(result);
                        }

                        @Override
                        public void onFailure(String exception) {

                        }
                    });
                } else {
                    ToolUtils.showToast("请输入文本！！！");
                }
            }
        });
        qc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_content.getText() != null) {
                    et_content.setText("");
                }
            }
        });
    }
}
