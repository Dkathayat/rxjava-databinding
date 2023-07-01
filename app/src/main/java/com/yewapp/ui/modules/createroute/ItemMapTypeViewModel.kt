package com.yewapp.ui.modules.createroute

import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.yewapp.data.network.api.routes.MapViewTypeModel

class ItemMapTypeViewModel(
    val item: MapViewTypeModel,
    val listener: ItemMapTypeViewModel.OnItemClickListener,
    val mapbox_access_token: String?
) : ViewModel() {


    var selectedMapImageUrl = ObservableField<String>(
        "https://api.mapbox.com/styles/v1/mapbox/satellite-streets-v11/static/[-77.043686,38.892035,-77.028923,38.904192]/400x400?access_token=${
            mapbox_access_token
        }"
    )

    init {
        selectedMapImageUrl.set(
            "https://api.mapbox.com/styles/v1/mapbox/${item.mapViewTypeUrl}/static/[${item.latLng}]/400x400?access_token=${mapbox_access_token}"
        )
    }


    interface OnItemClickListener {
        fun onClickItem(item: MapViewTypeModel)
    }

    fun onItemClick(view: View) {
        listener.onClickItem(item)
    }
}