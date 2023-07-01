package com.yewapp.ui.modules.profile.fragment.bottomsheet

import android.view.View
import android.widget.CompoundButton
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.yewapp.R
import com.yewapp.data.network.api.mypoint.MyPointsHistoryDetails
import com.yewapp.data.network.api.profile.FilterData


class ItemBottomSheetFilterViewModel(
    val item: FilterData,
    val listener: OnItemClickListener
):ViewModel() {


    fun onItemClick(view: View){
        listener.onClickItem(item)
    }

    fun onCheckedChange(button: CompoundButton?, check: Boolean) {
        item.isSelected = check
        if (check) {
            listener.onClickItem(item)
        } else {
            return listener.onRemoveItem(item)
        }
    }


    interface OnItemClickListener {
        fun onClickItem(item: FilterData)
        fun onRemoveItem(removeItem: FilterData)
    }
}
