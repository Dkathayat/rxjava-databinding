package com.yewapp.ui.modules.addspectator

import android.os.Bundle
import android.util.SparseBooleanArray
import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yewapp.R
import com.yewapp.data.network.DataManager
import com.yewapp.data.network.api.refer.PhoneContacts
import com.yewapp.data.network.api.spectator.AddSpectatorRequest
import com.yewapp.data.network.api.spectator.YewMember
import com.yewapp.data.network.api.spectator.YewMemberResponse
import com.yewapp.ui.base.BaseViewModel
import com.yewapp.ui.modules.addspectator.extras.AddSpectatorExtras
import com.yewapp.utils.rx.SchedulerProvider

class AddSpectatorViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) :
    BaseViewModel<AddSpectatorNavigator>(dataManager, schedulerProvider) {


    val selectedItems = SparseBooleanArray()

    var userFrom = ObservableField<String>("Yew! Members")
    var userTypeArray = arrayOf("Yew! Members", "Phone Contacts")

    var loadMore = ObservableField<Boolean>(false)
    var isBtnActive = ObservableField<Boolean>(false)
    var userFromYew = ObservableField<Boolean>(true)

    var mutableYewMemberList = mutableListOf<YewMember>()
    var yewMemberList = MutableLiveData<List<YewMember>>()
    val yewMemberListLiveData: LiveData<List<YewMember>> get() = yewMemberList

    var checkList = mutableListOf<PhoneContacts>()
    var spectatorCount = ObservableField<Int>(0)


    init {
        getYewMemberList()
    }

    override fun setData(extras: Bundle?) {
        spectatorCount.set(extras?.getInt(AddSpectatorExtras.SPECTATOR_COUNT, 0))
    }


    fun onActionClick(view: View) {
        when (view.id) {
            R.id.userType -> {
                if (isLoading.get()) return
                getNavigator()?.chooseUserType(view)
            }
            R.id.btnAddSpectatorMEmber -> {
                saveYewMemberToAssociate()
            }
        }

    }

    fun getYewMemberList() {
        if (isLoading.get()) return
        isLoading.set(true)
        compositeDisposable.add(
            dataManager.getYewMembersForInvite(
            ).subscribeOn(schedulerProvider.io()).observeOn(schedulerProvider.ui())
                .subscribe(this::onSuccess, this::onError)
        )

    }

    private fun onSuccess(response: YewMemberResponse) {
        isLoading.set(false)
        if (!response.list.isNullOrEmpty()) {
            mutableYewMemberList.addAll(response.list)
            yewMemberList.value = mutableYewMemberList
        }
//            getNavigator()?.navigateToSuccessScreen()
    }

    private fun onError(t: Throwable) {
        isLoading.set(false)
        getNavigator()?.onError(t, false)
    }


    private fun saveYewMemberToAssociate() {
        if (isLoading.get()) return
        isLoading.set(true)

        val contactList = arrayListOf<String>()
        val contactNameList = arrayListOf<String>()

        val spectatorMemberId = arrayListOf<Int>()

        var userFromForApi =
            ""// if (userFrom.get()!! == "Yew! Members") "YewMember" else "phoneContact"

        if (userFromYew.get()!!) {
            userFromForApi = "YewMember"
            for (i in 0 until mutableYewMemberList.size) {
                if (mutableYewMemberList[i].isSelected ?: return) {
                    spectatorMemberId.add(mutableYewMemberList[i].userId?.toInt() ?: return)
                }
            }
        } else {
            userFromForApi = "phoneContact"
            for (i in 0 until checkList.size) {
                contactList.add(checkList[i].phone?:return)
                contactNameList.add(checkList[i].name?:return)
            }
        }

        //Prepare Request
        val request =
            AddSpectatorRequest(contactNameList, contactList, spectatorMemberId, userFromForApi)
        compositeDisposable.add(
            dataManager.addSpectator(
                request
            ).subscribeOn(schedulerProvider.io()).observeOn(schedulerProvider.ui())
                .subscribe(this::onSuccess, this::onError)
        )
    }

    private fun onSuccess(message: String) {
        isLoading.set(false)
        getNavigator()?.navigateToSpectatorListing(message)
    }
}