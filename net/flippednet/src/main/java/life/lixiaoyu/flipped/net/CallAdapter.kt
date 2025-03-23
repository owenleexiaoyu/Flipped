package life.lixiaoyu.flipped.net

import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

interface CallAdapter<R, T> {

    fun responseType(): Type

    fun adapt(call: Call<R>): T

    abstract class Factory {

        abstract fun get(
            returnType: Type,
            annotations: Array<Annotation>,
            flippedNet: FlippedNet
        ): CallAdapter<*, *>?

        protected fun getParameterUpperBound(index: Int, type: ParameterizedType): Type? {
            return Utils.getParameterUpperBound(index, type)
        }

        protected fun getRawType(type: Type): Class<*>? {
            return Utils.getRawType(type)
        }
    }
}