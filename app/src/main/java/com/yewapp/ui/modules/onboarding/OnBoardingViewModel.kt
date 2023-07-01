package com.yewapp.ui.modules.onboarding

import android.os.Bundle
import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.yewapp.R
import com.yewapp.data.network.DataManager
import com.yewapp.ui.base.BaseViewModel
import com.yewapp.utils.rx.SchedulerProvider

class OnBoardingViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) :
    BaseViewModel<OnBoardingNavigator>(dataManager, schedulerProvider) {


    var currentPosition: Int = 0
    val marker1 = ObservableField<Int>(R.drawable.ic_page_marker_active)
    val marker2 = ObservableField<Int>(R.drawable.ic_page_marker_inactive)
    val marker3 = ObservableField<Int>(R.drawable.ic_page_marker_inactive)

    val title = ObservableField<String>(
        dataManager.getResourceProvider().getString(R.string.onboarding_title_1)
    )
    val subTitle = ObservableField<String>(
        dataManager.getResourceProvider().getString(R.string.onboarding_sub_title_1)
    )
    val skipVisibile = ObservableBoolean(true)

    override fun setData(extras: Bundle?) {

    }

    fun onPageToggleClick(view: View) {
        when (view.id) {
            R.id.img_prev_page -> {
                if (currentPosition == 0)
                    return
                getNavigator()?.scrollPagerToPosition(--currentPosition)
            }
            R.id.img_next_page -> {
                if (currentPosition == 2) {
                    getNavigator()?.navigateToSignUpOptionScreen()
                    return
                }
                getNavigator()?.scrollPagerToPosition(++currentPosition)
            }
            R.id.tv_skip -> getNavigator()?.navigateToSignUpOptionScreen()
        }
    }

    fun changeUi(position: Int) {
        currentPosition = position
        when (position) {
            0 -> {
                marker1.set(R.drawable.ic_page_marker_active)
                marker2.set(R.drawable.ic_page_marker_inactive)
                marker3.set(R.drawable.ic_page_marker_inactive)
                skipVisibile.set(true)
                title.set(dataManager.getResourceProvider().getString(R.string.onboarding_title_1))
                subTitle.set(
                    dataManager.getResourceProvider().getString(R.string.onboarding_sub_title_1)
                )
            }
            1 -> {
                marker1.set(R.drawable.ic_page_marker_inactive)
                marker2.set(R.drawable.ic_page_marker_active)
                marker3.set(R.drawable.ic_page_marker_inactive)
                skipVisibile.set(true)
                title.set(dataManager.getResourceProvider().getString(R.string.onboarding_title_2))
                subTitle.set(
                    dataManager.getResourceProvider().getString(R.string.onboarding_sub_title_2)
                )
            }
            2 -> {
                marker1.set(R.drawable.ic_page_marker_inactive)
                marker2.set(R.drawable.ic_page_marker_inactive)
                marker3.set(R.drawable.ic_page_marker_active)
                skipVisibile.set(false)
                title.set(dataManager.getResourceProvider().getString(R.string.onboarding_title_3))
                subTitle.set(
                    dataManager.getResourceProvider().getString(R.string.onboarding_sub_title_3)
                )
            }
        }
    }
}