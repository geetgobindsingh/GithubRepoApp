package com.geetgobindingh.githubrepoapp.utils.binding

import android.os.Build
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.geetgobindingh.githubrepoapp.widgets.outlineviewprovider.CardViewOutlineProvider
import com.geetgobindingh.githubrepoapp.widgets.recyclerviewadapter.ListViewModel
import com.geetgobindingh.githubrepoapp.widgets.recyclerviewadapter.RecyclerViewAdapter
import com.geetgobindingh.githubrepoapp.widgets.recyclerviewadapter.ViewProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.text.NumberFormat
import java.util.*

class BindingUtils {

    companion object {
        @JvmStatic
        @BindingAdapter("items", "viewProvider")
        fun bindDataInRecyclerView(
            recyclerView: RecyclerView,
            items: List<ListViewModel>?,
            viewProvider: ViewProvider
        ) {
            if (recyclerView.adapter == null || recyclerView.adapter !is RecyclerViewAdapter) {
                recyclerView.adapter = RecyclerViewAdapter(viewProvider)
            }
            items?.let {
                (recyclerView.adapter as RecyclerViewAdapter).updateData(items)
            }
        }

        @JvmStatic
        @BindingAdapter("app:color")
        fun bindTextColor(textView: TextView, color: Int) {
            textView.setTextColor(ContextCompat.getColor(textView.context, color))
        }

        @JvmStatic
        @BindingAdapter("app:visibility")
        fun bindViewVisibility(view: View, visibility: Boolean) {
            if (visibility) {
                view.visibility = View.VISIBLE
            } else {
                view.visibility = View.GONE
            }
        }

        @JvmStatic
        @BindingAdapter("app:visibility")
        fun bindSwipeRefreshLayoutVisibility(
            swipeRefreshLayout: SwipeRefreshLayout,
            visibility: Boolean
        ) {
            if (visibility) {
                swipeRefreshLayout.visibility = View.VISIBLE
            } else {
                swipeRefreshLayout.visibility = View.GONE
            }
        }

        @JvmStatic
        @BindingAdapter("app:refreshing")
        fun bindViewVisibility(swipeRefreshLayout: SwipeRefreshLayout, refreshing: Boolean) {
            swipeRefreshLayout.isRefreshing = refreshing
        }

        @JvmStatic
        @BindingAdapter("app:circularImageUrl")
        fun bindCircularImageView(imageView: ImageView, url: String) {
            Glide.with(imageView.context)
                .load(url)
                .apply(RequestOptions().circleCrop())
                .into(imageView)
        }

        @JvmStatic
        @BindingAdapter("app:formattedInteger")
        fun bindCircularImageView(textView: TextView, text: Number) {
            textView.text = NumberFormat.getNumberInstance(Locale.US).format(text)
        }

        @JvmStatic
        @BindingAdapter("app:backgroundElevation")
        fun bindElevatedView(view: View, elevation: Boolean) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (elevation) {
                    view.outlineProvider = CardViewOutlineProvider(0, 0, 5)
                } else {
                    view.outlineProvider = CardViewOutlineProvider(0, 0, 0)
                }
                view.clipToOutline = true
            }
        }
    }

}