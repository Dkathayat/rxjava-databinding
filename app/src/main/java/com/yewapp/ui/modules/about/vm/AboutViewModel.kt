package com.yewapp.ui.modules.about.vm

import android.os.Bundle
import androidx.databinding.ObservableField
import com.yewapp.data.network.DataManager
import com.yewapp.data.network.api.about.CmsResponse
import com.yewapp.ui.base.BaseViewModel
import com.yewapp.ui.modules.about.CmsExtras
import com.yewapp.ui.modules.about.navigator.AboutNavigator
import com.yewapp.utils.rx.SchedulerProvider

class AboutViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) :
    BaseViewModel<AboutNavigator>(dataManager, schedulerProvider), OnWebPageLoad, OnWebPageError {
    var url = ObservableField<String>("")
    var htmlTitle = ObservableField<String>("")
    var content = ObservableField<String>("")
    var slug: String? = null

    override fun setData(extras: Bundle?) {
        if (extras != null) {
            slug = extras.getString(CmsExtras.LINK)
        }

        about()
    }

    override fun onWebPageLoaded() {
        isLoading.set(false)
    }

    override fun onWebPageError(message: String?) {
        isLoading.set(false)
    }

    fun about() {
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
        isLoading.set(false)
        url.set(response.url)
        htmlTitle.set(response.htmlTitle)
        content.set(response.content)


    }

    private fun onError(t: Throwable) {
        isLoading.set(false)
        getNavigator()?.onError(t, false)
    }

}

interface OnWebPageLoad {
    fun onWebPageLoaded()
}

interface OnWebPageError {
    fun onWebPageError(message: String?)
}