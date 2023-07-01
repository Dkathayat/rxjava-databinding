package com.yewapp.ui.modules.emailphonechangeverifyotp

import android.os.Bundle
import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.yewapp.R
import com.yewapp.data.network.DataManager
import com.yewapp.data.network.api.emailchange.ChangeEmailRequest
import com.yewapp.data.network.api.emailchange.ChangePhoneRequest
import com.yewapp.data.network.api.emailchange.UpdateEmailRequest
import com.yewapp.data.network.api.emailchange.UpdatePhoneRequest
import com.yewapp.data.network.api.signup.Profile
import com.yewapp.ui.base.BaseViewModel
import com.yewapp.ui.modules.emailphonechangeverifyotp.extras.EmailPhoneChangeVerifyCodeExtras
import com.yewapp.ui.modules.addchallenge.challengeroutes.EmailPhoneChangeEnum
import com.yewapp.utils.rx.SchedulerProvider

class EmailPhoneChangeVerifyCodeViewModel(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider
) :
    BaseViewModel<EmailPhoneChangeVerifyCodeNavigator>(dataManager, schedulerProvider) {

    val invalidCode = ObservableBoolean(false)
    val isRotate = ObservableBoolean(true)
    var isError = ObservableField(false)
    val codeError =
        ObservableField<String>(dataManager.getResourceProvider().getString(R.string.invalid_code))
    var verificationCode = ObservableField<String>("")

    var emailVisibility = ObservableField(false)
    var type = ""
    var email = ""
    var countryId = ""
    var mobile = ""

    init {
        isTitleVisible.set(false)
        isLoading.set(false)
    }

    override fun setData(extras: Bundle?) {
        val extras = extras ?: return
        if (extras.getBoolean(EmailPhoneChangeVerifyCodeExtras.IS_EMAIL_CHANGE)) {
            type = EmailPhoneChangeEnum.EMAIL.Type
            email = extras.getString(EmailPhoneChangeVerifyCodeExtras.EMAIL).toString()
            emailVisibility.set(true)
            getNavigator()?.setMessageForEmail(true)
        } else {
            type = EmailPhoneChangeEnum.MOBILE.Type
            countryId = extras.getString(EmailPhoneChangeVerifyCodeExtras.COUNTRY_CODE).toString()
            mobile = extras.getString(EmailPhoneChangeVerifyCodeExtras.MOBILE).toString()
            emailVisibility.set(false)
            getNavigator()?.setMessageForEmail(false)
        }
    }

    fun onVerifyCodeClick(view: View) {
        if (!validate()) return
        isLoading.set(true)
        if (type == EmailPhoneChangeEnum.EMAIL.Type) {

            compositeDisposable.add(
                dataManager.updateEmail(
                    UpdateEmailRequest(
                        email ?: return,
                        verificationCode.get().toString()
                    )
                ).subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.ui())
                    .subscribe(this::onVerifyCodeSuccess, this::onVerifyCodeFailure)
            )
        } else {
            compositeDisposable.add(
                dataManager.updatePhone(
                    UpdatePhoneRequest(
                        mobile, countryId,
                        verificationCode.get().toString()
                    )
                ).subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.ui())
                    .subscribe(this::onVerifyCodeSuccess, this::onVerifyCodeFailure)
            )
        }

    }

    private fun onVerifyCodeSuccess(response: String) {
        isLoading.set(false)
        if (type == EmailPhoneChangeEnum.MOBILE.Type) {
            updatePreference()
        }
        getNavigator()?.onVerificationSuccess()
    }

    private fun updatePreference() {
        val user = dataManager.getUser()
        dataManager.saveUser(
            Profile(
                user.address,
                user.allowUserToSeeYourPoint,
                user.bio,
                user.city,
                user.cityId,
                user.compareStatsPrivacy,
                user.country,
                user.countryId,
                user.dob,
                user.email,
                user.emailVerified,
                user.firstName,
                user.followers,
                user.following,
                user.gender,
                user.heartRate,
                user.hideActivityStartingLocation,
                user.hideEmailId,
                user.hidePhoneNumber,
                user.isNotificationEnabled,
                user.lastName,
                user.latitude,
                user.longitude,
                true,
                mobile,
                user.pincode,
                user.profileCoverImage,
                user.profileImage,
                user.state,
                user.stateId,
                user.userId,
                user.weight,
                user.whoCanSeeYourStats,
                user.isFreeSubscription,
                user.isAssociate
            )
        )
    }

    private fun onVerifyCodeFailure(t: Throwable) {
        isLoading.set(false)
        codeError.set(t.message)
        invalidCode.set(true)
    }

    fun onResendCodeClick(view: View) {
        if (isLoading.get()) return
        isLoading.set(true)
        if (type == EmailPhoneChangeEnum.EMAIL.Type) {
            compositeDisposable.add(
                dataManager.changeEmail(
                    ChangeEmailRequest(
                        email
                    )
                ).subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.ui())
                    .subscribe(this::onChangeEmailSuccess, this::onChangeEmailFailure)
            )
        } else {
            compositeDisposable.add(
                dataManager.changePhone(
                    ChangePhoneRequest(
                        countryId,
                        mobile
                    )
                ).subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.ui())
                    .subscribe(this::onChangeEmailSuccess, this::onChangeEmailFailure)
            )
        }
    }

    private fun onChangeEmailFailure(t: Throwable) {
        getNavigator()?.onError(t)
    }

    private fun onChangeEmailSuccess(response: String) {
        getNavigator()?.resendEmailSuccess(response)

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