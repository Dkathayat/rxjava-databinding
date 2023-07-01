package com.yewapp.ui.modules.dashboard.fragment.challenges.fragments.upcoming

import android.os.Bundle
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yewapp.data.network.DataManager
import com.yewapp.data.network.api.challenges.AllChallengesResponse
import com.yewapp.data.network.api.challenges.ChallengesDetails
import com.yewapp.ui.base.BaseViewModel
import com.yewapp.ui.modules.dashboard.fragment.challenges.ChallengeEnum
import com.yewapp.utils.rx.SchedulerProvider

class UpcomingChallengeViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) :
    BaseViewModel<UpcomingNavigator>(dataManager, schedulerProvider) {

    var lastPage: Int = 1
    var currentPage: Int = 1
    var perPage: Int = 10


    val noData = ObservableField<Boolean>(false)
    val upcomingChallengeMutableList = mutableListOf<ChallengesDetails>()
    private val upcomingChallengeList = MutableLiveData<List<ChallengesDetails>>()
    val upcomingChallengeLiveList: LiveData<List<ChallengesDetails>> get() = upcomingChallengeList


    override fun setData(extras: Bundle?) {
    }


    fun getUpcomingChallengesList() {
        if (isLoading.get()) return
        isLoading.set(true)
        compositeDisposable.add(
            dataManager.getCommonChallengeList(ChallengeEnum.FAVORITE.type)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(this::onSuccess, this::onFailure)
        )
    }

    private fun onSuccess(response: AllChallengesResponse) {
        isLoading.set(false)
        upcomingChallengeMutableList.clear()
        if (response.list.isNotEmpty()) {
            noData.set(false)
            upcomingChallengeMutableList.addAll(response.list)
            upcomingChallengeList.value = upcomingChallengeMutableList
        } else
            noData.set(true)
        currentPage = response.pageData.current_page
        perPage = response.pageData.per_page
        lastPage = response.pageData.last_page
    }

    private fun onFailure(t: Throwable) {
        isLoading.set(false)
        getNavigator()?.onError(t)
    }

}