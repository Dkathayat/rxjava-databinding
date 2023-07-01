package com.yewapp.ui.modules.cms

import android.os.Bundle
import androidx.databinding.ObservableField
import com.yewapp.data.network.DataManager
import com.yewapp.data.network.api.about.CmsResponse
import com.yewapp.ui.base.BaseViewModel
import com.yewapp.ui.modules.about.CmsExtras
import com.yewapp.ui.modules.about.vm.OnWebPageError
import com.yewapp.ui.modules.about.vm.OnWebPageLoad
import com.yewapp.utils.rx.SchedulerProvider

class CmsViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) :
    BaseViewModel<CmsNavigator>(dataManager, schedulerProvider), OnWebPageLoad, OnWebPageError {

    var url = ObservableField<String>("")
    var slug: String? = null

    override fun setData(extras: Bundle?) {
        if (extras != null) {
            slug = extras.getString(CmsExtras.LINK)
        }

        getPageUrl()
    }


    override fun onWebPageLoaded() {
        isLoading.set(false)
    }

    override fun onWebPageError(message: String?) {
        isLoading.set(false)
    }

    fun getPageUrl() {
        if (isLoading.get()) return

        isLoading.set(true)
        compositeDisposable.add(
            dataManager.getCmsPages(slug ?: return)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(this::onSuccess, this::onError)
        )

    }

    private fun onSuccess(response: CmsResponse) {
        url.set(response.url)
    }

    private fun onError(t: Throwable) {
        isLoading.set(false)
        getNavigator()?.onError(t, false)
    }
}