package com.yewapp.ui.modules.profile.fragment.mypoints

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.yewapp.R
import com.yewapp.data.network.DataManager
import com.yewapp.data.network.api.feed.CreateFeedRequest
import com.yewapp.data.network.api.mypoint.*
import com.yewapp.data.network.api.profile.ActivitiesData
import com.yewapp.data.network.api.sports.ProfileSportsResponse
import com.yewapp.ui.base.BaseViewModel
import com.yewapp.ui.modules.profile.navigator.MyPointsNavigator
import com.yewapp.utils.rx.SchedulerProvider
import com.yewapp.utils.toJson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyPointsViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) :
    BaseViewModel<MyPointsNavigator>(dataManager, schedulerProvider) {

    private val _myPointsData = MutableLiveData<List<UserPointSummaryData>>()
    val myPointsData: MutableLiveData<List<UserPointSummaryData>> get() = _myPointsData
    val myPointsList = mutableListOf<UserPointSummaryData>()

    private val _profileSports = MutableLiveData<List<ProfileSportsResponse>>()
    val profileSports: MutableLiveData<List<ProfileSportsResponse>> get() = _profileSports
    val profileSportsList = mutableListOf<ProfileSportsResponse>()
    val profileSportId = ObservableField<Int>()

    private val _userPointsHistory = MutableLiveData<List<MyPointsHistoryDetails>>()
    val userPointsHistory: MutableLiveData<List<MyPointsHistoryDetails>> get() = _userPointsHistory
    val userPointsHistoryList = mutableListOf<MyPointsHistoryDetails>()

    var hiMessage = ObservableField<String>("")
    var totalPoints = ObservableField<String>("0")
    var runningPoints = ObservableField<String>("0")
    var bikingPoints = ObservableField<String>("0")
    var hikingPoints = ObservableField<String>("0")
    val activityDataSync = ObservableField<Boolean>(false)
    var buttonEnabled = ObservableBoolean(false)
    var isRunning = ObservableField<Boolean>(true)
    var isBiking = ObservableField<Boolean>(false)
    var isHiking = ObservableField<Boolean>(false)
    var page = 1
    var startDate = ObservableField<String>("")
    var endDate = ObservableField<String>("")
    var category = ObservableField<String>("")

    override fun setData(extras: Bundle?) {

    }

    init {
        hiMessage.set("Hi, ${dataManager.getUser().firstName} ${dataManager.getUser().lastName} below is your total point")
    }


    fun onItemClick(view: View) {
        when (view.id) {
            R.id.llRunning -> {
                isRunning.set(true)
                isBiking.set(false)
                isHiking.set(false)
                category.set("running")
                getUserSummary("","")

            }
            R.id.llBiking -> {
                isBiking.set(true)
                isRunning.set(false)
                isHiking.set(false)
                category.set("biking")
                getUserSummary("","")
            }
            R.id.llHiking -> {
                isHiking.set(true)
                isBiking.set(false)
                isRunning.set(false)
                category.set("hiking")
                getUserSummary("","")
            }
         }
    }

    fun sharePointsActivity(activityData: UserPointSummaryData) {
        if (isLoading.get()) return
        isLoading.set(true)
        compositeDisposable.add(
            dataManager.createFeed(
                CreateFeedRequest(
                    activityData.name,
                    activityData.description,
                    false,
                    activityData.location,
                    "",
                    "",
                    activityData.category_id,
                    mutableListOf(),
                    listOf(),
                    listOf()
                )
            ).subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(this::onFeedSuccess, this::onFailure)
        )
    }

    private fun onFeedSuccess(response: String) {
        isLoading.set(false)

    }

    private fun onFailure(error: Throwable) {
        isLoading.set(false)
        getNavigator()?.onError(error)
    }

    fun getUserPointHistoryData(
        startDate: String,
        endDate: String,
        sportId: List<String>,
        subSport: List<String>
    ) {
        if (isLoading.get()) return
        isLoading.set(true)
        compositeDisposable.add(
            dataManager.getUserPointsHistory(
                page,
                startDate,
                endDate,
                sportId,
                subSport
            ).subscribeOn(schedulerProvider.io()).observeOn(schedulerProvider.ui())
                .subscribe(this::pointsHistorySuccess, this::failure)
        )
    }

    private fun pointsHistorySuccess(response: MyPointsHistoryResponse) {
        isLoading.set(false)
        userPointsHistoryList.clear()
        userPointsHistoryList.addAll(response.list)
        _userPointsHistory.value = userPointsHistoryList


    }

    fun getUserSummary(startDate: String, endDate: String) {
        isLoading.set(true)
        myPointsList.clear()
        compositeDisposable.add(
            dataManager.getUserPointSummary(
                page,
                category.get() ?: return,
                startDate,
                endDate,
                "",
                listOf()
            ).subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(this::userSummarySuccess, this::failure)
        )
    }


    private fun userSummarySuccess(response: UserPointSummaryResponse) {
        isLoading.set(false)
        category.set("")
        myPointsList.addAll(response.list)
        _myPointsData.value = myPointsList

    }

    private fun failure(error: Throwable) {
        isLoading.set(false)
        getNavigator()?.onError(error, false)

    }


    fun getMyPointApi() {
        if (isLoading.get()) return
        isLoading.set(true)

        compositeDisposable.add(
            dataManager.getMyPoints().subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(this::onMyPointSuccess, this::onMyPointError)
        )

    }

    private fun onMyPointSuccess(response: MyPointResponse) {
        isLoading.set(false)
        runningPoints.set("${response.running.toInt() / 1000}k")
        bikingPoints.set("${response.biking.toInt() / 1000}k")
        hikingPoints.set("${response.hiking.toInt() / 1000}k")
        totalPoints.set(response.totalPoints.toString())
    }

    private fun onMyPointError(error: Throwable) {
        isLoading.set(false)
    }

    fun getProfileSportsList() {
        if (isLoading.get()) return
        isLoading.set(true)
        compositeDisposable.add(
            dataManager.getProfileSportsList().subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(this::profileSportsSuccess, this::failure)
        )
    }

    private fun profileSportsSuccess(response: List<ProfileSportsResponse>) {
        isLoading.set(false)
        profileSportId.set(response[0].id)
        profileSportsList.addAll(response)
        _profileSports.value = profileSportsList

    }

    private fun getProfileSportsSubSportList(sportID:Int){
        if (isLoading.get()) return
        isLoading.set(true)
        compositeDisposable.add(
            dataManager.getProfileSportsSubSportList(sportID).subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(this::profileSportsSubSuccess, this::failure)
        )
    }

    private fun profileSportsSubSuccess(response: List<ProfileSportsResponse>) {
        isLoading.set(false)
        Log.d("profileSportId",response.toJson().toString())
        profileSportsList.addAll(response)
        _profileSports.value = profileSportsList
        profileSportId.set(response[0].id)
    }

    fun showFilterActivity(view: View) {
        getNavigator()?.navigateToFilterActivity()
    }
}