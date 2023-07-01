package com.yewapp.ui.modules.videofeeds.followers

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yewapp.data.network.DataManager
import com.yewapp.data.network.api.FollowersUpdateResponse
import com.yewapp.data.network.api.follower.FollowerListingResponse
import com.yewapp.data.network.api.follower.MyFollowers
import com.yewapp.data.network.api.user.BlockUserRequest
import com.yewapp.ui.base.BaseViewModel
import com.yewapp.ui.modules.videofeeds.extras.UserIdExtras
import com.yewapp.utils.rx.SchedulerProvider

class VideoFeedFollowerViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) :
    BaseViewModel<VideoFeedFollowerNavigator>(dataManager, schedulerProvider) {

    var lastPage: Int = 1
    var currentPage: Int = 1
    var perPage: Int = 10
    private val _latestRouteList = MutableLiveData<List<MyFollowers>>()
    val latestRouteList: LiveData<List<MyFollowers>> get() = _latestRouteList
    val latestList = mutableListOf<MyFollowers>()
    var userId: Int = 0
    var selectedItem = 0

    override fun setData(extras: Bundle?) {
        userId = extras?.getInt(UserIdExtras.USER_ID)!!
        getFollowerList()//userId
    }


    fun getFollowerList() {
        isLoading.set(true)
//      if (isLoading.get()) return
        compositeDisposable.add(
            dataManager.videoFeedFollowerList().subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(this::onLatestFetchSuccess, this::onLatestFetchFailure)
        )
    }

    private fun onLatestFetchSuccess(response: FollowerListingResponse) {
        isLoading.set(false)
        if (response.list.isNotEmpty()) {
            _latestRouteList.value = response.list
            latestList.addAll(response.list)
        }
        currentPage = response.pageData?.current_page!!
        perPage = response.pageData.per_page
        lastPage = response.pageData.last_page
    }

    private fun onLatestFetchFailure(t: Throwable) {
        isLoading.set(false)
        getNavigator()?.onError(t)
    }

    // FollowUser
    fun toggleFollowUser(id: Int) {
        if (isLoading.get()) return
        isLoading.set(true)
        compositeDisposable.add(
            dataManager.followerFollowUser(
                BlockUserRequest(id)
            ).subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(this::onFollowSuccess, this::onFollowFailure)
        )
    }

    fun onFollowSuccess(followUpdateResponse: FollowersUpdateResponse) {
        isLoading.set(false)
        //  getNavigator()?.onSuccess(followUpdateResponse.details.)
        getNavigator()?.onFollowSuccess(followUpdateResponse.details!!, selectedItem)
    }

    //    private fun onFollowSuccess(message: String) {
//        isLoading.set(false)
//        getNavigator()?.onSuccess(message)
//
//    }
    private fun onFollowFailure(t: Throwable) {
        isLoading.set(false)
    }
}