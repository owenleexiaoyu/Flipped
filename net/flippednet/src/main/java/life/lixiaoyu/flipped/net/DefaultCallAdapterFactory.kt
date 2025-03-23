package life.lixiaoyu.flipped.net

import java.io.IOException
import java.lang.IllegalArgumentException
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.util.concurrent.Executor

class DefaultCallAdapterFactory(
    private val callbackExecutor: Executor?
): CallAdapter.Factory() {

    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        flippedNet: FlippedNet
    ): CallAdapter<*, *>? {
        if (getRawType(returnType) != Call::class.java) {
            return null
        }
        if (returnType !is ParameterizedType) {
            throw IllegalArgumentException("Call return type must be parameterized as Call<Foo> or Call<out Foo>")
        }
        val responseType = Utils.getParameterUpperBound(0, returnType)
        return object: CallAdapter<Any, Call<*>> {
            override fun responseType(): Type = responseType

            override fun adapt(call: Call<Any>): Call<*> {
                return if (callbackExecutor == null) call else ExecutorCallbackCall(callbackExecutor, call)
            }

        }
    }

    class ExecutorCallbackCall<T>(private val callbackExecutor: Executor, private val delegate: Call<T>): Call<T> {

        override fun execute(): Response<T> = delegate.execute()

        override fun isExecuted(): Boolean = delegate.isExecuted()

        override fun cancel() {
            delegate.cancel()
        }

        override fun isCanceled(): Boolean = delegate.isCanceled()

        override fun clone(): Call<T> = ExecutorCallbackCall(callbackExecutor, delegate.clone())

        override fun request(): FdRequest = delegate.request()

        override fun enqueue(callback: Callback<T>) {
            delegate.enqueue(object: Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    callbackExecutor.execute {
                        if (delegate.isCanceled()) {
                            callback.onFailure(this@ExecutorCallbackCall, IOException("Canceled"))
                        } else {
                            callback.onResponse(this@ExecutorCallbackCall, response)
                        }
                    }
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    callbackExecutor.execute {
                        callback.onFailure(this@ExecutorCallbackCall, t)
                    }
                }

            })
        }

    }
}