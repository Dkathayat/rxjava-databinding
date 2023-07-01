package com.yewapp.ui.modules.manageshortvideo.vm

import android.os.Bundle
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.yewapp.data.network.DataManager
import com.yewapp.data.network.api.report.GetReportedPost
import com.yewapp.data.network.api.report.GetShortReportedVideo
import com.yewapp.data.network.api.report.ReportedPosts
import com.yewapp.data.network.api.report.ShortReportedVideo
import com.yewapp.ui.base.BaseViewModel
import com.yewapp.ui.modules.managefeeds.fragment.comment.ReportedCommentsNavigator
import com.yewapp.ui.modules.manageshortvideo.navigator.ReportedShortVideoNavigator
import com.yewapp.utils.rx.SchedulerProvider
import com.yewapp.utils.toJson

class ReportedShortVideoViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) :
    BaseViewModel<ReportedShortVideoNavigator>(dataManager, schedulerProvider) {

    val _reportedList = MutableLiveData<List<ShortReportedVideo>>()
    val reportedListLive: MutableLiveData<List<ShortReportedVideo>> get() = _reportedList
    val reportedList = mutableListOf<ShortReportedVideo>()
    val noData = ObservableField<Boolean>(false)

    override fun setData(extras: Bundle?) {

    }
    fun getReportedShortVideo() {
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
            if (response.list.isNotEmpty()) {
                noData.set(false)
                _reportedList.value = response.list
                reportedList.addAll(response.list)
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
        getReportedShortVideo()
    }
    fun clearList(){
        _reportedList.value = mutableListOf()
        reportedList.clear()
    }
}