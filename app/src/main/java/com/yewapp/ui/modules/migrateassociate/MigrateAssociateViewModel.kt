package com.yewapp.ui.modules.migrateassociate

import android.os.Bundle
import androidx.databinding.ObservableField
import com.yewapp.R
import com.yewapp.data.network.DataManager
import com.yewapp.data.network.api.associate.MigrateAssociateAccountRequest
import com.yewapp.ui.base.BaseViewModel
import com.yewapp.ui.modules.addassociatepermission.AddAssociatePermissionExtras
import com.yewapp.utils.isPassword
import com.yewapp.utils.rx.SchedulerProvider

class MigrateAssociateViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) :
    BaseViewModel<MigrateAssociateNavigator>(dataManager, schedulerProvider) {


    var associateId = ObservableField<String>("")
    var oldPassword = ObservableField<String>("")
    var oldPasswordError = ObservableField<String>("")

    var newPassword = ObservableField<String>("")
    var newPasswordError = ObservableField<String>("")


    var confirmPassword = ObservableField<String>("")
    var confirmPasswordError = ObservableField<String>("")


    override fun setData(extras: Bundle?) {
        associateId.set(extras?.getString(AddAssociatePermissionExtras.ASSOCIATE_ID) ?: return)
    }


    fun onClickNext() {
        if (validate()) {
            if (isLoading.get()) return
            isLoading.set(true)
            compositeDisposable.add(
                dataManager.migrateAssociateAccount(
                    MigrateAssociateAccountRequest(
                        associateId.get() ?: return,
                        oldPassword.get() ?: return,
                        newPassword.get() ?: return,
                        confirmPassword.get() ?: return
                    )
                ).subscribeOn(schedulerProvider.io()).observeOn(schedulerProvider.ui())
                    .subscribe(this::onSuccess, this::onError)
            )
        }
    }

    private fun onSuccess(message: String) {
        isLoading.set(false)
        getNavigator()?.navigateToSuccessScreen()
    }

    private fun onError(t: Throwable) {
        isLoading.set(false)
        getNavigator()?.onError(t, false)
    }

    fun validate(): Boolean {
        return when {
            oldPassword.get()!!.isEmpty() -> {
                oldPasswordError.set(
                    dataManager.getResourceProvider().getString(R.string.empty_password_error)
                )
                false
            }
            !oldPassword.get()!!.isPassword() -> {
                oldPasswordError.set(
                    dataManager.getResourceProvider().getString(R.string.invalid_password_error)
                )
                false
            }

            newPassword.get()!!.isEmpty() -> {
                newPassword.set(
                    dataManager.getResourceProvider().getString(R.string.empty_password_error)
                )
                false
            }
            !newPassword.get()!!.isPassword() -> {
                newPassword.set(
                    dataManager.getResourceProvider().getString(R.string.invalid_password_error)
                )
                false
            }

            confirmPassword.get()!!.isEmpty() -> {
                confirmPasswordError.set(
                    dataManager.getResourceProvider().getString(R.string.empty_password_error)
                )
                false
            }
            !confirmPassword.get()!!.isPassword() -> {
                confirmPasswordError.set(
                    dataManager.getResourceProvider().getString(R.string.invalid_password_error)
                )
                false
            }

            !confirmPassword.get().equals(newPassword.get()) -> {
                confirmPasswordError.set(
                    dataManager.getResourceProvider().getString(R.string.password_not_match)
                )
                false
            }
            else -> true
        }
    }


    fun onOldPasswordChange(s: CharSequence, start: Int, before: Int, count: Int) {
        oldPasswordError.set("")
    }

    fun onNewPasswordChange(s: CharSequence, start: Int, before: Int, count: Int) {
        newPasswordError.set("")
    }

    fun onConfirmPasswordChange(s: CharSequence, start: Int, before: Int, count: Int) {
        confirmPasswordError.set("")
    }

}