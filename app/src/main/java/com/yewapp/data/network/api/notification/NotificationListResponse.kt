package com.yewapp.data.network.api.notification

import com.yewapp.data.network.api.routes.PageData

data class NotificationListResponse(val pageData: PageData?, val list: List<Notification>)

data class Notification(val id: Int, val title: String?, val dateTime: String?)
