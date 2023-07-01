package com.yewapp.ui.modules.subscription.vm

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.yewapp.data.network.DataManager
import com.yewapp.data.network.api.comment.GetFeedReportedComments
import com.yewapp.data.network.api.subscription.SubscriptionHistory
import com.yewapp.ui.base.BaseViewModel
import com.yewapp.ui.modules.subscription.navigator.CurrentPlanFragmentNavigtor
import com.yewapp.utils.DateUtils
import com.yewapp.utils.rx.SchedulerProvider
import com.yewapp.utils.toJson

class CurrentPlanFragmentViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) :
    BaseViewModel<CurrentPlanFragmentNavigtor>(dataManager, schedulerProvider) {

    private val _subscriptionHistory = MutableLiveData<SubscriptionHistory>()
    val subscriptionHistory: MutableLiveData<SubscriptionHistory> get() = _subscriptionHistory
    private val subscriptionList = mutableListOf<SubscriptionHistory>()
    var planName = ObservableField<String>()
    val subscribeButtonVisibility = ObservableField<Boolean>(false)
    val mainLayoutVisibility = ObservableField<Boolean>(false)
    var planPurchaseDate = ObservableField<String>()
    var nextPaymentDate = ObservableField<String>()
    override fun setData(extras: Bundle?) {

    }

    init {
        getSubscriptionList()
    }

    private fun getSubscriptionList() {
        isLoading.set(true)
        compositeDisposable.add(
            dataManager.getSubscriptionHistory().subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui()).subscribe(this::onSuccess, this::onFailure)
        )
    }

    fun onSubscribeClicked(view: View) {
        getNavigator()?.navigateToPlansList()

    }

    private fun onFailure(error: Throwable) {
        isLoading.set(false)
        getNavigator()?.onError(error)

    }

    private fun onSuccess(response: SubscriptionHistory) {
        if (!response.current_plan.isNullOrEmpty()) {
            isLoading.set(false)
            _subscriptionHistory.value = response
            subscriptionList.addAll(listOf(response))
            subscribeButtonVisibility.set(false)
            mainLayoutVisibility.set(true)
            setData()
        } else {
            subscribeButtonVisibility.set(true)
            mainLayoutVisibility.set(false)
        }
    }

    private fun setData() {
        planName.set(subscriptionList[0].current_plan[0].name)
        planPurchaseDate.set(DateUtils.getDateFromTimeStamp(subscriptionList[0].current_plan[0].purchase_date.toLong()))
        nextPaymentDate.set("Next Payment: " + DateUtils.getDateFromTimeStamp(subscriptionList[0].current_plan[0].due_date.toLong()))
    }

}