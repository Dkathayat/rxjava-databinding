package com.yewapp.ui.modules.addchallenge.addchallengesportstype.vm

import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.yewapp.data.network.api.sports.Sport

class ItemSportListViewModel(
    val item: Sport,
    val listener: OnItemClickListener,
    var index: Int
) :
    ViewModel() {

    var itemSelected = ObservableField<Boolean>(false)

    init {
        //  tittle.set(item.name)
    }

    interface OnItemClickListener {
        fun onClickItem(item: Sport, index: Int)
    }

    fun onItemClick(view: View) {
        listener.onClickItem(item, index)
    }

}