package com.android.guobao.liao.apptweak.util;

import android.util.Log;


import com.android.guobao.liao.apptweak.JavaTweakBridge;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class InterceptEncryptOrDecrypt implements Callable<String> {

    private String postBody;

    public void setHttpUrl(String httpUrl) {
        this.httpUrl = httpUrl;
    }

    private String httpUrl;

    private void setPostBody(String postBody) {
        this.postBody = postBody;
    }



    private String ECHO_SERVER="http://192.168.31.100:27080"; //echo server



    public static String doHttp(String postBody,String httpUrl) throws ExecutionException, InterruptedException {
        InterceptEncryptOrDecrypt d1=new InterceptEncryptOrDecrypt();
        d1.setPostBody(postBody);
        d1.setHttpUrl(httpUrl);
        FutureTask<String> task=new FutureTask<>(d1);
        new Thread(task).start();
        if (!task.isDone()) {
            System.out.println("task has not finished, please wait!");
        }
        System.out.println( "task return: " + task.get());
        return  task.get();
    }



    @Override
    public String call() throws Exception {
        String rsp="";
        JavaTweakBridge.writeToLogcat(Log.ERROR,"[*****]调用前原始参数 %s \n",postBody);
        try {
            HttpURLConnection con = null;
            BufferedReader buffer = null;
            StringBuffer resultBuffer = null;
            URL url = new URL(ECHO_SERVER+httpUrl);
            //得到连接对象
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(
                    "192.168.31.100", 8888)); //burp端口
            con = (HttpURLConnection) url.openConnection(proxy);
            //设置请求类型
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            //允许写出
            con.setDoOutput(true);                    //允许读入
            con.setDoInput(true);
            con.setUseCaches(false);
            OutputStream os = con.getOutputStream();
            os.write(postBody.getBytes());
            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // 必须返回200
                InputStream inputStream = con.getInputStream();
                //将响应流转换成字符串
                resultBuffer = new StringBuffer();
                String line;
                buffer = new BufferedReader(new InputStreamReader(inputStream, "GBK"));
                while ((line = buffer.readLine()) != null) {
                    resultBuffer.append(line);
                }

                rsp= resultBuffer.toString();
            }
        } catch (Exception e) {
            JavaTweakBridge.writeToLogcat(Log.ERROR,"echo Server 报错");
            e.printStackTrace();
        }
        JavaTweakBridge.writeToLogcat(Log.ERROR,"[*****]调用前修改后的参数 %s \n",rsp);
        return rsp;
    }
}

