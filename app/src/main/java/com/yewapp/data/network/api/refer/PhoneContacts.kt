package com.yewapp.data.network.api.refer

import android.graphics.Bitmap

data class PhoneContacts(
    var phone: String?,
    val name: String?,
    @Transient
    val id: String?,
    @Transient
    var bitmap: Bitmap?,
    @Transient
    var isBitmap: Boolean,
    @Transient
    var isSelected: Boolean=false
)
