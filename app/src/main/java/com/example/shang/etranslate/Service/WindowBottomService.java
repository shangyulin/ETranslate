package com.example.shang.etranslate.Service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.shang.etranslate.R;
import com.example.shang.etranslate.Tools.BaseApplication;

public class WindowBottomService extends Service implements View.OnClickListener {

    private WindowManager wManager;// 窗口管理者
    private WindowManager.LayoutParams params;// 窗口的属性
    private boolean flag = true;
    private View view;
    private SharedPreferences sp;
    private ImageView iv_autoquery;
    private TextView tv_autoquery;
    private ImageView iv_add;
    private TextView tv_add;
    private ImageView iv_share;
    private TextView tv_shared;
    private LinearLayout autoquery;
    private LinearLayout add;
    private LinearLayout shared;
    private LinearLayout exit;
    private boolean result;

    private MyReceiver receiver;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        sp = getSharedPreferences("config", MODE_PRIVATE);
        wManager = (WindowManager) this.getSystemService(
                Context.WINDOW_SERVICE);
        params = new WindowManager.LayoutParams();
        // 获取屏幕宽高
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        params.format = PixelFormat.TRANSLUCENT;
        if (Build.VERSION.SDK_INT >= 26) {
            params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }
        params.gravity = Gravity.BOTTOM;
        params.setTitle("Toast");
        //mParams.alpha = 0.1f;//窗口的透明度
        view = View.inflate(this, R.layout.window_bottom, null);

        autoquery = view.findViewById(R.id.auto_query);
        iv_autoquery = view.findViewById(R.id.iv_autoquery);
        tv_autoquery = view.findViewById(R.id.tv_autoquery);

        add = view.findViewById(R.id.add);
        iv_add = view.findViewById(R.id.iv_add);
        tv_add = view.findViewById(R.id.tv_add);

        shared = view.findViewById(R.id.shared);
        iv_share = view.findViewById(R.id.iv_shared);
        tv_shared = view.findViewById(R.id.tv_shared);

        exit = view.findViewById(R.id.exits);
        autoquery.setOnClickListener(this);
        add.setOnClickListener(this);
        shared.setOnClickListener(this);
        exit.setOnClickListener(this);
        // 动态注册广播接收者
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.android.inputmethod.pinyin.Service.changeCheckBox.BottomService");
        receiver = new MyReceiver();
        registerReceiver(receiver, filter);
        System.out.println("注册广播成功");
    }

    // 广播接收者
    class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            System.out.println("收到广播");
            // 当前是否开启服务
            boolean b = sp.getBoolean("auto_query", false);
            if (b) {
                iv_autoquery.setBackgroundResource(R.mipmap.govaffairs_press);
                tv_autoquery.setTextColor(getResources().getColor(R.color.textcolor));

            } else {
                iv_autoquery.setBackgroundResource(R.mipmap.govaffairs);
                tv_autoquery.setTextColor(getResources().getColor(R.color.textcolor_white));
            }
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (flag) {
            flag = false;
            wManager.addView(view, params);//添加窗口
        }
        result = sp.getBoolean("auto_query", false);
        if (result) {
            iv_autoquery.setBackgroundResource(R.mipmap.govaffairs_press);
            tv_autoquery.setTextColor(getResources().getColor(R.color.textcolor));
        } else {
            iv_autoquery.setBackgroundResource(R.mipmap.govaffairs);
            tv_autoquery.setTextColor(getResources().getColor(R.color.textcolor_white));
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (view.getParent() != null)
            wManager.removeView(view);//移除窗口
        unregisterReceiver(receiver);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            /**
             * 处理自动查询功能，开启服务，监听系统剪切板
             */
            case R.id.auto_query:
                boolean is_Open = sp.getBoolean("auto_query", false);
                // 如果服务已经开启
                if (is_Open) {
                    sp.edit().putBoolean("auto_query", false).commit();
                    iv_autoquery.setBackgroundResource(R.mipmap.govaffairs);
                    tv_autoquery.setTextColor(getResources().getColor(R.color.textcolor_white));
                    stopService(new Intent(WindowBottomService.this, AutoQueryService.class));
                } else {
                    sp.edit().putBoolean("auto_query", true).commit();
                    iv_autoquery.setBackgroundResource(R.mipmap.govaffairs_press);
                    tv_autoquery.setTextColor(getResources().getColor(R.color.textcolor));
                    startService(new Intent(WindowBottomService.this, AutoQueryService.class));
                }
                Intent intent = new Intent("com.android.inputmethod.pinyin.Service.changeCheckBox.InformationFragment");
                BaseApplication.getContext().sendBroadcast(intent);
                break;
            case R.id.add:

                break;
            case R.id.shared:

                break;
            case R.id.exits:
                onDestroy();
                sp.edit().putBoolean("window_bottom_tool", false).commit();
                break;
        }
    }
}
