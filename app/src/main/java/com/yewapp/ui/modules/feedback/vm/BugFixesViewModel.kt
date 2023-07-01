package com.yewapp.ui.modules.feedback.vm

import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.amazonaws.auth.BasicAWSCredentials
import com.yewapp.R
import com.yewapp.data.network.DataManager
import com.yewapp.data.network.api.report.SendFeedback
import com.yewapp.data.network.api.sports.Sport
import com.yewapp.ui.base.BaseViewModel
import com.yewapp.ui.modules.feedback.navigator.ShareFeedbackFragmentNavigator
import com.yewapp.utils.popup.ShareFeedBackPopUp
import com.yewapp.utils.rx.SchedulerProvider

class BugFixesViewModel(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider
) : BaseViewModel<ShareFeedbackFragmentNavigator>(dataManager, schedulerProvider) {
    val reportReasonList = mutableListOf<Sport>()
    var sportList = mutableListOf<Sport>()
    var _sportsList = MutableLiveData<List<Sport>>()

    //  var comments = ""
    var comments = ObservableField("")
    var reasonText = ObservableField("")
    var mediaList = mutableListOf<String>()
    val isUploading = ObservableBoolean(false)
    val dropboxIconVisibility = ObservableBoolean(true)
    var buttonEnabled = ObservableBoolean(false)
    var uploadedImg = ObservableField("")
    val attachedImage = ObservableBoolean(false)
    var selectedReason: Sport? = null
    var reasonError = ObservableField<String>("")
    var commentError = ObservableField<String>("")
    var feedId = 0
    var reasonId = 0
    var option = ""
    var list = ""
    var _list = MutableLiveData<List<String>>()
    val listLive: LiveData<List<String>> get() = _list

    override fun setData(extras: Bundle?) {
    }

    var awsCredentials: BasicAWSCredentials

    init {
        awsCredentials = BasicAWSCredentials(
            dataManager.getAwsCredential().bucket_key,
            dataManager.getAwsCredential().bucket_secret_key
        )
    }

    fun afterReasonChange(s: Editable) {
        reasonError.set("")
        enableButton()
    }

    fun onReasonClickFeedBack(view: View) {
        ShareFeedBackPopUp.Builder()
            .addReasonList(reportReasonList)
            .setListener {
                reasonId = it.id?.toInt() ?: return@setListener
                selectedReason = it
                reasonText.set(it.name)
                reasonId = it.id?.toInt()


            }.build()
            .show(view)
    }

    fun afterCommentChange(s: Editable) {
        commentError.set("")
        enableButton()
    }

    fun onActionClick(view: View) {
        list = ""
        dropboxIconVisibility.set(true)
        attachedImage.set(false)
    }

    fun getFeedbackList() {
        isLoading.set(true)
        compositeDisposable.add(
            dataManager.getSportsList()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(this::onSuccess, this::onError)
        )

    }

    fun onSendReportClick(view: View) {
        if (!validate() || isLoading.get()) return
        isLoading.set(true)
        compositeDisposable.add(
            dataManager.sendFeedback(
                SendFeedback(
                    comments.get(), "", list, reasonId
                )
            ).subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(this::success, this::onError)
        )

    }

    private fun success(message: String) {
        isLoading.set(false)
        getNavigator()?.onSendReportSuccess(message)
    }

    private fun onSuccess(response: List<Sport>) {
        isLoading.set(false)
        if (response.isNotEmpty()) {
            reportReasonList.addAll(response)
            _sportsList.value = response

        }
    }

    private fun onError(t: Throwable) {
        isLoading.set(false)
        getNavigator()?.onError(t, false)
    }

    fun validate(): Boolean {
        return when {
            reasonText.get().toString().isEmpty() -> {
                reasonError.set(
                    dataManager.getResourceProvider().getString(R.string.empty_reason_error)
                )
                false
            }
            comments.get().toString().isEmpty() -> {
                commentError.set(
                    dataManager.getResourceProvider().getString(R.string.empty_comment_error)
                )
                false
            }
            else -> true
        }
    }

    fun onBottomMenuClick(view: View) {
        when (view.id) {
            R.id.image_upload_note ->
                if (list.isNotEmpty()) {
                    getNavigator()?.uploadValidation(
                        dataManager.getResourceProvider()
                            .getString(R.string.you_cannot_upload_morethan_one_images)
                    )
                } else {
                    getNavigator()?.launchImageOptions()
                }

            R.id.image_upload_text -> getNavigator()?.launchImageOptions()
        }
    }

    fun addImages(url: String) {
        list = url
        _list.value = listOf(list)
    }

    private fun enableButton() {
        if (reasonText.get().toString().isNotEmpty() && comments.get().toString().isNotEmpty()) {
            buttonEnabled.set(true)
        } else {
            buttonEnabled.set(false)
        }
    }
}