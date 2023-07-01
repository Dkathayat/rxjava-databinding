package com.yewapp.ui.modules.addchallenge.challengeroutes

enum class RouteEnum(val Type: String) {
    POPULAR("popular"),
    LATEST("latest"),
    FAVORITES("favourite")
}

enum class ManageUserEnum(val Type: String) {
    BLOCKED("blocked"),
    MUTED("muted"),
    REPORTED("reported"),
    FAVORITE("favorite")
}

enum class EmailPhoneChangeEnum(val Type: String) {
    EMAIL("Email"),
    MOBILE("Mobile")
}
