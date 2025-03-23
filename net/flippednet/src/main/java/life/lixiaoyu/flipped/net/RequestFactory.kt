package life.lixiaoyu.flipped.net

import life.lixiaoyu.flipped.net.http.GET
import life.lixiaoyu.flipped.net.http.Headers
import life.lixiaoyu.flipped.net.http.POST
import life.lixiaoyu.flipped.net.http.Path
import java.lang.IllegalStateException
import java.lang.reflect.Method
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class RequestFactory {

    companion object {
        fun parseAnnotations(flippedNet: FlippedNet, method: Method): RequestFactory {
            val factory = RequestFactory()

            parseMethodAnnotations(factory, method)

            parseMethodReturnType(factory, method)

            parseMethodParamters(flippedNet, factory, method)

            return factory
        }

        private fun parseMethodParamters(flippedNet: FlippedNet, factory: RequestFactory, method: Method) {
            val parameterAnnotations = method.parameterAnnotations
            for (index in parameterAnnotations.indices) {
                val annotations = parameterAnnotations[index]
                require(annotations.size <= 1)  {
                    "Field can only have one Annotation in max"
                }
                if (annotations.isNotEmpty()) {
                    val annotation = annotations[0]
                    if (annotation is Path) {
                        val name = annotation.value
                        // 先支持基本数据类型，不支持其他类型
                        val type = method.
                        val converter = flippedNet.stringConverter()
                    }
                }
            }
        }

        private fun parseMethodReturnType(factory: RequestFactory, method: Method) {
            if (method.returnType != Call::class.java) {
                throw IllegalStateException("Method return type must be Call")
            }
            val genericReturnType = method.genericReturnType
            if (genericReturnType is ParameterizedType) {
                val actualTypeArguments = genericReturnType.actualTypeArguments
                require(actualTypeArguments.size == 1) { "Method can only have one generic type" }
                factory.returnType = actualTypeArguments[0]
            } else {
                throw IllegalStateException("Method must have generic type")
            }
        }

        private fun parseMethodAnnotations(factory: RequestFactory, method: Method) {
            val annotations = method.annotations
            for (annotation in annotations) {
                when (annotation) {
                    is GET -> {
                        factory.relativeUrl = annotation.value
                        factory.httpMethod = "GET"
                    }

                    is POST -> {
                        factory.relativeUrl = annotation.value
                        factory.httpMethod = "POST"
                    }

                    is Headers -> {
                        val headersArray = annotation.value
                        for (header in headersArray) {
                            val colon = header.indexOf(":")
                            check(colon == 0 || colon == -1) {
                                "@Headers value must be in the form [name: value]"
                            }
                            val name = header.substring(0, colon)
                            val value = header.substring(colon + 1).trim()
                            factory.headers[name] = value
                        }
                    }

                    else -> {
                        throw IllegalStateException("Cannot handle method annotation: ${annotation.javaClass.name}")
                    }
                }

            }
            require(factory.httpMethod.isNotEmpty()) {
                "Http method is empty"
            }
        }
    }

    private var method: Method
    private var baseUrl: String
    private var relativeUrl: String  = ""
    private var httpMethod: String = ""
    private var headers: MutableMap<String, String> = mutableMapOf()
    private var contentType: String
    private var hasBody: Boolean
    private var returnType: Type


    fun getRequest(args: Array<Any>?): FdRequest {

    }
}