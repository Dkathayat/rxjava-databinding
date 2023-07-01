package com.yewapp.ui.modules.dashboard.vm

import android.os.Bundle
import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import com.yewapp.R
import com.yewapp.data.network.DataManager
import com.yewapp.data.network.api.challenges.CheckSportsEquipmentAddedOrNotResponse
import com.yewapp.ui.base.BaseViewModel
import com.yewapp.ui.modules.dashboard.fragment.challenges.ChallengesFragment
import com.yewapp.ui.modules.dashboard.fragment.feeds.FeedsFragment
import com.yewapp.ui.modules.dashboard.navigator.DashboardNavigator
import com.yewapp.ui.modules.profile.MainProfileFragment
import com.yewapp.utils.rx.SchedulerProvider

class DashboardViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) :
    BaseViewModel<DashboardNavigator>(dataManager, schedulerProvider) {


    var scrollPosition = 0
    var dashboardToolbarTitle = ObservableField<String>("")
    var isDashboardTitleVisible = ObservableBoolean(false)

    var toolbarIcon1 = ObservableInt(R.drawable.ic_notification)
    var toolbarIcon2 = ObservableInt(R.drawable.ic_add_comment)
    var toolbarIcon3 = ObservableInt(R.drawable.ic_add_friend)

    var isHomeActive = ObservableBoolean(false)
    var isChallengesActive = ObservableBoolean(false)
    var isRoutesActive = ObservableBoolean(false)
    var isVideosActive = ObservableBoolean(false)
    var isProfileActive = ObservableBoolean(false)

    var isToolbarIcon1Visible = ObservableBoolean(true)
    var isToolbarIcon2Visible = ObservableBoolean(true)
    var isToolbarIcon3Visible = ObservableBoolean(true)

    init {
        isHomeActive.set(true)
    }

    override fun setData(extras: Bundle?) {

    }


    fun onBottomMenuClick(view: View) {

        if (view.id == R.id.img_menu_4) {
            if (scrollPosition == 3) return
//            scrollPosition = 3
            getNavigator()?.navigateToVideoFeeds()
            return
        }
        val navigator = getNavigator() ?: return
        val fragment = when (view.id) {
            R.id.iv_home -> {
                if (scrollPosition == 0) return
                scrollPosition = 0
                FeedsFragment()
            }
            R.id.iv_challenges -> {
                if (scrollPosition == 1) return
                scrollPosition = 1
                ChallengesFragment()
            }
            R.id.img_menu_3 -> {
                if (scrollPosition == 2) return
                scrollPosition = 2
                ChallengesFragment()
            }

            R.id.img_menu_5 -> {
                if (checkUserProfileCompletion(
                        dataManager.getResourceProvider()
                            .getString(R.string.complete_profile_use_feed)
                    )
                ) {
                    if (scrollPosition == 4) return
                    scrollPosition = 4
                    MainProfileFragment()
                } else {
                    null
                }
            }
            else -> throw IllegalArgumentException()
        } ?: return
        navigator.scrollToPosition(fragment)

        manageToolbarTitle()
        manageToolbarIcons()
        manageBottomBarIcons()

    }

    val updateScrollPosition = { v: Int ->
        scrollPosition = v
    }

    //    fun updateScrollPosition(scrollPosition :Int): (Int) -> Unit {
//        this.scrollPosition =scrollPosition
//    }
    private fun manageBottomBarIcons() {
        when (scrollPosition) {
            0 -> {
                isHomeActive.set(true)
                isChallengesActive.set(false)
                isRoutesActive.set(false)
                isVideosActive.set(false)
                isProfileActive.set(false)
            }
            1 -> {
                isHomeActive.set(false)
                isChallengesActive.set(true)
                isRoutesActive.set(false)
                isVideosActive.set(false)
                isProfileActive.set(false)
            }
            2 -> {
                isHomeActive.set(false)
                isChallengesActive.set(false)
                isRoutesActive.set(true)
                isVideosActive.set(false)
                isProfileActive.set(false)
            }
            3 -> {
                isHomeActive.set(false)
                isChallengesActive.set(false)
                isRoutesActive.set(false)
                isVideosActive.set(true)
                isProfileActive.set(false)
            }
            4 -> {
                isHomeActive.set(false)
                isChallengesActive.set(false)
                isRoutesActive.set(false)
                isVideosActive.set(false)
                isProfileActive.set(true)
            }
        }
    }

    private fun manageToolbarIcons() {
        when (scrollPosition) {
            0 -> {
                isToolbarIcon1Visible.set(true)
                isToolbarIcon2Visible.set(true)
                isToolbarIcon3Visible.set(true)

                toolbarIcon1.set(R.drawable.ic_notification)
                toolbarIcon2.set(R.drawable.ic_add_comment)
                toolbarIcon3.set(R.drawable.ic_add_friend)
            }
            1 -> {
                isToolbarIcon1Visible.set(false)
                isToolbarIcon2Visible.set(false)
                isToolbarIcon3Visible.set(true)

                toolbarIcon1.set(R.drawable.ic_notification)
                toolbarIcon2.set(R.drawable.ic_add_comment)
                toolbarIcon3.set(R.drawable.ic_create_challenge)
            }
            2 -> {
                isToolbarIcon1Visible.set(true)
                isToolbarIcon2Visible.set(true)
                isToolbarIcon3Visible.set(true)

                toolbarIcon1.set(R.drawable.ic_notification)
                toolbarIcon2.set(R.drawable.ic_add_comment)
                toolbarIcon3.set(R.drawable.ic_add_friend)
            }
            3 -> {
                isToolbarIcon1Visible.set(true)
                isToolbarIcon2Visible.set(true)
                isToolbarIcon3Visible.set(true)

                toolbarIcon1.set(R.drawable.ic_notification)
                toolbarIcon2.set(R.drawable.ic_add_comment)
                toolbarIcon3.set(R.drawable.ic_add_friend)
            }
            4 -> {
                isToolbarIcon1Visible.set(true)
                isToolbarIcon2Visible.set(false)
                isToolbarIcon3Visible.set(true)

                toolbarIcon1.set(R.drawable.ic_notification)
                toolbarIcon2.set(R.drawable.ic_add_comment)
                toolbarIcon3.set(R.drawable.ic_settings_icon)
            }
        }
    }

    private fun manageToolbarTitle() {
        when (scrollPosition) {
            0 -> {
                dashboardToolbarTitle.set("")
                isDashboardTitleVisible.set(false)
            }
            1 -> {
                dashboardToolbarTitle.set(
                    dataManager.getResourceProvider().getString(R.string.challenges)
                )
                isDashboardTitleVisible.set(true)
            }
            2 -> {
                dashboardToolbarTitle.set("")
                isDashboardTitleVisible.set(false)
            }
            3 -> {
                dashboardToolbarTitle.set("")
                isDashboardTitleVisible.set(false)
            }
            4 -> {
                dashboardToolbarTitle.set(dataManager.getResourceProvider().getString(R.string.me))
                isDashboardTitleVisible.set(true)
            }

        }
    }

    fun onToolbarMenuClick(view: View) {
        when (view.id) {
            R.id.img_icon_1 -> {
                manageIconOneClick()
            }

            R.id.img_icon_2 -> {
                manageIconTwoClick()
            }

            R.id.img_icon_3 -> {
                manageIconThreeClick()
            }
        }
    }

    private fun manageIconOneClick() {

    }

    private fun manageIconTwoClick() {
        when (scrollPosition) {
            0 -> {
                if (checkUserProfileCompletion(
                        dataManager.getResourceProvider()
                            .getString(R.string.complete_profile_use_feed)
                    )
                ) getNavigator()?.navigateToFeed()
            }

        }
    }

    private fun manageIconThreeClick() {
        when (scrollPosition) {
            0 -> getNavigator()?.navigateToAddMember()
            4 -> getNavigator()?.navigateToSetting()
            //1-> getNavigator()?.navigateToAddChallenge()
            1 -> {
                checkSportEquipment()
            }
        }
    }

    private fun checkSportEquipment() {
        if (isLoading.get()) return
        isLoading.set(true)
        compositeDisposable.add(
            dataManager.checkSportEquipmentAddedOrNot()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(this::onSuccess, this::onError)
        )
    }

    fun onSuccess(response: CheckSportsEquipmentAddedOrNotResponse) {
        isLoading.set(false)
        if (response.isSportEquipment ?: return) {
            if (checkUserProfileCompletion(
                    dataManager.getResourceProvider()
                        .getString(R.string.complete_profile_use_feed)
                )
            ) getNavigator()?.navigateToAddChallenge()
        } else {
            getNavigator()?.showSportsCompletionAlert(
                dataManager.getResourceProvider()
                    .getString(R.string.complete_profile_to_create_challenge)
            )
        }

    }

    fun onError(error: Throwable) {
        isLoading.set(false)
    }
}