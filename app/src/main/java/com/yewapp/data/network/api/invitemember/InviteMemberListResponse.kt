package com.yewapp.data.network.api.invitemember

import android.os.Parcelable
import com.yewapp.data.network.api.routes.PageData
import kotlinx.parcelize.Parcelize

data class InviteMemberListResponse(val pageData: PageData?, val list: List<InviteMember>)

//data class InviteMember(val id: Int?, val name: String?, val profileImage: String?, val city: String?, val country: String?)
@Parcelize
data class InviteMember(
    val userId: Int?,
    val fullName: String?,
    val roleId: String?,
    val first_name: String?,
    val last_name: String?,
    val email: String?,
    val city: String?,
    val state: String?,
    val country: String?,
    val profileImage: String?,
    var addStatus: Boolean
): Parcelable
