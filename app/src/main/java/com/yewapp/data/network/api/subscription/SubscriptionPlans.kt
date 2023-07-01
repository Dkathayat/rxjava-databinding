package com.yewapp.data.network.api.subscription

data class SubscriptionPlans(
    val code: Int,
    val list: List<PlanDetails>,
    val message: String
)

data class PageData(
    val current_page: Int,
    val last_page: Int,
    val per_page: Int,
    val total: Int
)

data class PlanDetails(
    val createdAt: Int,
    val description: String,
    val duration: String,
    val feed: Boolean,
    val feedVidoeSize: String,
    val id: Int,
    val isBeacon: Boolean,
    val isChat: Boolean,
    val isCompareStats: Boolean,
    val isJoinChallenge: Boolean,
    val isLeaderBoardView: Boolean,
    val isManageChallenge: Boolean,
    val isManageDeviceConnectivity: Boolean,
    val isManageFeedback: Boolean,
    val isManageFollowers: Boolean,
    val isManageFollowing: Boolean,
    val isManageFreeAds: Boolean,
    val isManageRoute: Boolean,
    val isRecordActivity: Boolean,
    val isReferFriend: Boolean,
    val isShowParticipantsPicOnTrail: Boolean,
    val isViewHiddenEggs: Boolean,
    val isViewNearbyBikers: Boolean,
    val maximumAssociateAccountLimit: String,
    val maximumChallengeLimit: String,
    val maximumSpectatorLimit: String,
    var name: String,
    val price: String,
    val recordingLimit: String,
    val status: Boolean
)