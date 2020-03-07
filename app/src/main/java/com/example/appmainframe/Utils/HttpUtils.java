package com.example.appmainframe.Utils;

import org.json.JSONObject;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtils {
    public int resultCode;
    public InputStream inputStream;

    /**
     * http连接工具类
     * @param url
     * @param json
     * @throws Exception
     */
    public void httputils(URL url , JSONObject json) throws Exception {
        String content = String.valueOf(json);
        //开启连接
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        //设置超时
        conn.setConnectTimeout(2000);
        //设置读写权限
        conn.setDoInput(true);
        conn.setDoOutput(true);
        //设置http方法
        conn.setRequestMethod("POST");
        //设置http头
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Connection", "keep-alive");
        conn.setRequestProperty("Charset", "UTF-8");
        //http连接，可不加
        conn.connect();
        //获取输出流
        OutputStream os = conn.getOutputStream();
        os.write(content.getBytes());
        os.close();
        //获取http返回码
        resultCode = conn.getResponseCode();
        //获取输入流
        inputStream = conn.getInputStream();
    }
    public int getResultCode(){
        return resultCode;
    }
    public InputStream getInputStream(){
        return inputStream;
    }
}
