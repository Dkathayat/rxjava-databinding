package com.yewapp.ui.modules.manageuser.vm

import android.os.Bundle
import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import com.yewapp.data.network.DataManager
import com.yewapp.data.network.api.ManageUserListResponse
import com.yewapp.data.network.api.UserList
import com.yewapp.data.network.api.user.BlockUserRequest
import com.yewapp.ui.base.BaseViewModel
import com.yewapp.ui.modules.managefeeds.navigator.ReportedFragmentNavigator
import com.yewapp.utils.rx.SchedulerProvider

class ReportedManageUserFragmentViewModel(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider
) :
    BaseViewModel<ReportedFragmentNavigator>(dataManager, schedulerProvider) {
    val _userList = MutableLiveData<List<UserList>?>()
    val userListLive: MutableLiveData<List<UserList>?> get() = _userList
    val userList = mutableListOf<UserList>()
    val noData = ObservableBoolean(false)
    var lastPage: Int = 1
    var currentPage: Int = 1
    var perPage: Int = 10
    override fun setData(extras: Bundle?) {

    }

    /**
     * @description: This method call an api to fetch the reported user listing
     * @param:currentPage
     * @return:  reported user list with pagination
     *  @author: Krishna kumari
     */
    fun getReportedUserList() {
        isLoading.set(true)
        compositeDisposable.add(
            dataManager.getReportedManageUserList().subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(this::onSuccess, this::onFailure)
        )
    }

    fun unBlockReportedUser(id: Int) {
        if (isLoading.get()) return
        isLoading.set(true)
        compositeDisposable.add(
            dataManager.blockUser(
                BlockUserRequest(id)
            ).subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(this::onUnBlockSuccess, this::onFailure)
        )
    }

    private fun onUnBlockSuccess(message: String) {
        isLoading.set(false)
        getNavigator()?.onSuccess(message)

    }

    /**
     * @description: This method is called when the api return Success response and
     * bind the corresponding response to the mutable reported user list of type UserList
     * @author: Krishna kumari
     */
    private fun onSuccess(response: ManageUserListResponse) {
        isLoading.set(false)
        if (response.list!!.isNotEmpty()) {
            noData.set(false)
            _userList.value = response.list
            userList.addAll(response.list!!)
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
}