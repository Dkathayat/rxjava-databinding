package com.yewapp.ui.modules.addchallenge.additionalform

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.ObservableField
import com.yewapp.R
import com.yewapp.data.network.DataManager
import com.yewapp.data.network.api.challenges.ChallengeModel
import com.yewapp.data.network.api.challenges.CreateChallengeResponse
import com.yewapp.data.network.api.createchallenge.CreateChallengeRequest
import com.yewapp.ui.base.BaseViewModel
import com.yewapp.ui.modules.listner.ChallengeExtras
import com.yewapp.utils.DateUtils
import com.yewapp.utils.rx.SchedulerProvider

class AdditionalFormViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) :
    BaseViewModel<AdditionalFormNavigator>(dataManager, schedulerProvider) {

    var selectedWinnerPicked = ObservableField<String>("")
    val pickedWinnerError = ObservableField<String>("")

    var overViewValue = ObservableField<String>("")
    var additionalInfoValue = ObservableField<String>("")
    var rulesValue = ObservableField<String>("")
    var guidelinesValue = ObservableField<String>("")
    var qualificationValue = ObservableField<String>("")
    val overViewError = ObservableField<String>("")
    var winnerValue = ObservableField<String>("")
    val winnerError = ObservableField<String>("")
    val rulesError = ObservableField<String>("")
    val qualificationError = ObservableField<String>("")
    var challengeExtras: ChallengeExtras? = null
    var selectedWinnerPrize = ObservableField<String>("")
    var selectedWinnerPrizeError = ObservableField<String>("")
    var winnerPickedArray = arrayOf("From Leaderboard", "Randomly")
    var winnerPrizeAwardedArray = arrayOf("Yes", "No")
    var winnerAwardedValue: Int? = null
    var winnerPickedValue: String? = null

    var isContinueActive = ObservableField<Boolean>(false)
    var isWinnerPickedDisabled = ObservableField<Boolean>(false)

    lateinit var challengeModel: ChallengeModel


    override fun setData(extras: Bundle?) {
        challengeModel =
            extras?.getParcelable(ChallengeExtras.CHALLENGE_DATA) ?: return
        Log.d("STEP 7:", "${challengeModel}")

        if (challengeModel.selectedSportsLevel.size > 1) {
            isWinnerPickedDisabled.set(false)
            selectedWinnerPicked.set("Randomly")
        } else {
            isWinnerPickedDisabled.set(true)
        }

        if (challengeModel.isEdit)
            initEditDetailsView()

    }

    private fun initEditDetailsView() {
        selectedWinnerPicked.set(challengeModel.winnerPickedFrom)
        if (challengeModel.selectedWinnerPrize.equals("true")) {
            selectedWinnerPrize.set("Yes")
        } else {
            selectedWinnerPrize.set("No")
        }

        overViewValue.set(challengeModel.overViewValue)
        winnerValue.set(challengeModel.winnerValue)
        additionalInfoValue.set(challengeModel.additionalInfoValue)
        rulesValue.set(challengeModel.rulesValue)
        guidelinesValue.set(challengeModel.guidelinesValue)
        qualificationValue.set(challengeModel.qualificationValue)
    }

    fun onActionClick(view: View) {
        when (view.id) {
            R.id.btn_continue -> {
                if (validate()) {
                    if (challengeModel.isEdit)
                        getNavigator()?.navigateEditToBannerScreen()
                    else
                        getNavigator()?.navigateToBannerScreen()
                }
            }
            R.id.tvSaveAsDraft -> {
                saveAsDraft()
            }
            R.id.winner_picked_edt -> {
//                if (isWinnerPickedDisabled.get()!!) return
                getNavigator()?.onWinnerPickedUpClick(view)
            }
            R.id.prize_awarded_edt -> {
                getNavigator()?.onWinnerPrizeAwarded(view)
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
                    "",
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
                    overViewValue.get() ?: "",
                    additionalInfoValue.get() ?: "",
                    rulesValue.get() ?: "",
                    qualificationValue.get() ?: "",
                    "",
                    "",
                    if (selectedWinnerPrize.get().equals("Yes"))
                        1
                    else 0,
                    winnerPickedValue ?: "",
                    "Goal",
                    DateUtils.convertCreateChallengeDatesToApiFormat(
                        challengeModel.startDate ?: return
                    ),
                    DateUtils.convertCreateChallengeDatesToApiFormat(
                        challengeModel.endDate ?: return
                    ),
                    challengeModel.routeID ?: "",
                    "",
                    "",
                    "",
                    challengeModel.challengeStatus,
                    arrayListOf(),
                    "7",
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

    fun validate(): Boolean {
        return when {
            selectedWinnerPicked.get()?.isEmpty() == true -> {
                pickedWinnerError.set(
                    dataManager.getResourceProvider().getString(R.string.empty_select_winner_error)
                )
                false
            }

            selectedWinnerPrize.get()?.isEmpty() == true -> {
                selectedWinnerPrizeError.set(
                    dataManager.getResourceProvider().getString(R.string.empty_prize_error)
                )
                false
            }


            overViewValue.get()?.isEmpty() == true -> {
                overViewError.set(
                    dataManager.getResourceProvider().getString(R.string.empty_overview_error)
                )
                false
            }

            winnerValue.get()?.isEmpty() == true -> {
                winnerError.set(
                    dataManager.getResourceProvider().getString(R.string.empty_winner_error)
                )
                false
            }
            rulesValue.get()?.isEmpty() == true -> {
                rulesError.set(
                    dataManager.getResourceProvider().getString(R.string.empty_rules_error)
                )
                false
            }
            qualificationValue.get()?.isEmpty() == true -> {
                qualificationError.set(
                    dataManager.getResourceProvider().getString(R.string.empty_qualification_error)
                )
                false
            }
            else -> true
        }
    }


    fun onWinnerChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        winnerError.set("")
        if (validate()) {
            isContinueActive.set(true)
        }
    }

    fun onAwardChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        selectedWinnerPrizeError.set("")
        if (validate()) {
            isContinueActive.set(true)
        }
    }

    fun onOverviewChange(s: CharSequence, start: Int, before: Int, count: Int) {
        overViewError.set("")
        if (validate()) {
            isContinueActive.set(true)
        }
    }

    fun onWinnerChange(s: CharSequence, start: Int, before: Int, count: Int) {
        winnerError.set("")
        if (validate()) {
            isContinueActive.set(true)
        }
    }

    fun onRulesChange(s: CharSequence, start: Int, before: Int, count: Int) {
        rulesError.set("")
        if (validate()) {
            isContinueActive.set(true)
        }

    }

    fun onQualificationChange(s: CharSequence, start: Int, before: Int, count: Int) {
        qualificationError.set("")
        if (validate()) {
            isContinueActive.set(true)
        }

    }
}