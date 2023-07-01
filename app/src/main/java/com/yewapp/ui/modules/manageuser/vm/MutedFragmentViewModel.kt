package com.yewapp.ui.modules.manageuser.vm

import android.os.Bundle
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import com.yewapp.data.network.DataManager
import com.yewapp.data.network.api.ManageUserListResponse
import com.yewapp.data.network.api.UserList
import com.yewapp.data.network.api.user.BlockUserRequest
import com.yewapp.ui.base.BaseViewModel
import com.yewapp.ui.modules.manageuser.navigator.MutedFragmentNavigator
import com.yewapp.utils.rx.SchedulerProvider

class MutedFragmentViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) :
    BaseViewModel<MutedFragmentNavigator>(dataManager, schedulerProvider) {

    var lastPage: Int = 1
    var currentPage: Int = 1
    var perPage: Int = 10

    val _userList = MutableLiveData<List<UserList>?>()
    val userListLive: MutableLiveData<List<UserList>?> get() = _userList
    val userList = mutableListOf<UserList>()
    val noData = ObservableBoolean(false)

    override fun setData(extras: Bundle?) {

    }

    /**
     * @description: This method call an api to fetch the Muted user listing
     * @param:currentPage
     * @return:  Muted user list with pagination
     *  @author: Krishna kumari
     */
    fun getMutedUserList() {
        isLoading.set(true)
        compositeDisposable.add(
            dataManager.getMutedManageUserList().subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(this::onSuccess, this::onFailure)
        )
    }

    /**
     * @description: This method is called when the api return Success response and
     * bind the corresponding response to the mutable Muted user list of type UserList
     * @author: Krishna kumari
     */
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
        } else { noData.set(true) }
    }

    private fun onFailure(t: Throwable) {
        isLoading.set(false)
        getNavigator()?.onError(t)
    }

    /**
     * @description: This method is called to clear the existing list from the recycler view
     * @author: Krishna kumari
     */
    //Clears the existing list from the recycler view
    fun clearList() {
        _userList.value = mutableListOf()
        userList.clear()
        currentPage = 1
    }

    /**
     * @description: This method is called to unblock user through api
     * @author: Krishna kumari
     */
    fun unMuteUser(id: Int) {
        if (isLoading.get()) return
        isLoading.set(true)
        compositeDisposable.add(
            dataManager.muteUser(
                BlockUserRequest(id)
            ).subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(this::onUnMuteSuccess, this::onFailure)
        )
    }

    private fun onUnMuteSuccess(message: String) {
        isLoading.set(false)
        getNavigator()?.onSuccess(message)
        getMutedUserList()
    }
}