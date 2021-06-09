# Begin: Debug Proguard rules

-dontobfuscate                              #난독화를 수행하지 않도록 함
-keepattributes SoureFile,LineNumberTable   #소스파일, 라인 정보 유지

-keepclassmembers,allowobfuscation class * {
    @com.google.gson.annotations.SerializedName <fields>;
  }

-keep,allowobfuscation @interface com.google.gson.annotations.SerializedName
-keep class com.kakao.sdk.**.model.* { <fields>; }
-keep class * extends com.google.gson.TypeAdapter

-keep public class * extends androidx.lifecycle.ViewModel {*;}

# End: Debug ProGuard rules