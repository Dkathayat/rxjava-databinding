package com.yewapp.ui.modules.videofeeds.vm

import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.yewapp.data.network.api.follower.MyFollowers

class ItemVideoFeedFollowerViewModel(
    val item: MyFollowers,
    val listener: OnItemClickListener,
    var index: Int
) :
    ViewModel() {

    var name = ObservableField<String>()
    var distance = ObservableField<String>()
    var description = ObservableField<String>()
    var stateAndCountry = ObservableField<String>()
    var followVisibility = ObservableField<Boolean>(false)
    val profileImageVisibility = ObservableField<Boolean>(true)
    val profileImage = ObservableField<String>("")
    val profileImageName = ObservableField<String>("")

    init {
        if (item.following) {
            followVisibility.set(true)
        } else {
            followVisibility.set(false)
        }

        if (!item.city.isNullOrEmpty()) {
            if (!item.country.isNullOrEmpty()) {
                stateAndCountry.set("${item.city},${item.country}")
            }
        }
        if (item.first_name.isNotEmpty()) {
            name.set("${item.first_name} ${item.last_name}")
        }
        if (item.image.isNullOrEmpty()) {
            profileImageVisibility.set(false)
            if (item.first_name.isNotBlank()) {
                var fName = item.first_name.substring(0, 1).uppercase()
                var lName = item.last_name.substring(0, 1).uppercase()
                profileImageName.set(fName + lName)

            } else {
                profileImage.set("")
                profileImageVisibility.set(true)
            }
        } else {
            profileImageVisibility.set(true)
            // profileImage.set()
        }

    }

    interface OnItemClickListener {
        fun onClickItem(item: MyFollowers, index: Int)
    }

    fun onItemClick(view: View) {
        listener.onClickItem(item, index)
    }

}