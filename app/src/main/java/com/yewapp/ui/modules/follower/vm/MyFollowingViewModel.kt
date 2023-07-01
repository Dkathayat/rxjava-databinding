package com.yewapp.ui.modules.follower.vm

import android.os.Bundle
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yewapp.data.network.DataManager
import com.yewapp.data.network.api.follower.FollowingListingResponse
import com.yewapp.data.network.api.follower.MyFollowing
import com.yewapp.ui.base.BaseViewModel
import com.yewapp.ui.modules.follower.navigator.MyFollowingNavigator
import com.yewapp.utils.rx.SchedulerProvider

class MyFollowingViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) :
    BaseViewModel<MyFollowingNavigator>(dataManager, schedulerProvider) {
    val noDataLoading = ObservableBoolean(false)

    var _list = mutableListOf<MyFollowing>()
    var list = MutableLiveData<List<MyFollowing>>()
    val listLive: LiveData<List<MyFollowing>>
        get() = list
    var noData = ObservableField<Boolean>(false)

    override fun setData(extras: Bundle?) {
    }

    init {
        getFollowingList()
    }

    fun getFollowingList() {
        isLoading.set(true)
        compositeDisposable.add(
            dataManager.getFollowingList()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(this::success, this::failure)
        )
    }

    private fun failure(error: Throwable) {
        isLoading.set(false)
        getNavigator()?.onError(error, false)
    }

    private fun success(followingListingResponse: FollowingListingResponse) {
        isLoading.set(false)
        if (followingListingResponse.list?.isNotEmpty()!! && followingListingResponse.list.size!! > 0) {
            _list.addAll(followingListingResponse.list)
            list.value = _list
            noDataLoading.set(false)
        } else {
            noDataLoading.set(true)
            if (followingListingResponse.pageData?.current_page == 1)
                noData.set(true)
        }
    }

}