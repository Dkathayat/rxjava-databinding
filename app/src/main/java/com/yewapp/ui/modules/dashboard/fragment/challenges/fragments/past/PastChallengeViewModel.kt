package com.yewapp.ui.modules.dashboard.fragment.challenges.fragments.past

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

class PastChallengeViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) :
    BaseViewModel<PastChallengeNavigator>(dataManager, schedulerProvider) {


    var lastPage: Int = 1
    var currentPage: Int = 1
    var perPage: Int = 10

    val noData = ObservableField<Boolean>(false)

    // adding response in this list
    val pastChallengeMutableList = mutableListOf<ChallengesDetails>()
    private val pastChallengeList = MutableLiveData<List<ChallengesDetails>>()
    val pastChallengeLiveList: LiveData<List<ChallengesDetails>> get() = pastChallengeList


    lateinit var extras: Bundle

    override fun setData(extras: Bundle?) {
    }

//    init {
//        getPastChallengesList()
//    }

    fun getPastChallengesList() {
        if (isLoading.get()) return
        isLoading.set(true)
        compositeDisposable.add(
            dataManager.getCommonChallengeList(ChallengeEnum.PAST.type)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(this::onSuccess, this::onFailure)
        )
    }

    private fun onSuccess(response: AllChallengesResponse) {
        isLoading.set(false)
        if (response.list.isNotEmpty()) {
            noData.set(false)
            pastChallengeMutableList.addAll(response.list)
            pastChallengeList.value = pastChallengeMutableList
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