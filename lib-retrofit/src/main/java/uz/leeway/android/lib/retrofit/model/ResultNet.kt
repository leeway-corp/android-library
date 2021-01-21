package uz.leeway.android.lib.retrofit.model

sealed class ResultNet<out T, out U> {

    /** None ... */
    object None : ResultNet<Nothing, Nothing>()

    /** Loading ... */
    object Loading : ResultNet<Nothing, Nothing>()

    /** Success ... */
    sealed class Success<T> : ResultNet<T, Nothing>() {

        abstract val value: T

        override fun toString() = "Success($value)"

        class Value<T>(override val value: T) : Success<T>()

        data class HttpResponse<T>(
            override val value: T,
            override val statusCode: Int,
            override val statusMessage: String? = null,
            override val url: String? = null
        ) : Success<T>(), uz.leeway.android.lib.retrofit.model.HttpResponse
    }

    /** Failure ... */
    sealed class Failure<E : Throwable>(open val error: E? = null) : ResultNet<Nothing, Nothing>() {

        override fun toString() = "Failure($error)"

        class Error(override val error: Throwable) : Failure<Throwable>(error)

        class HttpError(override val error: HttpException) : Failure<HttpException>(),
            HttpResponse {

            override val statusCode: Int get() = error.statusCode

            override val statusMessage: String? get() = error.statusMessage

            override val url: String? get() = error.url
        }
    }

    fun isLoading() = this == Loading

    fun isNotLoading() = !isLoading()
}

typealias EmptyResult = ResultNet<Nothing, Nothing>

