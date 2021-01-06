package uz.leeway.android.lib.retrofit.async

import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uz.leeway.android.lib.retrofit.model.AsyncResult
import uz.leeway.android.lib.retrofit.model.HttpException
import java.io.IOException

internal class ResultCall<T>(proxy: Call<T>) : CallDelegate<T, AsyncResult<T>>(proxy) {

    override fun enqueueImpl(callback: Callback<AsyncResult<T>>) {
        proxy.enqueue(ResultCallback(this, callback))
    }

    override fun cloneImpl(): ResultCall<T> {
        return ResultCall(proxy.clone())
    }

    private class ResultCallback<T>(
        private val proxy: ResultCall<T>,
        private val callback: Callback<AsyncResult<T>>
    ) : Callback<T> {

        override fun onResponse(call: Call<T>, response: Response<T>) {
            val result: AsyncResult<T>
            if (response.isSuccessful) {
                @Suppress("UNCHECKED_CAST")
                result = AsyncResult.Success.HttpResponse(
                    value = response.body() as T,
                    statusCode = response.code(),
                    statusMessage = response.message(),
                    url = call.request().url.toString(),
                )
            } else {
                result = AsyncResult.Failure.HttpError(
                    HttpException(
                        statusCode = response.code(),
                        statusMessage = response.message(),
                        url = call.request().url.toString(),
                    )
                )
            }
            callback.onResponse(proxy, Response.success(result))
        }

        override fun onFailure(call: Call<T>, error: Throwable) {
            val result = when (error) {
                is retrofit2.HttpException -> AsyncResult.Failure.HttpError(
                    HttpException(error.code(), error.message(), cause = error)
                )
                is IOException -> AsyncResult.Failure.Error(error)
                else -> AsyncResult.Failure.Error(error)
            }

            callback.onResponse(proxy, Response.success(result))
        }
    }

    override fun timeout(): Timeout {
        return proxy.timeout()
    }
}