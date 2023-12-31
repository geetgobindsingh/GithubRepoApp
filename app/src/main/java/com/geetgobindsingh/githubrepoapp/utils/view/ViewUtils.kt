package com.geetgobindingh.githubrepoapp.utils.view

import android.content.Context

object ViewUtils {
    fun convertDpToPixel(dp: Float, context: Context): Int {
        val resources = context.resources
        val metrics = resources.displayMetrics
        val px = dp * (metrics.densityDpi / 160f)
        return Math.round(px)
    }
}