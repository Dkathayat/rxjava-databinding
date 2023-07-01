package com.yewapp.ui.modules.managenotification.vm

import android.os.Bundle
import androidx.databinding.ObservableField
import com.yewapp.data.network.DataManager
import com.yewapp.data.network.api.notificationsetting.NotificationSettingRequest
import com.yewapp.ui.base.BaseViewModel
import com.yewapp.ui.modules.managenotification.navigator.SettingsFragmentNavigator
import com.yewapp.utils.rx.SchedulerProvider

class SettingsFragmentViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) :
    BaseViewModel<SettingsFragmentNavigator>(dataManager, schedulerProvider) {

    val activityBeacon = ObservableField<Boolean>(false)
    val activityComment = ObservableField<Boolean>(false)
    val activityDataSync = ObservableField<Boolean>(false)
    val activityLike = ObservableField<Boolean>(false)
    val activityLostPosition = ObservableField<Boolean>(false)
    val becomeSpectator = ObservableField<Boolean>(false)
    val challengeComment = ObservableField<Boolean>(false)
    val challengeInvite = ObservableField<Boolean>(false)
    val challengeJoined = ObservableField<Boolean>(false)
    val challengeLeaderboardChange = ObservableField<Boolean>(false)
    val challengeProgress = ObservableField<Boolean>(false)
    val challengeReward = ObservableField<Boolean>(false)
    val chatMessage = ObservableField<Boolean>(false)
    val chatSubscription = ObservableField<Boolean>(false)
    val emailAlerts = ObservableField<Boolean>(false)
    val featureSubscriptionTips = ObservableField<Boolean>(false)
    val feedComment = ObservableField<Boolean>(false)
    val feedLike = ObservableField<Boolean>(false)
    val friendActivity = ObservableField<Boolean>(false)
    val marketing = ObservableField<Boolean>(false)
    val newFollower = ObservableField<Boolean>(false)
    val newPublicChallenge = ObservableField<Boolean>(false)
    val onFriendJoin = ObservableField<Boolean>(false)
    val spectatedUserActivity = ObservableField<Boolean>(false)


    override fun setData(extras: Bundle?) {

    }

    init {
        getNotificationApiCall()
    }

    fun onSetNotificationApiCall() {
        isLoading.set(true)
        compositeDisposable.add(
            dataManager.setNotificationSetting(
                NotificationSettingRequest(
                    activityBeacon.get(),
                    activityComment.get(),
                    activityDataSync.get(),
                    activityLike.get(),
                    activityLostPosition.get(),
                    becomeSpectator.get(),
                    challengeComment.get(),
                    challengeInvite.get(),
                    challengeJoined.get(),
                    challengeLeaderboardChange.get(),
                    challengeProgress.get(),
                    challengeReward.get(),
                    chatMessage.get(),
                    //,chatSubscription.get(),
                    emailAlerts.get(),
                    featureSubscriptionTips.get(),
                    feedComment.get(),
                    feedComment.get(),
                    friendActivity.get(),
                    marketing.get(),
                    newFollower.get(),
                    newPublicChallenge.get(),
                    onFriendJoin.get(),
                    spectatedUserActivity.get()
                )
            ).subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(this::onSetNotificationSuccess, this::onSetNotificationFailure)
        )

    }

    private fun onSetNotificationSuccess(response: String) {
        isLoading.set(false)
        getNotificationApiCall()
    }

    private fun onSetNotificationFailure(t: Throwable) {
        isLoading.set(false)
        getNavigator()?.onError(
            t, false
        )
    }

    private fun getNotificationApiCall() {
        isLoading.set(true)
        compositeDisposable.add(
            dataManager.getNotificationSetting().subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(this::getNotificationSuccess, this::onSetNotificationFailure)
        )

    }

    private fun getNotificationSuccess(response: NotificationSettingRequest) {
        isLoading.set(false)
        activityLike.set(response.activityLike)
        activityComment.set(response.activityComment)
        activityLostPosition.set(response.activityLostPosition)
        activityDataSync.set(response.activityDataSync)
        activityBeacon.set(response.activityBeacon)

        becomeSpectator.set(response.becomeSpectator)
        spectatedUserActivity.set(response.spectatedUserActivity)

        onFriendJoin.set(response.onFriendJoin)
        friendActivity.set(response.friendActivity)

        newPublicChallenge.set(response.newPublicChallenge)
        challengeProgress.set(response.challengeProgress)
        challengeReward.set(response.challengeReward)
        challengeInvite.set(response.challengeInvite)
        challengeComment.set(response.challengeComment)
        challengeJoined.set(response.challengeJoined)
        challengeLeaderboardChange.set(response.challengeLeaderboardChange)

        feedLike.set(response.feedLike)
        feedComment.set(response.feedComment)

        chatMessage.set(response.activityLike)
        // chatSubscription.set(response.activityLike)

        marketing.set(response.marketing)
        featureSubscriptionTips.set(response.featureSubscriptionTips)

        emailAlerts.set(response.emailAlerts)


    }

}