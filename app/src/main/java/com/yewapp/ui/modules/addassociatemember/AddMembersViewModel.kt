package com.yewapp.ui.modules.addassociatemember

import android.os.Bundle
import android.view.View
import androidx.databinding.ObservableField
import com.yewapp.R
import com.yewapp.data.network.DataManager
import com.yewapp.di.addassociate.ValidateEmailRequest
import com.yewapp.ui.base.BaseViewModel
import com.yewapp.ui.modules.profile.navigator.AddMembersNavigator
import com.yewapp.utils.isEmail
import com.yewapp.utils.isPassword
import com.yewapp.utils.rx.SchedulerProvider

class AddMembersViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) :
    BaseViewModel<AddMembersNavigator>(dataManager, schedulerProvider) {

    var email = ""
    var dob = ObservableField<String>("")
    var dobSent = ObservableField<String>("")
    var password = ObservableField<String>("")
    var confirmPassword = ""

    var emailError = ObservableField<String>("")
    var dobError = ObservableField<String>("")
    var passwordError = ObservableField<String>("")
    var confirmPasswordError = ObservableField<String>("")

    override fun setData(extras: Bundle?) {
    }

    fun validate(): Boolean {
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
            password.get()!!.isEmpty() -> {
                passwordError.set(
                    dataManager.getResourceProvider().getString(R.string.empty_password_error)
                )
                false
            }
            !password.get()!!.isPassword() -> {
                passwordError.set(
                    dataManager.getResourceProvider().getString(R.string.invalid_password_error)
                )
                false
            }
            !confirmPassword.equals(password.get()!!, true) -> {
                confirmPasswordError.set(
                    dataManager.getResourceProvider().getString(R.string.password_not_match)
                )
                false
            }
            else -> true
        }
    }


    fun onEmailChange(s: CharSequence, start: Int, before: Int, count: Int) {
        emailError.set("")
    }

    fun onDobClick(view: View) {
        getNavigator()?.onDobClicked()
    }

    fun onPasswordChange(s: CharSequence, start: Int, before: Int, count: Int) {
        passwordError.set("")
    }

    fun onDobChange(s: CharSequence, start: Int, before: Int, count: Int) {
        dobError.set("")
    }

    fun onConfirmPasswordChange(s: CharSequence, start: Int, before: Int, count: Int) {
        confirmPasswordError.set("")
    }

    fun onClickNext() {
        if (validate()) {
            if (isLoading.get()) return
            isLoading.set(true)
            compositeDisposable.add(
                dataManager.validateAssociateEmail(
                    ValidateEmailRequest(
                        email
                    )
                ).subscribeOn(schedulerProvider.io()).observeOn(schedulerProvider.ui())
                    .subscribe(this::onSuccess, this::onError)
            )
        }
    }

    private fun onSuccess(message: String) {
        isLoading.set(false)
        getNavigator()?.navigateToAddMemberDetails()
    }

    private fun onError(t: Throwable) {
        isLoading.set(false)
        getNavigator()?.onError(t, false)
    }
}