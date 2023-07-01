package com.yewapp.ui.modules.forgotPassword.vm

import android.os.Bundle
import android.view.View
import androidx.databinding.ObservableField
import com.yewapp.R
import com.yewapp.data.network.DataManager
import com.yewapp.ui.base.BaseViewModel
import com.yewapp.ui.modules.forgotPassword.navigators.ForgotPasswordNavigator
import com.yewapp.utils.rx.SchedulerProvider

class ForgotPasswordViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) :
    BaseViewModel<ForgotPasswordNavigator>(dataManager, schedulerProvider) {

    val progressCounter = ObservableField<Int>(1)
    val stepLabel =
        ObservableField<String>(dataManager.getResourceProvider().getString(R.string.step_label, 1))

    init {
        isTitleVisible.set(false)
    }

    override fun setData(extras: Bundle?) {

    }

    fun onVerifyEmail(view: View) {

    }

    fun updateStepLabel(currentItem: Int) {
        when (currentItem) {
            0 -> stepLabel.set(
                dataManager.getResourceProvider().getString(R.string.step_label, currentItem + 1)
            )
            1 -> stepLabel.set(
                dataManager.getResourceProvider().getString(R.string.step_label, currentItem + 1)
            )
            2 -> stepLabel.set(
                dataManager.getResourceProvider().getString(R.string.step_label, currentItem + 1)
            )
        }
    }
}