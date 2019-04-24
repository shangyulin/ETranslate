package com.example.shang.etranslate.Dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Shang on 2017/4/2.
 */
public class CodeDao {
//    private static final String path = "data/data/com.example.shang.zhongwen/files/inputmethod.db";
    private static SQLiteDatabase database;
    private static String pinyin;


    synchronized public static String getPinyinFromWord(String path, String word, String firstLabel){
        database = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
        Cursor cursor = database.query("table_" + firstLabel, new String[]{"code_label"}, "word = ?", new String[]{word}, null, null, null);
        while(cursor.moveToNext()){
            pinyin = cursor.getString(0);
        }
        return pinyin;
    }
}
