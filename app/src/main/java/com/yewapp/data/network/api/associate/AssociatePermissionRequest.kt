package com.yewapp.data.network.api.associate

data class AssociatePermissionRequest(
    val associateId: Int,
    val permission: List<AssociatePermission>
)

data class AssociatePermission(
    val id: Int,
    val status: Boolean
)