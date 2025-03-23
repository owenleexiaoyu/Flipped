package life.lixiaoyu.flipped.net

import android.os.Build
import android.os.Handler
import android.os.Looper
import java.lang.UnsupportedOperationException
import java.lang.invoke.MethodHandles
import java.lang.invoke.MethodHandles.Lookup
import java.lang.reflect.Constructor
import java.lang.reflect.Method
import java.util.concurrent.Executor
import kotlin.jvm.Throws


open class Platform(private val hasJava8Types: Boolean) {

    private var lookupConstructor: Constructor<Lookup>? = null

    init {
        var lookupConstructor: Constructor<Lookup>? = null
        if (hasJava8Types) {
            try {
                // Because the service interface might not be public, we need to use a MethodHandle lookup
                // that ignores the visibility of the declaringClass.
                lookupConstructor = Lookup::class.java.getDeclaredConstructor(Class::class.java, Int::class.java)
                lookupConstructor.isAccessible = true
            } catch (ignored: NoClassDefFoundError) {
                // Android API 24 or 25 where Lookup doesn't exist. Calling default methods on non-public
                // interfaces will fail, but there's nothing we can do about it.
            } catch (ignored: NoSuchMethodException) {
                // Assume JDK 14+ which contains a fix that allows a regular lookup to succeed.
                // See https://bugs.openjdk.java.net/browse/JDK-8209005.
            }
        }
        this.lookupConstructor = lookupConstructor
    }

    open fun defaultCallbackExecutor(): Executor? = null

    fun defaultCallAdapterFactories(callbackExecutor: Executor?): List<out CallAdapter.Factory> {
        return listOf(
            DefaultCallAdapterFactory(callbackExecutor)
        )
    }

    fun defaultCallAdapterFactoriesSize(): Int = 1

    fun defaultConverterFactories(): List<out Converter.Factory> {
        return emptyList() // TODO
    }

    fun defaultConverterFactoriesSize(): Int = if (hasJava8Types) 1 else 0

    // Only called on API 24+.
    fun isDefaultMethod(method: Method?): Boolean {
        return hasJava8Types && method?.isDefault == true
    }

    @Throws(Throwable::class)
    open fun invokeDefaultMethod(
        method: Method?,
        declaringClass: Class<*>?,
        any: Any?,
        args: Array<out Any>?
    ): Any? {
        val lookup = if (lookupConstructor != null) lookupConstructor!!.newInstance(declaringClass, -1) else MethodHandles.lookup()
        return lookup.unreflectSpecial(method, declaringClass).bindTo(any).invokeWithArguments(*args)
    }

    class Android : Platform(Build.VERSION.SDK_INT >= 24) {

        override fun defaultCallbackExecutor(): Executor? {
            return MainThreadExecutor()
        }

        override fun invokeDefaultMethod(
            method: Method?,
            declaringClass: Class<*>?,
            any: Any?,
            args: Array<out Any>?
        ): Any? {
            if (Build.VERSION.SDK_INT < 26) {
                throw UnsupportedOperationException("Calling default methods on API 24 and 25 is not supported")
            }
            return super.invokeDefaultMethod(method, declaringClass, any, args)
        }

        class MainThreadExecutor : Executor {

            private val handler: Handler = Handler(Looper.getMainLooper())

            override fun execute(command: Runnable?) {
                if (command != null) {
                    handler.post(command)
                }
            }

        }
    }

    companion object {

        private val PLATFORM: Platform = findPlatform()

        fun get(): Platform {
            return PLATFORM
        }

        private fun findPlatform(): Platform {
            return if ("Dalvik" == System.getProperty("java.vm.name")) Android() else Platform(true)
        }
    }
}
