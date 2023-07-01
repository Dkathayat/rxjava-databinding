package com.yewapp.ui.modules.profile.fragment.activities

import android.database.Observable
import android.os.Bundle
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.yewapp.R
import com.yewapp.data.network.DataManager
import com.yewapp.data.network.api.feed.CreateFeedRequest
import com.yewapp.data.network.api.feed.Feed
import com.yewapp.data.network.api.profile.*
import com.yewapp.ui.base.BaseViewModel
import com.yewapp.ui.modules.profile.navigator.ActivitiesNavigator
import com.yewapp.utils.rx.SchedulerProvider
import com.yewapp.utils.toJson

class ActivitiesViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) :
    BaseViewModel<ActivitiesNavigator>(dataManager, schedulerProvider) {

    private val _activityData = MutableLiveData<List<ActivitiesData>?>()
    val activityData: MutableLiveData<List<ActivitiesData>?> get() = _activityData
    val activityList = mutableListOf<ActivitiesData>()
    var activitiesData:ActivitiesData?=null
    var totalCaloriesCount = ObservableField<String>()
    var activitySummary = MutableLiveData<ActivitySummary>()

    val xvalueChart = mutableListOf<Int>()
    val zvalueChart = mutableListOf<org.joda.time.DateTime>()
    val yvalueChart = mutableListOf<Int>()
    var activityImage = ObservableField<Int>()

    var selectedItem = 0

    val startDate = ObservableField<String>()
    val endDate = ObservableField<String>()
    val selectedFilterDate = ObservableField<String>()

    var filterList = mutableListOf<FilterData>()
    var filterType = mutableListOf<String>()

    init {
        filterList.add(FilterData("Show All Activities", "all", false))
        filterList.add(FilterData("Running Activities", "running", false))
        filterList.add(FilterData("Hiking Activities", "hiking", false))
        filterList.add(FilterData("Biking Activities", "biking", false))
        activityImage.set(R.drawable.test_image_activities)
    }

    override fun setData(extras: Bundle?) {

    }

    fun onDatePickerClicked() {
        getNavigator()?.navigateToDateRangePicker()
    }

    fun sendFilterData(startDate: String, endDate: String) {
        isLoading.set(true)
        xvalueChart.clear()
        yvalueChart.clear()
        activityList.clear()
        compositeDisposable.add(
            dataManager.getActivityFilterData(
                ActivitiesFilterRequest(
                    "",
                    startDate,
                    endDate,
                    filterType
                )
            ).subscribeOn(schedulerProvider.io()).observeOn(schedulerProvider.ui())
                .subscribe(this::onSuccess, this::onFailure)
        )
    }

    private fun onSuccess(responseData: ActivitiesFilterResponseData) {
        isLoading.set(false)
        _activityData.value = responseData.list
        activityList.addAll(responseData.list!!)
        activitySummary.value = responseData.summary
        filterType.clear()
        getDataForGraph()


    }

    fun shareActivity(activityData: ActivitiesData) {
        if (isLoading.get()) return
        isLoading.set(true)
        compositeDisposable.add(
            dataManager.createFeed(
                CreateFeedRequest(
                    activityData.activityName,
                    activityData.activityDescription,
                    false,
                    activityData.location,
                    "",
                    "",
                    activityData.activityId,
                    mutableListOf(),
                    listOf(activityData.activity_image),
                    listOf()
                )
            ).subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(this::onFeedSuccess, this::onFailure)
        )
    }

    private fun onFeedSuccess(response: String) {
        isLoading.set(false)
        getNavigator()?.onSuccessActivityShared(response)

    }

    private fun onFailure(error: Throwable) {
        isLoading.set(false)
        getNavigator()?.onError(error)
    }

    fun getDataForGraph() {
        xvalueChart.clear()
        yvalueChart.clear()
        for (i in activityList) {
            xvalueChart.add(i.userCalorie.toInt())
            yvalueChart.add(i.start_date)

        }
        getNavigator()?.configureLineChart()
    }


    fun onFilterClicked() {
        getNavigator()?.navigateToApplyFilter()

    }
}