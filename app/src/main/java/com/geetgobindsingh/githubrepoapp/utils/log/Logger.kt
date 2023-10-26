package com.geetgobindingh.githubrepoapp.utils.log

import org.jetbrains.annotations.NonNls
import timber.log.Timber

object Logger {

    fun d(t: Throwable?, @NonNls message: String?, vararg args: Any?) {
        Timber.d(t, message, *args)
    }

    fun d(@NonNls message: String?, vararg args: Any?) {
        Timber.d(message, *args)
    }

    fun d(throwable: Throwable) {
        Timber.d(throwable)
    }

    fun e(t: Throwable?, @NonNls message: String?, vararg args: Any?) {
        Timber.e(t, message, *args)
    }

    fun e(@NonNls message: String?, vararg args: Any?) {
        Timber.e(message, *args)
    }

    fun e(throwable: Throwable) {
        Timber.e(throwable)
    }

    fun i(t: Throwable?, @NonNls message: String?, vararg args: Any?) {
        Timber.i(t, message, *args)
    }

    fun i(@NonNls message: String?, vararg args: Any?) {
        Timber.i(message, *args)
    }

    fun i(throwable: Throwable) {
        Timber.i(throwable)
    }

    fun w(t: Throwable?, @NonNls message: String?, vararg args: Any?) {
        Timber.w(t, message, *args)
    }

    fun w(@NonNls message: String?, vararg args: Any?) {
        Timber.w(message, *args)
    }

    fun w(throwable: Throwable) {
        Timber.w(throwable)
    }

    fun wtf(t: Throwable?, @NonNls message: String?, vararg args: Any?) {
        Timber.wtf(t, message, *args)
    }

    fun wtf(@NonNls message: String?, vararg args: Any?) {
        Timber.wtf(message, *args)
    }

    fun wtf(throwable: Throwable) {
        Timber.wtf(throwable)
    }
}
