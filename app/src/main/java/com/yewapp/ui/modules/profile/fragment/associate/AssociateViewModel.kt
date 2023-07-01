package com.yewapp.ui.modules.profile.fragment.associate

import android.os.Bundle
import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yewapp.R
import com.yewapp.data.network.DataManager
import com.yewapp.data.network.api.associate.ActiveDeActiveAssociateAccountRequest
import com.yewapp.data.network.api.associate.Associate
import com.yewapp.data.network.api.associate.AssociateMemberResponse
import com.yewapp.data.network.api.associate.RemoveAssociateAccountRequest
import com.yewapp.ui.base.BaseViewModel
import com.yewapp.utils.rx.SchedulerProvider

class AssociateViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) :
    BaseViewModel<AssociateNavigator>(dataManager, schedulerProvider) {

    var freeSubscriptionMessage = dataManager.getResourceProvider()
        .getString(R.string.you_can_only_add_1_member_in_current_subscription_plan_upgrade_now)
    val upgradeStart = 55
    val upgradeEnd = 68

    var mutableMemberList = mutableListOf<Associate>()
    var memberList = MutableLiveData<List<Associate>>()
    val memberListLiveData: LiveData<List<Associate>> get() = memberList

    val addMemberVisibility = ObservableField<Boolean>(false)
    val isSubscribed = ObservableField<Boolean>(false)
    val inCompleteAssociateId = ObservableField<Int>(0)
    val displayUserName = ObservableField<String>("")


    init {
        displayUserName.set("Hi, ${dataManager.getUser().firstName}")
    }

    override fun setData(extras: Bundle?) {
    }

    fun onAddMemberClick(view: View) {
        when (view.id) {
            R.id.associate_adding -> {
                if (isLoading.get()) return
                getNavigator()?.navigateAddMembersAssociate()
            }
        }
    }


    fun onUpgradeClicked() {

    }


    fun getAssociateMember() {
        if (isLoading.get()) return
        isLoading.set(true)
        compositeDisposable.add(
            dataManager.getAssociateMember().subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui()).subscribe(this::onSuccess, this::onError)
        )
    }

    private fun onSuccess(response: AssociateMemberResponse) {
        isLoading.set(false)
        mutableMemberList.clear()
        memberList.value = mutableMemberList

        if (response.associateList.isNotEmpty()) {
            mutableMemberList.addAll(response.associateList)
            memberList.value = mutableMemberList
        }


        inCompleteAssociateId.set(response.associateId)
        if (dataManager.getUser().isFreeSubscription == true) {
            //Add Associate layout show when user is free user and Free user add only 1 Associate
            if (response.associateCount == 1) {
                addMemberVisibility.set(false)
                // used to show layout of upgrade subscription when
                isSubscribed.set(false)  // you consume add max associate limit in free subscription
            } else {
                addMemberVisibility.set(true)
            }
        } else if (dataManager.getUser().isFreeSubscription == false) {
            isSubscribed.set(true)  // used to hide layout of upgrade subscription

            //Add Associate layout show when user is subscribed user and Add Max user which is define in subscription
            if (response.associateCount == response.maxAssociates) {
                addMemberVisibility.set(false)
            } else {
                addMemberVisibility.set(true)
            }
        }

    }

    private fun onError(t: Throwable) {
        isLoading.set(false)
        getNavigator()?.onError(t, false)
    }


    fun removeAssociateAccount(associateID: Int) {
        if (isLoading.get()) return
        isLoading.set(true)

        compositeDisposable.add(
            dataManager.removeAssociateAccount(
                RemoveAssociateAccountRequest(associateID.toString())
            ).subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(this::onRemoveAssociateSuccess, this::onError)
        )
    }


    private fun onRemoveAssociateSuccess(message: String) {
        isLoading.set(false)
        getNavigator()?.removeAssociateSuccess(message)
    }


    fun activateDeactivateAssociateAccount(associateID: Int, updatedStatus: Int) {
        if (isLoading.get()) return
        isLoading.set(true)

        compositeDisposable.add(
            dataManager.activateDeactivateAssociateAccount(
                ActiveDeActiveAssociateAccountRequest(associateID, updatedStatus)
            ).subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(
                    this::onActivateDeactivateAssociateAccountAssociateSuccess,
                    this::onError
                )
        )
    }

    private fun onActivateDeactivateAssociateAccountAssociateSuccess(message: String) {
        isLoading.set(false)
        getNavigator()?.removeAssociateSuccess(message)
    }

}