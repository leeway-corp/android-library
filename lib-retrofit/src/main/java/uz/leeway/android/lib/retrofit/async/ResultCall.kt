package uz.leeway.android.lib.retrofit.async

import okhttp3.ResponseBody
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Converter
import retrofit2.Response
import uz.leeway.android.lib.retrofit.model.AbstractError
import uz.leeway.android.lib.retrofit.model.HttpException
import uz.leeway.android.lib.retrofit.model.NoConnectionException
import uz.leeway.android.lib.retrofit.model.ResultNet
import java.io.IOException

internal class ResultCall<T, E : AbstractError>(
    proxy: Call<T>,
    private val converter: Converter<ResponseBody, E>
) : CallDelegate<T, ResultNet<T, E>>(proxy) {

    override fun enqueueImpl(callback: Callback<ResultNet<T, E>>) {
        proxy.enqueue(ResultCallback(this, callback, converter))
    }

    override fun cloneImpl(): ResultCall<T, E> {
        return ResultCall(proxy.clone(), converter)
    }

    private class ResultCallback<T, E : AbstractError>(
        private val proxy: ResultCall<T, E>,
        private val callback: Callback<ResultNet<T, E>>,
        private val converter: Converter<ResponseBody, E>
    ) : Callback<T> {

        @Suppress("UNCHECKED_CAST")
        override fun onResponse(call: Call<T>, response: Response<T>) {

            val result: ResultNet<T, E> =
                if (response.isSuccessful) {
                    ResultNet.Success.HttpResponse(
                        value = response.body() as T,
                        statusCode = response.code(),
                        statusMessage = response.message(),
                        url = call.request().url.toString(),
                    )
                } else {
                    ResultNet.Failure.HttpError(
                        HttpException(
                            statusCode = response.code(),
                            statusMessage = findErrorBodyString(response.errorBody())?.errorMessage(),
                            url = call.request().url.toString(),
                        )
                    )
                }
            callback.onResponse(proxy, Response.success(result))
        }

        override fun onFailure(call: Call<T>, error: Throwable) {
            val result = when (error) {
                is NoConnectionException -> ResultNet.Failure.HttpError(
                    HttpException(
                        statusCode = HttpException.HTTP_STATUS_NO_CONNECTION,
                        statusMessage = "No connection",
                        url = call.request().url.toString(),
                    )
                )
                is retrofit2.HttpException -> ResultNet.Failure.HttpError(
                    HttpException(error.code(), error.message(), cause = error)
                )
                is IOException -> ResultNet.Failure.Error(error)
                else -> ResultNet.Failure.Error(error)
            }

            callback.onResponse(proxy, Response.success(result))
        }

        private fun findErrorBodyString(error: ResponseBody?): E? {
            return when {
                error == null -> null
                error.contentLength() == 0L -> null
                else -> try {
                    converter.convert(error)
                } catch (ex: Exception) {
//                Timber.e(ex)
                    null
                }
            }
        }
    }

    override fun timeout(): Timeout {
        return proxy.timeout()
    }
}