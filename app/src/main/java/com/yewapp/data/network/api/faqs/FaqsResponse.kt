package com.yewapp.data.network.api.faqs


/*
data class FaqsResponse(val data:List<FaqData>)
*/
data class FaqData(
    val id: Int,
    val answer: String,
    val position: String,
    val question: String,
    val status: String
)






