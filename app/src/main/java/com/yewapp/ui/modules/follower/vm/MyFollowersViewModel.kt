package com.yewapp.ui.modules.follower.vm

import android.os.Bundle
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yewapp.data.network.DataManager
import com.yewapp.data.network.api.follower.FollowerListingResponse
import com.yewapp.data.network.api.follower.MyFollowers
import com.yewapp.data.network.api.user.BlockUserRequest
import com.yewapp.ui.base.BaseViewModel
import com.yewapp.ui.modules.follower.navigator.MyFollowersNavigator
import com.yewapp.utils.rx.SchedulerProvider

class MyFollowersViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) :
    BaseViewModel<MyFollowersNavigator>(dataManager, schedulerProvider) {
    private val noDataLoading = ObservableBoolean(false)

    var _list = mutableListOf<MyFollowers>()
    var list = MutableLiveData<List<MyFollowers>>()
    val followerList: LiveData<List<MyFollowers>>
        get() = list

    var selectedOptionMyFollowerPosition = 0
    var selectedFollowers: MyFollowers? = null
    var noData = ObservableField<Boolean>(false)

    override fun setData(extras: Bundle?) {
    }

    init {
        getFollowerList()
    }

    private fun getFollowerList() {
        isLoading.set(true)
        compositeDisposable.add(
            dataManager.getFollowerList()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(this::success, this::failure)
        )
    }

    private fun failure(error: Throwable) {
        isLoading.set(false)
        getNavigator()?.onError(error, false)
    }

    private fun success(followerListingResponse: FollowerListingResponse) {
        isLoading.set(false)
        if (followerListingResponse.list.isNotEmpty() && followerListingResponse.list.size!! > 0) {
            _list.addAll(followerListingResponse.list)
            list.value = _list
            noDataLoading.set(false)
        } else {
            noDataLoading.set(true)
            if (followerListingResponse.pageData?.current_page == 1)
                noData.set(true)
        }
    }


    //Handles the Feed Options Menu
    fun onFollowOptionSelected(option: String, myFollowers: MyFollowers, position: Int) {
        selectedOptionMyFollowerPosition = position
        selectedFollowers = myFollowers
        when (option.replace(" ", "").lowercase()) {
            "follow" -> {
                toggleFollowUser(myFollowers.userId)
            }
            "chat" -> {

            }
            "mute" -> {
                toggleMuteUser(myFollowers.userId)
            }
            "report" -> {
                getNavigator()?.launchReportActivity()
            }
            "addtofavourite" -> {
                toggleFavouriteUser(myFollowers.userId)
            }
            "blockuser" -> {
                toggleBlockUser(myFollowers.userId)
            }
        }
    }

    // Block User
    private fun toggleBlockUser(id: Int) {
        if (isLoading.get()) return
        isLoading.set(true)

        compositeDisposable.add(
            dataManager.blockUser(
                BlockUserRequest(
                    id
                )
            ).subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(this::onBlockSuccess, this::onUserOperationFailure)
        )
    }

    // Favourite User
    private fun toggleFavouriteUser(id: Int) {
        if (isLoading.get()) return
        isLoading.set(true)

        compositeDisposable.add(
            dataManager.favouriteUser(
                BlockUserRequest(
                    id
                )
            ).subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(this::onFavouriteSuccess, this::onUserOperationFailure)
        )
    }

    // MuteUser
    private fun toggleMuteUser(id: Int) {
        if (isLoading.get()) return
        isLoading.set(true)

        compositeDisposable.add(
            dataManager.muteUser(
                BlockUserRequest(
                    id
                )
            ).subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(this::onMuteSuccess, this::onUserOperationFailure)
        )
    }

    // FollowUser
    private fun toggleFollowUser(id: Int) {
        if (isLoading.get()) return
        isLoading.set(true)

        compositeDisposable.add(
            dataManager.followUserFeed(
                BlockUserRequest(
                    id
                )
            ).subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(this::onFollowSuccess, this::onUserOperationFailure)
        )
    }


    fun onFollowSuccess(message: String) {
        isLoading.set(false)
//        getNavigator()?.userFollowedSuccess()
        getNavigator()?.onSuccess(message)
    }

    fun onMuteSuccess(message: String) {
        isLoading.set(false)
//        getNavigator()?.userMutedSuccess()
        getNavigator()?.onSuccess(message)
    }

    private fun onBlockSuccess(message: String) {
        isLoading.set(false)
//        getNavigator()?.userBlockedSuccess()
        getNavigator()?.onSuccess(message)
    }

    private fun onFavouriteSuccess(message: String) {
        isLoading.set(false)
//        getNavigator()?.userFavouriteSuccess()
        getNavigator()?.onSuccess(message)
    }

    private fun onUserOperationFailure(t: Throwable) {
        isLoading.set(false)
        getNavigator()?.onError(t)
    }
}