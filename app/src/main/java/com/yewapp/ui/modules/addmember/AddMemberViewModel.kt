package com.yewapp.ui.modules.addmember

import android.os.Bundle
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yewapp.data.network.DataManager
import com.yewapp.data.network.api.FollowUpdateResponse
import com.yewapp.data.network.api.ManageUserListResponse
import com.yewapp.data.network.api.UserList
import com.yewapp.data.network.api.feed.SuggestedUserList
import com.yewapp.data.network.api.feed.SuggestedUserResponse
import com.yewapp.data.network.api.user.BlockUserRequest
import com.yewapp.ui.base.BaseViewModel
import com.yewapp.utils.rx.SchedulerProvider

class AddMemberViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) :
    BaseViewModel<AddMemberNavigator>(dataManager, schedulerProvider) {
    var lastPage: Int = 1
    var currentPage: Int = 1
    var perPage: Int = 10
    var lastPageAll: Int = 1
    var currentPageAll: Int = 1
    var perPageAll: Int = 10
    var selectedItem = 0

    var loadMore = ObservableBoolean(false)
    private val _userList = MutableLiveData<List<SuggestedUserList>>()
    val userLiveList: LiveData<List<SuggestedUserList>>
        get() = _userList

    // adding response in this list
    val userList = mutableListOf<SuggestedUserList>()
    private var _internalAllUserList = ArrayList<UserList>()
    private var _internalSuggestedUserList = ArrayList<SuggestedUserList>()

    private val _allUserList = MutableLiveData<List<UserList>>()
    val allUserLiveList: LiveData<List<UserList>>
        get() = _allUserList

    // adding response in this list
    val allUserList = mutableListOf<UserList>()
    var noUserVisibility = ObservableBoolean(false)
    var searchEdittextValue = ""
    lateinit var selectedUser: UserList

    override fun setData(extras: Bundle?) {
    }

    //Fetch all the feeds from the Server
    fun getSuggestedUserList() {
        if (isLoading.get()) return
        loadMore.set(true)
        compositeDisposable.add(
            dataManager.getSuggestedUserList(
            ).subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(this::onSuggestedListSuccess, this::onListFailure)
        )
    }

    private fun onSuggestedListSuccess(response: SuggestedUserResponse) {
        isLoading.set(false)
        loadMore.set(false)
        if (response.list.isEmpty()) {
            noUserVisibility.set(true)
        } else {
            noUserVisibility.set(false)
            _userList.value = response.list
            userList.addAll(response.list)

            currentPage = response.pageData.current_page
            perPage = response.pageData.per_page
            lastPage = response.pageData.last_page
        }
    }

    private fun onListFailure(t: Throwable) {
        isLoading.set(false)
        loadMore.set(false)
        getNavigator()?.onError(t)
    }

    //    /Fetch all the feeds from the Server
    fun getAllUserList() {
        if (isLoading.get()) return
        if (userList.size > 0) {
            loadMore.set(true)
        } else {
            isLoading.set(true)
        }
        compositeDisposable.add(
            dataManager.getAllUserList(
                searchEdittextValue, currentPageAll
            ).subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(this::onAllListSuccess, this::onAllListFailure)
        )
    }

    private fun onAllListSuccess(response: ManageUserListResponse) {
        isLoading.set(false)
        loadMore.set(false)
        if (response.list?.isNotEmpty() == true) {
//            for (i in 0 until response.list.size) {
//                if (!response.list[i].fullName.isNullOrBlank())
//                    _internalAllUserList.add(response.list[i])
//            }
//            _allUserList.value = _internalAllUserList
//            allUserList.addAll(_internalAllUserList)
            _allUserList.value = response.list!!
            allUserList.addAll(response.list)
        }
        currentPageAll = response.pageData?.current_page!!
        perPageAll = response.pageData.per_page!!
        lastPageAll = response.pageData.last_page!!
    }

    private fun onAllListFailure(t: Throwable) {
        isLoading.set(false)
        loadMore.set(false)
        getNavigator()?.onError(t)
    }

    fun clearList() {
        _allUserList.value = mutableListOf()
        allUserList.clear()
        currentPageAll = 1
    }

    fun clearSuggestedUserList() {
        _userList.value = mutableListOf()
        userList.clear()
        currentPage = 1
    }

    // FollowUser
    fun followUser(id: Int) {
        //  if (isLoading.get()) return
        isLoading.set(true)
        compositeDisposable.add(
            dataManager.followUser(
                BlockUserRequest(id)
            ).subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(this::onFollowSuccess, this::onFolllowFailure)
        )
    }

    fun onFollowSuccess(followUpdateResponse: FollowUpdateResponse) {
        isLoading.set(false)
        //  getNavigator()?.onSuccess(followUpdateResponse.details.)
        getNavigator()?.onFollowSuccess(followUpdateResponse.details!!, selectedItem)
    }

    fun onFolllowFailure(t: Throwable) {
        isLoading.set(false)
        getNavigator()?.onError(t)
    }
}