@file:Suppress("unused")

package uz.leeway.android.lib.retrofit.model

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

fun <T> Result<T>.isSuccess(): Boolean = this is Result.Success

fun <T> Result<T>.asSuccess(): Result.Success<T> = this as Result.Success<T>

@OptIn(ExperimentalContracts::class)
fun <T> Result<T>.isFailure(): Boolean {
    contract {
        returns(true) implies (this@isFailure is Result.Failure<*>)
    }
    return this is Result.Failure<*>
}

fun <T> Result<T>.asFailure(): Result.Failure<*> = this as Result.Failure<*>

fun <T, R> Result<T>.map(transform: (value: T) -> R): Result<R> = when(this) {
    is Result.Success -> Result.Success.Value(transform(value))
    is Result.Failure<*> -> this
}

fun <T, R> Result<T>.flatMap(transform: (result: Result<T>) -> Result<R>): Result<R> =
    transform(this)