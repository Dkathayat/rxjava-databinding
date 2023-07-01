package com.yewapp.ui.modules.addchallenge.invitemember.vm

import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.yewapp.data.network.api.invitemember.InviteMember

class ItemInviteMemberViewModel(
    val item: InviteMember,
    val listener: OnItemClickListener,
    var index: Int
) :
    ViewModel() {

    var cityAndCountry = ObservableField<String>()
    var city = ObservableField<String>()
    var country = ObservableField<String>()

    init {
        if (!item.city.isNullOrEmpty()) {
            city.set(item.city)
        } else {
            city.set("")
        }
        if (!item.country.isNullOrEmpty()) {
            country.set(item.country)
        } else {
            country.set("")
        }
        cityAndCountry.set(city.get() + ", " + country.get())
    }

    interface OnItemClickListener {
        fun onClickItem(item: InviteMember, position: Int)
    }

    fun onItemClick(view: View) {
        listener.onClickItem(item, index)
    }

}