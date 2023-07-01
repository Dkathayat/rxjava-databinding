package com.yewapp.ui.modules.migrateassociatesucess

import android.os.Bundle
import com.yewapp.data.network.DataManager
import com.yewapp.ui.base.BaseViewModel
import com.yewapp.utils.rx.SchedulerProvider

class MigrateAssociateSuccessViewModel(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider
) : BaseViewModel<MigrateAssociateSuccessNavigator>(dataManager, schedulerProvider) {


    override fun setData(extras: Bundle?) {

    }


    fun loginToYourAccount() {

    }
}