package com.yewapp.ui.modules.profile.fragment.mypoints.filteractivity

import android.widget.CompoundButton
import com.yewapp.data.network.api.mypoint.MyPointsHistoryDetails

class ItemMyPointsFilterViewModel(
    val myPointsHistoryDetails: MyPointsHistoryDetails,
    val listener: OnFilterItemClickListener
) {


    fun onCheckedChange(button: CompoundButton?, check: Boolean) {
        myPointsHistoryDetails.isSelected = check
        if (check) {
            listener.onClickItem(myPointsHistoryDetails)
        } else {
            return listener.onRemoveItem(myPointsHistoryDetails)
        }
    }
    interface OnFilterItemClickListener {
        fun onClickItem(item: MyPointsHistoryDetails)
        fun onRemoveItem(removeItem:MyPointsHistoryDetails)
    }
}