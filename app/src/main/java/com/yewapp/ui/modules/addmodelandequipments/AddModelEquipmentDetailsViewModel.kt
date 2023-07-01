package com.yewapp.ui.modules.addmodelandequipments

import android.os.Bundle
import androidx.databinding.ObservableField
import com.yewapp.data.network.DataManager
import com.yewapp.data.network.api.profile.SportType
import com.yewapp.ui.base.BaseViewModel
import com.yewapp.ui.modules.addmodelandequipments.extras.AddModelEquipmentDetailsExtras
import com.yewapp.utils.rx.SchedulerProvider

class AddModelEquipmentDetailsViewModel(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider
) : BaseViewModel<AddModelEquipmentDetailsNavigator>(dataManager, schedulerProvider) {


    var activityTitle = ObservableField<String>("Additional Details")

    override fun setData(extras: Bundle?) {
        val name =
            extras?.getParcelable<SportType>(AddModelEquipmentDetailsExtras.SELECTED_SPORTS_TYPE)?.name
        activityTitle.set("$name Additional Details")
        getNavigator()?.adapterInitialize(extras ?: return)

    }
}