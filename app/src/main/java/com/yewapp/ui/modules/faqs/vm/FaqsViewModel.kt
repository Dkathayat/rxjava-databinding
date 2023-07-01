package com.yewapp.ui.modules.faqs.vm

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yewapp.data.network.DataManager
import com.yewapp.data.network.api.faqs.FaqData
import com.yewapp.ui.base.BaseViewModel
import com.yewapp.ui.modules.faqs.navigator.FaqsNavigator
import com.yewapp.utils.rx.SchedulerProvider

class FaqsViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) :
    BaseViewModel<FaqsNavigator>(dataManager, schedulerProvider) {

    /* var id = ObservableField<Int>()
     var position = ObservableField<String>("")
     var question = ObservableField<String>("")
     var answer = ObservableField<String>("")
     var status = ObservableField<String>("")*/

    var _list = mutableListOf<FaqData>()
    var list = MutableLiveData<List<FaqData>>()
    val listLive: LiveData<List<FaqData>>
        get() = list

    override fun setData(extras: Bundle?) {

    }

    init {
        faqs()
    }

    // hit api
    fun faqs() {
        if (isLoading.get()) return
        isLoading.set(true)

        compositeDisposable.add(
            dataManager.faqs(

            ).subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(this::onSuccess, this::onError)
        )

    }

    private fun onSuccess(response: List<FaqData>) {
        isLoading.set(false)
        _list.addAll((response))
        list.value = _list

    }

    private fun onError(t: Throwable) {
        isLoading.set(false)
        getNavigator()?.onError(t, false)
    }
}