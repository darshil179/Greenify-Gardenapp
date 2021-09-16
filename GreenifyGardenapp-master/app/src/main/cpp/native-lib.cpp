#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring

JNICALL
Java_designengineeeringgroup_greenify_1gardenapp_LoginPage_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}
