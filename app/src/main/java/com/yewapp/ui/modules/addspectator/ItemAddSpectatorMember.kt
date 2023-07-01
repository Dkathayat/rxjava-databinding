package com.yewapp.ui.modules.addspectator

import android.view.View
import android.widget.CompoundButton
import androidx.databinding.ObservableField
import com.yewapp.R
import com.yewapp.data.network.api.spectator.YewMember
import com.yewapp.utils.createNameWhenNoImage

class ItemAddSpectatorMember(
    val item: YewMember, val listener: OnSpectatorOptionClickListener, val position: Int
) {

    var name = ObservableField<String>("")
    var shortName = ObservableField<String>("")
    var cityCountry = ObservableField<String>("")
    var isImageAvailable = ObservableField<Boolean>(false)

    init {
        if (!item.fullName.isNullOrEmpty())
            name.set(item.fullName) else name.set(item.firstName)
        if (!item.city.isNullOrEmpty() && !item.country.isNullOrEmpty()) cityCountry.set("${item.city}, ${item.country}")

        if (!item.profileImage.isNullOrEmpty()) {
            isImageAvailable.set(true)
        } else {
            isImageAvailable.set(false)
            shortName.set(createNameWhenNoImage(item.fullName ?: item.firstName ?: "YW"))
        }
    }


    fun onCheckedChange(button: CompoundButton?, check: Boolean) {
        item.isSelected = check
        listener.onClickItem(item, position)
    }

    interface OnSpectatorOptionClickListener {
        fun onClickItem(item: YewMember, position: Int)
        fun onChatClicked(item: YewMember, position: Int)
//        fun onOptionsClicked(item: YewMember, position: Int)
    }


    fun onItemClick(view: View) {
        when (view.id) {
            R.id.ivChat -> {
                listener.onChatClicked(item, position)
            }
            R.id.ivOptions -> {

            }
        }
    }

}