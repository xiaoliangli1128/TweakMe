package com.android.guobao.liao.apptweak;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import android.util.Log;

import com.android.guobao.liao.apptweak.util.*;

public class JavaTweakBridge {
    static public final int PLUGIN_FLAG_DISABLE_OPENAT = 0x00000001;
    static public final int PLUGIN_FLAG_DISABLE_SYSCALL = 0x00000002;
    static public final int PLUGIN_FLAG_DISABLE_THREAD = 0x00000004;

    static private/*final*/int pluginFlags = 0;
    static private final String pluginPackage = JavaTweakBridge.class.getPackage().getName() + "." + "plugin";
    static private final String hookClassPrefix = pluginPackage + "." + "JavaTweak_";
    static private final ConcurrentHashMap<String, Method> backupMethods = new ConcurrentHashMap<String, Method>();

    /*
     * nativeHookSymbol��������hook��̬���еķ��ţ��˷��ſ����ǵ�����Ҳ����û�����ģ���Ϊ������so�����ṩhook��ڣ�רΪnative��hook��������ƣ�ͨ��������ã���java�㲻�ɵ��ã�֧��armeabi��armeabi-v7a��arm64-v8a����ABI��֧��thumb��arm����ָ���
     * symbol: ��Ҫhook�ĺ������ŵ�ַ
     * detour: ���巽���ĺ������ŵ�ַ
     * return: �ɹ�����ԭʼ�����ĵ��õ�ַ��ʧ�ܷ���0
     */
    static private native long nativeHookSymbol(long symbol, long detour);

    /*
     * nativeLoadLib�������Լ����Ѿ����ص�so��Ҳ���Լ�����δ���ص�so�����Լ���ϵͳso��Ҳ���Լ��ط�ϵͳso�����Լ����Լ�/dataĿ¼�µ�so��Ҳ���Լ���/sdcardĿ¼�µ�so
     * libname: ����дȫ·����Ҳ����ֻд�����֣����ֻд�����֣����Ȳ���/sdcard/tweak/$PACKAGEĿ¼����β���/data/data/$PACKAGE/libĿ¼��������ϵͳĿ¼
     * return: ���سɹ�����dlopen�����ʧ�ܷ���0
     * nativeLoadLib���صľ����û��namespace���ƣ����Ե���dlsym��λ����so������libart.so���ķ��ŵ�ַ,������״μ���so������������JNI_OnLoad����������еĻ���
     */
    static public native long nativeLoadLib(String libname);

    static private native void nativePrintLog(int bufid, int prio, String tag, String msg);

    static public void writeToLogcat(int prio, String msg) {
        nativePrintLog(0, prio, "WriteToLogcat", msg + "\r\n");
    }

    static public void writeToLogcat(int prio, String format, Object... args) {
        writeToLogcat(prio, String.format(format, args));
    }

    /*
     * tweak_class: ���滻������������
     * tweak_method�����滻����
     * hook_class: �滻������������
     * hook_method���滻����
     * ������C����һ����Ϊ��List<String> doCommand(int i1, byte[] b2, boolean z3, String s4, HashMap<int, Object> o5)���ķ���
     * ���ڷǹ��췽����tweak_method�Ķ��巽ʽ֧����������  
         1����doCommand�� //ͨ�����Ʋ��ҷǹ��췽��,���ַ�ʽҪȷ��������������Ψһ
         2����doCommand(int,byte[],boolean,java.lang.String,java.util.HashMap)�� //ͨ�������������ҷǹ��췽��
         3����(int,byte[],boolean,java.lang.String,java.util.HashMap<int,java.lang.Object>)java.util.List<java.lang.String>�� //ͨ������ǩ�����ҷǹ��췽��,���ַ�ʽҪȷ��ǩ��������Ψһ���ҷ��������Ҫд��ʵ������
     * ������C����һ����Ϊ��C(int i1, byte[] b2, boolean z3, String s4, HashMap<int, Object> o5)���Ĺ��췽��
     * ���ڹ��췽����tweak_method�Ķ��巽ʽ֧������һ��
         4����(int,byte[],boolean,java.lang.String,java.util.HashMap)�� //ͨ�������������ҹ��췽��
     * hook_method�Ķ��巽ʽ��tweak_methodһ����������hook_method���������Լ���д������ͨ�����÷�ʽ1������Ϊһ������Ψһ�ķ��������ɡ�
     * 2��3��4���ַ�ʽ���������������뾫ȷ���壬�����пո񣬲��������֮���ö��ŷָ�
     * ���滻��������ǷǾ�̬����ʱ���滻�����ĵ�һ�����������Ǳ��滻������thisָ�룬�����������κ��ƣ����滻�������һ��this������
     * �滻���������Ǿ�̬����
     * ���滻����������native��interface��abstract�ȷ���
     * �滻�����������࣬����������ͳһ��ǰ׺��com.android.guobao.liao.apptweak.JavaTweak_***��
     * return: ����ֵΪ���滻�����ı��ݷ���,��������ԭ����
     */
    static private native Method nativeHookMethod(Class<?> tweak_class, String tweak_method, Class<?> hook_class, String hook_method);

    static public boolean hookJavaMethod(Class<?> tweak_class, String tweak_method, String hook_method) {
        boolean hr = hookJavaMethod(tweak_class.getClassLoader(), tweak_class.getName(), tweak_method, null, hook_method);
        return hr;
    }

    static public boolean hookJavaMethod(Class<?> tweak_class, String tweak_method) {
        boolean hr = hookJavaMethod(tweak_class.getClassLoader(), tweak_class.getName(), tweak_method, null, null);
        return hr;
    }

    static public boolean hookJavaMethod(String tweak_class, String tweak_method, String hook_method) {
        boolean hr = hookJavaMethod(null, tweak_class, tweak_method, null, hook_method);
        return hr;
    }

    static public boolean hookJavaMethod(String tweak_class, String tweak_method) {
        boolean hr = hookJavaMethod(null, tweak_class, tweak_method, null, null);
        return hr;
    }

    static public boolean hookJavaMethod(ClassLoader tweak_loader, String tweak_class, String tweak_method, String hook_method) {
        boolean hr = hookJavaMethod(tweak_loader, tweak_class, tweak_method, null, hook_method);
        return hr;
    }

    static public boolean hookJavaMethod(ClassLoader tweak_loader, String tweak_class, String tweak_method) {
        boolean hr = hookJavaMethod(tweak_loader, tweak_class, tweak_method, null, null);
        return hr;
    }

    static private boolean hookJavaMethod(ClassLoader tweak_loader, String tweak_class, String tweak_method, String hook_class, String hook_method) {
        try {
            Class<?> tweak_class_ = null;
            Class<?> hook_class_ = null;
            String hook_method_name = null;

            if (tweak_loader == null) {
                tweak_class_ = Class.forName(tweak_class);
            } else {
                tweak_class_ = Class.forName(tweak_class, true, tweak_loader);
            }
            if (hook_class == null) {
                StackTraceElement[] stes = new Throwable().getStackTrace();
                //stes[0].getClassName()=="com.android.guobao.liao.apptweak.JavaTweakBridge", skip it.
                for (int i = 1; i < stes.length; i++) {
                    if (stes[i].getClassName().startsWith(hookClassPrefix)) {
                        hook_class_ = Class.forName(stes[i].getClassName());
                        break;
                    }
                }
            } else {
                hook_class_ = Class.forName(hook_class);
            }
            if (hook_method == null) {
                int index = tweak_method.indexOf('(');
                hook_method_name = (index == -1 ? tweak_method : tweak_method.substring(0, index));
                hook_method_name = (index == 0 ? tweak_class_.getSimpleName() : hook_method_name);
                hook_method = hook_method_name;
            } else {
                int index = hook_method.indexOf('(');
                hook_method_name = (index == -1 ? hook_method : hook_method.substring(0, index));
            }
            if (backupMethods.containsKey(hook_method_name)) {
                //writeToLogcat(Log.WARN, "hookJavaMethod: method<%s> hook repeat.", tweak_method);
                return false;
            }
            Method m = nativeHookMethod(tweak_class_, tweak_method, hook_class_, hook_method);
            if (m == null) {
                writeToLogcat(Log.ERROR, "hookJavaMethod: method<%s> hook error.", tweak_method);
                return false;
            }
            backupMethods.put(hook_method_name, m);
            writeToLogcat(Log.INFO, "hookJavaMethod: method<%s> hook ok.", tweak_method);
            return true;
        } catch (Throwable e) {
            writeToLogcat(Log.ERROR, e.toString());
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    static private <T> T callOriginalMethod(boolean log, String name, Object receiver, Object... args) {
        T hr = null;
        Method m = null;
        try {
            if (name == null) {
                StackTraceElement[] stes = new Throwable().getStackTrace();
                //stes[0].getClassName()=="com.android.guobao.liao.apptweak.JavaTweakBridge", skip it.
                //stes[0].getMethodName()=="callOriginalMethod", skip it.
                for (int i = 1; i < stes.length; i++) {
                    if (stes[i].getClassName().startsWith(hookClassPrefix)) {
                        name = stes[i].getMethodName();
                        break;
                    }
                }
            }
            m = backupMethods.get(name);
            hr = (T) m.invoke(receiver, args);
        } catch (Throwable e) {
            writeToLogcat(Log.ERROR, "callOriginalMethod: name<%s> error<%s>.", name, e); //���ԭ���������׳��쳣����������˵���˴η�����ô������쳣��
        }
        if (log) {
            writeToLogcat(Log.INFO, paramsToString(name, m, hr, receiver, args));
        }
        return hr;
    }

    static public <T> T callStaticOriginalMethod(Object... args) {
        T hr = callOriginalMethod(true, null, null, args);
        return hr;
    }

    static public <T> T callOriginalMethod(Object receiver, Object... args) {
        T hr = callOriginalMethod(true, null, receiver, args);
        return hr;
    }

    static public <T> T nologStaticOriginalMethod(Object... args) {
        T hr = callOriginalMethod(false, null, null, args);
        return hr;
    }

    static public <T> T nologOriginalMethod(Object receiver, Object... args) {
        T hr = callOriginalMethod(false, null, receiver, args);
        return hr;
    }

    static private String paramsToString(String name, Method m, Object hr, Object receiver, Object... args) {
        final int maxlen = 4096;
        Class<?> type = m.getReturnType();
        Class<?>[] types = m.getParameterTypes();

        String log = String.format("%s::%s%s->{\r\n", m.getDeclaringClass().getName(), m.getName(), !m.getName().equals(name) ? "@" + name : "");
        log += String.format("\t_this_ = %s->%s\r\n", m.getDeclaringClass().getName(), receiver);
        for (int i = 0; i < args.length; i++) {
            String byteArr = ((args[i] instanceof byte[]) ? StringUtil.hexToVisible(((byte[]) args[i]).length > maxlen ? Arrays.copyOf((byte[]) args[i], maxlen) : (byte[]) args[i]) : null);
            String objArr = ((args[i] instanceof Object[]) ? Arrays.deepToString((Object[]) args[i]) : null);
            log += String.format("\tparam%d = %s->%s\r\n", i + 1, types[i].getName(), byteArr != null ? byteArr : (objArr != null ? objArr : args[i]));
        }
        String byteArr = ((hr instanceof byte[]) ? StringUtil.hexToVisible(((byte[]) hr).length > maxlen ? Arrays.copyOf((byte[]) hr, maxlen) : (byte[]) hr) : null);
        String objArr = ((hr instanceof Object[]) ? Arrays.deepToString((Object[]) hr) : null);

        log += String.format("\treturn = %s->%s\r\n}\r\n", type.getName(), byteArr != null ? byteArr : (objArr != null ? objArr : hr));
        return log;
    }

    static public void setPluginFlags(int flags) {
        pluginFlags = flags;
    }
}
