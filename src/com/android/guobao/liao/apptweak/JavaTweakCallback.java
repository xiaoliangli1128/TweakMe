package com.android.guobao.liao.apptweak;

@SuppressWarnings("unused")
public class JavaTweakCallback {
    static private void loadDexFile(String dex) { //javatweak.dex������ʱ�ص�
        JavaTweak.loadDexFile(dex);
    }

    static private void defineClassLoader(ClassLoader loader) { //���������һ�α�����ʱ�ص�
        JavaTweak.defineClassLoader(loader);
    }

    static private void defineJavaClass(Class<?> clazz) { //���һ�α�����ʱ�ص�
        JavaTweak.defineJavaClass(clazz);
    }
}
