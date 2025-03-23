package life.lixiaoyu.flipped.net

import life.lixiaoyu.flipped.net.MediaType.Companion.toMediaType
import java.lang.IllegalArgumentException

class FdRequest(builder: Builder) {
    var url: String = builder.url
    var headers: List<Header> = builder.headers
    var httpMethod: HttpMethod = builder.httpMethod

    class Builder {
        var baseUrl: String = ""
        var relativeUrl: String = ""

        var url: String = ""
        val headers: MutableList<Header> = mutableListOf()
        var httpMethod: HttpMethod = HttpMethod.GET
        var contentType: MediaType? = null
        var hasBody: Boolean = false


        fun url(url: String): Builder {
            this.url = url
            return this
        }

        fun headers(headers: List<Header>): Builder {
            this.headers.clear()
            this.headers.addAll(headers)
            return this
        }

        fun addHeader(name: String, value: String) {
            if ("Content-Type".equals(name, ignoreCase = true)) {
                try {
                    contentType = value.toMediaType()
                } catch (e: IllegalArgumentException) {
                    throw IllegalArgumentException("Malformed content type: $value", e)
                }
            } else {
                val header = Header(name, value)
                headers.add(header)
            }
        }

        fun httpMethod(httpMethod: HttpMethod): Builder {
            this.httpMethod = httpMethod
            return this
        }

        fun addPathParam(name: String, value: String, encoded: Boolean) {

        }

        fun addQueryParam(name: String, queryValue: String, encoded: Boolean) {


        }

        fun build(): FdRequest {
            if (url.isEmpty()) {
                throw IllegalArgumentException("url of a request shouldn't be empty")
            }
            return FdRequest(this)
        }

        fun addFormField(name: String, fieldValue: String, encoded: Boolean) {


        }


    }

    enum class HttpMethod(val value: String) {
        GET("GET"),
        POST("POST")
    }
}