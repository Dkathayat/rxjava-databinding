package com.yewapp.ui.modules.editProfile.vm

import android.view.View
import android.widget.CompoundButton
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.yewapp.R
import com.yewapp.data.network.api.profile.SportType

class ItemSportTypeViewModel(
    val sportType: SportType,
    val position: Int,
    val listener: ItemSportTypeViewModel.OnItemClickListener
) : ViewModel() {

    var modelAdded = ObservableField<Boolean>(false)

    init {
        if (sportType.isModelAdded == true)
            modelAdded.set(false)
        else
            modelAdded.set(true)
    }

    interface OnItemClickListener {
        fun onAddItem(sportType: SportType, position: Int)

        //        fun onRemoveItem(sportType: SportType, position: Int)
        fun addSportsType(sportType: SportType, position: Int)
        fun editSportsType(sportType: SportType, position: Int)
        fun deleteSportsType(sportType: SportType, position: Int)
    }

    fun onCheckedChange(button: CompoundButton?, check: Boolean) {
        if (sportType.isModelAdded != true) {
            if (sportType.isChecked != check) {
                sportType.isChecked = check
                listener.onAddItem(sportType, position)
            }
        }

//        else {
//            listener.onRemoveItem(sportType, position)
//        }
    }

    fun onClick(view: View) {
        when (view.id) {
//            R.id.addSportsType -> listener.addSportsType(sportType, position)
            R.id.ivEditSportsType -> listener.editSportsType(sportType, position)
            R.id.deleteSportsType -> listener.deleteSportsType(sportType, position)
//            R.id.itemSportsMainLayout -> listener.onAddItem(sportType, position)

        }
    }
}