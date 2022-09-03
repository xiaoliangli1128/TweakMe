package com.android.guobao.liao.apptweak.plugin;

import android.util.Log;

import com.android.guobao.liao.apptweak.JavaTweakBridge;
import com.android.guobao.liao.apptweak.util.InterceptEncryptOrDecrypt;
import com.android.guobao.liao.apptweak.util.ReflectUtil;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.concurrent.ExecutionException;


public class JavaTweak_camille {

    static public void loadDexFile(String dex) {
        //�˺����ڿ�����һЩ��ʼ���������������native��̬�⣬����ϵͳ�ຯ���ȵ�
//        JavaTweakBridge.hookJavaMethod("android.app.ActivityThread", "performLaunchActivity");
//        JavaTweakBridge.hookJavaMethod("android.app.ActivityManager","getRunningAppProcesses");
//        JavaTweakBridge.hookJavaMethod("android.os.Environment","getExternalStorageDirectory");

    }


    static public void defineJavaClass(Class<?> clazz) {
        String name = clazz.getName();
        if(name.equals("com.yitong.mbank.util.security.CryptoUtil")){
//            JavaTweakBridge.hookJavaMethod(clazz.getClassLoader(), name, "a(android.app.Application,java.lang.String,java.lang.String)","_a");
//            JavaTweakBridge.hookJavaMethod(clazz.getClassLoader(), name, "b(android.app.Application,java.lang.String,java.lang.String)");

        }


    }

    /** 
     * @Hook_method: getRunningAppProcesses Ҫ�滻�ķ��� ���뾲̬����
     * @author: Pyth0n
     * @todo: 
     * @date: 2022/9/1 14:00
     * @param thiz
     * @return java.lang.Object  ԭ��������Ǿ�̬�ı�����callStaticOrignalMethod      
     */
    static private Object getRunningAppProcesses(Object thiz) { // ��ӡ��ȡapp �����б� ��˽�Ϲ�Ȩ��
        JavaTweakBridge.writeToLogcat(Log.INFO,"��ȡapp�����б� ����ջ��%s",Log.getStackTraceString(new Throwable()));
        Log.i("WriteToLogcat","��ӡ���6");
        return JavaTweakBridge.callOriginalMethod(thiz);
    }
    
    /*
     * public static File getExternalStorageDirectory() {
    *
    ���滻���� �Ǿ�̬��  ��� �滻���� _getExternalStorageDirectory ����Ҫthis ����
    ����ԭʼ���� Ҳ��Ҫ��callStaticOriginalMethod
     */
    static private Object getExternalStorageDirectory() { // ��ӡ��ȡapp ��ȡ�洢����Ϣ ��˽�Ϲ�Ȩ��
        JavaTweakBridge.writeToLogcat(Log.INFO,"��ȡ�洢����Ϣ ����ջ��%s",Log.getStackTraceString(new Throwable()));
        Log.i("WriteToLogcat","��ӡ���");
        return JavaTweakBridge.callStaticOriginalMethod();
    }
    /**
     * @Hook_method: a ���滻�ķ��� ���뾲̬����   _a �滻�ķ���
     * @author: Pyth0n
     * @todo:  com.yitong.mbank.util.security.CryptoUtil.a
     * @date: 2022/9/2 12:42
     * @param dataStr
     * @param keyStr
     * @return java.lang.Object  ԭ��������Ǿ�̬�ı�����callStaticOrignalMethod
     */
    static private Object _a(Object application,Object dataStr,Object keyStr) throws ExecutionException, InterruptedException { // ��ӡ���ܺ���
        //JavaTweakBridge.writeToLogcat(Log.INFO,"���ܺ��� ����ջ��%s",Log.getStackTraceString(new Throwable()));
        String rsp= InterceptEncryptOrDecrypt.doHttp((String) dataStr,"/Request"); //�Ѽ���ǰ�Ĳ�������burp
        return JavaTweakBridge.callStaticOriginalMethod(application,rsp,keyStr);// ���������޸ĺ�Ĳ�������
    }
        /**
         * @Hook_method: b Ҫ�滻�ķ��� ���뾲̬����
         * @author: Pyth0n
         * @todo: ����app�Ľ��ܺ���
         * @date: 2022/9/3 11:01
         * @param application
         * @param dataStr
         * @param keyStr
         * @return java.lang.Object  ԭ��������Ǿ�̬�ı�����callStaticOrignalMethod
         */
    static private Object b(Object application,Object dataStr,Object keyStr) throws ExecutionException, InterruptedException { // ��ӡ���ܺ���
        //JavaTweakBridge.writeToLogcat(Log.INFO,"���ܺ��� ����ջ��%s",Log.getStackTraceString(new Throwable()));
        Object rsp=JavaTweakBridge.callStaticOriginalMethod(application,dataStr,keyStr); // �ȵ���ԭʼ���ܺ���
        InterceptEncryptOrDecrypt.doHttp((String) rsp,"/Response"); //ֻ�ǵ����İѽ��ܺ�ķ��ذ���burp�� �����޸�ֵ��Ҳ�����޸�
        return JavaTweakBridge.callStaticOriginalMethod(application,dataStr,keyStr);
    }



}
