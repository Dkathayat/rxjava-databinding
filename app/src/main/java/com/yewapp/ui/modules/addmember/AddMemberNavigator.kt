package com.yewapp.ui.modules.addmember

import com.yewapp.data.network.api.UserList
import com.yewapp.ui.base.BaseNavigator

interface AddMemberNavigator : BaseNavigator {
    fun onFollowSuccess(userList: List<UserList>, position: Int)
}