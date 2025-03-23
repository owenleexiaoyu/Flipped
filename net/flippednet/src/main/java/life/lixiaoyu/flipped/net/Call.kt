package life.lixiaoyu.flipped.net

import java.io.IOException
import kotlin.jvm.Throws

/**
 * An invocation of a Retrofit method that sends a request to a webserver and returns a response.
 * Each call yields its own HTTP request and response pair. Use {@link #clone} to make multiple
 * calls with the same parameters to the same webserver; this may be used to implement polling or to
 * retry a failed call.
 *
 * <p>Calls may be executed synchronously with {@link #execute}, or asynchronously with {@link
 * #enqueue}. In either case the call can be canceled at any time with {@link #cancel}. A call that
 * is busy writing its request or reading its response may receive a {@link IOException}; this is
 * working as designed.
 *
 * @param <T> Successful response body type.
 */
interface Call<T> : Cloneable {

    /**
     * Synchronously send the request and return its response.
     *
     * @throws IOException if a problem occurred talking to the server.
     * @throws RuntimeException (and subclasses) if an unexpected error occurs creating the request or
     *     decoding the response.
     */
    @Throws(IOException::class)
    fun execute(): Response<T>

    /**
     * Asynchronously send the request and notify `callback` of its response or if an error
     * occurred talking to the server, creating the request, or processing the response.
     */
    fun enqueue(callback: Callback<T>)

    /**
     * Returns true if this call has been either [execute] executed or [enqueue] enqueued.
     * It is an error to execute or enqueue a call more than once.
     */
    fun isExecuted(): Boolean

    /**
     * Cancel this call. An attempt will be made to cancel in-flight calls, and if the call has not
     * yet been executed it never will be.
     */
    fun cancel()

    /** True if [cancel] was called. */
    fun isCanceled(): Boolean

    /**
     * Create a new, identical call to this one which can be enqueued or executed even if this call
     * has already been.
     */
    public override fun clone(): Call<T>

    /** The original HTTP request. */
    fun request(): FdRequest

    /**
     * Returns a timeout that spans the entire call: resolving DNS, connecting, writing the request
     * body, server processing, and reading the response body. If the call requires redirects or
     * retries all must complete within one timeout period.
     */
//    fun timeout(): Timeout?

    interface Factory {
        fun newCall(request: FdRequest): Call<*>
    }
}