package com.yewapp.ui.modules.splash

import android.os.Bundle
import androidx.databinding.ObservableField
import com.yewapp.data.network.DataManager
import com.yewapp.ui.base.BaseViewModel
import com.yewapp.utils.rx.SchedulerProvider

class SplashViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) :
    BaseViewModel<SplashNavigator>(dataManager, schedulerProvider) {

    var progress = ObservableField(0)

    override fun setData(extras: Bundle?) {
        increaseProgress()
    }

    private fun increaseProgress() {

        progress.set(50)

    }

    fun configureAppNavigation() {
        val isOnBoarded = dataManager.getUserOnBoarded()
        val isLoggedIn = dataManager.getLoginStatus()

        when {
            !isOnBoarded -> getNavigator()?.navigateToOnBoarding()
            !isLoggedIn -> getNavigator()?.navigateToSignIn()
            else -> getNavigator()?.navigateToDashboard()
        }
    }
}