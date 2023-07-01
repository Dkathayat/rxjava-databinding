package com.yewapp.ui.modules.dashboard.fragment.feeds.vm

import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.yewapp.data.network.api.feed.FavoriteList

class ItemFavoriteUserViewModel(
    val item: FavoriteList,
    val listener: OnItemClickListener,
    var index: Int
) :
    ViewModel() {

    var image = ObservableField<String>()
    val profileImageVisibility = ObservableBoolean(true)
    val profileImageName = ObservableField<String>("")


    init {
        if (item.profileImage.isNullOrEmpty()) {
            profileImageVisibility.set(false)
            if (!item.fullName.isNullOrBlank()) {
                val arr: Array<String> = item.fullName.split(" ").toTypedArray()
                val fname = arr[0].substring(0, 1).uppercase()
                var lname = ""
                if (arr[1].isNotEmpty()) {
                    lname = arr[1].substring(0, 1).uppercase()
                    profileImageName.set(fname + lname)
                } else {
                    lname = arr[0].substring(1, 2).uppercase()
                    profileImageName.set(fname + lname)
                }
            } else {
                image.set("")
                profileImageVisibility.set(true)
            }
        } else {
            profileImageVisibility.set(true)
            image.set(item.profileImage)
        }

    }

    interface OnItemClickListener {
        fun onClickItem(item: FavoriteList)
    }

    fun onItemClick(view: View) {
        listener.onClickItem(item)
    }

}