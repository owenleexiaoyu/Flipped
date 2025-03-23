package life.lixiaoyu.flipped.net

import life.lixiaoyu.flipped.net.interfaces.RawResponse
import life.lixiaoyu.flipped.net.interfaces.ResponseBody


class Response<T> private constructor(
    private val rawResponse: RawResponse?,
    private val body: T?,
    private val errorBody: ResponseBody?,
) {
    fun raw(): RawResponse? {
        return rawResponse
    }

    /**
     * HTTP status code
     */
    val code: Int
        get() {
            return 0 // TODO
        }

    val message: String
        get() {
            return "" // TODO
        }

    fun body(): T? {
        return null // TODO
    }

    val isSuccessful: Boolean
        get() {
            return true // TODO
        }

    companion object {
        fun <T> success(body: T?, rawResponse: RawResponse): Response<T> {
            return Response(rawResponse, body, null)
        }

        fun <T> error(body: ResponseBody, rawResponse: RawResponse): Response<T> {
            return Response(rawResponse, null, body)
        }
    }
}