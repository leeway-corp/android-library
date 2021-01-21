@file:Suppress("unused")

package uz.leeway.android.lib.retrofit.async

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import uz.leeway.android.lib.retrofit.model.AbstractError
import uz.leeway.android.lib.retrofit.model.ResultNet
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class ResultAdapterFactory private constructor() : CallAdapter.Factory() {
    companion object {
        fun create() = ResultAdapterFactory()
    }

    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {

        // suspend functions wrap the response type in `Call`
        if (Call::class.java != getRawType(returnType)) {
            return null
        }

        // check first that the return type is `ParameterizedType`
        check(returnType is ParameterizedType) {
            "return type must be parameterized as Call<ResultNet<<Foo>> or Call<ResultNet<out Foo>>"
        }

        // get the response type inside the `Call` type
        val responseType = getParameterUpperBound(0, returnType)
        // if the response type is not ApiResponse then we can't handle this type, so we return null
        if (getRawType(responseType) != ResultNet::class.java) {
            return null
        }

        // the response type is ApiResponse and should be parameterized
        check(responseType is ParameterizedType) { "Response must be parameterized as ResultNet<Foo> or ResultNet<out Foo>" }

        val successBodyType = getParameterUpperBound(0, responseType)
        val errorBodyType = getParameterUpperBound(1, responseType)

        val errorBodyConverter =
            retrofit.nextResponseBodyConverter<AbstractError>(null, errorBodyType, annotations)

        return ResultCallAdapter<Any?, AbstractError>(successBodyType, errorBodyConverter)

    }
}

private class ResultCallAdapter<R, E : AbstractError>(
    private val type: Type,
    private val converter: Converter<ResponseBody, E>
) : CallAdapter<R, Call<ResultNet<R, E>>> {

    override fun responseType() = type

    override fun adapt(call: Call<R>): Call<ResultNet<R, E>> = ResultCall(call, converter)
}