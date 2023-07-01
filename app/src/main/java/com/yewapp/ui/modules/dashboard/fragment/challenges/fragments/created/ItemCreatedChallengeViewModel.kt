package com.yewapp.ui.modules.dashboard.fragment.challenges.fragments.created

import android.text.Html
import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.yewapp.R
import com.yewapp.data.network.api.challenges.ChallengesDetails
import com.yewapp.utils.DateUtils
import java.text.DecimalFormat
import kotlin.math.floor
import kotlin.math.log10

class ItemCreatedChallengeViewModel(
    val createdChallengeDetails: ChallengesDetails,
    val flagCreated: Boolean,
    val listener: (Boolean,ChallengesDetails) -> Unit?
) : ViewModel() {


    var points = ""

    var isShow = ObservableField(false)
    var maxCalories = ObservableField<String>("")
    var maxDistance = ObservableField<String>("")
    var maxTime = ObservableField<String>("")
    var elevationType = ObservableField<String>("Elev. Gain")//ELev.Gain / Elev.Loss
    var elevation = ObservableField<String>("N/A")
    var sponsorName = ObservableField<String>("")
    var isSponsored = ObservableField<Boolean>(false)
    var haveNoChallengeData = ObservableField<Boolean>(false)
    var challengeStartFrom = ObservableField<Boolean>(false)
    var challengeStartMessage = ObservableField<String>("")
    var minmumBikingPoint = ObservableField<String>("Min Biking Points : 2.4k")
    var challengeDescription = ObservableField<String>("")
    var challengeDescriptionVisibility = ObservableField<Boolean>(false)

    init {
        // set points
//        points = "${createdChallengeDetails.earnedPoints}/${createdChallengeDetails.maxPoint} pts"
        isShow.set(flagCreated)

        if (!createdChallengeDetails.sponsorName.isNullOrEmpty()) {
            isSponsored.set(true)
            sponsorName.set(createdChallengeDetails.sponsorName)
        }
//        sponsorName.set(createdChallengeDetails.sponsorName)

        if (createdChallengeDetails.minimumCalories.isNullOrEmpty())
            haveNoChallengeData.set(true)
        else {
            maxCalories.set("${createdChallengeDetails.minimumCalories} cal")
            maxDistance.set("${createdChallengeDetails.minimumMiles} mile")
            maxTime.set("${createdChallengeDetails.totalTime}")
            elevation.set(createdChallengeDetails.minimumElevationGain)
        }

        if (createdChallengeDetails.isDraft == false) {
            val startDate =
                createdChallengeDetails.startDate?.toLong()
                    ?.let { DateUtils.getDateFromTimeStamp(it) }
            challengeStartMessage.set(
                "Starts in ${
                    DateUtils.getRemainingTime(startDate).replace("\n", " ")
                }"
            )
        } else {
            challengeStartMessage.set("Please create your challenge")
        }


        if (!createdChallengeDetails.minimumPointsToJoin.isNullOrEmpty()) {
            minmumBikingPoint.set("Min Biking Points : ${pointFormatter(createdChallengeDetails.minimumPointsToJoin.toInt())}")
        }

        if (!createdChallengeDetails.description.isNullOrEmpty()) {
            challengeDescription.set(Html.fromHtml(createdChallengeDetails.description).toString())
            challengeDescriptionVisibility.set(true)
        }
    }

    fun onItemClick(view: View) {
        when (view.id) {
            R.id.draft_name -> listener(true,createdChallengeDetails)
            R.id.mainLayout -> if (!createdChallengeDetails.isDraft) listener(false,createdChallengeDetails)
        }

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