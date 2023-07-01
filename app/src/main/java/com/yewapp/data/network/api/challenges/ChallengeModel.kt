package com.yewapp.data.network.api.challenges

import android.os.Parcelable
import com.yewapp.data.network.api.addmodelequipment.EquipmentData
import com.yewapp.data.network.api.invitemember.InviteMember
import kotlinx.parcelize.Parcelize


@Parcelize
data class ChallengeModel(
    //challenge step1 data
    var isEdit: Boolean = false,
    var challengeID: String = "",
    var step: Int?,
    val selectedSportId: Int?,
    val selectedSportImage: String?,
    val profileType: String?,
    val sportsName: String?,
    //challenge step2 data
    val startDate: String?,
    val endDate: String?,
    //challenge 3 step data
    val navigateToRouteFragment: Int?,
    val routeID: String = "",
    //Challenge 4 step data
    val challengeName: String?,
    val challengeVisibility: String?,
    val challengeStatus: String?,
    val selectedSportsLevel: ArrayList<String>,//sportsLevel/Grade level
    val ageGroup: ArrayList<String>,
    val challengeDescription: String?,
    val subSportTypeId: String?,
    val sportsEquipments: ArrayList<EquipmentData>,
    //    //step 5 A
    val challengeArea: String?,
    val location: String?,
    val latitude: Double?,
    val longitude: Double?,
    val radius: String?,
    //    //step 5 B
    var country: String = "",
    val countryId: Int = 0,
    var state: String = "",
    val stateId: Int = 0,
    val cityId: ArrayList<Int>,
    val zipCode: ArrayList<Int>,
    //step-6
    var calories: String?,
    var miles: String?,
    var elevationGain: String?,
    val avgWatt: String?,
    var time: String?,
    val maxMember: String?,
//    step-7
    val winnerPickedFrom: String?,
    val selectedWinnerPrize: String = "No",
    val overViewValue: String?,
    val winnerValue: String?,
    val additionalInfoValue: String?,
    val rulesValue: String?,
    val guidelinesValue: String?,
    val qualificationValue: String?,
    //step-8
    val bannerImage: String?,
    val challengeImage: String?,
    //step-9
    val inviteMembers: ArrayList<InviteMember>
) : Parcelable




