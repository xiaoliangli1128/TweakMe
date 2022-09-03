package com.android.guobao.liao.apptweak.plugin;

import java.io.File;
import android.content.pm.ApplicationInfo;
import android.os.Environment;
import android.util.Log;

import com.android.guobao.liao.apptweak.JavaTweakBridge;
import com.android.guobao.liao.apptweak.util.*;

public class JavaTweak_gadget {
    // frida -H 192.168.31.82:33026 gadget  ֱ��
    // objection.exe -N -h 192.168.31.82 -p 33026 -g gadget explore  ֱ��

    // �� adb forward tcp��27042 tcp��33026 �Ϳ��������
    // frida -U gadget
    // objection.exe -Ng gadget explore



    static private final String GADGET_NAME = "libminitools.so";
    //listen:  address, port, on_load
    //connect: address, port
    //script:           path
    //script-directory: path
    static private final String GADGET_CONF = "{\"interaction\":{\"type\":\"listen\",\"address\":\"0.0.0.0\",\"port\":33026,\"path\":\"/sdcard/tweak/com.android.tweakme/hello.js\",\"on_load\":\"resume\"},\"teardown\":\"minimal\",\"runtime\":\"default\",\"code_signing\":\"optional\"}";

    static public void loadDexFile(String dex) {
        String pn = TweakUtil.currentPackageName();
        ApplicationInfo ai = TweakUtil.getApplicationInfo(0);

        String sddir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/tweak/" + pn;
        String libdir = ai.nativeLibraryDir;
        String tweakdir = ai.dataDir + "/app_tweak";

        JavaTweakBridge.writeToLogcat(Log.ERROR,"sddir= %s, libdir=%s , tweakdir= %s",
                sddir,libdir,tweakdir);

        String gadget = GADGET_NAME;
        if (gadget.indexOf('/') == -1) {
            if (new File(sddir + "/" + gadget).exists()) {
                gadget = sddir + "/" + gadget; //�� /sdcard/tweak/$PACKAGE/Ŀ¼���ҵ��˶�̬��
            } else if (new File(libdir + "/" + gadget).exists()) {
                gadget = libdir + "/" + gadget; //�� /data/data/$PACKAGE/lib/Ŀ¼���ҵ��˶�̬��
            } else if (new File(tweakdir + "/" + gadget).exists()) {
                gadget = tweakdir + "/" + gadget; //�� /data/data/$PACKAGE/app_tweak/Ŀ¼���ҵ��˶�̬��
            }
        }
        if (!new File(gadget).exists()) {
            JavaTweakBridge.writeToLogcat(Log.ERROR, "gadget: %s no exists.", gadget);
            return;
        }
        if (!new File(gadget).canRead()) {
            JavaTweakBridge.writeToLogcat(Log.ERROR, "gadget: %s can not read.", gadget);
            return;
        }
        String copyto = tweakdir + gadget.substring(gadget.lastIndexOf('/'));
        if (gadget.indexOf("/app_tweak/") == -1 && TweakUtil.isMainProcess()) {
            if (new File(gadget).length() != new File(copyto).length()) {
                FileUtil.copyFile(gadget, copyto); //libgadget.so�ļ��ǳ�������򵥱Ƚ�һ���ļ���С���ж��Ƿ���Ҫ����
            }
        }
        gadget = copyto;
        String conf = gadget.substring(0, gadget.length() - 3) + ".config" + ".so";
        FileUtil.writeFile(conf, GADGET_CONF.getBytes()); //д�����ļ���/data/data/$PACKAGE/app_tweak/Ŀ¼��

        long handle = JavaTweakBridge.nativeLoadLib(gadget);
        JavaTweakBridge.writeToLogcat(handle == 0 ? Log.ERROR : Log.INFO, "nativeLoadLib: libname = %s, handle = 0x%x", gadget, handle);
    }
}
