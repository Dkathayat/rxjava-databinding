package com.yewapp.ui.modules.addchallenge.challengebanner

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.amazonaws.auth.BasicAWSCredentials
import com.yewapp.R
import com.yewapp.data.network.DataManager
import com.yewapp.data.network.api.challenges.ChallengeModel
import com.yewapp.data.network.api.challenges.CreateChallengeResponse
import com.yewapp.data.network.api.createchallenge.CreateChallengeRequest
import com.yewapp.ui.base.BaseViewModel
import com.yewapp.ui.modules.listner.ChallengeExtras
import com.yewapp.utils.DateUtils
import com.yewapp.utils.rx.SchedulerProvider

class ChallengeBannerViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) :
    BaseViewModel<ChallengeBannerNavigator>(dataManager, schedulerProvider) {

    var awsCredentials: BasicAWSCredentials

    val isUploading = ObservableBoolean(false)
    val bannerIcon = ObservableBoolean(false)
    val bannerImage = ObservableField<String>()
    val challengeImage = ObservableField<String>()
    val challengeAddIconVisibility = ObservableBoolean(true)
    val bannerAddIconVisibility = ObservableBoolean(true)
//    var challengeExtras: ChallengeExtras? = null

    lateinit var challengeModel: ChallengeModel
    var isContinueActive = ObservableField<Boolean>(false)


    init {
        awsCredentials = BasicAWSCredentials(
            dataManager.getAwsCredential().bucket_key,
            dataManager.getAwsCredential().bucket_secret_key
        )
    }

    override fun setData(extras: Bundle?) {
        challengeModel =
            extras?.getParcelable(ChallengeExtras.CHALLENGE_DATA) ?: return
        Log.d("STEP 8:", "${challengeModel}")

        if (challengeModel.isEdit) {
            if (challengeModel.challengeImage?.isNotEmpty()!!) {
                challengeImage.set(challengeModel.challengeImage)
                challengeAddIconVisibility.set(false)
            }
            if (challengeModel.bannerImage?.isNotEmpty()!!) {
                bannerImage.set(challengeModel.bannerImage)
                bannerAddIconVisibility.set(false)
                isContinueActive.set(true)
            }
        }
    }

    private fun saveAsDraft() {
        if (isLoading.get()) return
        isLoading.set(true)

        compositeDisposable.add(
            dataManager.createChallenge(
                CreateChallengeRequest(
                    challengeModel.challengeID ?: "",
                    true,
                    challengeModel.selectedSportId,
                    challengeModel.subSportTypeId ?: "",
                    challengeModel.challengeName,
                    challengeModel.selectedSportsLevel,
                    challengeModel.sportsEquipments,
                    challengeModel.challengeVisibility,
                    challengeModel.challengeDescription,
                    "",
                    challengeModel.miles ?: "",
                    challengeModel.elevationGain ?: "",
                    challengeModel.avgWatt ?: "",
                    challengeModel.time ?: "",
                    challengeModel.maxMember?.toInt(),
                    dataManager.getUser().countryId,//challengeModel.countryId?.toInt(),
                    dataManager.getUser().stateId,// challengeModel.stateId?.toInt(),
                    challengeModel.cityId,
                    challengeModel.zipCode,
                    challengeModel.ageGroup,
                    arrayListOf(),
                    challengeModel.location ?: "",//hfjkdshkjfhsdjkhfkjsd
                    challengeModel.latitude.toString() ?: "",
                    challengeModel.longitude.toString() ?: "",
                    challengeModel.overViewValue ?: "",
                    challengeModel.additionalInfoValue ?: "",
                    challengeModel.rulesValue ?: "",
                    challengeModel.qualificationValue ?: "",
                    "",
                    "",
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
                    "${challengeModel.routeID ?: ""}",
                    "",
                    challengeImage.get() ?: "",
                    bannerImage.get() ?: "",
                    challengeModel.challengeStatus,
                    arrayListOf(),
                    "8",
                    challengeModel.challengeArea ?: "",
                    challengeModel.radius ?: "", ""
                )
            ).subscribeOn(schedulerProvider.io()).observeOn(schedulerProvider.ui())
                .subscribe(this::saveAsDraftSuccess, this::saveAsDraftFailure)
        )

    }

    private fun saveAsDraftSuccess(response: CreateChallengeResponse) {
        isLoading.set(false)
        challengeModel.challengeID = response.id.toString()
        getNavigator()?.onSuccess("challenge saved")
    }

    private fun saveAsDraftFailure(error: Throwable) {
        isLoading.set(false)
    }

    fun onActionClick(view: View) {
        if (isLoading.get()) return
        when (view.id) {
            R.id.btn_continue ->if(challengeModel.isEdit){
                getNavigator()?.navigateToEditInviteMemberScreen()
            }else{
                getNavigator()?.navigateToInviteMemberScreen()
            }
            R.id.add_icon -> {
                bannerIcon.set(true)
                getNavigator()?.launchImageOptions()
            }
            R.id.challenge_add_icon -> {
                bannerIcon.set(false)
                getNavigator()?.launchImageOptions()
            }
            R.id.cross_icon -> {
                bannerImage.set("")
                bannerAddIconVisibility.set(true)
                isContinueActive.set(false)
            }
            R.id.challenge_cross_icon -> {
                challengeImage.set("")
                challengeAddIconVisibility.set(true)
                isContinueActive.set(false)
            }
            R.id.tvSaveAsDraft -> {
                // if (validate()) {
                saveAsDraft()
                // }
            }
        }
    }


    fun validate(): Boolean {
        return when {
            bannerImage.get().isNullOrEmpty() -> {
//                pickedWinnerError.set(
//                    dataManager.getResourceProvider().getString(R.string.empty_select_winner_error)
//                )
                isContinueActive.set(false)
                false
            }

            challengeImage.get().isNullOrEmpty() -> {
                isContinueActive.set(false)
                false
            }

            else -> {
                true
            }
        }
    }

}