package com.yewapp.ui.modules.dashboard.fragment.challenges.fragments.active

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

class ActiveChallengeViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) :
    BaseViewModel<ActiveNavigator>(dataManager, schedulerProvider) {

    var lastPage: Int = 1
    var currentPage: Int = 1
    var perPage: Int = 10


    val noData = ObservableField<Boolean>(false)
    val activeChallengeMutableList = mutableListOf<ChallengesDetails>()
    private val activeChallengeList = MutableLiveData<List<ChallengesDetails>>()
    val activeChallengeLiveList: LiveData<List<ChallengesDetails>> get() = activeChallengeList


    override fun setData(extras: Bundle?) {
    }


    fun getActiveChallengesList() {
        if (isLoading.get()) return
        isLoading.set(true)
        compositeDisposable.add(
            dataManager.getCommonChallengeList(ChallengeEnum.ACTIVE.type)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(this::onSuccess, this::onFailure)
        )
    }

    private fun onSuccess(response: AllChallengesResponse) {
        isLoading.set(false)
        activeChallengeMutableList.clear()
        if (response.list.isNotEmpty()) {
            noData.set(false)
            activeChallengeMutableList.addAll(response.list)
            activeChallengeList.value = activeChallengeMutableList
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