package com.yewapp.ui.modules.dashboard.fragment.challenges.fragments.created

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

class CreatedByMeViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) :
    BaseViewModel<CreatedByMeNavigator>(dataManager, schedulerProvider) {

    var lastPage: Int = 1
    var currentPage: Int = 1
    var perPage: Int = 10

    val noData = ObservableField<Boolean>(false)

    // adding response in this list
    val createdChallengeMutableList = mutableListOf<ChallengesDetails>()
    private val createdChallengeList = MutableLiveData<List<ChallengesDetails>>()
    val createdChallengeLiveList: LiveData<List<ChallengesDetails>> get() = createdChallengeList

    lateinit var extras: Bundle

    override fun setData(extras: Bundle?) {
    }


    fun getCreatedChallengesList() {
        if (isLoading.get()) return
        isLoading.set(true)
        compositeDisposable.add(
            dataManager.getCommonChallengeList(ChallengeEnum.CREATED.type)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(this::onSuccess, this::onFailure)
        )
    }

    private fun onSuccess(response: AllChallengesResponse) {
        isLoading.set(false)
        createdChallengeMutableList.clear()
        if (response.list.isNotEmpty()) {
            noData.set(false)
            createdChallengeMutableList.addAll(response.list)
            createdChallengeList.value = createdChallengeMutableList
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