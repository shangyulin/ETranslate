package com.example.shang.etranslate.Fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;

import com.example.shang.etranslate.Activity.OcrActivity;
import com.example.shang.etranslate.Activity.TestActivity;
import com.example.shang.etranslate.Activity.WordActivity;
import com.example.shang.etranslate.Constant.Constant;
import com.example.shang.etranslate.R;
import com.example.shang.etranslate.Service.AutoQueryService;
import com.example.shang.etranslate.Service.WindowBottomService;
import com.example.shang.etranslate.Service.WindowToolService;
import com.example.shang.etranslate.Tools.BaseApplication;

import java.io.File;


public class InformationFragment extends Fragment {


    private View view;
    private RelativeLayout account;
    private CheckBox window;
    private SharedPreferences sp;
    private RelativeLayout delete;
    private RelativeLayout wordbook;
    private CheckBox query;
    private RelativeLayout test;
    private MyReceiver myReceiver;
    private RelativeLayout ocr;

    String[] str = new String[]{"从相册中选取", "拍照"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = View.inflate(BaseApplication.getContext(), R.layout.fragment_information, null);

        initView();

        initData();

        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        // 动态注册广播接收者
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.android.inputmethod.pinyin.Service.changeCheckBox.InformationFragment");
        myReceiver = new MyReceiver();
        BaseApplication.getContext().registerReceiver(myReceiver, filter);
    }

    public void initData() {
        window.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    if (Build.VERSION.SDK_INT >= 23) {
                        if (!Settings.canDrawOverlays(getActivity())) {
                            //启动Activity让用户授权
                            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getActivity().getPackageName()));
                            startActivityForResult(intent, 10);
                        }
                    }
                    /** 开启服务 */
                    BaseApplication.getContext().startService(new Intent(getActivity(), WindowToolService.class));
                    sp.edit().putBoolean("window_tool", true).commit();
                }else{
                    /** 关闭服务 */
                    BaseApplication.getContext().stopService(new Intent(getActivity(), WindowToolService.class));
                    sp.edit().putBoolean("window_tool", false).commit();
                }
            }
        });
        query.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    /** 开启服务 */
                    BaseApplication.getContext().startService(new Intent(getActivity(), AutoQueryService.class));
                    sp.edit().putBoolean("auto_query", true).commit();
                }else{
                    /** 关闭服务 */
                    BaseApplication.getContext().stopService(new Intent(getActivity(), AutoQueryService.class));
                    sp.edit().putBoolean("auto_query", false).commit();
                }
                // 发送广播,通知底部悬浮窗实时更新服务状态
                Intent intent = new Intent("com.android.inputmethod.pinyin.Service.changeCheckBox.BottomService");
                BaseApplication.getContext().sendBroadcast(intent);
                System.out.println("发送广播");
            }
        });
    }

    private void initView() {
        sp = BaseApplication.getContext().getSharedPreferences("config", Context.MODE_PRIVATE);
        account = (RelativeLayout) view.findViewById(R.id.account);
        delete = (RelativeLayout)view.findViewById(R.id.rl_delete);
        wordbook = (RelativeLayout) view.findViewById(R.id.rl_wordbook);
        test = (RelativeLayout) view.findViewById(R.id.rl_test);
        ocr = (RelativeLayout) view.findViewById(R.id.rl_ocr);

        window = (CheckBox) view.findViewById(R.id.cb_window);
        query = (CheckBox) view.findViewById(R.id.auto_query);

        /**
         * 初始化悬浮窗
         */
        if(sp.getBoolean("window_tool", false)){
            window.setChecked(true);
            BaseApplication.getContext().startService(new Intent(getActivity(), WindowToolService.class));
        }else{
            window.setChecked(false);
            BaseApplication.getContext().stopService(new Intent(getActivity(), WindowToolService.class));
        }

        /**
         * 初始化自动查询
         */
        if(sp.getBoolean("auto_query", false)){
            query.setChecked(true);
            BaseApplication.getContext().startService(new Intent(getActivity(), AutoQueryService.class));
        }else{
            query.setChecked(false);
            BaseApplication.getContext().stopService(new Intent(getActivity(), AutoQueryService.class));
        }
        // 删除历史纪录文件
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = Constant.historyFile;
                if(file.exists()){
                    file.delete();
                }
            }
        });
        // 进入单词本
        wordbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WordActivity.class);
                startActivity(intent);
            }
        });
        // 自主查询测试部分
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), TestActivity.class));
            }
        });
        // 图像识别ocr功能
        ocr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), OcrActivity.class));
            }
        });
    }


    class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            boolean b = sp.getBoolean("auto_query", false);
            if(b){
                query.setChecked(true);
            }else{
                query.setChecked(false);
            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        BaseApplication.getContext().unregisterReceiver(myReceiver);
    }
}
