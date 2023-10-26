package com.geetgobindingh.githubrepoapp.widgets.outlineviewprovider

import android.graphics.Outline
import android.os.Build
import android.view.View
import android.view.ViewOutlineProvider
import androidx.annotation.RequiresApi
import com.geetgobindingh.githubrepoapp.utils.view.ViewUtils

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
class CardViewOutlineProvider(
    private val marginInDp: Int,
    private val radiusInDp: Int,
    private val elevationInDp: Int
) :
    ViewOutlineProvider() {
    override fun getOutline(
        view: View,
        outline: Outline
    ) {
        val margin: Int = ViewUtils.convertDpToPixel(marginInDp.toFloat(), view.context)
        val radius: Int = ViewUtils.convertDpToPixel(radiusInDp.toFloat(), view.context)
        val elevation: Int = ViewUtils.convertDpToPixel(elevationInDp.toFloat(), view.context)
        outline.setRoundRect(
            0,
            margin,
            view.width,
            view.height - margin,
            radius.toFloat()
        )
        view.elevation = elevation.toFloat()
    }

}
