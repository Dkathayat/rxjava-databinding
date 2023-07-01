package com.yewapp.ui.modules.managefeeds.fragment.comment

import android.os.Bundle
import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.yewapp.data.network.DataManager
import com.yewapp.data.network.api.comment.GetFeedReportedComments
import com.yewapp.data.network.api.comment.ReportedComments
import com.yewapp.data.network.api.report.DeleteComment
import com.yewapp.ui.base.BaseViewModel
import com.yewapp.utils.rx.SchedulerProvider
import com.yewapp.utils.toJson
import timber.log.Timber

class CommentedFragmentViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) :
    BaseViewModel<ReportedCommentsNavigator>(dataManager, schedulerProvider) {

    val _commentList = MutableLiveData<List<ReportedComments>>()
    val commentListLive: MutableLiveData<List<ReportedComments>> get() = _commentList
    private val commentList = mutableListOf<ReportedComments>()
    val noData = ObservableBoolean(false)

    override fun setData(extras: Bundle?) {

    }

    fun getCommentReportedList() {
        isLoading.set(true)
        compositeDisposable.add(
            dataManager.getReportedFeedComments().subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui()).subscribe(this::onSuccess, this::onFailure)
        )
    }

    private fun onSuccess(response: GetFeedReportedComments) {
        isLoading.set(false)
        clearList()
        if (response.list.isNotEmpty()) {
            noData.set(false)
            _commentList.value = response.list
            commentList.addAll(response.list)
        } else {
            noData.set(true)
        }
    }
    private fun onFailure(error: Throwable) {
        isLoading.set(false)
        getNavigator()?.onError(error)

    }
    fun clearList() {
        _commentList.value = mutableListOf()
        commentList.clear()
    }
    fun deleteCommentedReport(id: Int) {
        isLoading.set(true)
        compositeDisposable.add(
            dataManager.deleteReportedComment(id).subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(this::onSuccessDeleteComment, this::onFailure)
        )
    }

    private fun onSuccessDeleteComment(message: String) {
        isLoading.set(false)
        getNavigator()?.onSuccess(message)
        getCommentReportedList()
    }



}