package com.yewapp.ui.modules.createroute

import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.NonNull
import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.mapbox.api.tilequery.MapboxTilequery
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.Style
import com.yewapp.R
import com.yewapp.data.network.DataManager
import com.yewapp.data.network.api.routes.Coordinate
import com.yewapp.ui.base.BaseViewModel
import com.yewapp.ui.modules.createroute.extras.CreateRouteExtras
import com.yewapp.utils.rx.SchedulerProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.text.DecimalFormat


class CreateRouteViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) :
    BaseViewModel<CreateRouteNavigator>(dataManager, schedulerProvider) {


    var maxCalories = ObservableField<String>("0 cal")
    var maxDistance = ObservableField<String>("0 mi")
    var elevation = ObservableField<String>("0.0")

    var profileType = ObservableField<String>("")

    val ICON_GEOJSON_SOURCE_ID = "icon-source-id"
    val OPTIMIZED_ROUTE_SOURCE_ID = "optimized-route-source-id"


    var routeName = ObservableField<String>("")
    var sportTypeID = ObservableField<Int>(0)
    var rideType = ObservableField<String>("biking")
    var expectedTime = ObservableField<String>("00:00:00")
    var distance = ObservableField<Double>(0.0)


    var distanceUnit = ObservableField<String>("miles")
    var elevationGain = ObservableField<String>("")
    var elevationLoss = ObservableField<String>("")
    var description = ObservableField<String>("")
    var coordinates = arrayListOf<Coordinate>()

    var staticMapImageLocation =
        ObservableField<String>("-77.043686,38.892035,-77.028923,38.904192")
    var mapViewType = ObservableField<String>("satellite-streets-v11")
    var currentAddress = ObservableField<String>("")
    var selectedMapImageUrl = ObservableField<String>(
        "https://api.mapbox.com/styles/v1/mapbox/${mapViewType.get()}/static/[${staticMapImageLocation.get()}]/400x400?access_token=${
            dataManager.getResourceProvider().getString(R.string.mapbox_access_token)
        }"
    )
    var mapImagesWithRouteAndMArker = ObservableField<String>("")
    var mapStyle = ObservableField<String>(Style.SATELLITE_STREETS)
    var selectedMapName = ObservableField<String>("3D Terrain")
    var isMapTypeVisible = ObservableField<Boolean>(false)
    var editRoute = ObservableField<Boolean>(false)


    val stops: ArrayList<Point> = ArrayList()
    var origin = ObservableField<Point>(Point.fromLngLat(0.0, 0.0))

    lateinit var locationManager: LocationManager
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient


    override fun setData(extras: Bundle?) {
//        challengeModel =
//        extras?.getParcelable<ChallengeModel>(ChallengeExtras.CHALLENGE_DATA) ?: return
//        profileType.set(challengeModel.profileType)
        if (extras?.getInt(CreateRouteExtras.ROUTE_ID, 0) == 0) {
            profileType.set(extras.getString(CreateRouteExtras.PROFILE_TYPE))
            editRoute.set(false)
        } else {
            profileType.set(extras?.getString(CreateRouteExtras.PROFILE_TYPE))
            mapStyle.set(extras?.getString(CreateRouteExtras.MAP_STYLE))
            coordinates.addAll(
                extras?.getParcelableArrayList<Coordinate>(CreateRouteExtras.CREATE_EDIT_ROUTE_DATA)
                    ?: return
            )
            profileType.set(extras.getString(CreateRouteExtras.PROFILE_TYPE))
            editRoute.set(true)
        }


    }


    fun onActionClick(view: View) {
        when (view.id) {
            R.id.current_location_img -> {
                editRoute.set(false)
                getNavigator()?.getCurrentLocation()
            }
            R.id.mapViewType -> {
                if (isMapTypeVisible.get()!!) {
                    isMapTypeVisible.set(false)
                } else {
                    isMapTypeVisible.set(true)
                }
            }
            R.id.resetMapbox -> {
                elevation.set("0.0")
                maxCalories.set("00")
                maxDistance.set("0 mi")
                expectedTime.set("00:00:00")
                getNavigator()?.resetMapBox()
            }

            R.id.btn_continue -> {
                getNavigator()?.setDataToCreateRoute()
//                getNavigator()?.onError(Throwable("Create route under development"))
            }
            R.id.search_EditTxt -> {
                getNavigator()?.navigateToMapBoxSearch()
            }
//            R.id.popular_layout -> {
//                getNavigator()?.navigateToPopularRouteScreen()
//            }
//            R.id.ivAddRoute -> {
//                getNavigator()?.navigateToCreateRouteActivity()
//            }
//           R.id.popular_txt -> {
//                getNavigator()?.navigateToPopularRouteScreen()
//            }
//            R.id.latest_txt -> {
//                getNavigator()?.navigateToLatestRouteScreen()
//            }
//            R.id.favorites_txt -> {
//                getNavigator()?.navigateToFavoriteRouteScreen()
//            }

        }
    }


    fun makeElevationRequestToTileQueryApi(@NonNull point: LatLng) {
        val elevationQuery = MapboxTilequery.builder()
            .accessToken(
                dataManager.getResourceProvider().getString(R.string.mapbox_access_token)
            )
            .tilesetIds("mapbox.mapbox-terrain-v2")
            .query(Point.fromLngLat(point.longitude, point.latitude)).layers("icon-layer-id")
            .limit(50).build()



        elevationQuery.enqueueCall(object : Callback<FeatureCollection> {
            override fun onResponse(
                call: Call<FeatureCollection>, response: Response<FeatureCollection>
            ) {
                Log.d("SUCESS", response.message())
                if (response.body() != null) {
                    val responseFeatureCollection: FeatureCollection = response.body() ?: return


                }
            }

            override fun onFailure(call: Call<FeatureCollection>, t: Throwable) {
                Log.d("FAILURE", t.message.toString())

            }

        })

    }


    fun getRouteElevation() {
        if (stops.isNullOrEmpty()) return
        isLoading.set(true)
/*TODO:STATIC ROUTE COORDINATES
        coordinates.clear()
        coordinates.add(Coordinate("28.44732142839848", "77.07915916520017"))
        coordinates.add(Coordinate("28.442056748661244", "77.08562903452184"))
       coordinates.add(Coordinate("28.43892187633891", "77.08931805649172"))
 */
        coordinates.clear()
        for (i in 0 until stops.size) {
            coordinates.add(
                Coordinate(
                    stops[i].latitude().toString(),
                    stops[i].longitude().toString()
                )
            )
        }


        viewModelScope.launch(Dispatchers.Main) {
            val elevationProcess = async(Dispatchers.IO) {
                for (i in 0 until coordinates.size) {
                    val t = async {
                        makeElevationRequestToTilequeryApi(
                            LatLng(
                                coordinates[i].latitude.toDouble(),
                                coordinates[i].longitude.toDouble()
                            ), i
                        )

                    }
                    t.await()
                }
            }
            elevationProcess.await()
//            //    ()
        }
    }


    val featuresRoute = arrayListOf<Feature>()
    val ele = arrayListOf<Int>()


    //Elevation
    suspend fun makeElevationRequestToTilequeryApi(point: LatLng, position: Int) {
        val elevationQuery = MapboxTilequery.builder()
            .accessToken(getString(com.yewapp.R.string.mapbox_access_token))
            .tilesetIds("mapbox.mapbox-terrain-v2")
            .query(Point.fromLngLat(point.longitude, point.latitude)).geometry("polygon")
            .layers("contour").build()

        elevationQuery.enqueueCall(object : Callback<FeatureCollection> {
            override fun onResponse(
                call: Call<FeatureCollection>, response: Response<FeatureCollection>
            ) {
                if (!response.body()?.features().isNullOrEmpty()) {
                    val elevation = arrayListOf<Int>()
                    for (i in 0 until response.body()?.features()?.size!!) {
                        elevation.add(
                            serializeElevation(
                                response.body()?.features()?.get(i)?.properties()?.get("ele")
                                    ?: return
                            ) ?: return
                        )
                    }
                    if (!elevation.isNullOrEmpty()) {
                        Log.d(
                            "CALL",
                            "Find MAX Elevation : *** ${elevation.toString()}"
                        )
                        ele.add(elevation.sortedDescending()[0])
                    }
                    if (position + 1 == coordinates.size) {
                        viewModelScope.launch(Dispatchers.Main) {
                            calculateElevation()
                        }
                    }
//                    viewModelScope.launch(Dispatchers.Main) {
//                        if (position + 1 == coordinates.size) {
//                            Log.d(
//                                "CALL",
//                                " calculateElevation :${position} *** ${coordinates.size}"
//                            )
//                            calculateElevation()
//                        }
//                    }
                }


            }

            override fun onFailure(call: Call<FeatureCollection>, t: Throwable) {
                Timber.d("Request failed: %s", t.message);
            }

        })
    }

    fun serializeElevation(ele: JsonElement): Int? {
        var elevation: Int? = null
        elevation = Gson().fromJson(
            ele, Int::class.java
        )
        return elevation
    }

    private fun calculateElevation() {
        Log.d(
            "CALL",
            " ELE LIST : *** ${ele.size}"
        )
        var eleGain = 0.0
        var eleLoss = 0.0
        for (i in 0 until ele.size) {
            if (i != 0) {
                val preElevation = ele[i - 1].toDouble()
                val postElevation = ele[i].toDouble()
                if (postElevation > preElevation) {
                    eleGain += (postElevation - preElevation)
                } else {
                    eleLoss += (preElevation - postElevation)
                }
            }
        }
        eleGain *= 3.28084
        eleLoss *= 3.28084

//        viewModelScope.launch(Dispatchers.Main) {
        elevation.set(twoDecimalPlaceValue(eleGain))
        elevationGain.set("$eleGain")
        elevationLoss.set("$eleLoss")
        Log.d("CALL", " ELE RESULT :${eleGain} *** ${eleLoss}")
        isLoading.set(false)
//        }
    }

    fun twoDecimalPlaceValue(value: Double): String {
        val decimalFormat = DecimalFormat("#.##")
        return decimalFormat.format(value)
    }


}