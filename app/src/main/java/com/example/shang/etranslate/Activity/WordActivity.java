package com.example.shang.etranslate.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.shang.etranslate.JavaBean.TranslateInformation;
import com.example.shang.etranslate.R;
import com.example.shang.etranslate.Tools.BaseApplication;
import com.example.shang.etranslate.Tools.FileUtils;
import com.example.shang.etranslate.Tools.ToolUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class WordActivity extends AppCompatActivity {

    private List<TranslateInformation> list = new ArrayList<TranslateInformation>();
    private ListView lv;
    private ToolUtils toolUtils;

//    private File desFile = FileUtils.getWorkDir();
    private File desFile = FileUtils.getHistoryDir();
    private File file = new File(desFile, "history.xml");

    static class ViewHolder{
        TextView content;
        TextView result;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_word);
        lv = findViewById(R.id.lv);
        initData();
    }

    private void initData() {
        toolUtils = new ToolUtils();
        list = toolUtils.readXml(file);
        MyListAdapter adapter = new MyListAdapter();
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

    class MyListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            ViewHolder holder;
            if(convertView == null){
                holder = new ViewHolder();// holder初始化
                view = View.inflate(BaseApplication.getContext(), R.layout.word_item, null);
                holder.content = (TextView) view.findViewById(R.id.first);
                holder.result = (TextView) view.findViewById(R.id.second);
                view.setTag(holder);
            }else{
                view = convertView;
                holder = (ViewHolder) view.getTag();
            }
            holder.content.setText(list.get(position).getContent());
            holder.result.setText(list.get(position).getResult());
            return view;
        }
    }

}
