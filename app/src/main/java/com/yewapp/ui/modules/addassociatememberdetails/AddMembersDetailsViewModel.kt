package com.yewapp.ui.modules.addassociatememberdetails

import android.os.Bundle
import androidx.databinding.ObservableField
import com.yewapp.data.network.DataManager
import com.yewapp.ui.base.BaseViewModel
import com.yewapp.ui.modules.addassociatepermission.AddAssociatePermissionExtras
import com.yewapp.utils.rx.SchedulerProvider

class AddMembersDetailsViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) :
    BaseViewModel<AddMembersDetailsNavigator>(dataManager, schedulerProvider) {


    var associateID = ObservableField<Int>(0)
    var isUpdate = ObservableField<Boolean>(false)

    var extras = Bundle()
    override fun setData(extras: Bundle?) {
        if (extras?.containsKey(AddAssociatePermissionExtras.ASSOCIATE_ID) ?: return) {
            isUpdate.set(true)
            getNavigator()?.updateTabLayout(1, true)
        } else {
            isUpdate.set(false)
            getNavigator()?.updateTabLayout(1, false)
        }
        this.extras = extras ?: return

    }


}