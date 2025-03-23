package life.lixiaoyu.flipped.net

import life.lixiaoyu.flipped.net.interfaces.RequestBody
import life.lixiaoyu.flipped.net.interfaces.ResponseBody
import java.lang.reflect.Type

class BuiltInConverters: Converter.Factory() {

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<Annotation>,
        flippedNet: FlippedNet
    ): Converter<ResponseBody, *>? {
        return super.responseBodyConverter(type, annotations, flippedNet)
    }

    override fun requestBodyConverter(
        type: Type,
        annotations: Array<Annotation>,
        flippedNet: FlippedNet
    ): Converter<*, RequestBody>? {
        return super.requestBodyConverter(type, annotations, flippedNet)
    }

    override fun stringConverter(
        type: Type,
        annotations: Array<Annotation>,
        flippedNet: FlippedNet
    ): Converter<*, String>? {
        return super.stringConverter(type, annotations, flippedNet)
    }

    object ToStringConverter: Converter<Any, String> {

        override fun convert(value: Any): String? {
            return value.toString()
        }

    }


}