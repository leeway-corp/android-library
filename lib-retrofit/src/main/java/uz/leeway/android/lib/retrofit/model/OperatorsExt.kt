@file:Suppress("unused")

package uz.leeway.android.lib.retrofit.model

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

fun <T, E : AbstractError> ResultNet<T, E>.isSuccess(): Boolean = this is ResultNet.Success

fun <T, E : AbstractError> ResultNet<T, E>.asSuccess(): ResultNet.Success<T> =
    this as ResultNet.Success<T>

@OptIn(ExperimentalContracts::class)
fun <T, E : AbstractError> ResultNet<T, E>.isFailure(): Boolean {
    contract {
        returns(true) implies (this@isFailure is ResultNet.Failure<*>)
    }
    return this is ResultNet.Failure<*>
}

fun <T, E : AbstractError> ResultNet<T, E>.asFailure(): ResultNet.Failure<*> =
    this as ResultNet.Failure<*>

fun <T, R, E : AbstractError> ResultNet<T, E>.map(transform: (value: T) -> R): ResultNet<R, E> =
    when (this) {
        is ResultNet.None -> this
        is ResultNet.Loading -> this
        is ResultNet.Success -> ResultNet.Success.Value(transform(value))
        is ResultNet.Failure<*> -> this
    }

fun <T, R, E : AbstractError> ResultNet<T, E>.flatMap(transform: (result: ResultNet<T, E>) -> ResultNet<R, E>): ResultNet<R, E> =
    transform(this)