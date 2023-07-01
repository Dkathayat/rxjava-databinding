package com.yewapp.ui.modules.addchallenge.challengepreview

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.ObservableField
import com.yewapp.R
import com.yewapp.data.local.PreferenceKeys.Companion.CHALLENGE_CREATED_STATUS
import com.yewapp.data.network.DataManager
import com.yewapp.data.network.api.challenges.ChallengeModel
import com.yewapp.data.network.api.challenges.CreateChallengeResponse
import com.yewapp.data.network.api.createchallenge.CreateChallengeRequest
import com.yewapp.ui.base.BaseViewModel
import com.yewapp.ui.modules.listner.ChallengeExtras
import com.yewapp.utils.DateUtils
import com.yewapp.utils.rx.SchedulerProvider

class ChallengePreviewViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) :
    BaseViewModel<ChallengePreviewNavigator>(dataManager, schedulerProvider) {
//    var challengeExtras: ChallengeExtras? = null


    lateinit var challengeModel: ChallengeModel

    //    var isContinueActive = ObservableField<Boolean>(false)
//    var noData = ObservableField<Boolean>(false)
    var scoreMessage =
        ObservableField<String>(dataManager.getResourceProvider().getString(R.string.score_get))
    var bannerImage = ObservableField<String>("")
    var challengeImage = ObservableField<String>("")
    var challengeName = ObservableField<String>("")
    var challengeLocation = ObservableField<String>("")
    var description = ObservableField<String>("")
    var startDateEnadDate = ObservableField<String>("")
    var maxMember = ObservableField<String>("Max. users 0")

    var isDraft = ObservableField<Boolean>(false)
    var memberImageStatus1 = ObservableField<Boolean>(false)
    var memberImageUrl1 = ObservableField<String>("")

    var memberImageStatus2 = ObservableField<Boolean>(false)
    var memberImageUrl2 = ObservableField<String>("")

    var memberImageStatus3 = ObservableField<Boolean>(false)
    var memberImageUrl3 = ObservableField<String>("")

    var memberImageStatus4 = ObservableField<Boolean>(false)
    var memberImageUrl4 = ObservableField<String>("")

    var memberImageStatus5 = ObservableField<Boolean>(false)
    var memberImageUrl5 = ObservableField<String>("")
    var athletes = ObservableField<String>("0 Athletes >")
    var athletesVisibility = ObservableField<Boolean>(false)

    var selectedSportImage = ObservableField<String>("")
    var qualificationValue = ObservableField<String>("")
    var winnerValue = ObservableField<String>("")


    override fun setData(extras: Bundle?) {
        challengeModel = extras?.getParcelable(ChallengeExtras.CHALLENGE_DATA) ?: return

        Log.d("STEP 10:", "${challengeModel}")

        setChallengeDetails(challengeModel)
    }

    fun setChallengeDetails(challengeModel: ChallengeModel) {
        bannerImage.set(challengeModel.bannerImage)
        challengeImage.set(challengeModel.challengeImage)
        challengeName.set(challengeModel.challengeName)

        scoreMessage.set("• Run ${challengeModel.miles} Miles \n• Burn ${challengeModel.calories} Calories \n• Total Time ${challengeModel.time}")
        selectedSportImage.set(challengeModel.selectedSportImage ?: "")
        qualificationValue.set(challengeModel.qualificationValue ?: "")
        winnerValue.set(challengeModel.winnerValue ?: "")


        if (challengeModel.challengeArea.equals("Radius Reach", ignoreCase = true)) {
            if (challengeModel.location.equals("")) {
                challengeLocation.set("${dataManager.getUser().city}, ${dataManager.getUser().country}")
            } else {
                challengeLocation.set(challengeModel.location)
            }
        } else
            challengeLocation.set("${challengeModel.state}, ${challengeModel.country}")




        description.set(challengeModel.challengeDescription)
        startDateEnadDate.set("${challengeModel.startDate} - ${challengeModel.endDate}")
        maxMember.set("Max. users ${challengeModel.maxMember}")


        if (challengeModel.inviteMembers.isNotEmpty()) {
            athletesVisibility.set(true)
            if (challengeModel.inviteMembers.size == 1) athletes.set("1 Athlete >")
            else athletes.set("+ ${challengeModel.inviteMembers.size} Athletes >")


            for (i in 0 until challengeModel.inviteMembers.size) {
                when (i) {
                    0 -> {
                        memberImageStatus1.set(challengeModel.inviteMembers[i].addStatus)
                        memberImageUrl1.set(challengeModel.inviteMembers[i].profileImage)
                    }
                    1 -> {
                        memberImageStatus2.set(challengeModel.inviteMembers[i].addStatus)
                        memberImageUrl2.set(challengeModel.inviteMembers[i].profileImage)
                    }

                    2 -> {
                        memberImageStatus3.set(challengeModel.inviteMembers[i].addStatus)
                        memberImageUrl3.set(challengeModel.inviteMembers[i].profileImage)
                    }

                    3 -> {
                        memberImageStatus4.set(challengeModel.inviteMembers[i].addStatus)
                        memberImageUrl4.set(challengeModel.inviteMembers[i].profileImage)
                    }
                    4 -> {
                        memberImageStatus5.set(challengeModel.inviteMembers[i].addStatus)
                        memberImageUrl5.set(challengeModel.inviteMembers[i].profileImage)
                    }
                }
            }
        } else {
            athletesVisibility.set(false)
        }

    }

    //Click to publish create channel
    fun onActionClicked(view: View) {
        when (view.id) {
            R.id.tvSaveAsDraft -> publishChallenge(true)
            R.id.btnPublish -> if (challengeModel.challengeStatus.equals("Active"))
                publishChallenge(false)
            else
                getNavigator()?.showAlertForInactiveChallenge()
        }
    }

     fun publishChallenge(isSaveAsDraft: Boolean) {
        if (isLoading.get()) return
        isLoading.set(true)
        this.isDraft.set(isSaveAsDraft)

        val inviteMembers = arrayListOf<String>()
        for (i in 0 until challengeModel.inviteMembers.size) {
            inviteMembers.add(challengeModel.inviteMembers.get(i).userId.toString())
        }

        compositeDisposable.add(
            dataManager.createChallenge(
                CreateChallengeRequest(
                    challengeModel.challengeID ?: "",
                    isSaveAsDraft,
                    challengeModel.selectedSportId,
                    challengeModel.subSportTypeId,
                    challengeModel.challengeName,
                    challengeModel.selectedSportsLevel,
                    challengeModel.sportsEquipments,
                    challengeModel.challengeVisibility,
                    challengeModel.challengeDescription,
                    challengeModel.calories,
                    challengeModel.miles,
                    challengeModel.elevationGain,
                    challengeModel.avgWatt,
                    challengeModel.time,
                    challengeModel.maxMember?.toInt(),
                    if (challengeModel.countryId == 0) dataManager.getUser().countryId
                    else challengeModel.countryId,
                    if (challengeModel.stateId == 0) dataManager.getUser().stateId
                    else challengeModel.stateId,
                    challengeModel.cityId,
                    challengeModel.zipCode,
                    challengeModel.ageGroup,
                    inviteMembers,
                    challengeModel.location,
                    challengeModel.latitude.toString(),
                    challengeModel.longitude.toString(),
                    challengeModel.overViewValue,
                    challengeModel.additionalInfoValue,
                    challengeModel.rulesValue,
                    challengeModel.qualificationValue,
                    "prizeType Static",
                    challengeModel.winnerValue,
                    if (challengeModel.selectedWinnerPrize.equals(
                            "yes", ignoreCase = true
                        )
                    ) 1 else 0,
                    challengeModel.winnerPickedFrom,
                    "Goal",
                    DateUtils.convertCreateChallengeDatesToApiFormat(
                        challengeModel.startDate ?: return
                    ),
                    DateUtils.convertCreateChallengeDatesToApiFormat(
                        challengeModel.endDate ?: return
                    ),
                    challengeModel.routeID.toString(),
                    "12",
                    challengeModel.challengeImage,
                    challengeModel.bannerImage ?: "",
                    challengeModel.challengeStatus,
                    arrayListOf(),
                    "10",
                    challengeModel.challengeArea ?: "",
                    challengeModel.radius ?: "", ""
                )
            ).subscribeOn(schedulerProvider.io()).observeOn(schedulerProvider.ui())
                .subscribe(this::publishChallengeSuccess, this::publishChallengeError)
        )

    }

    private fun publishChallengeSuccess(response: CreateChallengeResponse) {
        isLoading.set(false)
        challengeModel.challengeID = response.id.toString()
//        if (!isDraft.get()!!) {
        dataManager.getPreference().saveBoolean(CHALLENGE_CREATED_STATUS, true)
        getNavigator()?.createChallengeSuccess()
//        }
    }

    private fun publishChallengeError(throwable: Throwable) {
        isLoading.set(false)
        getNavigator()?.onError(throwable)
    }


    //This is Used to show Athlete listing
    fun onAthleteClick() {
        getNavigator()?.onAthleteClicked()
    }

    fun resetImageViews() {
        memberImageStatus1.set(false)
        memberImageStatus2.set(false)
        memberImageStatus3.set(false)
        memberImageStatus4.set(false)
        memberImageStatus5.set(false)
        memberImageUrl1.set("")
        memberImageUrl2.set("")
        memberImageUrl3.set("")
        memberImageUrl4.set("")
        memberImageUrl5.set("")
    }

}