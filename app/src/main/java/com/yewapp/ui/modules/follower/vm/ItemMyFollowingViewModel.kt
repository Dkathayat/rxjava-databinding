package com.yewapp.ui.modules.follower.vm

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.yewapp.data.network.api.follower.MyFollowing

class ItemMyFollowingViewModel(
    val myfollowing: MyFollowing
) : ViewModel() {
    val profileImageUrl = ObservableField<String>("")

    init {
        profileImageUrl.set(myfollowing.image ?: "")
    }
}