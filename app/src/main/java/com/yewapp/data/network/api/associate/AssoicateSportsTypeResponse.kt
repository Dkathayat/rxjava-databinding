package com.yewapp.data.network.api.associate

import com.yewapp.data.network.api.profile.SportType
import com.yewapp.data.network.api.sports.Sport


data class AssociateSportsTypeResponse(
    val city: String,
    val country: String,
    val fullName: String,
    val gender: String,
    val profileCoverImage: String,
    val profileImage: String,
    val sportsType: List<SportType>,
    val state: String
)
//
//data class SportsType(
//    val backgroundImage: String,
//    val description: String,
//    val equipList: List<Any>,
//    val gradeLevel: String,
//    val icon: String,
//    val id: String,
//    val isChecked: Boolean,
//    val isModelAdded: Boolean,
//    val modelList: List<Any>,
//    val name: String,
//    val profileType: String,
//    val sportId: String
//)
//data class AssociateSportsTypeResponse(
//    val profileImage: String?,
//    val profileCoverImage: String?,
//    val city: String?,
//    val country: String?,
//    val gender: String?,
//    val sportsType: List<SportType>
//)
//
//data class AssociateSportsType(
//    val background_image: String,
//    val description: String,
//    val icon: String,
//    val id: String,
//    var isChecked: Boolean,
//    val isModelAdded: Boolean,
//    val name: String,
//    val parent_id: String
//)


//data class AssociateSportsType(
//    val background_image: String,
//    val description: String,
//    val icon: String,
//    val id: String,
//    val isChecked: Boolean,
//    val isModelAdded: Boolean,
//    val name: String,
//    val parent_id: String
//)