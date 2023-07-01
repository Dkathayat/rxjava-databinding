package com.yewapp.ui.modules.signup.vm

import android.database.Observable
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.ObservableField
import com.yewapp.R
import com.yewapp.data.network.DataManager
import com.yewapp.data.network.api.signup.SignUpRequestSocial
import com.yewapp.data.network.api.signup.SignUpResponse
import com.yewapp.ui.base.BaseViewModel
import com.yewapp.ui.modules.signup.navigator.SignUpOptionsNavigator
import com.yewapp.utils.rx.SchedulerProvider
import com.yewapp.utils.toJson

class SignUpOptionsViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) :
    BaseViewModel<SignUpOptionsNavigator>(dataManager, schedulerProvider) {

    val spanStart: Int = 38
    val spanEnd: Int = 54
    val secondSpanStart: Int = 57
    val secondSpanEnd: Int = 71
    val isUnderline: Boolean = true
    val spanColor: Int = R.color.colorPrimary
    val spannableText = dataManager.getResourceProvider().getString(R.string.terms_and_conditions)
    var email = ""
    var provider = ""
    var perviousSignUpValidation = ""


    override fun setData(extras: Bundle?) {
    }

    fun onButtonClick(view: View) {
        when (view.id) {
            R.id.btn_gmail_sign_up -> {
                getNavigator()?.navigateToGoogleSignUp()
            }
            R.id.btn_fb_sign_up -> {
                getNavigator()?.navigateToFacebookSignUp()
            }
            R.id.btn_email_sign_up -> {
                getNavigator()?.navigateToSignUp()
            }
            R.id.btn_login -> {
                getNavigator()?.navigateToLogin()
            }
        }
    }

    fun onTermsClick() {
        getNavigator()?.navigateToTermsAndCondition()
    }

    fun onPrivacyClick() {
        getNavigator()?.navigateToPrivacyPolicy()

    }

    fun socialSignUp(
        deviceToken: String,
        deviceType: String,
        email: String,
        provider: String,
        providerUserID: String
    ) {
        isLoading.set(true)
        compositeDisposable.add(
            dataManager.signUpSocial(
                SignUpRequestSocial(
                    deviceToken,
                    deviceType,
                    email,
                    provider,
                    providerUserID,
                    ""
                )
            ).subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(this::onSignUpSocialSuccess, this::onSignUpSocialError)
        )
    }

    private fun onSignUpSocialSuccess(response: SignUpResponse) {
        isLoading.set(false)
        perviousSignUpValidation = response.profile.pincode.toString()
        getNavigator()?.onSignUpSocialSuccess()
        dataManager.saveUser(response.profile)
        dataManager.setLoginStatus(true)
        dataManager.setUserOnBoarded(true)
        dataManager.saveSubscription(response.subscriptionDetail)
        dataManager.saveAccessToken(response.token.accessToken)
        dataManager.saveAwsCredential(response.awsCredential)

    }

    private fun onSignUpSocialError(t: Throwable) {
        isLoading.set(false)
        getNavigator()?.onError(t, false)
    }
}