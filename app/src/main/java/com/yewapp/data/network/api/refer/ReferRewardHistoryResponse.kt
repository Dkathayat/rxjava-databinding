package com.yewapp.data.network.api.refer


data class ReferRewardHistoryResponse(
    val history: List<ReferRewardHistory>,
    val pageData: PageData,
    val points: Int
)

data class ReferRewardHistory(
    val description: String?,
    val points: String?,
    val towhomreferred: String?
)

data class PageData(
    val current_page: Int,
    val last_page: Int,
    val per_page: Int,
    val total: Int
)