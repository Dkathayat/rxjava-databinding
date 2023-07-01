package com.yewapp.ui.modules.addchallenge.challengeroutes.popular.vm

import android.os.Build
import android.text.Html
import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.yewapp.data.network.api.routes.Route

class ItemPopularRouteViewModel(
    val item: Route,
    val listener: OnItemClickListener,
    var index: Int
) :
    ViewModel() {

    var distance = ObservableField<String>()
    var description = ObservableField<String>()

    init {
        distance.set(item.distance.toString() + " miles")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            description.set(
                Html.fromHtml(item.description, Html.FROM_HTML_MODE_COMPACT).toString()
            );
        } else {
            description.set(Html.fromHtml(item.description).toString())
        }
    }

    interface OnItemClickListener {
        fun onClickItem(item: Route)
    }

    fun onItemClick(view: View) {
        listener.onClickItem(item)
    }

}