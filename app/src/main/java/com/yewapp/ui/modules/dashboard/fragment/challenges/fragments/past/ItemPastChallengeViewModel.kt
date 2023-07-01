package com.yewapp.ui.modules.dashboard.fragment.challenges.fragments.past

import android.text.Html
import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.yewapp.data.network.api.challenges.ChallengesDetails
import java.text.DecimalFormat
import kotlin.math.floor
import kotlin.math.log10

class ItemPastChallengeViewModel(
    val pastChallenge: ChallengesDetails,
    val flagPast: Boolean,
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
    var userRank = ObservableField<String>("")
    var minmumBikingPoint = ObservableField<String>("Min Biking Points : 2.4k")
    var challengeDescription = ObservableField<String>("")
    var challengeDescriptionVisibility = ObservableField<Boolean>(false)

    var isRewardTypeGiveAway = ObservableField<Boolean>(false)
    var rewardType = ObservableField<String>("")


    init {
        // set points
//        points = "${activeChallenge.earnedPoints}/${activeChallenge.maxPoint} pts"
//        isShow.set(flagActive)
        if (!pastChallenge.sponsorName.isNullOrEmpty()) {
            isSponsored.set(true)
            sponsorName.set(pastChallenge.sponsorName)
        }

        maxCalories.set("Calorie (${pastChallenge.minimumCalories})")//assign when creating challenge
        maxDistance.set("Distance (${pastChallenge.minimumMiles})")
        maxTime.set("Time (${pastChallenge.totalTime})")
        elevation.set("Elev. Gain (${pastChallenge.minimumElevationGain})")

        userCalories.set("${pastChallenge.userCalories} cal")//assign when creating challenge
        userDistance.set("${pastChallenge.userDistance} mile")
        userTime.set("${pastChallenge.userTotalTime}")
        userElevation.set(pastChallenge.userElevation)

        //check user calories is consumed or not
        if (pastChallenge.minimumCalories.equals(
                pastChallenge.userCalories,
                ignoreCase = true
            )
        ) {
            calorieConsumed.set(true)
        } else
            calorieConsumed.set(false)

        if (pastChallenge.minimumMiles.equals(
                pastChallenge.userDistance,
                ignoreCase = true
            )
        ) {
            distanceConsumed.set(true)
        } else
            distanceConsumed.set(false)

        if (pastChallenge.minimumCalories.equals(
                pastChallenge.userCalories,
                ignoreCase = true
            )
        ) {
            calorieConsumed.set(true)
        } else
            calorieConsumed.set(false)



        if (pastChallenge.challengeStatus == true)
            userRank.set("${pastChallenge.userRank} Rank")
        else {
            userRank.set("NOT ACHIEVED")
        }


        if (!pastChallenge.minimumPointsToJoin.isNullOrEmpty()) {
            minmumBikingPoint.set("Min Biking Points : ${pointFormatter(pastChallenge.minimumPointsToJoin.toInt())}")
        }

        if (!pastChallenge.description.isNullOrEmpty()) {
            challengeDescription.set(Html.fromHtml(pastChallenge.description).toString())
            challengeDescriptionVisibility.set(true)
        }


        if (pastChallenge.combinedRewardType.equals("giveaway", ignoreCase = true)) {
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