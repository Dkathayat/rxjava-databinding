package com.yewapp.ui.modules.addchallenge.selectorcreateroute

import android.graphics.Color
import android.widget.Button
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import com.mapbox.api.directions.v5.DirectionsCriteria
import com.mapbox.api.directions.v5.MapboxDirections
import com.mapbox.api.directions.v5.models.DirectionsResponse
import com.mapbox.api.directions.v5.models.DirectionsRoute
import com.mapbox.core.constants.Constants.PRECISION_6
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.LineString
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.style.layers.LineLayer
import com.mapbox.mapboxsdk.style.layers.Property
import com.mapbox.mapboxsdk.style.layers.PropertyFactory.*
import com.mapbox.mapboxsdk.style.layers.SymbolLayer
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import com.mapbox.mapboxsdk.utils.BitmapUtils
import com.yewapp.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.util.concurrent.TimeUnit
import android.os.Bundle as Bundle1


class MapDirectionActivity : AppCompatActivity(), MapboxMap.OnMapClickListener {

    private var mapView: MapView? = null
    var mapboxMap: MapboxMap? = null
    var client: MapboxDirections? = null

    private var TAG = "DirectionsProfileToggleActivity"
    private var ROUTE_LAYER_ID = "route-layer-id"
    private var ROUTE_SOURCE_ID = "route-source-id"
    private var ICON_LAYER_ID = "icon-layer-id"
    private var ICON_SOURCE_ID = "icon-source-id"
    private var RED_PIN_ICON_ID = "red-pin-icon-id"
    private var profiles = arrayOf(
        DirectionsCriteria.PROFILE_DRIVING,
        DirectionsCriteria.PROFILE_CYCLING,
        DirectionsCriteria.PROFILE_WALKING
    )
    private var firstRouteDrawn = false

    private var origin = Point.fromLngLat(-99.13037323366, 19.40488375253)
    private var destination = Point.fromLngLat(-99.167663574, 19.426984786987)
    private var lastSelectedDirectionsProfile = DirectionsCriteria.PROFILE_DRIVING
    private var drivingRoute: DirectionsRoute? = null
    private var walkingRoute: DirectionsRoute? = null
    private var cyclingRoute: DirectionsRoute? = null
    private var drivingButton: Button? = null
    private var walkingButton: Button? = null
    private var cyclingButton: Button? = null

    override fun onCreate(savedInstanceState: Bundle1?) {
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token))
        super.onCreate(savedInstanceState)

        setContentView(R.layout.map_direction_layout)
        mapView = findViewById(R.id.mapView)
        //  drivingButton = findViewById(R.id.driving_profile_button)
        with(drivingButton) { this?.setTextColor(Color.WHITE) }
        // walkingButton = findViewById(R.id.walking_profile_button)
        // cyclingButton = findViewById(R.id.cycling_profile_button)

        mapView?.onCreate(savedInstanceState)
        mapView?.getMapAsync(object : OnMapReadyCallback {
            override fun onMapReady(mapboxMap: MapboxMap) {

                mapboxMap.setStyle(Style.MAPBOX_STREETS, object : Style.OnStyleLoaded {
                    override fun onStyleLoaded(style: Style) {
                        this@MapDirectionActivity.mapboxMap = mapboxMap
                        initSource(style)
                        initLayers(style)
                        getAllRoutes(false)
                        initButtonClickListeners()
                        mapboxMap.addOnMapClickListener(this@MapDirectionActivity)
                        Toast.makeText(
                            this@MapDirectionActivity,
                            R.string.instruction, Toast.LENGTH_SHORT
                        ).show()
                    }
                })
            }

        })

        /*  val client = MapboxDirections.builder()
             // .origin(origin)
              //.destination(destination)
              //.overview(DirectionsCriteria.OVERVIEW_FULL)
             // .profile(DirectionsCriteria.PROFILE_DRIVING)
              .accessToken(MAPBOX_ACCESS_TOKEN)
              .build()*/


/*
        client?.enqueueCall(object : Callback<DirectionsResponse> {
            override fun onResponse(call: Call<DirectionsResponse>, response: Response<DirectionsResponse>) {

                if (response.body() == null) {
                    Log.e("","No routes found, make sure you set the right user and access token.")
                    return
                } else if (response.body()!!.routes().size < 1) {
                    Log.e("","No routes found")
                    return
                }

// Get the directions route
                val response = response

                val currentRoute = response.body()!!.routes()[0]

            }

            override fun onFailure(call: Call<DirectionsResponse>, throwable: Throwable) {

                Log.e("","Error: " + throwable.message)

            }
        })
*/
    }

    /**
     * Set up the click listeners on the buttons for each Directions API profile.
     */
    private fun initButtonClickListeners() {
        drivingButton!!.setOnClickListener {

            drivingButton!!.setTextColor(Color.WHITE)
            walkingButton!!.setTextColor(Color.BLACK)
            cyclingButton!!.setTextColor(Color.BLACK)
            lastSelectedDirectionsProfile = DirectionsCriteria.PROFILE_DRIVING
            showRouteLine()

        }
        walkingButton!!.setOnClickListener {
            drivingButton!!.setTextColor(Color.BLACK)
            walkingButton!!.setTextColor(Color.WHITE)
            cyclingButton!!.setTextColor(Color.BLACK)
            lastSelectedDirectionsProfile = DirectionsCriteria.PROFILE_WALKING
            showRouteLine()

        }
        cyclingButton!!.setOnClickListener {

            drivingButton!!.setTextColor(Color.BLACK)
            walkingButton!!.setTextColor(Color.BLACK)
            cyclingButton!!.setTextColor(Color.WHITE)
            lastSelectedDirectionsProfile = DirectionsCriteria.PROFILE_CYCLING
            showRouteLine()
        }

    }

    override fun onMapClick(point: LatLng): Boolean {
        destination = Point.fromLngLat(point.longitude, point.latitude);
        moveDestinationMarkerToNewLocation(point);
        getAllRoutes(true);
        return true;
    }

    /**
     * Add the source for the Directions API route line LineLayer.
     */
    private fun initSource(loadedMapStyle: Style) {
        loadedMapStyle.addSource(GeoJsonSource(ROUTE_SOURCE_ID))
        //  val iconGeoJsonSource = GeoJsonSource( ICON_SOURCE_ID, Feature.fromGeometry(  Point.fromLngLat( destination.longitude(), destination.latitude())))

        val iconGeoJsonSource = GeoJsonSource(
            ICON_SOURCE_ID, FeatureCollection.fromFeatures(
                arrayOf(
                    Feature.fromGeometry(Point.fromLngLat(origin.longitude(), origin.latitude())),
                    Feature.fromGeometry(
                        Point.fromLngLat(
                            destination.longitude(),
                            destination.latitude()
                        )
                    )
                )
            )
        )
        // loadedMapStyle.addSource(iconGeoJsonSource)
        loadedMapStyle.addSource(iconGeoJsonSource)
    }

    /**
     * Add the route and icon layers to the map
     */
    private fun initLayers(@NonNull loadedMapStyle: Style) {
        val routeLayer = LineLayer(ROUTE_LAYER_ID, ROUTE_SOURCE_ID)

        // Add the LineLayer to the map. This layer will display the directions route.
        routeLayer.setProperties(
            lineCap(Property.LINE_CAP_ROUND),
            lineJoin(Property.LINE_JOIN_ROUND),
            lineWidth(5f),
            lineColor(Color.parseColor("#006eff"))
        )
        loadedMapStyle.addLayer(routeLayer)

        // Add the red marker icon image to the map
        loadedMapStyle.addImage(
            RED_PIN_ICON_ID, BitmapUtils.getBitmapFromDrawable(
                resources.getDrawable(R.drawable.mapbox_marker_icon_default)
            )!!
        )
        // Add the red marker icon SymbolLayer to the map
        loadedMapStyle.addLayer(
            SymbolLayer(ICON_LAYER_ID, ICON_SOURCE_ID).withProperties(
                iconImage(RED_PIN_ICON_ID),
                iconIgnorePlacement(true),
                iconAllowOverlap(true),
                iconOffset(arrayOf(0f, -9f))
            )
        )
    }

    /**
     * Load route info for each Directions API profile.
     *
     * @param fromMapClick whether the route loading is being triggered from tapping
     * on the map
     */
    private fun getAllRoutes(fromMapClick: Boolean) {
        for (profile in profiles) {
            getSingleRoute(profile, fromMapClick)
        }
    }


    /**
     * Make a request to the Mapbox Directions API. Once successful, pass the route to the
     * route layer.
     *
     * @param profile the directions profile to use in the Directions API request
     */
    private fun getSingleRoute(profile: String, fromMapClick: Boolean) {
        client = MapboxDirections.builder()

//            .origin(origin)
//            .destination(destination)
//            .overview(DirectionsCriteria.OVERVIEW_FULL)
//            .profile(profile)
            .accessToken(getString(R.string.mapbox_access_token))
            .build()
        client?.enqueueCall(object : Callback<DirectionsResponse?> {
            override fun onResponse(
                call: Call<DirectionsResponse?>,
                response: Response<DirectionsResponse?>
            ) {
                // You can get the generic HTTP info about the response
                Timber.d("Response code: %s", response.code())
                if (response.body() == null) {
                    Timber.e("No routes found, make sure you set the right user and access token.")
                    return
                } else if (response.body()!!.routes().size < 1) {
                    Timber.e("No routes found")
                    return
                }
                when (profile) {
                    DirectionsCriteria.PROFILE_DRIVING -> {
                        drivingRoute = response.body()!!.routes()[0]
                        drivingButton?.text = String.format(
                            getString(R.string.driving_profile),
                            java.lang.String.valueOf(
                                TimeUnit.SECONDS.toMinutes(
                                    drivingRoute?.duration()?.toLong()!!
                                )
                            )
                        )
                        if (!firstRouteDrawn) {
                            showRouteLine()
                            firstRouteDrawn = true
                        }
                    }
                    DirectionsCriteria.PROFILE_WALKING -> {
                        walkingRoute = response.body()!!.routes()[0]
                        walkingButton?.text = String.format(
                            getString(R.string.walking_profile),
                            java.lang.String.valueOf(
                                TimeUnit.SECONDS
                                    .toMinutes(walkingRoute?.duration()!!.toLong())
                            )
                        )
                    }
                    DirectionsCriteria.PROFILE_CYCLING -> {
                        cyclingRoute = response.body()!!.routes()[0]
                        cyclingButton?.text = String.format(
                            getString(R.string.cycling_profile),
                            java.lang.String.valueOf(
                                TimeUnit.SECONDS
                                    .toMinutes(cyclingRoute?.duration()!!.toLong())
                            )
                        )
                    }
                    else -> {}
                }
                if (fromMapClick) {
                    showRouteLine()
                }


            }

            override fun onFailure(call: Call<DirectionsResponse?>, t: Throwable) {
            }

        })
    }

    /**
     * Move the destination marker to wherever the map was tapped on.
     *
     * @param pointToMoveMarkerTo where the map was tapped on
     */
    private fun moveDestinationMarkerToNewLocation(pointToMoveMarkerTo: LatLng) {
        mapboxMap!!.getStyle { style ->
            val destinationIconGeoJsonSource =
                style.getSourceAs<GeoJsonSource>(ICON_SOURCE_ID)
            destinationIconGeoJsonSource?.setGeoJson(
                Feature.fromGeometry(
                    Point.fromLngLat(
                        pointToMoveMarkerTo.longitude, pointToMoveMarkerTo.latitude
                    )
                )
            )
        }
    }


    /**
     * Display the Directions API route line depending on which profile was last
     * selected.
     */
    private fun showRouteLine() {
        if (mapboxMap != null) {
            mapboxMap!!.getStyle { style -> // Retrieve and update the source designated for showing the directions route
                val routeLineSource =
                    style.getSourceAs<GeoJsonSource>(ROUTE_SOURCE_ID)

                // Create a LineString with the directions route's geometry and
                // reset the GeoJSON source for the route LineLayer source
                if (routeLineSource != null) {
                    when (lastSelectedDirectionsProfile) {
                        DirectionsCriteria.PROFILE_DRIVING -> routeLineSource.setGeoJson(
                            LineString.fromPolyline(
                                drivingRoute!!.geometry()!!,
                                PRECISION_6
                            )
                        )
                        DirectionsCriteria.PROFILE_WALKING -> routeLineSource.setGeoJson(
                            LineString.fromPolyline(
                                walkingRoute!!.geometry()!!,
                                PRECISION_6
                            )
                        )
                        DirectionsCriteria.PROFILE_CYCLING -> routeLineSource.setGeoJson(
                            LineString.fromPolyline(
                                cyclingRoute!!.geometry()!!,
                                PRECISION_6
                            )
                        )
                        else -> {}
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mapView!!.onResume()
    }

    override fun onStart() {
        super.onStart()
        mapView!!.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView!!.onStop()
    }

    override fun onPause() {
        super.onPause()
        mapView!!.onPause()
    }

//    fun onSaveInstanceState(outState: Bundle1?) {
//        super.onSaveInstanceState(outState!!)
//        mapView!!.onSaveInstanceState(outState)
//    }

    override fun onDestroy() {
        super.onDestroy()
        // Cancel the Directions API request
        if (client != null) {
            client!!.cancelCall()
        }
        if (mapboxMap != null) {
            mapboxMap!!.removeOnMapClickListener(this)
        }
        mapView!!.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView!!.onLowMemory()
    }
}