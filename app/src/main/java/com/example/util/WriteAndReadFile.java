package com.example.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class WriteAndReadFile {
    public static String mFilePath = "";
//    private Context context;
//    public WriteAndReadFile(Context context){
//        this.context = context;
//    }
    /*
    * 写入文件
    * */
    public void writeFile(String name,String data){
        name = name.replaceAll("[.:/,%?&amp;=]", "+").replaceAll("[+]+", "+");

//        File file = new File(mFilePath + "/" + name);

//        File file = context.getFilesDir();
        File fi = new File(mFilePath+File.separator+name);   //文件存储位置
        Logs.e("filePath--------------"+fi.getAbsolutePath());
        FileOutputStream br = null;
        try {
            br = new FileOutputStream(fi);
            br.write(data.getBytes("UTF-8"));
            Logs.e("file----------写入成功");
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (br != null){
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    /*
    * 读出文件
    * */
    public String readFile(String url){
      //  FileInputStream fis = null;
        BufferedReader br = null;
        StringBuffer sb = new StringBuffer();
        try {
//            fis = new FileInputStream(context.getFilesDir());
            url = url.replaceAll("[.:/,%?&amp;=]", "+").replaceAll("[+]+", "+");

            File file = new File(mFilePath + "/" + url);
            if (file.exists() && file.isFile()) {


                Reader bis = new FileReader(file);
                br = new BufferedReader(bis);
                String StrData;
                while ((StrData = br.readLine()) != null) {
                    sb.append(StrData);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (br !=null){
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

}
