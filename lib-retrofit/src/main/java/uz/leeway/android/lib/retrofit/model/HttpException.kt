package uz.leeway.android.lib.retrofit.model

class HttpException(
    val statusCode: Int,
    val statusMessage: String? = null,
    val url: String? = null,
    cause: Throwable? = null
) : Exception(null, cause) {

    /** Called for 401 responses */
    fun isUnauthenticated() = statusCode == HTTP_STATUS_UNAUTHENTICATED

    /** Called when server is down ... */
    fun isServerDown() = statusCode == HTTP_STATUS_SERVER_DOWN

    /** No Active connection ... */
    fun isNoConnection() = statusCode == HTTP_STATUS_NO_CONNECTION

    companion object {
        const val HTTP_STATUS_UNAUTHENTICATED = 401
        const val HTTP_STATUS_SERVER_DOWN = 503
        const val HTTP_STATUS_NO_CONNECTION = 999
    }
}