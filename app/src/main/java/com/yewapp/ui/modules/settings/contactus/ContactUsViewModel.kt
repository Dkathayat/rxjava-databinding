package com.yewapp.ui.modules.settings.contactus

import android.os.Bundle
import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.amazonaws.auth.BasicAWSCredentials
import com.yewapp.R
import com.yewapp.data.network.DataManager
import com.yewapp.data.network.api.contact.ContactRequest
import com.yewapp.ui.base.BaseViewModel
import com.yewapp.utils.isEmail
import com.yewapp.utils.rx.SchedulerProvider

class ContactUsViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) :
    BaseViewModel<ContactUsNavigator>(dataManager, schedulerProvider) {

    var name = ObservableField<String>("")
    var email = ObservableField<String>("")
    var phone = ""
    var message = ""
    var image = ""

    var nameError = ObservableField<String>("")
    var emailError = ObservableField<String>("")
    var phoneError = ObservableField<String>("")
    var messageError = ObservableField<String>("")
    val dropboxIconVisibility = ObservableBoolean(true)
    val isUploading = ObservableBoolean(false)
    val attachedImage = ObservableField<String>("")

    var awsCredentials: BasicAWSCredentials

    init {
        awsCredentials = BasicAWSCredentials(
            dataManager.getAwsCredential().bucket_key,
            dataManager.getAwsCredential().bucket_secret_key
        )
    }


    override fun setData(extras: Bundle?) {
        if (!dataManager.getUser().email.toString().isNullOrEmpty()) {
            email.set(dataManager.getUser().email.toString())
        }
        if (dataManager.getUser().firstName.toString()
                .isEmpty() || dataManager.getUser().firstName == null
        ) {
            name.set("")
        } else {
            name.set(dataManager.getUser().firstName.toString() + " " + dataManager.getUser().lastName.toString())

        }
//        if (dataManager.getUser().firstName.toString().isNotEmpty()||dataManager.getUser().firstName!=null) {
//            name.set(dataManager.getUser().firstName.toString() +" "+ dataManager.getUser().lastName.toString())
//        }
    }

    fun onNameChange(s: CharSequence, start: Int, before: Int, count: Int) {
        nameError.set("")
    }

    fun onEmailChange(s: CharSequence, start: Int, before: Int, count: Int) {
        emailError.set("")
    }

    fun onMessageChange(s: CharSequence, start: Int, before: Int, count: Int) {
        messageError.set("")
    }

    fun onPhoneChange(s: CharSequence, start: Int, before: Int, count: Int) {
        phoneError.set("")
    }

    fun onSubmitClick(view: View) {
        if (!validate()) return
        if (isLoading.get()) return
        isLoading.set(true)

        compositeDisposable.add(
            dataManager.contactUs(
                ContactRequest(
                    name.get().toString(),
                    email.get().toString(),
                    phone,
                    message,
                    attachedImage.get().toString()
                )
            ).subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(this::onSubmitSuccess, this::onSubmitError)
        )

    }

    private fun onSubmitSuccess(message: String) {
        isLoading.set(false)
        getNavigator()?.onContactSuccess(message)
    }

    private fun onSubmitError(t: Throwable) {
        isLoading.set(false)
        getNavigator()?.onError(t, false)
    }

    fun onBottomMenuClick(view: View) {
        when (view.id) {
            R.id.add_image_icon -> getNavigator()?.launchImageOptions()
        }
    }

    fun onCrossImageClick() {
        attachedImage.set("")
        dropboxIconVisibility.set(true)
    }

    fun validate(): Boolean {
        return when {
            name.get().toString().isEmpty() -> {
                nameError.set(
                    dataManager.getResourceProvider().getString(R.string.empty_fullname_error)
                )
                false
            }
            !email.get().toString().isEmail() -> {
                emailError.set(
                    dataManager.getResourceProvider().getString(R.string.invalid_email_error)
                )
                false
            }

            phone.isNotEmpty() -> {
                if (phone.length != 10) {
                    phoneError.set(
                        dataManager.getResourceProvider()
                            .getString(R.string.invalid_phoneNumber_error)
                    )
                    false
                } else {
                    true
                }
            }

            message.isEmpty() -> {
                messageError.set(
                    dataManager.getResourceProvider().getString(R.string.empty_message_error)
                )
                false
            }
            else -> true
        }
    }
}