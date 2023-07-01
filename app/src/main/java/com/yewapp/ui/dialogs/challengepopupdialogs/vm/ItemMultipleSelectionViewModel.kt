package com.yewapp.ui.dialogs.challengepopupdialogs.vm

import android.widget.CompoundButton
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.yewapp.data.network.api.createchallenge.StaticMultipleSelection

class ItemMultipleSelectionViewModel(
    val item: StaticMultipleSelection,
    val listener: (Int, Boolean) -> Unit,
    val index: Int
) : ViewModel() {
    var itemChecked = ObservableField<Boolean>(false)

    init {
        itemChecked.set(item.status)
    }


    fun onCheckedChange(button: CompoundButton?, check: Boolean) {
        if (check) {
//            button?.isChecked = true
            listener(index, true)
            //  listener.onAddItem(item)
        } else {
//            button?.isChecked = true

            listener(index, false)
            //return listener.onRemoveItem(item)
        }
    }
}