package com.yewapp.ui.modules.addchallenge.challengeroutes.latest

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yewapp.data.network.DataManager
import com.yewapp.data.network.api.challenges.ChallengeModel
import com.yewapp.data.network.api.routes.Route
import com.yewapp.data.network.api.routes.RouteListResponse
import com.yewapp.ui.base.BaseViewModel
import com.yewapp.ui.modules.listner.ChallengeExtras
import com.yewapp.ui.modules.addchallenge.challengeroutes.RouteEnum
import com.yewapp.utils.rx.SchedulerProvider

class LatestRoutesViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) :
    BaseViewModel<LatestRoutesNavigator>(dataManager, schedulerProvider) {

    var lastPage: Int = 1
    var currentPage: Int = 1
    var perPage: Int = 10
    private val _latestRouteList = MutableLiveData<List<Route>>()
    val latestRouteList: LiveData<List<Route>> get() = _latestRouteList
    val latestList = mutableListOf<Route>()

    lateinit var challengeModel: ChallengeModel


    override fun setData(extras: Bundle?) {
        challengeModel =
            extras?.getParcelable(ChallengeExtras.CHALLENGE_DATA) ?: return
    }


    fun getLatestRoutesList() {
        if (isLoading.get()) return
        isLoading.set(true)
        compositeDisposable.add(
            dataManager.getRouteList(RouteEnum.LATEST.Type).subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(this::onLatestFetchSuccess, this::onLatestFetchFailure)
        )
    }

    private fun onLatestFetchSuccess(response: RouteListResponse) {
        isLoading.set(false)
        if (response.list.isNotEmpty()) {
            _latestRouteList.value = response.list
            latestList.addAll(response.list)
        }

        currentPage = response.pageData?.current_page!!
        perPage = response.pageData.per_page!!
        lastPage = response.pageData.last_page!!

    }

    private fun onLatestFetchFailure(t: Throwable) {
        isLoading.set(false)
        getNavigator()?.onError(t)
    }

}