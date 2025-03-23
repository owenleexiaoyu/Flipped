package life.lixiaoyu.flipped.net

import life.lixiaoyu.flipped.net.interfaces.ResponseBody
import java.lang.RuntimeException
import java.lang.reflect.Method
import java.lang.reflect.Type

abstract class HttpServiceMethod<ResponseT, ReturnT>(
    val requestFactory: RequestFactory,
    val callFactory: Call.Factory,
    val responseConverter: Converter<ResponseBody, ResponseT>
): ServiceMethod<ReturnT>() {

    companion object {
        fun <ResponseT, ReturnT> parseAnnotations(
            flippedNet: FlippedNet,
            method: Method,
            requestFactory: RequestFactory
        ): HttpServiceMethod<ResponseT, ReturnT> {
            val annotations = method.annotations
            val adapterType: Type = method.genericReturnType
            val callAdapter: CallAdapter<ResponseT, ReturnT> = createCallAdapter(flippedNet, method, adapterType, annotations)
            val responseType = callAdapter.responseType()
            val responseConverter: Converter<ResponseBody, ResponseT> = createResponseConverter(flippedNet, method, responseType, annotations)
            val callFactory = flippedNet.callFactory
            return CallAdapted(requestFactory, callFactory, responseConverter, callAdapter)
        }

        private fun <ResponseT, ReturnT> createCallAdapter(
            flippedNet: FlippedNet,
            method: Method,
            returnType: Type,
            annotations: Array<Annotation>
        ): CallAdapter<ResponseT, ReturnT> {
            try {
                return flippedNet.callAdapter(returnType, annotations) as CallAdapter<ResponseT, ReturnT>
            } catch (e: RuntimeException) {
                throw Utils.methodError(method, e, "Unable to create call adapter for $returnType")
            }
        }

        private fun <ResponseT> createResponseConverter(
            flippedNet: FlippedNet,
            method: Method,
            responseType: Type,
            annotations: Array<Annotation>
        ): Converter<ResponseBody, ResponseT> {
            try {
                return flippedNet.responseBodyConverter(responseType, annotations)
            } catch (e: RuntimeException) {
                throw Utils.methodError(method, e, "Unable to create converter for $responseType")
            }
        }
    }



    override fun invoke(args: Array<Any>?): ReturnT? {
        val call: Call<ResponseT> = callFactory.newCall(requestFactory.getRequest(args)) as Call<ResponseT>
        return adapt(call, args)
    }

    protected abstract fun adapt(call: Call<ResponseT>, args: Array<Any>?): ReturnT

    class CallAdapted<ResponseT, ReturnT>(
        requestFactory: RequestFactory,
        callFactory: Call.Factory,
        responseConverter: Converter<ResponseBody, ResponseT>,
        private val callAdapter: CallAdapter<ResponseT, ReturnT>
    ) : HttpServiceMethod<ResponseT, ReturnT>(requestFactory, callFactory, responseConverter) {

        override fun adapt(call: Call<ResponseT>, args: Array<Any>?): ReturnT {
            return callAdapter.adapt(call)
        }

    }
}