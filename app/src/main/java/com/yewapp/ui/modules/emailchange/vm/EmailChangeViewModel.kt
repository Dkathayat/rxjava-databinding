package com.yewapp.ui.modules.emailchange.vm

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.yewapp.R
import com.yewapp.data.network.DataManager
import com.yewapp.data.network.LOGIN
import com.yewapp.data.network.api.emailchange.ChangeEmailRequest
import com.yewapp.data.network.api.emailchange.ChangePhoneRequest
import com.yewapp.data.network.api.profile.Country
import com.yewapp.data.network.api.profile.CountryCode
import com.yewapp.data.network.api.profile.CountryListResponse
import com.yewapp.ui.base.BaseViewModel
import com.yewapp.ui.modules.emailchange.extras.EmailPhoneChangeExtras
import com.yewapp.ui.modules.emailchange.navigator.EmailChangeNavigator
import com.yewapp.ui.modules.addchallenge.challengeroutes.EmailPhoneChangeEnum
import com.yewapp.utils.isEmail
import com.yewapp.utils.rx.SchedulerProvider

class EmailChangeViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) :
    BaseViewModel<EmailChangeNavigator>(dataManager, schedulerProvider) {

    val newEmail = ObservableField<String>("")
    val mobileNumber = ObservableField<String>("")
    val countryID = ObservableField<String>("")

    var countryCodeList: List<CountryCode> = mutableListOf()

    //    var selectedCountryCode = ObservableField<String>("")
    val otp = ObservableField<String>("")
    val updatedLabel =
        ObservableField<String>(dataManager.getResourceProvider().getString(R.string.updated_email))
    var fromScreen = -1
    var mobileNumberError = ObservableField("")
    var phoneCodeError = ObservableField("")
    var emailIDError = ObservableField("")
    var emailVisibility = ObservableField(false)
    var updateBtnEnabled = ObservableBoolean(false)
    val isCountryLoading = ObservableBoolean(false)
    var countryList: List<Country> = mutableListOf()
    var type = ""
//    var selectedCountryId = ""


    override fun setData(extras: Bundle?) {
        if (extras?.getBoolean(EmailPhoneChangeExtras.IS_EMAIL_CHANGE) == true) {
            type = EmailPhoneChangeEnum.EMAIL.Type
            emailVisibility.set(true)
            updatedLabel.set(dataManager.getResourceProvider().getString(R.string.updated_email))
        } else {
            type = EmailPhoneChangeEnum.MOBILE.Type
            emailVisibility.set(false)
            updatedLabel.set(
                dataManager.getResourceProvider().getString(R.string.updated_mobile_no)
            )
            getCountry()
        }
    }

    fun onSelectCountryCode(view: View) {
        getNavigator()?.selectCountryCodeClick()
    }

    fun onUpdateClick(view: View) {
        if (type == EmailPhoneChangeEnum.EMAIL.Type) {
            if (!validateEmail()) {
                showEmailError()
                return
            } else {
                if (isLoading.get()) return
                isLoading.set(true)
                compositeDisposable.add(
                    dataManager.changeEmail(
                        ChangeEmailRequest(
                            newEmail.get().toString()
                        )
                    ).subscribeOn(schedulerProvider.io())
                        .observeOn(schedulerProvider.ui())
                        .subscribe(this::onChangeEmailSuccess, this::onChangeEmailFailure)
                )
            }
        } else {
            if (!validatePhone()) {
                showPhoneNumberError()
                return
            } else {
                if (isLoading.get()) return
                isLoading.set(true)
                compositeDisposable.add(
                    dataManager.changePhone(
                        ChangePhoneRequest(
                            mobileNumber.get().toString(),
                            countryID.get().toString()
                        )
                    ).subscribeOn(schedulerProvider.io())
                        .observeOn(schedulerProvider.ui())
                        .subscribe(this::onChangeEmailSuccess, this::onChangeEmailFailure)
                )
            }
        }

    }

    fun showEmailError() {
        when {
            newEmail.get().toString().isEmpty() -> {
                emailIDError.set(
                    dataManager.getResourceProvider().getString(R.string.empty_email_error)
                )
            }
            !newEmail.get().toString().isEmail() -> {
                emailIDError.set(
                    dataManager.getResourceProvider().getString(R.string.invalid_email_error)
                )
            }
        }
    }

    fun getCountry() {
        if (isCountryLoading.get()) return
        isCountryLoading.set(true)

        compositeDisposable.add(
            dataManager.getCountries()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(this::onCountrySuccess, this::onCountryFailure)
        )

    }

    private fun onCountryFailure(t: Throwable) {
        isCountryLoading.set(false)
        getNavigator()?.onError(t)
    }

    private fun onCountrySuccess(response: CountryListResponse) {
        isCountryLoading.set(false)
        countryList = response.countryList
    }


    private fun onChangeEmailFailure(t: Throwable) {
        isLoading.set(false)
        getNavigator()?.onError(t)
    }

    private fun onChangeEmailSuccess(response: String) {
        isLoading.set(false)
        getNavigator()?.changeEmailSuccess(response)

    }

    private fun showPhoneNumberError() {
        when {
            mobileNumber.get().toString().isEmpty() -> {
                mobileNumberError.set(
                    dataManager.getResourceProvider().getString(R.string.empty_phoneNumber_error)
                )
            }
            mobileNumber.get().toString().length in 12 downTo 3 -> {
                mobileNumberError.set(
                    dataManager.getResourceProvider().getString(R.string.invalid_phoneNumber_error)
                )
            }
            countryID.get().toString().isEmpty() -> {
                phoneCodeError.set(
                    dataManager.getResourceProvider().getString(R.string.empty_phone_code)
                )
            }
        }
    }

    fun validateEmail(): Boolean {
        return when {
            newEmail.get().toString().isEmpty() -> {
                false
            }
            !newEmail.get()?.trim().toString().isEmail() -> {
                false
            }
            else -> true
        }
    }

    private fun validatePhone(): Boolean {
        return when {
            countryID.get().toString().isEmpty() -> {
                false
            }
            mobileNumber.get().toString().isEmpty() -> {
                false
            }
            mobileNumber.get().toString().length < 9 -> {
                false
            }
            else -> true
        }
    }

    private fun enableButtonOnEmail() {
        if (validateEmail()) {// && validatePassword()
            updateBtnEnabled.set(true)
        } else {
            updateBtnEnabled.set(false)
        }
    }

    private fun enableButtonForPhone() {
        if (validatePhone()) {
            updateBtnEnabled.set(true)
        } else {
            updateBtnEnabled.set(false)
        }
    }

    fun onPhoneNoTextChange(s: CharSequence, start: Int, before: Int, count: Int) {
        mobileNumberError.set("")
        Log.d("onPhoneNoTextChange", "onPhoneNoTextChange: ${s.length}")
        if (s.length == 10) {
            enableButtonForPhone()
        }
    }

    fun onCountryCodeTextChange(s: CharSequence, start: Int, before: Int, count: Int) {
        phoneCodeError.set("")
        enableButtonForPhone()
    }

    fun onEmailIdTextChange(s: CharSequence, start: Int, before: Int, count: Int) {
        emailIDError.set("")
        enableButtonOnEmail()
    }

    fun onEmailFocusChanged(view: View, hasFocus: Boolean) {
        if (!hasFocus && !validateEmail()) {
            showEmailError()
        }
    }
}