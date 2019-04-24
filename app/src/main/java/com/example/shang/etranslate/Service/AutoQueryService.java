package com.example.shang.etranslate.Service;

import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.example.shang.etranslate.Activity.WindowTranslateActivity;

/**
 * Created by Shang on 2017/4/8.
 */
public class AutoQueryService extends Service {

    private ClipboardManager cb;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        cb = (ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
        cb.setPrimaryClip(ClipData.newPlainText("",""));
        cb.addPrimaryClipChangedListener(new ClipboardManager.OnPrimaryClipChangedListener() {
            @Override
            public void onPrimaryClipChanged() {
                ClipData.Item item = cb.getPrimaryClip().getItemAt(0);
                if(item.getText().toString()!=null&&item.getText().toString().trim().length()>0){
                    Intent intent = new Intent(AutoQueryService.this, WindowTranslateActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("clipContent", item.getText().toString());
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
