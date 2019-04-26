package com.example.shang.etranslate.Service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.shang.etranslate.Activity.WindowTranslateActivity;
import com.example.shang.etranslate.R;

/**
 * Created by Shang on 2017/4/2.
 */
public class WindowToolService extends Service {

    private WindowManager wManager;// 窗口管理者
    private WindowManager.LayoutParams params;// 窗口的属性
    private boolean flag = true;
    private View view;
    private int winWidth;
    private int winHeight;
    /**
     * 起始坐标
     */
    private int startX;
    private int startY;
    private ImageView icon;

    long[] src = new long[2];

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        wManager = (WindowManager) this.getSystemService(
                Context.WINDOW_SERVICE);
        params = new WindowManager.LayoutParams();
        // 获取屏幕宽高
        winWidth = wManager.getDefaultDisplay().getWidth();
        winHeight = wManager.getDefaultDisplay().getHeight();
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        params.format = PixelFormat.TRANSLUCENT;
        if (Build.VERSION.SDK_INT >= 26) {
            params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }
        params.gravity = Gravity.LEFT + Gravity.TOP;// 将重心位置设置为左上方,
        // 也就是(0,0)从左上方开始,而不是默认的重心位置
        params.setTitle("Toast");
        //mParams.alpha = 0.1f;//窗口的透明度
        view = View.inflate(this, R.layout.window_tool, null);
        icon = view.findViewById(R.id.image);
        // 处理view的双击事件
        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //复制数组
                System.arraycopy(src, 1, src, 0, src.length - 1);
                src[src.length - 1] = SystemClock.uptimeMillis();
                if (src[0] >= (SystemClock.uptimeMillis() - 500)) {
                    Intent intent = new Intent(WindowToolService.this, WindowTranslateActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        });
        view.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        icon.setBackgroundResource(R.mipmap.th_finderip3);

                        int endX = (int) event.getRawX();
                        int endY = (int) event.getRawY();

                        // 计算移动偏移量
                        int dx = endX - startX;
                        int dy = endY - startY;

                        // 更新浮窗位置
                        params.x += dx;
                        params.y += dy;
                        // 防止坐标偏离屏幕
                        if (params.x > winWidth - view.getWidth()) {
                            params.x = winWidth - view.getWidth();
                        }
                        if (params.x < 0){
                            params.x = 0;
                        }

                        if (params.y > winHeight - view.getHeight()) {
                            params.y = winHeight - view.getHeight();
                        }
                        if (params.y < 0){
                            params.y = 0;
                        }
                        wManager.updateViewLayout(view, params);
                        // 重新初始化起点坐标
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_UP:
                        icon.setBackgroundResource(R.mipmap.th_finder);
                        int x = (int) event.getRawX();
                        int y = (int) event.getRawY();

                        if (x > winWidth / 2) {
                            params.x = winWidth - view.getWidth() / 2;
                        } else {
                            params.x = -view.getWidth() / 2;
                        }
                        wManager.updateViewLayout(view, params);
                        break;
                }
                // 事件向下传递，处理onclick事件
                return false;
            }
        });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
        if (flag) {
            flag = false;
            wManager.addView(view, params);//添加窗口
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        if (view.getParent() != null)
            wManager.removeView(view);//移除窗口
        super.onDestroy();
    }
}
