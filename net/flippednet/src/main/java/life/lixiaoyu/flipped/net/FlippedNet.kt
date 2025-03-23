package life.lixiaoyu.flipped.net

import life.lixiaoyu.flipped.net.interfaces.ResponseBody
import java.lang.IllegalArgumentException
import java.lang.StringBuilder
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy
import java.lang.reflect.Type
import java.util.Collections
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executor

class FlippedNet constructor(
    val baseUrl: String,  // TODO replace with HttpUrl
    val callFactory: Call.Factory,
    val converterFactories: MutableList<Converter.Factory>,
    val callAdapterFactories: MutableList<CallAdapter.Factory>,
    val callbackExecutor: Executor?,
//    val validateEagerly: Boolean,
    val interceptors: MutableList<Interceptor>,
) {

    private val serviceMethodCache: MutableMap<Method, ServiceMethod<*>> = ConcurrentHashMap()


    fun addInterceptor(interceptor: Interceptor) {
        interceptors.add(interceptor)
    }

    fun <T> create(service: Class<T>): T {
        validateServiceInterface(service)
        return Proxy.newProxyInstance(
            service.classLoader,
            arrayOf<Class<*>>(service),
            object : InvocationHandler {

                private val platform: Platform = Platform.get()

                private val emptyArgs: Array<Any> = emptyArray()

                override fun invoke(proxy: Any?, method: Method?, args: Array<Any>?): Any? {
                    if (method?.declaringClass == Any::class.java) {
                        return method.invoke(this, args)
                    }
                    val localArgs = args ?: emptyArgs
                    return if (platform.isDefaultMethod(method)) platform.invokeDefaultMethod(method, service, proxy, args)
                        else loadServiceMethod(method)?.invoke(localArgs)
                }
            }
        ) as T
    }



    private fun validateServiceInterface(service: Class<*>) {
        if (!service.isInterface) {
            throw IllegalArgumentException("API declaration must be interfaces")
        }
        val check = ArrayDeque<Class<*>>(1)
        check.add(service)
        while(check.isNotEmpty()) {
            val candidate = check.removeFirst()
            if (candidate.typeParameters.isNotEmpty()) {
                val message = StringBuilder("Type parameters are unsupported on ")
                    .append(candidate.name)
                if (candidate != service) {
                    message.append(" which is an interface of ")
                        .append(service.name)
                }
                throw IllegalArgumentException(message.toString())
            }
            Collections.addAll(check, *candidate.interfaces)
        }
    }

    private fun loadServiceMethod(method: Method?): ServiceMethod<*>? {
        if (method == null) return null
        var result = serviceMethodCache[method]
        if (result != null) return result
        synchronized(serviceMethodCache) {
            if (result == null) {
                result = ServiceMethod.parseAnnotations(this, method)
                serviceMethodCache[method] = result!!
            }
        }
        return result
    }


    internal fun callAdapter(returnType: Type, annotations: Array<Annotation>): CallAdapter<*, *> {
        return nextCallAdapter(null, returnType, annotations)
    }

    internal fun nextCallAdapter(skipPast: CallAdapter.Factory?, returnType: Type, annotations: Array<Annotation>): CallAdapter<*, *> {
        val start = callAdapterFactories.indexOf(skipPast) + 1
        for (i in start until callAdapterFactories.size) {
            val adapter = callAdapterFactories[i].get(returnType, annotations, this)
            if (adapter != null) return adapter
        }
        throw IllegalArgumentException("Could not find adapter for $returnType")
    }

    internal fun <ResponseT> responseBodyConverter(responseType: Type, annotations: Array<Annotation>): Converter<ResponseBody, ResponseT> {
        return nextResponseBodyConverter(null, responseType, annotations)
    }

    internal fun <ResponseT> nextResponseBodyConverter(skipPast: Converter.Factory?, responseType: Type, annotations: Array<Annotation>): Converter<ResponseBody, ResponseT> {
        val start = converterFactories.indexOf(skipPast) + 1
        for (i in start until converterFactories.size) {
            val responseConverter = converterFactories[i].responseBodyConverter(responseType, annotations, this)
            if (responseConverter != null) return (responseConverter as Converter<ResponseBody, ResponseT>)
        }
        throw IllegalArgumentException("Could not find converter for $responseType")
    }

    fun <T> stringConverter(type: Type, annotations: Array<Annotation>): Converter<T, String> {
        for (converterFactory in converterFactories) {
            val converter = converterFactory.stringConverter(type, annotations, this)
            if (converter != null) {
                return converter as Converter<T, String>
            }
        }
        return BuiltInConverters.ToStringConverter as Converter<T, String>
    }

    class Builder constructor() {
        private val platform: Platform = Platform.get()
        private var baseUrl: String = ""
        private var callFactory: Call.Factory? = null

        private val converterFactories: MutableList<Converter.Factory> = mutableListOf()
        private val callAdapterFactories: MutableList<CallAdapter.Factory> = mutableListOf()
        private var callbackExecutor: Executor? = null
        private var interceptors: MutableList<Interceptor> = mutableListOf()

        constructor(flippedNet: FlippedNet) : this() {
            callFactory = flippedNet.callFactory
            baseUrl = flippedNet.baseUrl

            val customConverterFactoriesSize = flippedNet.converterFactories.size - platform.defaultConverterFactoriesSize()
            for (i in 0 until  customConverterFactoriesSize) {
                converterFactories.add(flippedNet.converterFactories[i])
            }

            val customCallAdapterFactoriesSize = flippedNet.callAdapterFactories.size - platform.defaultCallAdapterFactoriesSize()
            for (i in 1 until customCallAdapterFactoriesSize) {
                callAdapterFactories.add(flippedNet.callAdapterFactories[i])
            }

            callbackExecutor = flippedNet.callbackExecutor
            interceptors = flippedNet.interceptors
        }

        fun baseUrl(baseUrl: String): Builder {
            this.baseUrl = baseUrl
            return this
        }

        fun callFactory(callFactory: Call.Factory): Builder {
            this.callFactory = callFactory
            return this
        }

        fun addConverterFactory(factory: Converter.Factory): Builder {
            converterFactories.add(factory)
            return this
        }

        fun addCallAdapterFactory(factory: CallAdapter.Factory): Builder {
            callAdapterFactories.add(factory)
            return this
        }

        fun callbackExecutor(executor: Executor): Builder {
            this.callbackExecutor = executor
            return this
        }

        fun build(): FlippedNet {
            checkNotNull(baseUrl) { "Base URL required"}
            checkNotNull(callFactory) { "CallFactory required"}
            val callbackExecutor = this.callbackExecutor ?: platform.defaultCallbackExecutor()
            val callAdapterFactories = mutableListOf<CallAdapter.Factory>()
            callAdapterFactories.addAll(this.callAdapterFactories)
            callAdapterFactories.addAll(platform.defaultCallAdapterFactories(callbackExecutor))

            val converterFactories = mutableListOf<Converter.Factory>()

            return FlippedNet(baseUrl, callFactory!!, converterFactories, callAdapterFactories, callbackExecutor, interceptors)
        }
    }
}