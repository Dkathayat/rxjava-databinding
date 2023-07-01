package com.yewapp.data.network.api.profile

data class AddAssociateRequest(
    val firstName: String,
    val lastName: String,
    val phone: String,
    val gender: String,
    val dob: String,
    val weight: String,
    val heartRate: String,
    val status: String,
    val relation: String,
    val bio: String,
    val email: String,
    val password: String,
    val address: String,
    val cityId: String,
    val pincode: String,
    val profileCoverImage: String?,
    val profileImage: String?
) : Comparable<AddAssociateRequest> {
    override fun compareTo(addAssociateRequest: AddAssociateRequest): Int {
        return when {
            firstName != addAssociateRequest.firstName -> -1
            lastName != addAssociateRequest.lastName -> -1
            phone != addAssociateRequest.phone -> -1
            gender != addAssociateRequest.gender -> -1
            dob != addAssociateRequest.dob -> -1
            weight != addAssociateRequest.weight -> -1
            heartRate != addAssociateRequest.heartRate -> -1
            status != addAssociateRequest.status -> -1
            relation != addAssociateRequest.relation -> -1
            bio != addAssociateRequest.bio -> -1
            email.lowercase() != addAssociateRequest.email -> -1
//            profileImage != addAssociateRequest.profileImage -> -1
//            profileCoverImage != addAssociateRequest.profileCoverImage -> -1
            else -> 0
        }
    }
}
