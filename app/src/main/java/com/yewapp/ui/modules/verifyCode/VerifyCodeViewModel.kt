package com.yewapp.ui.modules.verifyCode

import android.os.Bundle
import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.yewapp.R
import com.yewapp.data.network.DataManager
import com.yewapp.data.network.api.forgotpassword.ForgotPasswordRequest
import com.yewapp.data.network.api.forgotpassword.ForgotPasswordResponse
import com.yewapp.data.network.api.signup.SignUpResponse
import com.yewapp.data.network.api.verify.VerifyRequest
import com.yewapp.ui.base.BaseViewModel
import com.yewapp.ui.modules.verifyCode.extras.VerifyCodeExtras
import com.yewapp.utils.rx.SchedulerProvider

class VerifyCodeViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) :
    BaseViewModel<VerifyCodeNavigator>(dataManager, schedulerProvider) {

    val invalidCode = ObservableBoolean(false)
    val isRotate = ObservableBoolean(true)
    var isError = ObservableField(false)
    val codeError =
        ObservableField<String>(dataManager.getResourceProvider().getString(R.string.invalid_code))
    var verificationCode = ObservableField<String>("")

    //Values initialised from Extras
    var email: String? = null
    var fromFlag: Int? = null


    init {
        isTitleVisible.set(false)
        isLoading.set(false)
    }

    override fun setData(extras: Bundle?) {
        val extras = extras ?: return
        email = extras.getString(VerifyCodeExtras.EMAIL)
        fromFlag = extras.getInt(VerifyCodeExtras.FROM)
    }

    fun onVerifyCodeClick(view: View) {
        if (!validate()) return
        isLoading.set(true)
        compositeDisposable.add(
            dataManager.verifyCode(
                VerifyRequest(
                    email ?: return,
                    fromFlag ?: return,
                    verificationCode.get().toString()
                )
            ).subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(this::onVerifyCodeSuccess, this::onVerifyCodeFailure)
        )

    }

    private fun onVerifyCodeSuccess(response: SignUpResponse) {
        isLoading.set(false)
        dataManager.saveAccessToken(response.token.accessToken)
        dataManager.saveUser(response.profile)
        dataManager.saveSubscription(response.subscriptionDetail)
        dataManager.setLoginStatus(true)
        getNavigator()?.onVerificationSuccess()
    }

    private fun onVerifyCodeFailure(t: Throwable) {
        isLoading.set(false)
        codeError.set(t.message)
        invalidCode.set(true)
//        getNavigator()?.onError(
//            t, false
//        )
    }

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

    /**
     * TextChangeListener for OTP field set using Data Binding Adapter
     */
    fun onOtpChange(s: CharSequence, start: Int, before: Int, count: Int) {
        invalidCode.set(false)
    }

    /**
     * Validates the Input Parameters before network call
     */
    fun validate(): Boolean {
        return when {
            verificationCode.get().toString().isEmpty() || verificationCode.get()
                .toString().length != 4 -> {
                invalidCode.set(true)
                false
            }
            else -> true
        }
    }
}