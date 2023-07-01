package com.yewapp.ui.modules.profile.fragment.spectator

import android.os.Bundle
import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yewapp.R
import com.yewapp.data.network.DataManager
import com.yewapp.data.network.api.spectator.DeleteSpectatorRequest
import com.yewapp.data.network.api.spectator.SpectatorMember
import com.yewapp.data.network.api.spectator.SpectatorMemberResponse
import com.yewapp.ui.base.BaseViewModel
import com.yewapp.utils.rx.SchedulerProvider

class SpectatorViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) :
    BaseViewModel<SpectatorNavigator>(dataManager, schedulerProvider) {


    var freeSubscriptionMessage = dataManager.getResourceProvider()
        .getString(R.string.you_can_only_add_1_member_in_current_subscription_plan_upgrade_now)
    val upgradeStart = 55
    val upgradeEnd = 68

    var mutableMemberList = mutableListOf<SpectatorMember>()
    var memberList = MutableLiveData<List<SpectatorMember>>()
    val memberListLiveData: LiveData<List<SpectatorMember>> get() = memberList

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
            R.id.spectator_adding -> {
                if (isLoading.get()) return
                getNavigator()?.navigateAddSpectator()
            }
        }
    }


    fun onUpgradeClicked() {

    }


    fun getSpectatorMember() {
        if (isLoading.get()) return
        isLoading.set(true)
        compositeDisposable.add(
            dataManager.getSpectatorMembers().subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui()).subscribe(this::onSuccess, this::onError)
        )
    }

    private fun onSuccess(response: SpectatorMemberResponse) {
        isLoading.set(false)
        if (dataManager.getUser().isFreeSubscription == true) {
            //Add Spectator layout show when user is free user and Free user add only 1 Spectator
            if (response.list.size == 1) {
                addMemberVisibility.set(false)
                // used to show layout of upgrade subscription when
                isSubscribed.set(false)  // you consume add max spectator limit in free subscription
            } else {
                addMemberVisibility.set(true)
            }
        } else if (dataManager.getUser().isFreeSubscription == false) {
            isSubscribed.set(true)  // used to hide layout of upgrade subscription

            //Add Spectator layout show when user is subscribed user and Add Max user which is define in subscription
            if (response.list.size >= dataManager.getSubscription().maxSpectatorLimit.toInt()) {
                addMemberVisibility.set(false)
            } else {
                addMemberVisibility.set(true)
            }
        }

        if (!response.list.isNullOrEmpty()) {
            mutableMemberList.clear()
            mutableMemberList.addAll(response.list)
            memberList.value = mutableMemberList

        }
    }

    private fun onError(t: Throwable) {
        isLoading.set(false)
        getNavigator()?.onError(t, false)
    }

    fun removeSpectatorMember(associateItem: SpectatorMember) {
        if (isLoading.get()) return
        isLoading.set(true)
        compositeDisposable.add(
            dataManager.deleteSpectator(
                DeleteSpectatorRequest(
                    associateItem.userFrom ?: return,
                    associateItem.userId ?: return
                )
            )
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui()).subscribe(this::onSuccess, this::onError)
        )
    }

    private fun onSuccess(message: String) {
        isLoading.set(false)
        mutableMemberList.clear()
        memberList.value = mutableMemberList
        getSpectatorMember()
//        getNavigator()?.onSuccess(message)
    }
}