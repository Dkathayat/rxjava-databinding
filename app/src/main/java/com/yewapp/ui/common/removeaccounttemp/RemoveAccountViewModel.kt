package com.yewapp.ui.common.removeaccounttemp

import android.os.Bundle
import android.view.View
import com.yewapp.R
import com.yewapp.data.network.DataManager
import com.yewapp.ui.base.BaseViewModel
import com.yewapp.utils.rx.SchedulerProvider

class RemoveAccountViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) :
    BaseViewModel<RemoveAccountNavigator>(dataManager, schedulerProvider) {
    override fun setData(extras: Bundle?) {

    }

    fun onClick(view:View){
        when(view.id){
            R.id.icCross->{
                getNavigator()?.onDismissClicked()
            }
            R.id.btnRemoveAccount->{
                getNavigator()?.onRemoveClicked()
            }
        }
    }
}