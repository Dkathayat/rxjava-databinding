package com.yewapp.ui.modules.refer.vm

import android.os.Bundle
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yewapp.data.network.DataManager
import com.yewapp.data.network.api.refer.ReferResponse
import com.yewapp.ui.base.BaseViewModel
import com.yewapp.ui.modules.refer.navigator.ReferredFriendsNavigator
import com.yewapp.utils.rx.SchedulerProvider

class ReferredFriendsViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) :
    BaseViewModel<ReferredFriendsNavigator>(dataManager, schedulerProvider) {

//    var lastPage: Int = 1
//    var currentPage: Int = 1
//    var perPage: Int = 10
//    var lastPageAll: Int = 1
//    var currentPageAll: Int = 1
//    var perPageAll: Int = 10

    var noData = ObservableField<Boolean>(false)

    var _list = mutableListOf<ReferResponse>()
    var list = MutableLiveData<List<ReferResponse>>()
    val referredList: LiveData<List<ReferResponse>>
        get() = list

    override fun setData(extras: Bundle?) {

    }

    init {
        ReferFriendList()
    }

    fun ReferFriendList() {
        isLoading.set(true)
        compositeDisposable.add(
            dataManager.referFriendList(

            )
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(this::success, this::failure)
        )
    }

    private fun failure(error: Throwable) {
        isLoading.set(false)
        getNavigator()?.onError(error, false)
    }

    private fun success(response: List<ReferResponse>) {
        isLoading.set(false)
        _list.addAll(response)
        list.value = _list
        if (response.isNullOrEmpty()) noData.set(true)
        else noData.set(false)
    }

}