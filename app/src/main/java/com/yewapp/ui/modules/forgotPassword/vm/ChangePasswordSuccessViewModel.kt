package com.yewapp.ui.modules.forgotPassword.vm

import android.os.Bundle
import android.view.View
import com.yewapp.data.network.DataManager
import com.yewapp.ui.base.BaseViewModel
import com.yewapp.ui.modules.forgotPassword.navigators.ChangePasswordSuccessNavigator
import com.yewapp.utils.rx.SchedulerProvider

class ChangePasswordSuccessViewModel(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider
) : BaseViewModel<ChangePasswordSuccessNavigator>(dataManager, schedulerProvider) {

    override fun setData(extras: Bundle?) {

    }

    fun onLoginClick(view: View) {
        getNavigator()?.navigateToLogin()
    }
}