package com.yewapp.ui.modules.manageshortvideo.vm

import android.os.Bundle
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.yewapp.data.network.DataManager
import com.yewapp.data.network.api.report.GetShortReportedVideo
import com.yewapp.data.network.api.report.ReportedComment
import com.yewapp.ui.base.BaseViewModel
import com.yewapp.ui.modules.manageshortvideo.navigator.ReportedShortCommentsNavigator
import com.yewapp.utils.rx.SchedulerProvider
import com.yewapp.utils.toJson

class ReportedShortCommentViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) :
    BaseViewModel<ReportedShortCommentsNavigator>(dataManager, schedulerProvider) {

    private val _reportedCommentList = MutableLiveData<List<ReportedComment>>()
    val reportedCommentListLive: MutableLiveData<List<ReportedComment>> get() = _reportedCommentList
    val reportedCommentList = mutableListOf<ReportedComment>()
    val noData = ObservableField<Boolean>(false)
    override fun setData(extras: Bundle?) {

    }

    fun getReportedShortVideoComments() {
        isLoading.set(true)
        compositeDisposable.add(
            dataManager.getReportedShortVideos().subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui()).subscribe(this::onSuccess, this::onFailure)
        )
    }

    fun deleteReportedShortVideo(id:Int){
        compositeDisposable.add(
            dataManager.deleteReportedFeed(id).subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui()).subscribe(this::onSuccessDeleteFeed,this::onFailure)
        )

    }
    private fun onSuccess(response: GetShortReportedVideo) {
        isLoading.set(false)
        clearList()
        if (response.commentBy.isNotEmpty()) {
            noData.set(false)
            _reportedCommentList.value = response.commentBy
            reportedCommentList.addAll(response.commentBy)
        } else {
            noData.set(true)
        }

    }
    private fun onFailure(error: Throwable) {
        isLoading.set(false)
        getNavigator()?.onError(error)

    }

    private fun onSuccessDeleteFeed(message: String) {
        isLoading.set(false)
        getNavigator()?.onSuccess(message)
        getReportedShortVideoComments()
    }
    fun clearList(){
        _reportedCommentList.value = mutableListOf()
        reportedCommentList.clear()
    }
}