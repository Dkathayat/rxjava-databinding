package com.yewapp.ui.modules.addchallenge.invitemember

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yewapp.R
import com.yewapp.data.network.DataManager
import com.yewapp.data.network.api.challenges.ChallengeModel
import com.yewapp.data.network.api.challenges.CreateChallengeResponse
import com.yewapp.data.network.api.createchallenge.CreateChallengeRequest
import com.yewapp.data.network.api.invitemember.InviteMember
import com.yewapp.data.network.api.invitemember.InviteMemberListResponse
import com.yewapp.ui.base.BaseViewModel
import com.yewapp.ui.modules.listner.ChallengeExtras
import com.yewapp.utils.DateUtils
import com.yewapp.utils.rx.SchedulerProvider

class InviteMemberViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) :
    BaseViewModel<InviteMemberNavigator>(dataManager, schedulerProvider) {

    var lastPage: Int = 1
    var currentPage: Int = 1
    var perPage: Int = 10

    val memberList = mutableListOf<InviteMember>()
    val _inviteMemberList = MutableLiveData<List<InviteMember>>()
    val inviteMemberList: LiveData<List<InviteMember>> get() = _inviteMemberList
    var challengeExtras: ChallengeExtras? = null


    lateinit var challengeModel: ChallengeModel
    var isContinueActive = ObservableField<Boolean>(false)
    var noData = ObservableField<Boolean>(false)


    override fun setData(extras: Bundle?) {
        challengeModel = extras?.getParcelable(ChallengeExtras.CHALLENGE_DATA) ?: return
        Log.d("STEP 9:", "${challengeModel}")

    }


    init {
        getMemberList()
    }

    private fun getMemberList() {
        if (isLoading.get()) return
        isLoading.set(true)
        compositeDisposable.add(
            dataManager.getInviteMemberList().subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui()).subscribe(this::onSuccess, this::onFailure)
        )
    }

    private fun onSuccess(response: InviteMemberListResponse) {
        isLoading.set(false)
        if (response.list.isNotEmpty()) {

            if (challengeModel.isEdit) {
                var count = 0
                for (i in 0 until response.list.size) {
                    for (j in 0 until challengeModel.inviteMembers.size) {
                        if ((response.list[i].userId
                                ?: return) == (challengeModel.inviteMembers[j].userId ?: return)
                        ) {
                            count++
                            response.list[i].addStatus = true
                        }
                    }
                }
                if (count != 0) isContinueActive.set(true)
            }
            memberList.addAll(response.list)
            _inviteMemberList.value = response.list
            noData.set(false)
        } else {
            noData.set(true)
            isContinueActive.set(true)
        }
        currentPage = response.pageData?.current_page!!
        perPage = response.pageData.per_page!!
        lastPage = response.pageData.last_page!!
    }

    private fun onFailure(t: Throwable) {
        isLoading.set(false)
        getNavigator()?.onError(t)
    }

    fun onActionClick(view: View) {
        when (view.id) {
            R.id.tvSaveAsDraft -> {
                saveAsDraft()
            }
            R.id.tvSkip -> {
                getNavigator()?.skipScreen()
            }
            R.id.btn_continue -> getNavigator()?.onContinueClick()
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
                    challengeModel.challengeImage ?: "",
                    challengeModel.bannerImage ?: "",
                    challengeModel.challengeStatus,
                    arrayListOf(),
                    "9",
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

}