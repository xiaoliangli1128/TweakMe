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
        //此函数内可以做一些初始化操作，比如加载native动态库，拦截系统类函数等等
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
     * @Hook_method: getRunningAppProcesses 要替换的方法 必须静态函数
     * @author: Pyth0n
     * @todo: 
     * @date: 2022/9/1 14:00
     * @param thiz
     * @return java.lang.Object  原函数如果是静态的必须用callStaticOrignalMethod      
     */
    static private Object getRunningAppProcesses(Object thiz) { // 打印获取app 运行列表 隐私合规权限
        JavaTweakBridge.writeToLogcat(Log.INFO,"获取app运行列表 调用栈：%s",Log.getStackTraceString(new Throwable()));
        Log.i("WriteToLogcat","打印完毕6");
        return JavaTweakBridge.callOriginalMethod(thiz);
    }
    
    /*
     * public static File getExternalStorageDirectory() {
    *
    被替换方法 是静态的  因此 替换方法 _getExternalStorageDirectory 不需要this 参数
    调用原始函数 也需要用callStaticOriginalMethod
     */
    static private Object getExternalStorageDirectory() { // 打印获取app 获取存储卡信息 隐私合规权限
        JavaTweakBridge.writeToLogcat(Log.INFO,"获取存储卡信息 调用栈：%s",Log.getStackTraceString(new Throwable()));
        Log.i("WriteToLogcat","打印完毕");
        return JavaTweakBridge.callStaticOriginalMethod();
    }
    /**
     * @Hook_method: a 被替换的方法 必须静态函数   _a 替换的方法
     * @author: Pyth0n
     * @todo:  com.yitong.mbank.util.security.CryptoUtil.a
     * @date: 2022/9/2 12:42
     * @param dataStr
     * @param keyStr
     * @return java.lang.Object  原函数如果是静态的必须用callStaticOrignalMethod
     */
    static private Object _a(Object application,Object dataStr,Object keyStr) throws ExecutionException, InterruptedException { // 打印加密函数
        //JavaTweakBridge.writeToLogcat(Log.INFO,"加密函数 调用栈：%s",Log.getStackTraceString(new Throwable()));
        String rsp= InterceptEncryptOrDecrypt.doHttp((String) dataStr,"/Request"); //把加密前的参数发给burp
        return JavaTweakBridge.callStaticOriginalMethod(application,rsp,keyStr);// 继续调用修改后的参数加密
    }
        /**
         * @Hook_method: b 要替换的方法 必须静态函数
         * @author: Pyth0n
         * @todo: 拦截app的解密函数
         * @date: 2022/9/3 11:01
         * @param application
         * @param dataStr
         * @param keyStr
         * @return java.lang.Object  原函数如果是静态的必须用callStaticOrignalMethod
         */
    static private Object b(Object application,Object dataStr,Object keyStr) throws ExecutionException, InterruptedException { // 打印加密函数
        //JavaTweakBridge.writeToLogcat(Log.INFO,"加密函数 调用栈：%s",Log.getStackTraceString(new Throwable()));
        Object rsp=JavaTweakBridge.callStaticOriginalMethod(application,dataStr,keyStr); // 先调用原始解密函数
        InterceptEncryptOrDecrypt.doHttp((String) rsp,"/Response"); //只是单纯的把解密后的返回包给burp， 并不修改值，也可以修改
        return JavaTweakBridge.callStaticOriginalMethod(application,dataStr,keyStr);
    }



}
