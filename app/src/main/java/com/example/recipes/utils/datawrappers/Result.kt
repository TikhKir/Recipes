package com.example.recipes.utils.datawrappers

import java.lang.Exception

sealed class Result<out T> {
    data class Success<T>(val data: T): Result<T>()
    data class Error(val exception: Exception): Result<Nothing>()

    fun onSuccess(execute: (data: T) -> Unit): Result<T> {
        if (this is Success) execute(data)
        return this
    }

    fun onError(execute: (exception: Exception) -> Unit): Result<T> {
        if (this is Error) execute(exception)
        return this
    }

    fun <R> transformInside(transform: (result: T) -> R): Result<R> {
        return when (this) {
            is Success -> Success(transform(this.data))
            is Error -> this
        }
    }
}
