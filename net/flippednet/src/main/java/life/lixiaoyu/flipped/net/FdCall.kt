package life.lixiaoyu.flipped.net

class FdCall<T>(
    val delegate: Call<T>,

) : Call<T> {
    override fun execute(): Response<T> {
        TODO("Not yet implemented")
    }

    override fun isExecuted(): Boolean {
        TODO("Not yet implemented")
    }

    override fun cancel() {
        TODO("Not yet implemented")
    }

    override fun isCanceled(): Boolean {
        TODO("Not yet implemented")
    }

    override fun clone(): Call<T> {
        TODO("Not yet implemented")
    }

    override fun request(): FdRequest {
        TODO("Not yet implemented")
    }

    override fun enqueue(callback: Callback<T>) {
        TODO("Not yet implemented")
    }

    fun getResponseWithInterceptorChain(): Response<*> {

    }
}