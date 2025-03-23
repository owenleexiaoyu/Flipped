package life.lixiaoyu.flipped.net

import java.io.IOException
import java.lang.reflect.Method
import kotlin.jvm.Throws

abstract class ParameterHandler<T> {

    @Throws(IOException::class)
    abstract fun apply(builder: FdRequest.Builder, value: T?)

    fun iterable(): ParameterHandler<Iterable<T>> {
        return object: ParameterHandler<Iterable<T>>() {

            override fun apply(builder: FdRequest.Builder, value: Iterable<T>?) {
                value ?: return // Skip null values.
                for (eachValue in value) {
                    this@ParameterHandler.apply(builder, eachValue)
                }
            }
        }
    }

    class Path<T>(
        private val method: Method,
        private val p: Int,
        private val name: String,
        private val valueConverter: Converter<T, String>,
        private val encoded: Boolean,
    ): ParameterHandler<T>() {

        override fun apply(builder: FdRequest.Builder, value: T?) {
            value ?: throw Utils.parameterError(method, p, "Path parameter [$name] value must not be null.")
            val convertedValue = valueConverter.convert(value) ?: throw Utils.parameterError(method, p, "Path parameter [$name] converted value must not be null.")
            builder.addPathParam(name, convertedValue, encoded)
        }

    }

    class Query<T>(
        private val name: String,
        private val valueConverter: Converter<T, String>,
        private val encoded: Boolean,
    ): ParameterHandler<T>() {

        override fun apply(builder: FdRequest.Builder, value: T?) {
            value ?: return // Skip null values.
            val queryValue = valueConverter.convert(value) ?: return // Skip converted but null values
            builder.addQueryParam(name, queryValue, encoded)
        }

    }

    class Field<T>(
        private val name: String,
        private val valueConverter: Converter<T, String>,
        private val encoded: Boolean,
    ): ParameterHandler<T>() {

        override fun apply(builder: FdRequest.Builder, value: T?) {
            value ?: return // Skip null values.
            val fieldValue = valueConverter.convert(value) ?: return // Skip converted but null values
            builder.addFormField(name, fieldValue, encoded)
        }

    }
}