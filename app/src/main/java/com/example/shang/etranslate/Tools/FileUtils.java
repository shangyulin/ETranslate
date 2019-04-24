package com.example.shang.etranslate.Tools;

import android.os.Environment;

import java.io.File;

/**
 * Created by Shang on 2017/4/14.
 */
public class FileUtils {

    public static final String ROOT = "InputMethod";

    public static final String HISTORY = "history";

    public static final String WORK = "work";

    public static final String DATABASE = "database";

    /**
     * 数据缓存文件
     * @return
     */
    public static File getHistoryDir(){
        return getDir(HISTORY);
    }

    /**
     * 图片缓存地址
     * @return
     */
    public static File getWorkDir(){
        return getDir(WORK);
    }

    /**
     * 图片url缓存路径
     * @return
     */
    public static File getDatabaseDir(){
        return getDir(DATABASE);
    }

    // 数据库缓存地址
    public static String getDatabasePath() {
        return getPath(DATABASE);
    }

    public static File getDir(String dir){

        StringBuilder builder = new StringBuilder();
        if(isSDExist()){
            builder.append(Environment.getExternalStorageDirectory().getAbsolutePath());
            builder.append(File.separator);
            builder.append(ROOT);
            builder.append(File.separator);
            builder.append(dir);
        }else{
            File cacheDir = BaseApplication.getContext().getCacheDir();
            builder.append(cacheDir.getAbsolutePath());
            builder.append(File.separator);
            builder.append(dir);
        }
        File file = new File(builder.toString());
        if(!file.exists()){
            file.mkdirs();
        }
        return file;
    }

    public static String getPath(String dir){
        StringBuilder builder = new StringBuilder();
        if(isSDExist()){
            builder.append(Environment.getExternalStorageDirectory().getAbsolutePath());
            builder.append(File.separator);
            builder.append(ROOT);
            builder.append(File.separator);
            builder.append(dir);
        }else{
            File cacheDir = BaseApplication.getContext().getCacheDir();
            builder.append(cacheDir.getAbsolutePath());
            builder.append(File.separator);
            builder.append(dir);
        }

        return builder.toString();
    }

    private static boolean isSDExist() {
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            return true;
        }else{
            return false;
        }
    }

}
