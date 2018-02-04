# App
-keepclasseswithmembers class cafe.adriel.messages.model.** {
  <init>(...);
  <fields>;
}

# Kotlin
-dontwarn org.jetbrains.annotations.**
-dontwarn kotlin.reflect.jvm.internal.**
-keep public class kotlin.reflect.jvm.internal.impl.builtins.* { public *; }

# Realm
-keepnames public class * extends io.realm.RealmObject
-keep public class * extends io.realm.RealmModel { *; }
-keep public class io.realm.RealmList { *; }
-keep class io.realm.RealmObject { *; }
-keep @io.realm.annotations.RealmModule class *

# Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

# Moshi
-dontwarn okio.**
-dontwarn javax.annotation.**
-keepclasseswithmembers class * {
    @com.squareup.moshi.* <methods>;
}
-keep @com.squareup.moshi.JsonQualifier interface *
-keepclassmembers class ** {
  @com.squareup.moshi.FromJson *;
  @com.squareup.moshi.ToJson *;
}
-keep class kotlin.Metadata { *; }
-keepclassmembers class kotlin.Metadata {
    public <methods>;
}

# FastAdapter
-dontwarn com.mikepenz.fastadapter.**