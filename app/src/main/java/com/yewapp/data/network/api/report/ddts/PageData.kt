package com.yewapp.data.network.api.report.ddts

data class PageData(
    val current_page: Int,
    val last_page: Int,
    val per_page: Int,
    val total: Int
)