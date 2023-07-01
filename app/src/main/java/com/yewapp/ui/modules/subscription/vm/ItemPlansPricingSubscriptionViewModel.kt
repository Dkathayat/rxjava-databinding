package com.yewapp.ui.modules.subscription.vm

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.yewapp.data.network.api.subscription.PlanDetails

class ItemPlansPricingSubscriptionViewModel(
    val subscriptionPlan: PlanDetails,
    val postion: Int

) : ViewModel() {
    val planName = ObservableField<String>()
    val planPriceDate = ObservableField<String>()
    val feedDescription = ObservableField<String>()
    val challangeDescription = ObservableField<String>()
    val recordingLimit = ObservableField<String>()
    val manageRoute = ObservableField<String>()
    val manageAssociate = ObservableField<String>()
    val compareState = ObservableField<String>()
    val isChat = ObservableField<String>()
    val deviceConnect = ObservableField<String>()
    val bikerNearMe = ObservableField<String>()
    val hiddenEggs = ObservableField<String>()
    val showProfilePicture = ObservableField<String>()
    val isUpdating = ObservableField<Boolean>()


    init {
        setUserDataUi()
    }

    private fun setUserDataUi() {
        if (subscriptionPlan.name.isNullOrEmpty()) {
            planName.set("You Currently don't have any \nActive Plan")
            planPriceDate.set("$ 00.00 /month")
        } else {
            planName.set(subscriptionPlan.name + " Plan")
        }
        planPriceDate.set(subscriptionPlan.price + "/${subscriptionPlan.duration}")
        feedDescription.set("Feeds \nFeeds (Upload Up-To ${subscriptionPlan.feedVidoeSize} MB Size Video)")
        challangeDescription.set("Challenge \n(Create Max. ${subscriptionPlan.maximumChallengeLimit} Challenges)")
        recordingLimit.set("Video Feeds \n(Record Up-To ${subscriptionPlan.recordingLimit} Second’s Videos)")
        if (!subscriptionPlan.isManageRoute) {
            manageRoute.set("Routes \n(Create Route/Search Route)")
            isUpdating.set(true)
        } else {
            manageRoute.set("Routes \n(Create Route/Search Route)")
            isUpdating.set(false)
        }
        manageAssociate.set("Associate Account \n(Add Up-To ${subscriptionPlan.maximumAssociateAccountLimit} Associate Members)")
        if (!subscriptionPlan.isCompareStats) {
            compareState.set("Compare Stats")
            isUpdating.set(true)
        } else {
            compareState.set("Compare Stats")
            isUpdating.set(false)
        }
        if (!subscriptionPlan.isChat) {
            isChat.set("Chat")
            isUpdating.set(true)
        } else {
            isChat.set("Chat")
            isUpdating.set(false)
        }
        if (!subscriptionPlan.isManageDeviceConnectivity) {
            deviceConnect.set("Device Connectivity")
            isUpdating.set(true)
        } else {
            deviceConnect.set("Device Connectivity")
            isUpdating.set(false)
        }
        if (!subscriptionPlan.isViewNearbyBikers) {
            bikerNearMe.set("Bikers Near Me")
            isUpdating.set(true)
        } else {
            bikerNearMe.set("Bikers Near Me")
            isUpdating.set(false)
        }
        if (!subscriptionPlan.isViewHiddenEggs) {
            hiddenEggs.set("Hidden Egg On Trail")
            isUpdating.set(true)
        } else {
            hiddenEggs.set("Hidden Egg On Trail")
            isUpdating.set(false)
        }
        if (!subscriptionPlan.isShowParticipantsPicOnTrail) {
            showProfilePicture.set("Show User Picture On Map Route")
            isUpdating.set(true)
        } else {
            showProfilePicture.set("Show User Picture On Map Route")
            isUpdating.set(false)
        }
    }
}