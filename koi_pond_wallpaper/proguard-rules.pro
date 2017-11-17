-dontpreverify
-allowaccessmodification
-optimizations !code/simplification/arithmetic
-keepattributes *Annotation*

-keep public class * extends android.app.Service

-keep class * implements com.yalin.style.engine.IProvider