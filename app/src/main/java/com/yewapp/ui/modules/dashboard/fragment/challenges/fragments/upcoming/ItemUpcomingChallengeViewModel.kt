package com.yewapp.ui.modules.dashboard.fragment.challenges.fragments.upcoming

import android.text.Html
import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.yewapp.data.network.api.challenges.ChallengesDetails
import com.yewapp.utils.DateUtils
import java.text.DecimalFormat
import kotlin.math.floor
import kotlin.math.log10

class ItemUpcomingChallengeViewModel(
    val activeChallengeDetails: ChallengesDetails,
    val flagActive: Boolean,
    val listener: () -> Unit?
) : ViewModel() {
    var points = ""

    //    var isShow = ObservableField(false)


    var isShow = ObservableField<Boolean>(false)
    var calorieConsumed = ObservableField<Boolean>(false)
    var distanceConsumed = ObservableField<Boolean>(false)
    var maxCalories = ObservableField<String>("")
    var maxDistance = ObservableField<String>("")
    var maxTime = ObservableField<String>("")
    var elevation = ObservableField<String>("N/A")
    var userCalories = ObservableField<String>("")
    var userDistance = ObservableField<String>("")
    var userTime = ObservableField<String>("")
    var userElevation = ObservableField<String>("N/A")


    var elevationType = ObservableField<String>("Elev. Gain")//ELev.Gain / Elev.Loss
    var sponsorName = ObservableField<String>("")
    var isSponsored = ObservableField<Boolean>(false)

    //    var haveNoChallengeData = ObservableField<Boolean>(false)
    var challengeStartFrom = ObservableField<Boolean>(false)
    var challengeEndRemainingTime = ObservableField<String>("")
    var minmumBikingPoint = ObservableField<String>("Min Biking Points : 2.4k")
    var challengeDescription = ObservableField<String>("")
    var challengeDescriptionVisibility = ObservableField<Boolean>(false)

    var isRewardTypeGiveAway = ObservableField<Boolean>(false)
    var rewardType = ObservableField<String>("")


    var challengeStartMessage = ObservableField<String>("")


    init {
        // set points
//        points = "${activeChallenge.earnedPoints}/${activeChallenge.maxPoint} pts"
//        isShow.set(flagActive)
        if (!activeChallengeDetails.sponsorName.isNullOrEmpty()) {
            isSponsored.set(true)
            sponsorName.set(activeChallengeDetails.sponsorName)
        }


        val startDate =
            activeChallengeDetails.startDate?.toLong()
                ?.let { DateUtils.getDateFromTimeStamp(it) }
        challengeStartMessage.set(
            "Starts in ${
                DateUtils.getRemainingTime(startDate).replace("\n", " ")
            }"
        )

        maxCalories.set("${activeChallengeDetails.minimumCalories} cal")//assign when creating challenge
        maxDistance.set("${activeChallengeDetails.minimumMiles} mil")
        maxTime.set("${activeChallengeDetails.totalTime}")
        elevation.set("${activeChallengeDetails.minimumElevationGain} ft")

//        userCalories.set("${activeChallengeDetails.userCalories} cal")//assign when creating challenge
//        userDistance.set("${activeChallengeDetails.userDistance} mile")
//        userTime.set("${activeChallengeDetails.userTotalTime}")
//        userElevation.set(activeChallengeDetails.userElevation)

        //check user calories is consumed or not
//        if (activeChallengeDetails.minimumCalories.equals(
//                activeChallengeDetails.userCalories,
//                ignoreCase = true
//            )
//        ) {
//            calorieConsumed.set(true)
//        } else
//            calorieConsumed.set(false)

//        if (activeChallengeDetails.minimumMiles.equals(
//                activeChallengeDetails.userDistance,
//                ignoreCase = true
//            )
//        ) {
//            distanceConsumed.set(true)
//        } else
//            distanceConsumed.set(false)

//        if (activeChallengeDetails.minimumCalories.equals(
//                activeChallengeDetails.userCalories,
//                ignoreCase = true
//            )
//        ) {
//            calorieConsumed.set(true)
//        } else
//            calorieConsumed.set(false)


//        if (activeChallengeDetails.isDraft == false) {
//            val startDate =
//                activeChallengeDetails.startDate?.toLong()
//                    ?.let { DateUtils.getDateFromTimeStamp(it) }
//            challengeEndRemainingTime.set(
//                "${
//                    DateUtils.getRemainingTime(startDate).replace("\n", " ")
//                } Left"
//            )
//        }


        if (!activeChallengeDetails.minimumPointsToJoin.isNullOrEmpty()) {
            minmumBikingPoint.set("Min Biking Points : ${pointFormatter(activeChallengeDetails.minimumPointsToJoin.toInt())}")
        }

        if (!activeChallengeDetails.description.isNullOrEmpty()) {
            challengeDescription.set(Html.fromHtml(activeChallengeDetails.description).toString())
            challengeDescriptionVisibility.set(true)
        }


        if (activeChallengeDetails.combinedRewardType.equals("giveaway", ignoreCase = true)) {
            rewardType.set("GIVEAWAY ENTERED")
            isRewardTypeGiveAway.set(true)
        } else {
            isRewardTypeGiveAway.set(false)
        }
    }

    fun onItemClick(view: View) {
        listener()
    }


    //convert to k, m etc.
    private fun pointFormatter(number: Number): String? {
        val suffix = charArrayOf(' ', 'k', 'M', 'B', 'T', 'P', 'E')
        val numValue = number.toLong()
        val value = floor(log10(numValue.toDouble())).toInt()
        val base = value / 3
        return if (value >= 3 && base < suffix.size) {
            DecimalFormat("#0.0").format(
                numValue / Math.pow(
                    10.0,
                    (base * 3).toDouble()
                )
            ) + suffix[base]
        } else {
            DecimalFormat("#,##0").format(numValue)
        }
    }
}