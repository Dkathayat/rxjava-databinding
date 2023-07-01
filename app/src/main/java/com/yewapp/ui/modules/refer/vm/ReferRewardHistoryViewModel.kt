package com.yewapp.ui.modules.refer.vm

import android.os.Bundle
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yewapp.data.network.DataManager
import com.yewapp.data.network.api.refer.ReferRewardHistory
import com.yewapp.data.network.api.refer.ReferRewardHistoryResponse
import com.yewapp.ui.base.BaseViewModel
import com.yewapp.ui.modules.refer.navigator.ReferRewardHistoryNavigator
import com.yewapp.utils.rx.SchedulerProvider

class ReferRewardHistoryViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) :
    BaseViewModel<ReferRewardHistoryNavigator>(dataManager, schedulerProvider) {


    var lastPage: Int = 1
    var currentPage: Int = 1
    var perPage: Int = 10
    var lastPageAll: Int = 1
    var currentPageAll: Int = 1
    var perPageAll: Int = 10

    var noData = ObservableField<Boolean>(false)

    var mutableReferRewardHistory = mutableListOf<ReferRewardHistory>()
    var referRewardHistoryList = MutableLiveData<List<ReferRewardHistory>>()
    val liveDataReferRewardHistory: LiveData<List<ReferRewardHistory>>
        get() = referRewardHistoryList


    override fun setData(extras: Bundle?) {

    }

    init {
        getReferRewardHistoryList()
    }


    fun getReferRewardHistoryList() {
        isLoading.set(true)
        compositeDisposable.add(
            dataManager.referRewardHistory(
                currentPage
            )
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(this::success, this::failure)
        )
    }

    private fun success(response: ReferRewardHistoryResponse) {
        isLoading.set(false)
        currentPage = response.pageData.current_page
        lastPage = response.pageData.last_page
        mutableReferRewardHistory.addAll(response.history)
        referRewardHistoryList.value = mutableReferRewardHistory
        if (currentPage == 1 && response.history.isNullOrEmpty()) noData.set(true)
        else noData.set(false)
    }

    private fun failure(error: Throwable) {
        isLoading.set(false)
        getNavigator()?.onError(error, false)
    }
}