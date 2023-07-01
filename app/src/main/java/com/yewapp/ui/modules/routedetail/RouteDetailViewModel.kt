package com.yewapp.ui.modules.routedetail

import android.os.Bundle
import android.view.View
import androidx.databinding.ObservableField
import com.mapbox.geojson.Point
import com.yewapp.R
import com.yewapp.data.network.DataManager
import com.yewapp.data.network.api.challenges.ChallengeModel
import com.yewapp.data.network.api.routes.Route
import com.yewapp.data.network.api.routes.RouteDetailResponse
import com.yewapp.ui.base.BaseViewModel
import com.yewapp.ui.modules.listner.ChallengeExtras
import com.yewapp.utils.rx.SchedulerProvider
import java.util.*

class RouteDetailViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) :
    BaseViewModel<RouteDetailNavigator>(dataManager, schedulerProvider) {
    lateinit var route: Route

    var isRouteFavorite = ObservableField<Boolean>()
    var mapIcon = ObservableField<String>()
    var sportTypeIcon = ObservableField<String>()
    var routeTitle = ObservableField<String>()
    var routeDescription = ObservableField<String>()
    var distance = ObservableField<String>()
    var elevation = ObservableField<String>()
    var calories = ObservableField<String>()
    var time = ObservableField<String>()
    var athelete = ObservableField<String>()
    val pointList: ArrayList<Point> = ArrayList()
    var id = ""

    lateinit var challengeModel: ChallengeModel

    override fun setData(extras: Bundle?) {
//        id = extras?.getInt(RouteExtras.ID)!!

        challengeModel =
            extras?.getParcelable(ChallengeExtras.CHALLENGE_DATA) ?: return
        id = challengeModel.routeID ?: return
//        routeTitle.set(route.name)
//        calories.set(route.calories.toString()+" Cal")
//        time.set(route.expectedTime)
//        elevation.set(route.elevation.toString()+" ft")
//        distance.set(route.distance.toString()+" mi")
//        athelete.set(route.atheletesTravelled.toString()+" Athelets")
/*
        if (route.coordinates?.isNotEmpty() == true) {
//            val latitute = route.coordinates!![0].latitude
//            val longitude = route.coordinates!![0].longitude
//

            for (i in route.coordinates?.indices!!) {
                pointList.addAll(
                    listOf(Point.fromLngLat(
                    route.coordinates!![i].latitude!!,
                    route.coordinates!![i].longitude!!
                    )
                ))

            }
            println("Coordinates List $pointList")
            getNavigator()?.addCoordinates()

        }
*/
    }

    fun getRouteDetail() {
        isLoading.set(true)
        compositeDisposable.add(
            dataManager.getRouteDetail(id.toInt())
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(this::detailSuccess, this::failure)
        )
    }

    private fun detailSuccess(response: RouteDetailResponse) {
        isLoading.set(false)
        response.id
        routeTitle.set(response.name)
        mapIcon.set(response.mapIcon)
        sportTypeIcon.set(response.sportTypeIcon)
        isRouteFavorite.set(response.isRouteFavorite)
//        mapIcon.set("https://cdn-icons-png.flaticon.com/512/8186/8186720.png")
        calories.set(response.calories.toString() + " Cal")
        time.set(response.expectedTime)
        elevation.set(response.elevationGain.toString() + " ft")
        distance.set(response.distance.toString() + " ${response.distanceUnit}")
        athelete.set(response.atheletesTravelled.toString() + " Athelets")

        //Set Route Details in bundle model
        challengeModel.calories = "${response.calories}"
        challengeModel.miles = "${response.distance}"
        challengeModel.time = response.expectedTime
        challengeModel.elevationGain = "${response.elevationGain}"
    }

    private fun failure(error: Throwable) {
        isLoading.set(false)
        getNavigator()?.onError(error, false)
    }

    /**
     * Method to get call the api to make the route in favorite list
     * @param route id
     * @return
     */
    fun addToFavorite() {
        isLoading.set(true)
        compositeDisposable.add(
            dataManager.addToFavorite(id.toInt())
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(this::favoriteSuccess, this::failure)
        )
    }

    private fun favoriteSuccess(response: String) {
        isLoading.set(false)
    }

    /**
     * Method to get distance in miles and convert into Kilometers
     * @param miles
     * @return
     */
    fun convertIntoKms(miles: Double): Double {
        return 1.609 * miles
    }

    fun onActionClick(view: View) {
        when (view.id) {
            R.id.header_tv -> getNavigator()?.onBackPress()
            R.id.choose_route_btn -> {
                getNavigator()?.navigateToLocationAdditionalDetailActivity()
            }
        }
    }
}