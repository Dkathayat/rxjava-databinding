package com.yewapp.ui.modules.dashboard.fragment.challenges.fragments.favorite

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

class FavoriteChallengeViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) :
    BaseViewModel<FavoriteNavigator>(dataManager, schedulerProvider) {

    var lastPage: Int = 1
    var currentPage: Int = 1
    var perPage: Int = 10


    val noData = ObservableField<Boolean>(false)
    val favoriteChallengeMutableList = mutableListOf<ChallengesDetails>()
    private val favoriteChallengeList = MutableLiveData<List<ChallengesDetails>>()
    val favoriteChallengeLiveList: LiveData<List<ChallengesDetails>> get() = favoriteChallengeList


    override fun setData(extras: Bundle?) {
    }


    fun getFavoriteChallengesList() {
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
        favoriteChallengeMutableList.clear()
        if (response.list.isNotEmpty()) {
            noData.set(false)
            favoriteChallengeMutableList.addAll(response.list)
            favoriteChallengeList.value = favoriteChallengeMutableList
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