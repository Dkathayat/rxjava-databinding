package com.yewapp.ui.modules.forgotPassword.vm

import android.os.Bundle
import android.text.Editable
import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.yewapp.R
import com.yewapp.data.network.DataManager
import com.yewapp.data.network.api.forgotpassword.ChangePasswordRequest
import com.yewapp.data.network.api.forgotpassword.ForgotPasswordRequest
import com.yewapp.data.network.api.forgotpassword.ForgotPasswordResponse
import com.yewapp.data.network.api.signup.SignUpResponse
import com.yewapp.data.network.api.verify.VerifyRequest
import com.yewapp.ui.base.BaseViewModel
import com.yewapp.ui.modules.forgotPassword.navigators.ResetPasswordNavigator
import com.yewapp.ui.modules.verifyCode.extras.VERIFY
import com.yewapp.utils.isEmail
import com.yewapp.utils.isPassword
import com.yewapp.utils.rx.SchedulerProvider

class ResetPasswordViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) :
    BaseViewModel<ResetPasswordNavigator>(dataManager, schedulerProvider) {

    var email = ""
    var code = ""
    var password = ""
    var confirmPassword = ""

    val emailError = ObservableField<String>("")
    val passwordError = ObservableField<String>("")
    val confirmPasswordError = ObservableField<String>("")
    val codeError =
        ObservableField<String>(dataManager.getResourceProvider().getString(R.string.invalid_code))
    val invalidCode = ObservableBoolean(false)
    var timerFinish = false

    var token = ""

    val isVerifyEmailEnabled = ObservableBoolean(false)
    val isVerifyCodeEnabled = ObservableBoolean(false)
    val isChangePasswordEnabled = ObservableBoolean(false)

    init {
        isTitleVisible.set(false)
    }

    override fun setData(extras: Bundle?) {

    }

    /**
     * Verify Email API call
     */
    fun onVerifyEmail(view: View) {
        if (!validateEmail()) return

        if (isLoading.get()) return
        isLoading.set(true)

        compositeDisposable.add(
            dataManager.forgotPassword(
                ForgotPasswordRequest(
                    email
                )
            ).subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(this::onEmailVerificationSuccess, this::onEmailVerificationError)
        )
    }

    private fun onEmailVerificationSuccess(response: ForgotPasswordResponse) {
        isLoading.set(false)
        token = response.token
        getNavigator()?.onSuccess()
    }

    private fun onEmailVerificationError(t: Throwable) {
        isLoading.set(false)
        emailError.set(t.message)
        enableVerifyEmailButton()
    }

    fun onEmailFocusChanged(view: View, hasFocus: Boolean) {
        if (!hasFocus && !validateEmail()) {
            showEmailError()
        }
    }

    fun afterEmailChange(s: Editable) {
        emailError.set("")
        invalidCode.set(false)
        enableVerifyEmailButton()
    }

    private fun validateEmail(): Boolean {
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

    private fun showEmailError() {
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

    fun enableVerifyEmailButton() {
        if (validateEmail())
            isVerifyEmailEnabled.set(true)
        else
            isVerifyEmailEnabled.set(false)
    }

    /**
     * Verify Code API
     */
    fun onVerifyCodeClick(view: View) {

        if (!validateCode()) return
        if (isLoading.get()) return
        isLoading.set(true)

        compositeDisposable.add(
            dataManager.verifyCode(
                VerifyRequest(
                    email,
                    VERIFY.FROM_RESET_PASSWORD.type,
                    code
                )
            ).subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(this::onVerifyCodeSuccess, this::onVerifyCodeFailure)
        )

    }

    fun onVerifyCodeSuccess(response: SignUpResponse) {
        isLoading.set(false)
        getNavigator()?.onSuccess()
    }

    fun onVerifyCodeFailure(t: Throwable) {
        isLoading.set(false)
        codeError.set(t.message)
        invalidCode.set(true)
    }

    fun onCodeFocusChanged(view: View, hasFocus: Boolean) {
        if (!hasFocus && !validateCode())
            showCodeError()

    }

    fun afterCodeChange(s: Editable) {
        invalidCode.set(false)
        enableVerifyCodeButton()
    }

    fun validateCode(): Boolean {
        return when {
            code.isEmpty() || code.length != 4 -> {
                false
            }
            else -> true
        }
    }

    fun showCodeError() {
        when {
            code.isEmpty() || code.length != 4 -> {
                codeError.set(dataManager.getResourceProvider().getString(R.string.invalid_code))
                invalidCode.set(true)
            }
        }
    }

    fun enableVerifyCodeButton() {
        if (validateCode())
            isVerifyCodeEnabled.set(true)
        else
            isVerifyCodeEnabled.set(false)
    }


    /**
     * Called in ChangePasswordFragment when User clicks on Submit
     */
    fun onCreatePasswordClick(view: View) {
        if (!validatePassword()) {
            showPasswordError()
            return
        }

        if (!validateConfirmPassword()) {
            showConfirmPasswordError()
            return
        }

        if (isLoading.get()) return
        isLoading.set(true)

        compositeDisposable.add(
            dataManager.changePassword(
                ChangePasswordRequest(
                    email, token, password
                )
            ).subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(this::onChangePasswordSuccess, this::onChangePasswordFailure)
        )

    }

    private fun onChangePasswordSuccess(message: String) {
        isLoading.set(false)
        getNavigator()?.onSuccess()
    }

    private fun onChangePasswordFailure(t: Throwable) {
        isLoading.set(false)
        getNavigator()?.onError(t, false)
    }

    fun onPasswordFocusChanged(view: View, hasFocus: Boolean) {
        if (!hasFocus && !validatePassword()) {
            showPasswordError()
        }
    }

    fun onConfirmPasswordFocusChanged(view: View, hasFocus: Boolean) {
        if (!hasFocus && !validateConfirmPassword()) {
            showConfirmPasswordError()
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
            confirmPassword.isEmpty() -> {
                false
            }
            !confirmPassword.isPassword() -> {
                false
            }
            password != confirmPassword -> {
                false
            }
            else -> true
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
            confirmPassword.isEmpty() -> {
                confirmPasswordError.set(
                    dataManager.getResourceProvider().getString(R.string.empty_password_error)
                )
            }
            !confirmPassword.isPassword() -> {
                confirmPasswordError.set(
                    dataManager.getResourceProvider().getString(R.string.invalid_password_error)
                )
            }
            password != confirmPassword -> {
                confirmPasswordError.set(
                    dataManager.getResourceProvider().getString(R.string.password_not_match)
                )
            }
        }
    }

    fun afterPasswordChange(s: Editable) {
        passwordError.set("")
        enablePasswordCodeButton()
    }

    fun afterConfirmPasswordChange(s: Editable) {
        confirmPasswordError.set("")
        enablePasswordCodeButton()
    }


    fun enablePasswordCodeButton() {
        if (validatePassword() && validateConfirmPassword())
            isChangePasswordEnabled.set(true)
        else
            isChangePasswordEnabled.set(false)
    }

    /**
     *  Resend Verification Code
     */
    fun onResendCodeClick(view: View) {
        if (isLoading.get()) return
        isLoading.set(true)

        getNavigator()?.onResendClick()

        compositeDisposable.add(
            dataManager.forgotPassword(
                ForgotPasswordRequest(
                    email ?: return
                )
            ).subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(this::onResendCodeSuccess, this::onResendCodeFailure)
        )
    }


    private fun onResendCodeSuccess(message: ForgotPasswordResponse) {
        isLoading.set(false)
    }

    private fun onResendCodeFailure(t: Throwable) {
        isLoading.set(false)
    }


}