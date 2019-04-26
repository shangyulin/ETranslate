package com.example.shang.etranslate;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;

import com.example.shang.etranslate.Fragment.FragmentFactory;
import com.example.shang.etranslate.Tools.FileUtils;
import com.example.shang.etranslate.Tools.ToolUtils;
import com.giant.channel.base.permission.PermissionChannels;
import com.giant.channel.base.permission.PermissionRequest;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private String[] titleList;
    private ViewPager vp;
    private PagerTabStrip strip;

    private File desFile = FileUtils.getDatabaseDir();
    private File file = new File(desFile, "database.db");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        initView();
        initData();
        vp.setAdapter(new MyPageAdapter(getSupportFragmentManager()));
        PermissionRequest.request(this, PermissionChannels.channel_jinli);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10){
        }
    }

    private void initData() {
        ToolUtils.copyDB(this, file);// 拷贝数据库
    }

    private void initView() {
        titleList = getResources().getStringArray(R.array.title);
        vp = findViewById(R.id.vp);
        strip = findViewById(R.id.pager_tab_strip);
        strip.setTabIndicatorColorResource(R.color.indicatorcolor);
    }

    class MyPageAdapter extends FragmentStatePagerAdapter {

        public MyPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return FragmentFactory.getFragment(position);
        }

        @Override
        public int getCount() {
            return titleList.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titleList[position];
        }
    }
}
