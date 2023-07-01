package com.yewapp.ui.modules.chat

import com.yewapp.data.network.api.associate.Associate
import com.yewapp.ui.base.BaseNavigator

interface ChatNavigator : BaseNavigator {
    fun sendMessageToAssociate()
    fun onMigrateAccountClicked(associateDetails: Associate)
    fun onEditClicked(associateDetails: Associate)
    fun onManagePermissionClicked(associateDetails: Associate)
}