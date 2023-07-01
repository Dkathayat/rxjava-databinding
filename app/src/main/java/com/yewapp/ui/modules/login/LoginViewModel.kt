package com.yewapp.ui.modules.login

import android.os.Bundle
import android.text.Editable
import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.yewapp.R
import com.yewapp.data.network.DataManager
import com.yewapp.data.network.api.forgotpassword.ForgotPasswordRequest
import com.yewapp.data.network.api.forgotpassword.ForgotPasswordResponse
import com.yewapp.data.network.api.login.LoginRequest
import com.yewapp.data.network.api.signup.SignUpResponse
import com.yewapp.ui.base.BaseViewModel
import com.yewapp.utils.isEmail
import com.yewapp.utils.isPassword
import com.yewapp.utils.rx.SchedulerProvider

class LoginViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) :
    BaseViewModel<LoginNavigator>(dataManager, schedulerProvider) {

    val spanStart: Int = 22
    val spanEnd: Int = 32
    val isUnderline: Boolean = true
    val spanColor: Int = R.color.colorPrimary
    val spannableText =
        dataManager.getResourceProvider().getString(R.string.forgot_your_password_click_here)

    var email = ""
    var password = ""
    var deviceToken = ""

    var emailError = ObservableField<String>("")
    var passwordError = ObservableField<String>("")
    var loginEnabled = ObservableBoolean(false)

    init {
        preLoginToolbarTitle.set(dataManager.getResourceProvider().getString(R.string.login))
        isTitleVisible.set(true)
    }

    override fun setData(extras: Bundle?) {

    }


    fun afterPasswordChange(s: Editable) {
        passwordError.set("")
        enableButton()
    }

    fun afterEmailChange(s: Editable) {
        emailError.set("")
        enableButton()
    }

    fun onEmailFocusChanged(view: View, hasFocus: Boolean) {
        if (!hasFocus && !validateEmail()) {
            showEmailError()
        }
    }

    fun onPasswordFocusChanged(view: View, hasFocus: Boolean) {
        if (!hasFocus && !validatePassword())
            showPasswordError()
    }

    private fun enableButton() {
        if (validateEmail() && validatePassword()) {
            loginEnabled.set(true)
        } else {
            loginEnabled.set(false)
        }
    }

    fun onLoginClick(view: View) {
        if (!validateEmail()) {
            showEmailError()
        }

        if (!validatePassword()) {
            showPasswordError()
            return
        }

        if (isLoading.get()) return
        isLoading.set(true)


        compositeDisposable.add(
            dataManager.login(
                LoginRequest(
                    email,
                    password,
                    1,
                    deviceToken,
                )
            ).subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(this::onLoginSuccess, this::onLoginError)
        )
    }

    private fun onLoginSuccess(response: SignUpResponse) {
        isLoading.set(false)
        if (!response.profile.emailVerified!!) {
            sendVerificationCode(response.profile.email)
            getNavigator()?.navigateToVerifyCode()
            return
        }
        dataManager.setLoginStatus(true)
        dataManager.saveUser(response.profile)
        dataManager.setUserOnBoarded(true)
        dataManager.saveSubscription(response.subscriptionDetail)
        dataManager.saveAccessToken(response.token.accessToken)
        dataManager.saveAwsCredential(response.awsCredential)
        dataManager.setUserOnBoarded(true)
//        val decryptResponse = response.decrypt().fromJson<ForumListResponse>()

        getNavigator()?.navigateToLogin()
    }

    private fun sendVerificationCode(email: String?) {
        if (email.isNullOrEmpty()) return

        compositeDisposable.add(
            dataManager.forgotPassword(
                ForgotPasswordRequest(
                    email
                )
            ).subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(this::onCodeSent, this::onCodeFailure)
        )

    }

    private fun onCodeSent(response: ForgotPasswordResponse) {

    }

    private fun onCodeFailure(t: Throwable) {
        getNavigator()?.onError(t)
    }

    private fun onLoginError(t: Throwable) {
        isLoading.set(false)
        getNavigator()?.onError(t, false)
    }

    fun showEmailError(): Boolean {
        return when {
            email.isEmpty() -> {
                emailError.set(
                    dataManager.getResourceProvider().getString(R.string.empty_email_error)
                )
                false
            }
            !email.isEmail() -> {
                emailError.set(
                    dataManager.getResourceProvider().getString(R.string.invalid_email_error)
                )
                false
            }
            else -> true
        }
    }

    fun showPasswordError(): Boolean {
        return when {
            password.isEmpty() -> {
                passwordError.set(
                    dataManager.getResourceProvider().getString(R.string.empty_password_error)
                )
                false
            }
            !password.isPassword() -> {
                passwordError.set(
                    dataManager.getResourceProvider().getString(R.string.invalid_password_error)
                )
                false
            }
            else -> true
        }
    }

    fun validateEmail(): Boolean {
        return when {
            email.isEmpty() -> {
                false
            }
            !email.isEmail() -> {
                false
            }
            else -> true
        }
    }

    fun validatePassword(): Boolean {
        return when {
            password.isEmpty() -> {
                false
            }
            !password.isPassword() -> {
                false
            }
            else -> true
        }
    }

    fun onForgotPasswordClick() {
        getNavigator()?.navigateToForgotPassword()
    }
}