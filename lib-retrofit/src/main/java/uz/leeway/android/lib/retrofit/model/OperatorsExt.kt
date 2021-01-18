@file:Suppress("unused")

package uz.leeway.android.lib.retrofit.model

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

fun <T> AsyncResult<T>.isSuccess(): Boolean = this is AsyncResult.Success

fun <T> AsyncResult<T>.asSuccess(): AsyncResult.Success<T> = this as AsyncResult.Success<T>

@OptIn(ExperimentalContracts::class)
fun <T> AsyncResult<T>.isFailure(): Boolean {
    contract {
        returns(true) implies (this@isFailure is AsyncResult.Failure<*>)
    }
    return this is AsyncResult.Failure<*>
}

fun <T> AsyncResult<T>.asFailure(): AsyncResult.Failure<*> = this as AsyncResult.Failure<*>

fun <T, R> AsyncResult<T>.map(transform: (value: T) -> R): AsyncResult<R> = when(this) {
    is AsyncResult.Success -> AsyncResult.Success.Value(transform(value))
    is AsyncResult.Failure<*> -> this
}

fun <T, R> AsyncResult<T>.flatMap(transform: (result: AsyncResult<T>) -> AsyncResult<R>): AsyncResult<R> =
    transform(this)