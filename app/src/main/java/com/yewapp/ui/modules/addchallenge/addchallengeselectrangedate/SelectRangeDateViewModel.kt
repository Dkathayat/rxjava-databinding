package com.yewapp.ui.modules.addchallenge.addchallengeselectrangedate

import android.os.Bundle
import androidx.databinding.ObservableField
import com.yewapp.data.network.DataManager
import com.yewapp.data.network.api.challenges.ChallengeModel
import com.yewapp.data.network.api.challenges.CreateChallengeResponse
import com.yewapp.data.network.api.createchallenge.CreateChallengeRequest
import com.yewapp.ui.base.BaseViewModel
import com.yewapp.ui.modules.listner.ChallengeExtras
import com.yewapp.utils.DateUtils
import com.yewapp.utils.rx.SchedulerProvider
import java.util.*

class SelectRangeDateViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) :
    BaseViewModel<SelectRangeDateNavigator>(dataManager, schedulerProvider) {


    lateinit var challengeModel: ChallengeModel

    var currentMonth = ObservableField<Date>()
    var isMonthYearSpinnerVisible = ObservableField<Boolean>(false)

    //    var challengeStatus = ObservableField<Int>(0)
    var monthYear = ObservableField<String>("")
    var month = ObservableField<String>("")
    var year = ObservableField<String>("")
    var previousBtnVisibility = ObservableField<Boolean>(false)
    var challengeExtras: ChallengeExtras? = null


    var startDate: String = ""
    var endDate: String = ""
    var isBtnActive = ObservableField<Boolean>(false)
    var challengeID = ObservableField<String>("")


    override fun setData(extras: Bundle?) {
        challengeModel =
            extras?.getParcelable<ChallengeModel>(ChallengeExtras.CHALLENGE_DATA) ?: return
    }

    fun onClickContinue() {
        getNavigator()?.clickOnContinue()
    }

    //when click on dropdown month & year
    fun onMonthYearClicked() {
        if (isMonthYearSpinnerVisible.get()!!) {
            getNavigator()?.animateLayout(false)
        } else {
            getNavigator()?.animateLayout(true)
        }
    }


    fun saveAsDraft() {
        if (!validate()) return
        if (isLoading.get()) return
        isLoading.set(true)

        compositeDisposable.add(
            dataManager.createChallenge(
                CreateChallengeRequest(
                    challengeID.get() ?: "",
                    true,
                    challengeModel.selectedSportId,
                    "",
                    "",
                    arrayListOf(),
                    arrayListOf(),
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    0,
                    dataManager.getUser().countryId,//challengeModel.countryId?.toInt(),
                    dataManager.getUser().stateId,// challengeModel.stateId?.toInt(),
                    arrayListOf(),
                    arrayListOf(),
                    arrayListOf(),
                    arrayListOf(),
                    "",
                    "",
                    "",
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
                        startDate
                    ),
                    DateUtils.convertCreateChallengeDatesToApiFormat(
                        endDate
                    ),
                    "",
                    "",
                    "",
                    "",
                    "",
                    arrayListOf(),
                    "2",
                    "","",""
                )
            ).subscribeOn(schedulerProvider.io()).observeOn(schedulerProvider.ui())
                .subscribe(this::saveAsDraftSuccess, this::saveAsDraftFailure)
        )


    }

    private fun saveAsDraftSuccess(response: CreateChallengeResponse) {
        isLoading.set(false)
        challengeID.set(response.id.toString())
        getNavigator()?.onSuccess("challenge saved")
    }

    private fun saveAsDraftFailure(error: Throwable) {
        isLoading.set(false)
    }

    fun validate(): Boolean {
        return when {
            startDate.isEmpty() -> {
                getNavigator()?.onError(Throwable("Please select start Date"))
                false
            }
            endDate.isEmpty() -> {

                getNavigator()?.onError(Throwable("Please select end Date"))
                false
            }

            else -> true
        }
    }


}