package com.yewapp.data.network.api.notificationsetting

data class NotificationSettingRequest(
    val activityBeacon: Boolean?,
    val activityComment: Boolean?,
    val activityDataSync: Boolean?,
    val activityLike: Boolean?,
    val activityLostPosition: Boolean?,
    val becomeSpectator: Boolean?,
    val challengeComment: Boolean?,
    val challengeInvite: Boolean?,
    val challengeJoined: Boolean?,
    val challengeLeaderboardChange: Boolean?,
    val challengeProgress: Boolean?,
    val challengeReward: Boolean?,
    val chatMessage: Boolean?,
    //   val chatSubscription: Boolean?,
    val emailAlerts: Boolean?,
    val featureSubscriptionTips: Boolean?,
    val feedComment: Boolean?,
    val feedLike: Boolean?,
    val friendActivity: Boolean?,
    val marketing: Boolean?,
    val newFollower: Boolean?,
    val newPublicChallenge: Boolean?,
    val onFriendJoin: Boolean?,
    val spectatedUserActivity: Boolean?
)