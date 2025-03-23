package life.lixiaoyu.flipped.net

import life.lixiaoyu.flipped.net.interfaces.RequestBody
import life.lixiaoyu.flipped.net.interfaces.ResponseBody
import java.io.IOException
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import kotlin.jvm.Throws

interface Converter<F, T> {

    @Throws(IOException::class)
    fun convert(value: F): T?

    abstract class Factory {

        open fun responseBodyConverter(
            type: Type,
            annotations: Array<Annotation>,
            flippedNet: FlippedNet
        ): Converter<ResponseBody, *>? = null

        open fun requestBodyConverter(
            type: Type,
            annotations: Array<Annotation>,
            flippedNet: FlippedNet
        ): Converter<*, RequestBody>? = null

        open fun stringConverter(
            type: Type,
            annotations: Array<Annotation>,
            flippedNet: FlippedNet
        ): Converter<*, String>? = null

        protected fun getParameterUpperBound(index: Int, type: ParameterizedType): Type? {
            return Utils.getParameterUpperBound(index, type)
        }

        protected fun getRawType(type: Type): Class<*>? {
            return Utils.getRawType(type)
        }
    }
}