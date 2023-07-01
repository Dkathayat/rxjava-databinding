package com.yewapp.ui.modules.signup.vm

import android.os.Bundle
import android.text.Editable
import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.yewapp.R
import com.yewapp.data.network.DataManager
import com.yewapp.data.network.api.signup.SignUpRequest
import com.yewapp.data.network.api.signup.SignUpResponse
import com.yewapp.ui.base.BaseViewModel
import com.yewapp.ui.modules.signup.navigator.SignUpNavigator
import com.yewapp.utils.isEmail
import com.yewapp.utils.isPassword
import com.yewapp.utils.rx.SchedulerProvider

class SignUpViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) :
    BaseViewModel<SignUpNavigator>(dataManager, schedulerProvider) {

    val spanStart: Int = 38
    val spanEnd: Int = 54
    val secondSpanStart: Int = 57
    val secondSpanEnd: Int = 71
    val isUnderline: Boolean = true
    val spanColor: Int = R.color.colorPrimary
    val spannableText = dataManager.getResourceProvider().getString(R.string.terms_and_conditions)

    var email = ""
    var dob = ObservableField<String>("")
    var dobSent = ""
    var password = ""
    var confirmPassword = ""
    var referralCode = ""
    var fcmToken = ""

    var emailError = ObservableField<String>("")
    var dobError = ObservableField<String>("")
    var passwordError = ObservableField<String>("")
    var confirmPasswordError = ObservableField<String>("")
    var referralCodeError = ObservableField<String>("")

    var signUpEnabled = ObservableBoolean(false)

    init {
        preLoginToolbarTitle.set(dataManager.getResourceProvider().getString(R.string.sig_up))
        isTitleVisible.set(true)
    }

    override fun setData(extras: Bundle?) {

    }

    fun onDobClick(view: View) {
        getNavigator()?.onDobClicked()
    }

    fun onSignUpClick(view: View) {
        if (!validateEmail()) {
            showEmailError()
            return
        }
        if (!validatePassword()) {
            showPasswordError()
            return
        }
        if (!validateConfirmPassword()) {
            showConfirmPasswordError()
            return
        }
        if (!validateReferralCode()) {
            showReferralError()
            return
        }

        if (isLoading.get()) return
        isLoading.set(true)

        compositeDisposable.add(
            dataManager.signup(
                SignUpRequest(
                    email,
                    password,
                    referralCode,
                    dobSent,
                    1,
                    fcmToken
                )
            ).subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(this::onSignUpSuccess, this::onSignUpError)
        )
    }

    fun onSignUpSuccess(response: SignUpResponse) {
        isLoading.set(false)
        getNavigator()?.onSignUpSuccess()
    }

    fun onSignUpError(t: Throwable) {
        isLoading.set(false)
        getNavigator()?.onError(t, false)
    }

    fun afterEmailChange(s: Editable) {
        emailError.set("")
        enableButton()
    }

    fun afterPasswordChange(s: Editable) {
        passwordError.set("")
        enableButton()
    }

    fun afterDobChange(s: Editable) {
        dobError.set("")
        enableButton()
    }

    fun afterConfirmPasswordChange(s: Editable) {
        confirmPasswordError.set("")
        enableButton()
    }

    fun afterReferralCodeChange(s: Editable) {
        referralCodeError.set("")
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

    fun onConfirmPasswordFocusChanged(view: View, hasFocus: Boolean) {
        if (!hasFocus && !validateConfirmPassword()) {
            showConfirmPasswordError()
        }
    }

    fun onReferralCodeFocusChanged(view: View, hasFocus: Boolean) {
        if (!hasFocus && !validateReferralCode())
            showReferralError()
    }

    fun enableButton() {
        if (validateEmail() && validatePassword()
            && validateConfirmPassword() && validateReferralCode()
        ) {
            signUpEnabled.set(true)
        } else {
            signUpEnabled.set(false)
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

    fun validateConfirmPassword(): Boolean {
        return when {
            !confirmPassword.equals(password, false) -> {
                false
            }
            else -> true
        }
    }

    fun validateReferralCode(): Boolean {
        return when {
            referralCode.isNotEmpty() && referralCode.length < 3 -> {
                false
            }
            else -> true
        }
    }

    fun showEmailError() {
        when {
            email.isEmpty() -> {
                emailError.set(
                    dataManager.getResourceProvider().getString(R.string.empty_email_error)
                )
            }
            !email.isEmail() -> {
                emailError.set(
                    dataManager.getResourceProvider().getString(R.string.invalid_email_error)
                )
            }
        }
    }

    fun showPasswordError() {
        when {
            password.isEmpty() -> {
                passwordError.set(
                    dataManager.getResourceProvider().getString(R.string.empty_password_error)
                )
            }
            !password.isPassword() -> {
                passwordError.set(
                    dataManager.getResourceProvider().getString(R.string.invalid_password_error)
                )
            }
        }
    }

    fun showConfirmPasswordError() {
        when {
            !confirmPassword.equals(password, false) -> {
                confirmPasswordError.set(
                    dataManager.getResourceProvider().getString(R.string.password_not_match)
                )
            }
        }
    }

    fun showReferralError() {
        when {
            referralCode.isNotEmpty() && referralCode.length < 3 -> {
                referralCodeError.set(
                    dataManager.getResourceProvider().getString(R.string.referral_code_error)
                )
            }
        }
    }

    fun onTermsClick() {
        getNavigator()?.navigateToTermsAndCondition()
    }

    fun onPrivacyClick() {
        getNavigator()?.navigateToPrivacyPolicy()

    }
}