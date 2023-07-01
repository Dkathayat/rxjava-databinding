package com.yewapp.ui.modules.settings.setting

import android.os.Bundle
import android.util.Log
import android.view.View
import com.facebook.AccessToken
import com.facebook.GraphRequest
import com.facebook.HttpMethod
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.yewapp.R
import com.yewapp.data.network.DataManager
import com.yewapp.ui.base.BaseViewModel
import com.yewapp.utils.rx.SchedulerProvider


class SettingsViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) :
    BaseViewModel<SettingsNavigator>(dataManager, schedulerProvider) {
    private val googleSignInClient: GoogleSignInClient?=null
    override fun setData(extras: Bundle?) {
    }

    fun profileSettingClicked(view: View) {
        when (view.id) {
            R.id.chatWithParent -> {
                getNavigator()?.navigateToChatWithParent()
            }
            R.id.shareFeedback_text -> {
                getNavigator()?.navigateToShareFeedback()
            }

            R.id.contact_us_text -> {
                getNavigator()?.navigateToContactUs()
            }

            R.id.about_text -> {
                getNavigator()?.navigateToAbout()

            }

            R.id.subscription_text -> {
                getNavigator()?.navigateToSubscription()

            }

            R.id.logout_text -> {
                logout()
            }
            R.id.faqs_text -> {
                getNavigator()?.navigateToFaqs()

            }
            R.id.refer_friend_text -> {
                getNavigator()?.navigateToRefer()

            }
            R.id.terms_and_condition_text -> {
                getNavigator()?.navigateToTermsAndCondition()

            }
            R.id.privacy_policy_text -> {
                getNavigator()?.navigateToPrivacyPolicy()

            }
            R.id.notification_text -> {
                getNavigator()?.navigateToManageNotification()

            }
            R.id.users_text -> {
                getNavigator()?.navigateToManageUser()
            }
            R.id.feeds_text -> {
                getNavigator()?.navigateToManageFeeds()

            }
            R.id.feeds_short -> {
                getNavigator()?.navigateToManageShort()
            }
            R.id.devices_text -> {
                getNavigator()?.navigateToManageDevices()

            }

        }
    }

    private fun logout() {
        if (isLoading.get()) return
        isLoading.set(true)
        compositeDisposable.add(
            dataManager.logout()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(this::onLogoutSuccess, this::onLogoutFailure)
        )
    }
    private fun onLogoutSuccess(response: String) {
        isLoading.set(false)
        getNavigator()?.onLogoutSuccess()


    }

    private fun onLogoutFailure(t: Throwable) {
        isLoading.set(false)
        getNavigator()?.onError(t, false)
    }

}

