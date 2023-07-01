package com.yewapp.ui.base

import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.yewapp.data.network.DataManager
import com.yewapp.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import java.lang.ref.WeakReference

abstract class BaseViewModel<N>(
    var dataManager: DataManager,
    var schedulerProvider: SchedulerProvider
) : ViewModel() {
    lateinit var navigator: WeakReference<N>
    val isLoading = ObservableBoolean(false)
    var animateAlpha = true

    val preLoginToolbarTitle = ObservableField<String>("")
    val isTitleVisible = ObservableBoolean(true)

    val compositeDisposable: CompositeDisposable = CompositeDisposable()
    abstract fun setData(extras: Bundle?)

    fun setNavigator(navigator: N) {
        this.navigator = WeakReference(navigator)
    }

    fun getNavigator(): N? = navigator.get()

    fun onBackPressed(view: View) {
        (getNavigator() as BaseNavigator).onBackPress()
    }

    fun onSettingClick(view: View) {
        (getNavigator() as BaseNavigator).navigateToSetting()
    }

    fun clearFlags() {
        dataManager.saveUser(null)
        dataManager.saveSubscription(null)
        dataManager.setLoginStatus(false)
        dataManager.setUserOnBoarded(false)
        dataManager.saveAccessToken("")
    }

    fun getString(@StringRes id: Int): String {
        return dataManager.getResourceProvider().getString(id)
    }

    fun checkUserProfileCompletion(message: String): Boolean {
        val user = dataManager.getUser() ?: return false
        if (user.firstName.isNullOrEmpty() || user.lastName.isNullOrEmpty() || user.country.isNullOrEmpty() || user.state.isNullOrEmpty() || user.city.isNullOrEmpty() || user.pincode.isNullOrEmpty()) {
            (getNavigator() as BaseNavigator).showProfileCompletionAlert(message)
            return false
        }
        return true
    }

}