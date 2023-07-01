package com.yewapp.data.network.api.subscription

data class SubscriptionHistory(
    val current_plan: List<CurrentPlan>,
    val history: History
)

data class CurrentPlan(
    val due_date: String,
    val name: String,
    val plan: String,
    val purchase_date: String
)

data class History(
    val list: List<purchaseHistory>
)
data class purchaseHistory(
    val name: String,
    val plan: String,
    val purchase_date: String
)

