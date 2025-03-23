package life.lixiaoyu.flipped.net

import life.lixiaoyu.flipped.net.interfaces.RawResponse

interface Interceptor {

    fun intercept(chain: Chain): RawResponse

    interface Chain {

        fun request(): FdRequest

        fun proceed(request: FdRequest): RawResponse
    }
}

