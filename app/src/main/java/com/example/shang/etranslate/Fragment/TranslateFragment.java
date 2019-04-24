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


public class TranslateFragment extends Fragment implements TextToSpeech.OnInitListener {

    private View view;
    private Button read;// 朗读
    private EditText et_content;// 翻译内容
    private SharedPreferences sp;
    private ToolUtils toolUtils;

    // 获取保存历史纪录的文件地址
    private File desFile = FileUtils.getHistoryDir();
    private File file = new File(desFile, "history.xml");// 历史文件

    private String db_path = FileUtils.getDatabasePath() + "/database.db";

    private ImageView qc;
    private ImageView translate;
    private TextToSpeech textToSpeech;

    private InputMethodManager manager;
    private TextView et_result_en;
    private Spinner spinner_from;
    private Spinner spinner_to;

    private String from = "auto";
    private String to = "auto";

    String[] language = {"auto", "zh", "en", "yue", "wyw", "jp", "kor", "fra", "spa", "th", "ara", "ru", "pt", "de", "it", "el", "nl", "pl", "bul", "est", "dan", "fin", "cs", "rom"
            , "slo", "swe", "hu", "cht"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        sp = BaseApplication.getContext().getSharedPreferences("config", Context.MODE_PRIVATE);
        view = View.inflate(BaseApplication.getContext(), R.layout.fragment_translate, null);
        initView();
        return view;
    }

    private void initView() {
        manager = (InputMethodManager) BaseApplication.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        textToSpeech = new TextToSpeech(getActivity(), this);

        spinner_from = (Spinner) view.findViewById(R.id.spinner_from);
        spinner_to = (Spinner) view.findViewById(R.id.spinner_to);
        qc = (ImageView) view.findViewById(R.id.qingchu);// 清除
        translate = (ImageView) view.findViewById(R.id.do_translate);// 翻译
        et_content = (EditText) view.findViewById(R.id.content);// 内容
        read = (Button) view.findViewById(R.id.read);


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
                        String s = "";
                        String newCode = "";
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
                    // 保存查询记录
                    TranslateInformation information = new TranslateInformation(content, result);
                    if (file.exists()) {
                        ToolUtils.addData(file, information);// 追加数据
                    } else {
                        ToolUtils.writeXml(file, information);// 添加数据
                    }
                    // 显示结果
                    // 根据输入的中文查询英文或其他
                    RequestUtils requestUtils = new RequestUtils();
                    try {
                        requestUtils.translate(content, from, to, new HttpCallBack() {
                            @Override
                            public void onSuccess(String result) {
                            }

                            @Override
                            public void onFailure(String exception) {
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    ToolUtils.showToast("请输入文本！！！");
                }
                // 判断当前软键盘是否隐藏，如果不，隐藏掉她
//                if (getActivity().getWindow().getAttributes().softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE) {
//                    System.out.println("软键盘开启");
//                    InputMethodManager manager = (InputMethodManager) BaseApplication.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//                    manager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
//                    getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
//                }

                if (manager.isActive(et_content)) {
                    System.out.println("软键盘开启");
                    getView().requestFocus();
                    manager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
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
        read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = et_content.getText().toString();
                if (content != null && content.trim().length() > 0) {
                    if (textToSpeech != null && !textToSpeech.isSpeaking()) {
                        textToSpeech.setPitch(-3.0f);// 设置音调，值越大声音越尖（女生），值越小则变成男声,1.0是常规
                        textToSpeech.setSpeechRate(0.8f);
                        textToSpeech.speak(content, TextToSpeech.QUEUE_FLUSH, null);
                    }
                }
            }
        });
        /**
         * 监听Spinner切换
         */
        spinner_from.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                from=language[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_to.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                to=language[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    public boolean isChinese(char ch) {
        if (ch < 0) {
            Toast.makeText(BaseApplication.getContext(), "is chinese", Toast.LENGTH_LONG).show();
            return true;
        } else {
            Toast.makeText(BaseApplication.getContext(), "not is chinese", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = textToSpeech.setLanguage(Locale.CHINA);
            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(BaseApplication.getContext(), "数据丢失或不支持", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
