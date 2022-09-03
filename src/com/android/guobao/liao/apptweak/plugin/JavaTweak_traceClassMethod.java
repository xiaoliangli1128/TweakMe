package com.android.guobao.liao.apptweak.plugin;

import android.util.Log;

import com.android.guobao.liao.apptweak.JavaTweakBridge;
import com.android.guobao.liao.apptweak.util.ReflectUtil;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JavaTweak_traceClassMethod {


    static public void defineJavaClass(Class<?> clazz) {
        /**
         * f 是字段  c 是构造方法 m是普通方法
         */
        String name = clazz.getName();
        String findClass="AES.*?";

        Pattern pattern = Pattern.compile(findClass,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(name);
        while(matcher.find()){
            JavaTweakBridge.writeToLogcat(Log.ERROR, "[**** found class name ***]" + name + "");
            JavaTweakBridge.writeToLogcat(Log.ERROR,"[**** getDeclaredMethods ****]"+ ReflectUtil.classToString(clazz));//打印class下所有方法
            // 下面是 指定 hook的方法
            /*
            Method a = ReflectUtil.findClassMethod(clazz, "a", 2); //指定打印a 的第二个重载方法
            String fullClassMethod = a.toString();
            JavaTweakBridge.writeToLogcat(Log.ERROR, " hook 方法:" + fullClassMethod);
            String methodSign = fullClassMethod.substring(
                    fullClassMethod.indexOf(name) + name.length() + 1);
            JavaTweakBridge.writeToLogcat(Log.ERROR, " hook 方法:" + methodSign);
            Parameter[] parameters = a.getParameters();
            if (parameters.length < 1) {
                JavaTweakBridge.writeToLogcat(Log.ERROR, "无参数");
            }
            for (Parameter p1 : parameters) {
                JavaTweakBridge.writeToLogcat(Log.ERROR, "参数名:  >" + p1.getName() + " 参数类型: > " + p1.getType());
            }
            JavaTweakBridge.writeToLogcat(Log.ERROR, "hooking:  >" + name);
            JavaTweakBridge.writeToLogcat(Log.ERROR, "methodsign: > " + methodSign);
        */
        }



    }

}
