package com.example.shang.etranslate.Tools;

import android.content.Context;
import android.util.Xml;
import android.widget.Toast;

import com.example.shang.etranslate.JavaBean.TranslateInformation;
import com.example.shang.etranslate.R;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Shang on 2017/3/30.
 */
public class ToolUtils {

    public static Toast toast;
    private TranslateInformation translateInformation;
    private List<TranslateInformation> informationsList = new ArrayList<TranslateInformation>();;


    private static FileOutputStream output;
    private static InputStream in;

    public static void copyDB(Context context, File desFile) {
        if (desFile.exists()) {
            // 如果存在则跳出
            Toast.makeText(BaseApplication.getContext(), "DataBase already exists", Toast.LENGTH_LONG).show();
            return;
        } else {
            try {
                // 找到数据库源文件打开它，得到输入流---读取数据库
                in = context.getResources().openRawResource(R.raw.inputmethod);
                // 将数据库写到当前目录下
                output = new FileOutputStream(desFile);
                int len = 0;
                byte[] b = new byte[1024];
                // 输入流读取数据库--写入目录下
                while ((len = in.read(b)) != -1) {
                    output.write(b, 0, len);
                }
                Toast.makeText(BaseApplication.getContext(), "创建成功", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    output.close();
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    public static void showToast(String str){
        if(toast==null){
            toast = Toast.makeText(BaseApplication.getContext(), str, Toast.LENGTH_LONG);
        }else{
            toast.setText(str);
        }
        toast.show();
    }

    /**
     * 将查询信心保存到xml文件中
     */
    public static void writeXml(File file, TranslateInformation information){
        XmlSerializer serializer = Xml.newSerializer();
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file, true);
            serializer.setOutput(fos, "UTF-8");
            serializer.startDocument("UTF-8", true);
            // 开始根标签
            serializer.startTag("", "Informations");

            serializer.startTag("", "Information");
            serializer.startTag("", "content");
            serializer.text(information.getContent());
            serializer.endTag("", "content");
            serializer.startTag("", "result");
            serializer.text(information.getResult());
            serializer.endTag("", "result");
            serializer.endTag("", "Information");

            // 结束根标签
            serializer.endTag("", "Informations");
            serializer.endDocument();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 向xml文件中追加数据
     * @param information
     */
    public static void addData(File file, TranslateInformation information){
        Document document = null;
        SAXReader saxReader = new SAXReader();
        try {
            document = saxReader.read(file);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        // 获取根节点
        Element root = document.getRootElement();
        Element newInformation = root.addElement("Information");
        Element contentElem = newInformation.addElement("content");
        // 使用setText()方法设置sex元素的文本
        contentElem.setText(information.getContent());

        Element resultElem = newInformation.addElement("result");
        resultElem.setText(information.getResult());
        try {
            XMLWriter output = new XMLWriter(new FileWriter(file));
            output.write(document);
            output.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * 读取文件
     * @return
     */
    public List<TranslateInformation> readXml(File file){
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(inputStream, "UTF-8");
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String name = parser.getName();// 标签名称
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        if ("Information".equals(name)) {
                            translateInformation = new TranslateInformation();
                        } else if ("content".equals(name)) {
                            translateInformation.setContent(parser.nextText());
                        } else if ("result".equals(name)) {
                            translateInformation.setResult(parser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if ("Information".equals(name)) {
                            informationsList.add(translateInformation);
                        }
                        break;
                    default:
                        break;
                }
                eventType = parser.next();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        List<TranslateInformation> list = new ArrayList<TranslateInformation>();;
        for(int i = informationsList.size()-1; i >= 0; i--){
            list.add(informationsList.get(i));
        }
        return list;
    }

    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS_SUPPLEMENT) {
            return true;
        } else {
            return false;
        }
    }

}
