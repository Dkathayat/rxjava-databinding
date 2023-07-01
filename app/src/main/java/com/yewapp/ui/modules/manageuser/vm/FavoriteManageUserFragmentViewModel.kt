package com.yewapp.ui.modules.manageuser.vm

import android.os.Bundle
import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import com.yewapp.data.network.DataManager
import com.yewapp.data.network.api.ManageUserListResponse
import com.yewapp.data.network.api.UserList
import com.yewapp.data.network.api.feed.FavoriteList
import com.yewapp.data.network.api.feed.FavoritesUserResponse
import com.yewapp.data.network.api.user.BlockUserRequest
import com.yewapp.ui.base.BaseViewModel
import com.yewapp.ui.modules.manageuser.navigator.FavouriteFragmentNavigator
import com.yewapp.utils.rx.SchedulerProvider

class FavoriteManageUserFragmentViewModel(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider
) :
    BaseViewModel<FavouriteFragmentNavigator>(dataManager, schedulerProvider) {
    val _userList = MutableLiveData<List<UserList>?>()
    val userListLive: MutableLiveData<List<UserList>?> get() = _userList
    val userList = mutableListOf<UserList>()
    val noData = ObservableBoolean(false)
    var lastPage: Int = 1
    var currentPage: Int = 1
    var perPage: Int = 10
    override fun setData(extras: Bundle?) {

    }

    fun getFavoriteUserList() {
        isLoading.set(true)
        compositeDisposable.add(
            dataManager.getFavoriteManageUserList().subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(this::onSuccess, this::onFailure)
        )
    }

    private fun onSuccess(response: ManageUserListResponse) {
        isLoading.set(false)
        clearList()
        if (response.list!!.isNotEmpty()) {
            noData.set(false)
            _userList.value = response.list
            userList.addAll(response.list)
            currentPage = response.pageData?.current_page!!
            perPage = response.pageData.per_page!!
            lastPage = response.pageData.last_page!!
        } else {
            noData.set(true)
        }
    }


private fun onFailure(t: Throwable) {
    isLoading.set(false)
    getNavigator()?.onError(t)
}

fun unFavoriteUser(id: Int) {
    if (isLoading.get()) return
    isLoading.set(true)
    compositeDisposable.add(
        dataManager.favouriteUser(
            BlockUserRequest(id)
        ).subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe(this::onUnFavoriteSuccess, this::onFailure)
    )
}

private fun onUnFavoriteSuccess(message: String) {
    isLoading.set(false)
    getNavigator()?.onSuccess(message)
    getFavoriteUserList()
}

fun clearList() {
    _userList.value = mutableListOf()
    userList.clear()
    currentPage = 1
}
}