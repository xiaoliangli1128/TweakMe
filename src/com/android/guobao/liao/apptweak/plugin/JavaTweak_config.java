package com.android.guobao.liao.apptweak.plugin;

import com.android.guobao.liao.apptweak.JavaTweakBridge;
import com.android.guobao.liao.apptweak.util.*;

@SuppressWarnings("unused")
public class JavaTweak_config {
    static public void loadDexFile(String dex) {
        //������ò��Ǳ���ģ�����ش����app���������о��������á�
        //����ش����app�����������󣬿��Գ������ηſ���������ô��룬��ĳЩ�ӹ̣��������л��޸��������󣬵�����֤���޸������������⡣
        JavaTweakBridge.setPluginFlags(JavaTweakBridge.PLUGIN_FLAG_DISABLE_SYSCALL);
        //JavaTweakBridge.setPluginFlags(JavaTweakBridge.PLUGIN_FLAG_DISABLE_OPENAT);
        //JavaTweakBridge.setPluginFlags(JavaTweakBridge.PLUGIN_FLAG_DISABLE_SYSCALL|JavaTweakBridge.PLUGIN_FLAG_DISABLE_OPENAT);
        //JavaTweakBridge.setPluginFlags(JavaTweakBridge.PLUGIN_FLAG_DISABLE_THREAD);
    }
}
