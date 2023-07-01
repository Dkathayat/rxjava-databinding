package com.yewapp.ui.modules.addchallenge.addchallengedetails.adapter

import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.yewapp.data.network.api.addmodelequipment.EquipmentData
import com.yewapp.data.network.api.sports.Sport

class ItemChallengeSportsEquipmentViewModel(
    var item: EquipmentData,
    val listener: OnItemClickListener,
    var index: Int
) :
    ViewModel() {

//    var itemSelected = ObservableField<Boolean>(false)
//
//    init {
//        //  tittle.set(item.name)
//        itemSelected.set(item.isSelected)
//    }

    interface OnItemClickListener {
        fun onClickItem(item: EquipmentData, index: Int)
    }

    fun onItemClick(view: View) {
        item.isSelected = !item.isSelected
        listener.onClickItem(item, index)
    }

}