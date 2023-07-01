package com.yewapp.ui.modules.subscription.vm

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.yewapp.data.network.DataManager
import com.yewapp.data.network.api.subscription.SubscriptionPlans
import com.yewapp.ui.base.BaseViewModel
import com.yewapp.ui.modules.subscription.navigator.PricePlanNavigator
import com.yewapp.utils.rx.SchedulerProvider

class PricePlanFragmentViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) :
    BaseViewModel<PricePlanNavigator>(dataManager, schedulerProvider) {

    private val _subscriptionPlans = MutableLiveData<SubscriptionPlans>()
    val subscriptionPlans: MutableLiveData<SubscriptionPlans> get() = _subscriptionPlans
    private val subscriptionPlanList = mutableListOf<SubscriptionPlans>()
    override fun setData(extras: Bundle?) {

    }
    fun getSubscriptionPlansList(){
        isLoading.set(true)
        compositeDisposable.add(
            dataManager.getSubscriptionPlansDetails().subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui()).subscribe(this::onSuccess,this::onFailure)
        )
    }
    private fun onFailure(error: Throwable) {
        isLoading.set(false)
        getNavigator()?.onError(error)

    }
    private fun onSuccess(response: SubscriptionPlans){
        isLoading.set(false)
        _subscriptionPlans.value = response
        subscriptionPlanList.addAll(listOf(response))

    }
    fun OnClickedPurchase(view:View){
        getNavigator()?.navigateToBuySubscription()
    }
}