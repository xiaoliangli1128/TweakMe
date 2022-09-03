@echo off
set ANDROID_VER=26
set ANDROID_SDK=%~dp0
set ANDROID_API=%ANDROID_SDK%\platforms\android-%ANDROID_VER%
set BUILD_TOOLS=%ANDROID_SDK%\build-tools\30.0.2

set SRCDIR_TWEAKME=%~dp0\src
set SRCDIR_APPTWEAK=%SRCDIR_TWEAKME%\com\android\guobao\liao\apptweak
set SRCDIR_PLUGIN=%SRCDIR_APPTWEAK%\plugin
set SRCDIR_UTIL=%SRCDIR_APPTWEAK%\util

mkdir %~dp0\tmp
javac -bootclasspath %ANDROID_API%\android.jar -classpath %ANDROID_API%\optional\*;%~dp0\libs\* -d %~dp0\tmp %SRCDIR_APPTWEAK%\*.java %SRCDIR_PLUGIN%\*.java %SRCDIR_UTIL%\*.java -Xlint:deprecation
java -jar %BUILD_TOOLS%\lib\dx.jar --dex --min-sdk-version=%ANDROID_VER% --output=%~dp0\javatweak.dex %~dp0\libs\* %~dp0\tmp
rmdir /s /q %~dp0\tmp
echo 临时文件已删除
adb push .\javatweak.dex /sdcard/tweak/com.csii.changjiang.mobilebank
adb shell am force-stop com.csii.changjiang.mobilebank