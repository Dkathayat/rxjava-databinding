package com.yewapp.data.network.api.associate

data class SaveAssociateSportsTypeRequest(
    val associateId: Int,
    val profileImage: String,
    val coverImage: String,
    val associateSportsType: List<SaveAssociateSportsType>,
)

data class SaveAssociateSportsType(
    val id: String,
    val isChecked: Boolean
)