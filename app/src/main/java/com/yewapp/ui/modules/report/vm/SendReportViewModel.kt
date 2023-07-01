package com.yewapp.ui.modules.report.vm

import android.os.Bundle
import android.text.Editable
import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.amazonaws.auth.BasicAWSCredentials
import com.yewapp.R
import com.yewapp.data.network.DataManager
import com.yewapp.data.network.api.report.CommentReportPost
import com.yewapp.data.network.api.report.ReportPost
import com.yewapp.data.network.api.report.ReportReason
import com.yewapp.data.network.api.report.SendFeedReport
import com.yewapp.ui.base.BaseViewModel
import com.yewapp.ui.modules.report.ReportExtras
import com.yewapp.ui.modules.report.navigator.SendReportNavigator
import com.yewapp.utils.popup.ReasonPopUp
import com.yewapp.utils.rx.SchedulerProvider

class SendReportViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) :
    BaseViewModel<SendReportNavigator>(dataManager, schedulerProvider) {

    val reportReasonList = mutableListOf<ReportReason>()

    //  var comments = ""
    var comments = ObservableField("")
    var reasonText = ObservableField("")
    var mediaList = mutableListOf<String>()
    val isUploading = ObservableBoolean(false)
    val dropboxIconVisibility = ObservableBoolean(true)
    var buttonEnabled = ObservableBoolean(false)

    val attachedImage = ObservableField<String>()
    var selectedReason: ReportReason? = null
    var reasonError = ObservableField<String>("")
    var commentError = ObservableField<String>("")
    var feedId = 0
    var reasonId = 0
    var option = ""
    var list = mutableListOf<String>()
    var _list = MutableLiveData<List<String>>()
    val listLive: LiveData<List<String>> get() = _list

    override fun setData(extras: Bundle?) {
        feedId = extras?.getInt(ReportExtras.FEED_ID)!!
        feedId = extras.getInt(ReportExtras.FEED_ID)
        option = extras.getString(ReportExtras.OPTION).toString()
    }

   var awsCredentials: BasicAWSCredentials

    init {
        awsCredentials = BasicAWSCredentials(
            dataManager.getAwsCredential().bucket_key,
            dataManager.getAwsCredential().bucket_secret_key
        )
        getReasonList()
    }


    fun getReasonList() {
        isLoading.set(true)
        compositeDisposable.add(
            dataManager.reportReasonList()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(this::success, this::failure)
        )
    }

    private fun failure(error: Throwable) {
        isLoading.set(false)
        getNavigator()?.onError(error, false)
    }

    private fun success(response: List<ReportReason>) {
        isLoading.set(false)
        reportReasonList.addAll(response)
    }

    fun onSendReportClick(view: View) {
        if (!validate() || isLoading.get()) return
        isLoading.set(true)
        if (option == "") {
            compositeDisposable.add(
                dataManager.sendFeedReport(
                    SendFeedReport(
                        feedId, reasonId, comments.get(), list
                    )
                ).subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.ui())
                    .subscribe(this::onSuccess, this::onError)
            )
        }
        if (option == "Report Feed") {
            compositeDisposable.add(
                dataManager.sendFeedReport(
                    SendFeedReport(
                        feedId, reasonId, comments.get().toString(), list
                    )
                ).subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.ui())
                    .subscribe(this::onSuccess, this::onError)
            )
        }
        if (option == "Report Comment") {
            compositeDisposable.add(
                dataManager.sendReportForComment(
                    CommentReportPost(
                        feedId, reasonId, comments.get().toString(), list
                    )
                ).subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.ui())
                    .subscribe(this::onSuccess, this::onError)
            )
        }
        if (option == "Report User") {
            compositeDisposable.add(
                dataManager.sendReport(
                    ReportPost(
                        feedId, reasonId, 0, comments.get().toString(), list
                    )
                ).subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.ui())
                    .subscribe(this::onSuccess, this::onError)
            )
        }

    }

    private fun onSuccess(message: String) {
        isLoading.set(false)
        getNavigator()?.onSendReportSuccess(message)
    }

    private fun onError(t: Throwable) {
        isLoading.set(false)
        getNavigator()?.onError(t, false)
    }

    fun onReasonClick(view: View) {
        ReasonPopUp.Builder()
            .addReasonList(reportReasonList)
            .setListener {
                selectedReason = it
                reasonText.set(it.reason)
                reasonId = it.id
                reasonId = it.id
            }.build()
            .show(view)
    }

    fun onBottomMenuClick(view: View) {
        when (view.id) {
            R.id.image_upload_note ->
                if (option == "Report Comment") {
                    if (list.size == 1) {
                        getNavigator()?.uploadValidation(
                            dataManager.getResourceProvider()
                                .getString(R.string.you_cannot_upload_morethan_one_images)
                        )

                    } else {
                        getNavigator()?.launchImageOptions()
                    }
                } else {
                    if (list.size == 3) {
                        getNavigator()?.uploadValidation(
                            dataManager.getResourceProvider()
                                .getString(R.string.you_cannot_upload_morethan_three_images)
                        )

                    } else {
                        getNavigator()?.launchImageOptions()
                    }
                }
            R.id.image_upload_text -> getNavigator()?.launchImageOptions()
        }
    }

    fun addImages(url: String) {
        list.add(url)
        _list.value = list
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

    fun afterCommentChange(s: Editable) {
        commentError.set("")
        enableButton()
    }

    fun afterReasonChange(s: Editable) {
        reasonError.set("")
        enableButton()
    }

    private fun enableButton() {
        if (reasonText.get().toString().isNotEmpty() && comments.get().toString().isNotEmpty()) {
            buttonEnabled.set(true)
        } else {
            buttonEnabled.set(false)
        }
    }

}
