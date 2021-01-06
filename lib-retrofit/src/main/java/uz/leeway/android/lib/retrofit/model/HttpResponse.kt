package uz.leeway.android.lib.retrofit.model

interface HttpResponse {

    val statusCode: Int

    val statusMessage: String?

    val url: String?
}