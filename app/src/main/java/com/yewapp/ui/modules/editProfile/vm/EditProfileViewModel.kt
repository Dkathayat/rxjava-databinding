package com.yewapp.ui.modules.editProfile.vm

import android.os.Bundle
import android.util.Log
import com.yewapp.data.network.DataManager
import com.yewapp.ui.base.BaseViewModel
import com.yewapp.ui.modules.editProfile.extras.EditProfileExtras.Companion.ASSOCIATE_ID
import com.yewapp.ui.modules.editProfile.extras.EditProfileExtras.Companion.IS_SIGN_UP
import com.yewapp.ui.modules.editProfile.navigator.EditProfileNavigator
import com.yewapp.utils.rx.SchedulerProvider

class EditProfileViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) :
    BaseViewModel<EditProfileNavigator>(dataManager, schedulerProvider) {

    var fromSignUp = false
    var associateID = ""
    override fun setData(extras: Bundle?) {
        fromSignUp = extras?.getBoolean(IS_SIGN_UP) ?: false
        associateID = extras?.getString(ASSOCIATE_ID) ?: ""
        Log.i("setData: ", "$fromSignUp")
        getNavigator()?.initializeAdapter()
    }

//    fun onBackPress(view: View){
//        getNavigator()?.onBackClick()
//    }
}