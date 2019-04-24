package com.example.shang.etranslate;

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
    }

    private void initData() {
        ToolUtils.copyDB(this, file);// 拷贝数据库
    }

    private void initView() {
        titleList = getResources().getStringArray(R.array.title);
        vp = (ViewPager) findViewById(R.id.vp);
        strip = (PagerTabStrip) findViewById(R.id.pager_tab_strip);
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
