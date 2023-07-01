package com.yewapp.ui.modules.addchallenge.challengeroutes.favorites

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

class FavoritesRoutesViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) :
    BaseViewModel<FavoritesRoutesNavigator>(dataManager, schedulerProvider) {

    var lastPage: Int = 1
    var currentPage: Int = 1
    var perPage: Int = 10
    private val _favoritesRouteList = MutableLiveData<List<Route>>()
    val favoritesRouteList: LiveData<List<Route>> get() = _favoritesRouteList
    val favoritesList = mutableListOf<Route>()


    lateinit var challengeModel: ChallengeModel

    override fun setData(extras: Bundle?) {
        challengeModel =
            extras?.getParcelable(ChallengeExtras.CHALLENGE_DATA) ?: return
    }


    fun getFavoritesRoutesList() {
        if (isLoading.get()) return

        isLoading.set(true)
        compositeDisposable.add(
            dataManager.getRouteList(RouteEnum.FAVORITES.Type).subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(this::onFavoriteFetchSuccess, this::onFavoriteFetchFailure)
        )
    }

    private fun onFavoriteFetchSuccess(response: RouteListResponse) {
        isLoading.set(false)
        if (response.list.isNotEmpty()) {
            _favoritesRouteList.value = response.list
            favoritesList.addAll(response.list)
        }

        currentPage = response.pageData?.current_page!!
        perPage = response.pageData.per_page!!
        lastPage = response.pageData.last_page!!
    }

    private fun onFavoriteFetchFailure(t: Throwable) {
        isLoading.set(false)
        getNavigator()?.onError(t)
    }

}