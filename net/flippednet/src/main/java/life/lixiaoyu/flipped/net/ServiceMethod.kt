package life.lixiaoyu.flipped.net

import java.lang.reflect.Method

abstract class ServiceMethod<T> {

    companion object {
        fun parseAnnotations(flippedNet: FlippedNet, method: Method): ServiceMethod<*> {
            val requestFactory = RequestFactory.parseAnnotations(flippedNet, method)
            val returnType = method.genericReturnType
            if (Utils.hasUnresolvableType(returnType)) {
                throw Utils.methodError(method, "Method return type must not include a type variable or wildcard: $returnType")
            }
            return HttpServiceMethod.parseAnnotations(flippedNet, method, requestFactory)
        }
    }

    abstract fun invoke(args: Array<Any>?): T?
}