package com.yewapp.ui.modules.addchallenge.challengeroutes.popular

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

class PopularRoutesViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) :
    BaseViewModel<PopularRoutesNavigator>(dataManager, schedulerProvider) {

    var lastPage: Int = 1
    var currentPage: Int = 1
    var perPage: Int = 10
    val _popularRouteList = MutableLiveData<List<Route>>()
    val popularRouteList: LiveData<List<Route>> get() = _popularRouteList
    val popularList = mutableListOf<Route>()

    lateinit var challengeModel: ChallengeModel

    override fun setData(extras: Bundle?) {
        challengeModel =
            extras?.getParcelable<ChallengeModel>(ChallengeExtras.CHALLENGE_DATA) ?: return
    }


    fun getPopularRoutesList() {
        if (isLoading.get()) return
        isLoading.set(true)
        compositeDisposable.add(
            dataManager.getRouteList(RouteEnum.POPULAR.Type).subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(this::onPopularFetchSuccess, this::onPopularFetchFailure)
        )
    }

    private fun onPopularFetchSuccess(response: RouteListResponse) {
        isLoading.set(false)
        if (response.list.isNotEmpty()) {
            _popularRouteList.value = response.list
            popularList.addAll(response.list)
        }

        currentPage = response.pageData?.current_page!!
        perPage = response.pageData.per_page!!
        lastPage = response.pageData.last_page!!
    }

    private fun onPopularFetchFailure(t: Throwable) {
        isLoading.set(false)
        getNavigator()?.onError(t)
    }

}