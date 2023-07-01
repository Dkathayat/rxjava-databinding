package com.yewapp.ui.modules.managefeeds.fragment.reported

import android.os.Bundle
import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import com.yewapp.data.network.DataManager
import com.yewapp.data.network.api.report.GetReportedPost
import com.yewapp.data.network.api.report.ReportedPosts
import com.yewapp.ui.base.BaseViewModel
import com.yewapp.utils.rx.SchedulerProvider
import com.yewapp.utils.toJson

class FeedReportedViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) :
    BaseViewModel<FeedReportedNavigator>(dataManager, schedulerProvider) {

    val _reportedList = MutableLiveData<List<ReportedPosts>>()
    val reportedListLive: MutableLiveData<List<ReportedPosts>> get() = _reportedList
    val reportedList = mutableListOf<ReportedPosts>()
    val noData = ObservableBoolean(false)

    override fun setData(extras: Bundle?) {

    }
    fun getReportedPostList() {
        isLoading.set(true)
        compositeDisposable.add(
            dataManager.getFeedReports().subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui()).subscribe(this::onSuccess, this::onFailure)
        )
    }

    fun deleteReportedPost(id:Int){
        isLoading.set(true)
        compositeDisposable.add(
            dataManager.deleteReportedFeed(id).subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui()).subscribe(this::onSuccessDeleteFeed,this::onFailure)
        )

    }
    private fun onFailure(error: Throwable) {
        isLoading.set(false)
        getNavigator()?.onError(error)

    }
    private fun onSuccess(response: GetReportedPost) {
        isLoading.set(false)
        clearList()
                //&& response.list[0].files[0].is_short_video == 0
        if (response.list.isNotEmpty()) {
            noData.set(false)
            _reportedList.value = response.list
            reportedList.addAll(response.list)
        } else {
            noData.set(true)
        }
    }
    private fun onSuccessDeleteFeed(message: String) {
        isLoading.set(false)
        getNavigator()?.onSuccess(message)
        getReportedPostList()
    }

    fun clearList(){
        _reportedList.value = mutableListOf()
        reportedList.clear()
    }
}