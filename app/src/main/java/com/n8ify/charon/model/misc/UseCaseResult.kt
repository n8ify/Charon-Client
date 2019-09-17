package com.n8ify.charon.model.misc

sealed class UseCaseResult<out T : Any> {

    class Success<out T : Any>(val result : T) : UseCaseResult<T>()
    class Error(val t : Throwable) : UseCaseResult<Nothing>()

}