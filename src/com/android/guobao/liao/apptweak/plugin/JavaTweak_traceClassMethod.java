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
         * f ���ֶ�  c �ǹ��췽�� m����ͨ����
         */
        String name = clazz.getName();
        String findClass="AES.*?";

        Pattern pattern = Pattern.compile(findClass,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(name);
        while(matcher.find()){
            JavaTweakBridge.writeToLogcat(Log.ERROR, "[**** found class name ***]" + name + "");
            JavaTweakBridge.writeToLogcat(Log.ERROR,"[**** getDeclaredMethods ****]"+ ReflectUtil.classToString(clazz));//��ӡclass�����з���
            // ������ ָ�� hook�ķ���
            /*
            Method a = ReflectUtil.findClassMethod(clazz, "a", 2); //ָ����ӡa �ĵڶ������ط���
            String fullClassMethod = a.toString();
            JavaTweakBridge.writeToLogcat(Log.ERROR, " hook ����:" + fullClassMethod);
            String methodSign = fullClassMethod.substring(
                    fullClassMethod.indexOf(name) + name.length() + 1);
            JavaTweakBridge.writeToLogcat(Log.ERROR, " hook ����:" + methodSign);
            Parameter[] parameters = a.getParameters();
            if (parameters.length < 1) {
                JavaTweakBridge.writeToLogcat(Log.ERROR, "�޲���");
            }
            for (Parameter p1 : parameters) {
                JavaTweakBridge.writeToLogcat(Log.ERROR, "������:  >" + p1.getName() + " ��������: > " + p1.getType());
            }
            JavaTweakBridge.writeToLogcat(Log.ERROR, "hooking:  >" + name);
            JavaTweakBridge.writeToLogcat(Log.ERROR, "methodsign: > " + methodSign);
        */
        }



    }

}
