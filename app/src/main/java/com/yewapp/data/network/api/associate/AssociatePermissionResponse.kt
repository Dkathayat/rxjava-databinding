package com.yewapp.data.network.api.associate


data class AssociatePermissionResponse(
    val challenges: List<Challenge>,
    val feeds: List<Feed>,
    val routes: List<Route>,
    val video_feeds: List<VideoFeed>
)

data class Challenge(
    val id: Int,
    val isGranted: Boolean,
    val parent_slug: String,
    val parent_title: String,
    val permission_slug: String,
    val permission_title: String
)

data class Feed(
    val id: Int,
    val isGranted: Boolean,
    val parent_slug: String,
    val parent_title: String,
    val permission_slug: String,
    val permission_title: String
)

data class Route(
    val id: Int,
    val isGranted: Boolean,
    val parent_slug: String,
    val parent_title: String,
    val permission_slug: String,
    val permission_title: String
)

data class VideoFeed(
    val id: Int,
    val isGranted: Boolean,
    val parent_slug: String,
    val parent_title: String,
    val permission_slug: String,
    val permission_title: String
)