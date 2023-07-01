package com.yewapp.ui.modules.addchallenge.minimumgoal

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.databinding.ObservableField
import com.yewapp.R
import com.yewapp.data.network.DataManager
import com.yewapp.data.network.api.challenges.ChallengeModel
import com.yewapp.data.network.api.challenges.CreateChallengeResponse
import com.yewapp.data.network.api.challenges.MaxMemberResponse
import com.yewapp.data.network.api.createchallenge.CreateChallengeRequest
import com.yewapp.ui.base.BaseViewModel
import com.yewapp.ui.modules.listner.ChallengeExtras
import com.yewapp.utils.DateUtils
import com.yewapp.utils.rx.SchedulerProvider

class MinGoalRequirementViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) :
    BaseViewModel<MinGoalRequirementNavigator>(dataManager, schedulerProvider) {

    //    var challengeExtras: ChallengeExtras? = null
    var calories = ObservableField<String>("")
    var caloriesError = ObservableField<String>("")

    var miles = ObservableField<String>("")
    var milesError = ObservableField<String>("")
    var elevationGain = ObservableField<String>("")
    var elevationGainError = ObservableField<String>("")
    var avgWatt = ObservableField<String>("")
    var avgWattError = ObservableField<String>("")
    var time = ObservableField<String>("")
    var timeError = ObservableField<String>("")
    var maxMemberAllowed = ObservableField<Int>(0)
    var maxMember = ObservableField<String>("0")
    var maxMemberError = ObservableField<String>("")
    var maxLength = ObservableField<Int>(4)

    lateinit var challengeModelLocationData: ChallengeModel
    var isContinueActive = ObservableField<Boolean>(false)
//    var maxMemberArray = arrayListOf<String>()


    lateinit var challengeModel: ChallengeModel


    override fun setData(extras: Bundle?) {
//        challengeModelLocationData =
//            extras?.getParcelable(ChallengeExtras.CHALLENGE_DATA) ?: return
        challengeModel =
            extras?.getParcelable(ChallengeExtras.CHALLENGE_DATA) ?: return
        Log.d("STEP 6:", "${challengeModel}")

        if (challengeModel.isEdit) {
            calories.set(challengeModel.calories)
            miles.set(challengeModel.miles)
            elevationGain.set(challengeModel.elevationGain)
            avgWatt.set(challengeModel.avgWatt)
            time.set(challengeModel.time)
        } else {
            calories.set(challengeModel.calories)
            miles.set(challengeModel.miles)
            elevationGain.set(challengeModel.elevationGain)
            time.set(challengeModel.time)
        }
    }


    fun getMaxMemberToJoinChallenge() {
        if (isLoading.get()) return
        isLoading.set(true)
        compositeDisposable.add(
            dataManager.getMaxMemberCount()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(this::maxMemberSuccess, this::failure)
        )
    }

    private fun maxMemberSuccess(maxMemberResponse: MaxMemberResponse) {
        isLoading.set(false)
        maxMemberAllowed.set(maxMemberResponse.list.toInt())
        if (challengeModel.isEdit)
            maxMember.set(challengeModel.maxMember)

    }

    private fun failure(error: Throwable) {
        isLoading.set(false)
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
                    maxMember.get()?.toInt(),
                    dataManager.getUser().countryId,//challengeModel.countryId?.toInt(),
                    dataManager.getUser().stateId,// challengeModel.stateId?.toInt(),
                    challengeModel.cityId,
                    challengeModel.zipCode,
                    challengeModel.ageGroup,
                    arrayListOf(),
                    challengeModel.location ?: "",//hfjkdshkjfhsdjkhfkjsd
                    challengeModel.latitude.toString() ?: "",
                    challengeModel.longitude.toString() ?: "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    if (challengeModel.selectedWinnerPrize.equals(
                            "yes", ignoreCase = true
                        )
                    ) 1 else 0,
                    "",
                    "Goal",
                    DateUtils.convertCreateChallengeDatesToApiFormat(
                        challengeModel.startDate ?: return
                    ),
                    DateUtils.convertCreateChallengeDatesToApiFormat(
                        challengeModel.endDate ?: return
                    ),
                    "",
                    "",
                    "",
                    "",
                    challengeModel.challengeStatus,
                    arrayListOf(),
                    "4",
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
        when (view.id) {
            R.id.btn_continue -> {
                if (validate()) {
                    if (challengeModel.isEdit)
                        getNavigator()?.navigateToEditAdditionalFormScreen()
                    else
                        getNavigator()?.navigateToAdditionalFormScreen()
                }
            }
            R.id.type -> saveAsDraft()
//            R.id.etMaxMember -> getNavigator()?.onMaxMemberClicked(view)
        }
    }


    fun onCaloriesChange(s: CharSequence, start: Int, before: Int, count: Int) {
        caloriesError.set("")
        if (validate()) {
            isContinueActive.set(true)
        }
    }

    fun onMilesChange(s: CharSequence, start: Int, before: Int, count: Int) {
        milesError.set("")
        if (validate()) {
            isContinueActive.set(true)
        }
    }

//    fun onElevationGainChange(s: CharSequence, start: Int, before: Int, count: Int) {
//        elevationGainError.set("")
//        if (validate()) {
//            isContinueActive.set(true)
//        }
//    }

//    fun onAvgWattChange(s: CharSequence, start: Int, before: Int, count: Int) {
//        avgWattError.set("")
//        if (validate()) {
//            isContinueActive.set(true)
//        }
//    }

    //    fun onTimeChange(s: CharSequence, start: Int, before: Int, count: Int) {
//        timeError.set("")
//        if (validate()) {
//            isContinueActive.set(true)
//        }
//    }
    fun onMaxMemberChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        maxMemberError.set("")

        Handler().postDelayed({
            if (validateMember()) {
                isContinueActive.set(true)
            }
        }, 1000)

    }

//    private fun validateMaxMember(): Boolean {
//        return when {
//            maxMember.get().toString().trim().isEmpty() -> {
//                maxMemberError.set(
//                    dataManager.getResourceProvider().getString(R.string.empty_max_member_error)
//                )
//                false
//            }
//            maxMember.get()?.toInt()!! > (maxMemberAllowed.get() ?: 0) -> {
//                maxMemberError.set(
//                    dataManager.getResourceProvider()
//                        .getString(R.string.max_member_error) + maxMemberAllowed.get()
//                )
//                false
//            }
//
//            else -> true
//        }
//    }

    fun validate(): Boolean {
        return when {
            calories.get().toString().trim().isEmpty() -> {
                caloriesError.set(
                    dataManager.getResourceProvider().getString(R.string.empty_calories_error)
                )
                false
            }
            miles.get().toString().trim().isEmpty() -> {
                milesError.set(
                    dataManager.getResourceProvider().getString(R.string.empty_miles_error)
                )
                false
            }
            maxMember.get().toString().trim().isEmpty() -> {
                maxMemberError.set(
                    dataManager.getResourceProvider().getString(R.string.empty_max_member_error)
                )
                false
            }
            maxMember.get()?.toInt()!! > (maxMemberAllowed.get() ?: 0) -> {
                maxLength.set(maxMember.get()!!.length)
                maxMemberError.set(
                    dataManager.getResourceProvider()
                        .getString(R.string.max_member_error) + maxMemberAllowed.get()
                )
                false
            }

            else -> true
        }
    }


    fun validateMember(): Boolean {
        return when {
            maxMember.get().toString().trim().isEmpty() -> {
                maxMemberError.set(
                    dataManager.getResourceProvider().getString(R.string.empty_max_member_error)
                )
                false
            }
            maxMember.get()?.toInt()!! > (maxMemberAllowed.get()!!) -> {
                maxMemberError.set(
                    dataManager.getResourceProvider()
                        .getString(R.string.max_member_error) + " ${maxMemberAllowed.get()}"
                )
//                maxLength.set(maxMember.get()!!.length)
                false
            }
            calories.get().toString().trim().isEmpty() -> {
                caloriesError.set(
                    dataManager.getResourceProvider().getString(R.string.empty_calories_error)
                )
                false
            }
            miles.get().toString().trim().isEmpty() -> {
                milesError.set(
                    dataManager.getResourceProvider().getString(R.string.empty_miles_error)
                )
                false
            }

            else -> true
        }
    }


}