package com.yewapp.ui.modules.profile.fragment.associate

import com.yewapp.ui.base.BaseNavigator

interface AssociateNavigator : BaseNavigator {
    fun navigateAddMembersAssociate()
    fun removeAssociateSuccess(message: String)
}