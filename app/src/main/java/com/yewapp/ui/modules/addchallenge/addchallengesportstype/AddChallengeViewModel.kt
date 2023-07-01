package com.yewapp.ui.modules.addchallenge.addchallengesportstype

import android.os.Bundle
import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yewapp.R
import com.yewapp.data.network.DataManager
import com.yewapp.data.network.api.challenges.ChallengeModel
import com.yewapp.data.network.api.sports.Sport
import com.yewapp.ui.base.BaseViewModel
import com.yewapp.utils.rx.SchedulerProvider

class AddChallengeViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) :
    BaseViewModel<AddChallengeNavigator>(dataManager, schedulerProvider) {

    var selectedSportId = 0
    var sportsType = ""
    var riderType = ""

    // values added that get from response
    lateinit var challengeModel: ChallengeModel
    var sportList = mutableListOf<Sport>()
    var _sportsList = MutableLiveData<List<Sport>>()
    val sportListLive: LiveData<List<Sport>> get() = _sportsList

    var isBtnActive = ObservableField<Boolean>(false)
    var noData = ObservableField<Boolean>(false)

    override fun setData(extras: Bundle?) {
    }

    init {
        getSportsList()
    }

    private fun getSportsList() {
        isLoading.set(true)
        compositeDisposable.add(
            dataManager.getSportsList()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(this::success, this::failure)
        )
    }

    private fun failure(error: Throwable) {
        isLoading.set(false)
        getNavigator()?.onError(error, false)
    }

    private fun success(response: List<Sport>) {
        isLoading.set(false)
        if (response.isNotEmpty()) {
            sportList.addAll(response)
            _sportsList.value = sportList
        } else {
            noData.set(true)
        }
    }

    fun saveAsDraft() {
        isLoading.set(true)
//        compositeDisposable.add(
//            dataManager.createChallenge(
//                CreateChallengeRequest(
//                    true, selectedSportId, null, "", "", "", "", "",
//                    "", "", "", "", null, null, null, null, null, "", "",
//                    "", "", "", "", "", "", "", "", 0, "", "", "",
//                    "", "", "", "", "", 1,""
//                )
//            )
//                .subscribeOn(schedulerProvider.io())
//                .observeOn(schedulerProvider.ui())
//                .subscribe(this::challengeSuccess, this::failure)
//        )
    }

    private fun challengeSuccess(response: String) {
        isLoading.set(false)
    }

    fun onActionClick(view: View) {
        when (view.id) {
            R.id.btn_continue -> {
                if (selectedSportId != 0) {
                    getNavigator()?.navigateToSelectDateRange()
                } else {
                    getNavigator()?.navigateToastMessage()
                }
            }
            R.id.type -> getNavigator()?.saveAsDraft()
        }
    }
}