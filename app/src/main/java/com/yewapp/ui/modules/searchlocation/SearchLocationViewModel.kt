package com.yewapp.ui.modules.searchlocation

import android.os.Bundle
import android.view.View
import com.yewapp.R
import com.yewapp.data.network.DataManager
import com.yewapp.ui.base.BaseViewModel
import com.yewapp.utils.rx.SchedulerProvider

class SearchLocationViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) :
    BaseViewModel<SearchLocationNavigator>(dataManager, schedulerProvider) {

    override fun setData(extras: Bundle?) {
    }

    fun onActionClick(view: View) {
        when (view.id) {
            R.id.btn_choose_location -> {
                getNavigator()?.navigateToLocationAdditionalDetailActivity()
            }

        }
    }
}