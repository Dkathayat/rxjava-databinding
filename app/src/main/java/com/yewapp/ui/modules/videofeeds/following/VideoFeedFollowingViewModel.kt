package com.yewapp.ui.modules.videofeeds.following

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

class VideoFeedFollowingViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) :
    BaseViewModel<VideoFeedFollowingNavigator>(dataManager, schedulerProvider) {

    var lastPage: Int = 1
    var currentPage: Int = 1
    var perPage: Int = 10
    private val _latestRouteList = MutableLiveData<List<MyFollowers>>()
    val latestRouteList: LiveData<List<MyFollowers>> get() = _latestRouteList
    val latestList = mutableListOf<MyFollowers>()
    var userId: Int = 0
    var userName = ""
    var selectedItem = 0
    override fun setData(extras: Bundle?) {
        userId = extras?.getInt(UserIdExtras.USER_ID)!!
        userName = extras.getString(UserIdExtras.USER_Name).toString()

        getFollowingUserList()//userId
    }

    fun getFollowingUserList() {//userId:Int
        isLoading.set(true)
//        if (isLoading.get()) return
        compositeDisposable.add(
            dataManager.videoFeedFollowingList().subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(this::onSuccess, this::onFailure)
        )
    }

    private fun onSuccess(response: FollowerListingResponse) {
        isLoading.set(false)
        if (response.list.isNotEmpty()) {
            _latestRouteList.value = response.list
            latestList.addAll(response.list)
        }
        currentPage = response.pageData?.current_page!!
        perPage = response.pageData.per_page
        lastPage = response.pageData.last_page
    }

    private fun onFailure(t: Throwable) {
        isLoading.set(false)
        getNavigator()?.onError(t)
    }

    // FollowUser
    fun followUser(id: Int) {
        if (isLoading.get()) return
        isLoading.set(true)
        compositeDisposable.add(
            dataManager.followerFollowUser(
                BlockUserRequest(
                    id
                )
            ).subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(this::onFollowSuccess, this::onFolllowFailure)
        )
    }

    fun onFollowSuccess(followUpdateResponse: FollowersUpdateResponse) {
        isLoading.set(false)
        //  getNavigator()?.onSuccess(followUpdateResponse.details.)
        getNavigator()?.onFollowSuccess(followUpdateResponse.details!!, selectedItem)
    }

    //    fun onFollowSuccess(message: String) {
//        isLoading.set(false)
//        getNavigator()?.onSuccess(message)
//
//
//    }
    fun onFolllowFailure(t: Throwable) {
        isLoading.set(false)
    }
}