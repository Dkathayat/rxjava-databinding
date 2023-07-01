package com.yewapp.ui.modules.manageuser.vm

import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.yewapp.data.network.api.UserList
import com.yewapp.ui.modules.addchallenge.challengeroutes.ManageUserEnum

class ItemManageUserViewModel(
    val item: UserList,
    val listener: OnItemClickListener,
    var index: Int,
    var type: String
) : ViewModel() {
    var name = ObservableField<String>()
    var stateAndCountry = ObservableField<String>()
    var unMuteVisibility = ObservableField(false)
    var unblockVisibility = ObservableField(false)
    var unFavoriteVisibility = ObservableField(false)

    init {
        name.set(item.first_name + " " + item.last_name)
        stateAndCountry.set(item.state + "," + item.country)
        if (type == ManageUserEnum.BLOCKED.Type) {
            unblockVisibility.set(true)
            unMuteVisibility.set(false)
            unFavoriteVisibility.set(false)

        }
        if (type == ManageUserEnum.MUTED.Type) {
            unblockVisibility.set(false)
            unMuteVisibility.set(true)
            unFavoriteVisibility.set(false)
        }
        if (type == ManageUserEnum.REPORTED.Type) {
            unblockVisibility.set(false)
            unMuteVisibility.set(false)
            unFavoriteVisibility.set(false)
        }
        if (type == ManageUserEnum.FAVORITE.Type){
            unFavoriteVisibility.set(true)
            unblockVisibility.set(false)
            unMuteVisibility.set(false)


        }
    }

    interface OnItemClickListener {
        fun onClickItem(item: UserList)
    }

    fun onItemClick(view: View) {
        listener.onClickItem(item)
    }

}