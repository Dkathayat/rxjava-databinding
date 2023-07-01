package com.yewapp.ui.modules.signup

import android.os.Bundle
import android.view.View
import com.yewapp.data.network.DataManager
import com.yewapp.ui.base.BaseViewModel
import com.yewapp.ui.modules.signup.extras.SignUpSuccessExtras
import com.yewapp.utils.rx.SchedulerProvider

class SignUpSuccessViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) :
    BaseViewModel<SignUpSuccessNavigator>(dataManager, schedulerProvider) {

    var email = ""
    override fun setData(extras: Bundle?) {
        val extras = extras ?: return
        email = extras.getString(SignUpSuccessExtras.EMAIL) ?: ""
    }

    fun onAddDetailsClick(view: View) {
        getNavigator()?.navigateToProfile()
    }
}