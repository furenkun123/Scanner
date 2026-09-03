# ------------------------------------------------------------------
# 1. Room 数据库防混淆（关键：保留实体类与 DAO）
# ------------------------------------------------------------------
-keep class * extends androidx.room.RoomDatabase
-keepclassmembers class * extends androidx.room.RoomDatabase {
    public <init>();
}
-keep class * implements androidx.room.RoomDatabase { *; }

# 替换为你的真实 Entity 和 DAO 所在包名，防止列名/接口被混淆重命名
-keep class com.scanner.lite** { *; }

# ------------------------------------------------------------------
# 2. ML Kit & CameraX 防混淆
# ------------------------------------------------------------------
-keep class com.google.mlkit.** { *; }
-keep class com.google.android.gms.** { *; }
-keep class androidx.camera.** { *; }

-dontwarn com.google.mlkit.**
-dontwarn androidx.camera.**

# ------------------------------------------------------------------
# 3. 通用 JNI 与注解保留
# ------------------------------------------------------------------
-keepattributes *Annotation*,Signature,InnerClasses,EnclosingMethod
-keepclasseswithmembernames class * {
    native <methods>;
}