package com.yewapp.ui.modules.createroute

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.NonNull
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.api.directions.v5.DirectionsCriteria
import com.mapbox.api.directions.v5.models.DirectionsRoute
import com.mapbox.api.optimization.v1.MapboxOptimization
import com.mapbox.api.optimization.v1.models.OptimizationResponse
import com.mapbox.api.staticmap.v1.MapboxStaticMap
import com.mapbox.core.constants.Constants
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.LineString
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions
import com.mapbox.mapboxsdk.style.layers.LineLayer
import com.mapbox.mapboxsdk.style.layers.PropertyFactory
import com.mapbox.mapboxsdk.style.layers.SymbolLayer
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import com.yewapp.R
import com.yewapp.data.network.api.routes.MapViewTypeModel
import com.yewapp.databinding.ActivityCreateRouteBinding
import com.yewapp.ui.base.BaseActivity
import com.yewapp.utils.ROUTE_DATA
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.io.IOException
import java.text.DecimalFormat
import java.util.*


class CreateRouteActivity :
    BaseActivity<CreateRouteNavigator, CreateRouteViewModel, ActivityCreateRouteBinding>(),
    CreateRouteNavigator, OnMapReadyCallback, MapboxMap.OnMapClickListener,
    MapboxMap.OnMapLongClickListener {
    //, MapboxMap.OnMapClickListener,
//    MapboxMap.OnMapLongClickListener, PermissionsListener {
    override fun getLayout() = R.layout.activity_create_route

    private val geojsonSourceLayerId = "geojsonSourceLayerId"

    lateinit var mapBoxViewTypeAdapter: MapBoxViewTypeAdapter
    private var mapView: MapView? = null
    private var mapboxMap: MapboxMap? = null
    lateinit var permissionsManager: PermissionsManager


    private val SOURCE_ICON_GEOJSON_SOURCE_ID = "source_icon-source-id"
    private val DESTINATION_ICON_GEOJSON_SOURCE_ID = "destination_icon-source-id"

    private val FIRST = "first"
    private val LAST = "last"
    private val POLYLINE_COLOR = "#E9A42B"
    private val POLYLINE_WIDTH = 4f

    private var optimizedRoute: DirectionsRoute? = null
    private var optimizedClient: MapboxOptimization? = null
    private var routeLayer = "icon-layer-id"

    //Received search result and update mapbox with searched location
    @SuppressLint("LongLogTag", "LogNotTimber")
    var startForSearchLocationResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.data != null && result.resultCode == RESULT_OK) {
                // Retrieve selected location's CarmenFeature
                val selectedCarmenFeature = PlaceAutocomplete.getPlace(result.data)
                viewModel.currentAddress.set("${selectedCarmenFeature.placeName()}")

                viewModel.origin.set(
                    Point.fromLngLat(
                        (selectedCarmenFeature.geometry() as Point?)?.longitude()
                            ?: return@registerForActivityResult,
                        (selectedCarmenFeature.geometry() as Point?)?.latitude()
                            ?: return@registerForActivityResult
                    )
                )
                updateMapBoxView(true)
            } else {
                Log.d("CreateRouteActivity", "Search result not received:    NO DATA\n")
            }

        }


    //New way to check permission
    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                // Precise location access granted.
                checkLocationSettings {
                    if (!viewModel.editRoute.get()!!)
                        getLocation()
                }
            }
            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                // Only approximate location access granted.
                checkLocationSettings {
                    if (!viewModel.editRoute.get()!!)
                        getLocation()
                }
            }
            else -> {
                // No location access granted.
            }
        }

    }


    override fun init() {
        bind(CreateRouteViewModel::class.java)
    }


    override fun onViewModelCreated(viewModel: CreateRouteViewModel) {
        viewModel.setNavigator(this)
    }

    override fun onViewBound(viewDataBinding: ActivityCreateRouteBinding) {
        locationPermissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
        // Setup the MapView
        mapView = findViewById(R.id.mapView)
        mapView?.getMapAsync(this)
        viewModel.fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(this)
        initializeAdapter()
    }


    private fun initializeAdapter() {
        val list = getMapTypeList()
        mapBoxViewTypeAdapter = MapBoxViewTypeAdapter(list,
            viewModel.dataManager.getResourceProvider().getString(R.string.mapbox_access_token),
            object : ItemMapTypeViewModel.OnItemClickListener {
                override fun onClickItem(item: MapViewTypeModel) {
                    viewModel.isMapTypeVisible.set(false)//Hide when user choose anyone of map style
                    viewModel.mapViewType.set(item.mapViewTypeUrl)//set selected map style for static map image
                    viewModel.selectedMapName.set(item.mapViewType)//set map style name
                    viewModel.mapStyle.set(item.mapStyle)//set map style on mapbox


                    //set map static image on imageview
                    viewModel.selectedMapImageUrl.set(
                        "https://api.mapbox.com/styles/v1/mapbox/${viewModel.mapViewType.get()}/static/[${viewModel.staticMapImageLocation.get()}]/400x400?access_token=${
                            viewModel.dataManager.getResourceProvider()
                                .getString(R.string.mapbox_access_token)
                        }"
                    )

                    mapboxMap?.setStyle(item.mapStyle) { style ->
                        initMarkerIconSymbolLayer(style)
                        initOptimizedRouteLineLayer(style)
                        if (viewModel.stops.size != 0) {
                            for (i in 0 until viewModel.stops.size) {
                                addDestinationMarker(
                                    style, LatLng(
                                        viewModel.stops[i].longitude(),
                                        viewModel.stops[i].longitude()
                                    )
                                )
                            }
                            getOptimizedRoute(mapboxMap?.style ?: return@setStyle, viewModel.stops)
                        }

                    }
                }

            })
        mapBoxViewTypeAdapter.setHasStableIds(true)
        viewDataBinding.mapViewRecycler.adapter = mapBoxViewTypeAdapter
        viewDataBinding.mapViewRecycler.layoutManager = LinearLayoutManager(this)
    }

    //Add static name and image to show map view type
    private fun getMapTypeList(): MutableList<MapViewTypeModel> {
        val list = mutableListOf<MapViewTypeModel>()
        list.add(
            MapViewTypeModel(
                "Streets",
                "streets-v11",
                Style.MAPBOX_STREETS,
                viewModel.staticMapImageLocation.get()
            )
        )
        list.add(
            MapViewTypeModel(
                "Satellite", "satellite-v9", Style.SATELLITE, viewModel.staticMapImageLocation.get()
            )
        )
        list.add(
            MapViewTypeModel(
                "Light", "light-v10", Style.LIGHT, viewModel.staticMapImageLocation.get()
            )
        )
        list.add(
            MapViewTypeModel(
                "Dark", "dark-v10", Style.DARK, viewModel.staticMapImageLocation.get()
            )
        )
        list.add(
            MapViewTypeModel(
                "Navigation Day",
                "navigation-day-v1",
                Style.TRAFFIC_DAY,
                viewModel.staticMapImageLocation.get()
            )
        )
        list.add(
            MapViewTypeModel(
                "Navigation Night",
                "navigation-night-v1",
                Style.TRAFFIC_NIGHT,
                viewModel.staticMapImageLocation.get()
            )
        )
        list.add(
            MapViewTypeModel(
                "3D Terrain",
                "satellite-streets-v11",
                Style.SATELLITE_STREETS,
                viewModel.staticMapImageLocation.get()
            )
        )
        return list
    }


    //Removes the focus from text fields when the user click on the screen
    @SuppressLint("ClickableViewAccessibility")
    private fun setupFocusOutside(view: View) {
        if (view !is EditText) {
            view.setOnTouchListener { _, _ ->
                hideKeyboard()
                false
            }
        }

        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                val innerView = view.getChildAt(i)
                setupFocusOutside(innerView)
            }
        }
    }

    //Hides the Keyboard when the User clicks on the screen
//In this method we initialize mapbox for create or edit route
    override fun onMapReady(@NonNull mapboxMap: MapboxMap) {
        this.mapboxMap = mapboxMap
        if (viewModel.editRoute.get()!!) {
            //TODO: When edit route
            mapboxMap.setStyle(
                viewModel.mapStyle.get()
            ) { style ->
                initMarkerIconSymbolLayer(style)
                initOptimizedRouteLineLayer(style)

                mapboxMap.addOnMapClickListener(this@CreateRouteActivity)
                mapboxMap.addOnMapLongClickListener(this@CreateRouteActivity)

                if (viewModel.coordinates.size != 0) {
                    for (i in 0 until viewModel.coordinates.size) {
                        val point = LatLng(
                            viewModel.coordinates[i].latitude.toDouble(),
                            viewModel.coordinates[i].longitude.toDouble()
                        )
                        if (i == 0) {
                            mapboxMap.animateCamera(
                                CameraUpdateFactory.newCameraPosition(
                                    CameraPosition.Builder().target(
                                        point
                                    ).zoom(15.0).build()
                                ), 4000
                            )

                        }
                        addDestinationMarker(style, point)
                        addPointToStopsList(point)
                    }
                    getAddressFromLatlng(
                        viewModel.coordinates[0].latitude.toDouble(),
                        viewModel.coordinates[0].longitude.toDouble()
                    )
                    getOptimizedRoute(style, viewModel.stops)
                }
            }

        } else {
            //TODO:when create route
            mapboxMap.setStyle(
                Style.SATELLITE_STREETS
            ) { style ->
                initMarkerIconSymbolLayer(style)
                initOptimizedRouteLineLayer(style)

                mapboxMap.addOnMapClickListener(this@CreateRouteActivity)
                mapboxMap.addOnMapLongClickListener(this@CreateRouteActivity)
            }
        }

    }

    private fun initMarkerIconSymbolLayer(loadedMapStyle: Style) {
        // Add the marker image to map
        loadedMapStyle.addImage(
            "icon-image", BitmapFactory.decodeResource(
                this.resources, R.drawable.ic_map_pin_icon
            )
        )
        // Add the source to the map
        loadedMapStyle.addSource(
            GeoJsonSource(
                viewModel.ICON_GEOJSON_SOURCE_ID, Feature.fromGeometry(
                    Point.fromLngLat(
                        viewModel.origin.get()!!.longitude(), viewModel.origin.get()!!.latitude()
                    )
                )
            )
        )
        loadedMapStyle.addLayer(
            SymbolLayer(routeLayer, viewModel.ICON_GEOJSON_SOURCE_ID).withProperties(
                PropertyFactory.iconImage("icon-image"),
                PropertyFactory.iconSize(1f),
                PropertyFactory.iconAllowOverlap(true),
                PropertyFactory.iconIgnorePlacement(true),
                PropertyFactory.iconOffset(arrayOf(0f, -7f)),


//                PropertyFactory.textField(Expression.get("ele")),
//                PropertyFactory.textColor(Color.BLUE),
//                PropertyFactory.textSize(23f),
//                PropertyFactory.textHaloBlur(10f),
//                PropertyFactory.textIgnorePlacement(true),
//                PropertyFactory.textAllowOverlap(true)
//                PropertyFactory.iconAllowOverlap(true),

            )
        )
    }


    private fun initOptimizedRouteLineLayer(loadedMapStyle: Style) {
        loadedMapStyle.addSource(GeoJsonSource(viewModel.OPTIMIZED_ROUTE_SOURCE_ID))
        loadedMapStyle.addLayerBelow(
            LineLayer(
                "optimized-route-layer-id", viewModel.OPTIMIZED_ROUTE_SOURCE_ID
            ).withProperties(
                PropertyFactory.lineColor(Color.parseColor(POLYLINE_COLOR)),
                PropertyFactory.lineWidth(POLYLINE_WIDTH)
            ), routeLayer
        )
    }

    override fun onMapClick(point: LatLng): Boolean {

        checkLocationSettings {
            if (alreadyTwelveMarkersOnMap()) {
                Toast.makeText(
                    this@CreateRouteActivity, R.string.only_twelve_stops_allowed, Toast.LENGTH_LONG
                ).show()
            } else {
                val style = mapboxMap?.style
                if (style != null) {
                    addDestinationMarker(style, point)
                    addPointToStopsList(point)
                    if (viewModel.stops.size > 1) {
                        getOptimizedRoute(style, viewModel.stops)
                    }
                }
            }
        }


        // Optimization API is limited to 12 coordinate sets
        return true
    }

//    private fun addMarker(point: Point) {
//        val bitmap = convertDrawablintoBitmap(
//            AppCompatResources.getDrawable(
//                this,
//                R.drawable.ic_map_pin_icon
//            )
//        )
//
//        val pointAnnotation: PointAnnotationOptions = PointAnnotationOptions()
//            .withPoint(point)
//            .withIconImage(bitmap)
//
//        markerList.add(pointAnnotation)
//        pointAnnotationManager?.create(markerList)
//    }

    private fun convertDrawablintoBitmap(sourceDrawable: Drawable?): Bitmap {
        return if (sourceDrawable is BitmapDrawable) {
            sourceDrawable.bitmap
        } else {
            val constantState = sourceDrawable?.constantState
            val drawable = constantState?.newDrawable()?.mutate()
            val bitmap: Bitmap = Bitmap.createBitmap(
                drawable!!.intrinsicWidth, drawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            bitmap
        }
    }

    override fun onMapLongClick(point: LatLng): Boolean {
        viewModel.stops.clear()
        if (mapboxMap != null) {
            val style = mapboxMap!!.style
            if (style != null) {
                resetDestinationMarkers(style)
                removeOptimizedRoute(style)
//                addFirstStopToStopsList()
                return true
            }
        }
        return false
    }

    private fun resetDestinationMarkers(style: Style) {
        val optimizedLineSource = style.getSourceAs<GeoJsonSource>(viewModel.ICON_GEOJSON_SOURCE_ID)
        optimizedLineSource?.setGeoJson(
            Point.fromLngLat(
                viewModel.origin.get()!!.longitude(), viewModel.origin.get()!!.latitude()
            )
        )
    }

    private fun removeOptimizedRoute(style: Style) {
        val optimizedLineSource =
            style.getSourceAs<GeoJsonSource>(viewModel.OPTIMIZED_ROUTE_SOURCE_ID)
        optimizedLineSource?.setGeoJson(FeatureCollection.fromFeatures(arrayOf()))
    }

    private fun alreadyTwelveMarkersOnMap(): Boolean {
        return viewModel.stops.size == 12
    }

    private fun addDestinationMarker(style: Style, point: LatLng) {
        val destinationMarkerList: MutableList<Feature> = ArrayList()
        for (singlePoint in viewModel.stops) {
            destinationMarkerList.add(
                Feature.fromGeometry(
                    Point.fromLngLat(singlePoint.longitude(), singlePoint.latitude())
                )
            )
        }
        destinationMarkerList.add(
            Feature.fromGeometry(
                Point.fromLngLat(
                    point.longitude, point.latitude
                )
            )
        )
        val iconSource = style.getSourceAs<GeoJsonSource>(viewModel.ICON_GEOJSON_SOURCE_ID)
        iconSource?.setGeoJson(FeatureCollection.fromFeatures(destinationMarkerList))
    }

    private fun addPointToStopsList(point: LatLng) {
        viewModel.stops.add(Point.fromLngLat(point.longitude, point.latitude))
    }


    private fun getOptimizedRoute(style: Style, coordinates: List<Point>) {
        optimizedClient = MapboxOptimization.builder().coordinates(coordinates)
            .profile( //pass sports type like ,hiking, biking & etc. if used any other string route will not be created
                viewModel.profileType.get()?.lowercase() ?: return
            ).accessToken(
                (if (Mapbox.getAccessToken() != null) Mapbox.getAccessToken()
                else getString(
                    R.string.mapbox_access_token
                ))!!
            ).source(FIRST)
            .destination(LAST).roundTrip(false)//restrict to re join origin
            .overview(DirectionsCriteria.OVERVIEW_FULL)
            .build()


        optimizedClient?.enqueueCall(object : Callback<OptimizationResponse?> {
            override fun onResponse(
                call: Call<OptimizationResponse?>, response: Response<OptimizationResponse?>
            ) {
                if (!response.isSuccessful) {
                    Timber.d(getString(R.string.no_success))
                    Toast.makeText(
                        this@CreateRouteActivity, R.string.no_success, Toast.LENGTH_SHORT
                    ).show()
                } else {
                    if (response.body() != null) {
                        val routes = response.body()!!.trips()
                        if (routes != null) {
                            if (routes.isEmpty()) {
                                Timber.d(
                                    "%s size = %s",
                                    getString(R.string.successful_but_no_routes),
                                    routes.size
                                )
                                Toast.makeText(
                                    this@CreateRouteActivity,
                                    R.string.successful_but_no_routes,
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                // Get most optimized route from API response
                                optimizedRoute = routes[0]
                                drawOptimizedRoute(style, optimizedRoute!!)

                                viewModel.getRouteElevation()
                                setRequestData(response)
                            }
                        } else {
                            Timber.d("list of routes in the response is null")
                            Toast.makeText(
                                this@CreateRouteActivity, String.format(
                                    getString(R.string.null_in_response),
                                    "The Optimization API response's list of routes"
                                ), Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Timber.d("response.body() is null")
                        Toast.makeText(
                            this@CreateRouteActivity, String.format(
                                getString(R.string.null_in_response),
                                "The Optimization API response's body"
                            ), Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            override fun onFailure(call: Call<OptimizationResponse?>, throwable: Throwable) {
                Timber.d("Error: %s", throwable.message)
            }
        })
    }

    private fun setRequestData(response: Response<OptimizationResponse?>) {
        val distance = response.body()?.trips()?.get(0)?.distance() ?: return
        val milesValue = 0.00062137119

        viewModel.distance.set(
            DecimalFormat("0.00").format(distance * milesValue).toDouble()
        )//received default distance in meters and convert into miles
        viewModel.maxDistance.set(
            "${DecimalFormat("0.00").format(distance * milesValue).toDouble()} mi"
        )

        val time = getTime(response.body()?.trips()?.get(0)?.duration()?.toLong() ?: return)

        viewModel.expectedTime.set(
            time
        ) //received default time in meters

//        viewModel.maxTime.set(time)

//        val staticImage = takeSnapshot(
//            mapboxMap!!.cameraPosition,
//            if (viewModel.mapStyle.get()?.equals("") ?: return) viewModel.mapViewType.get()
//                ?: return else viewModel.mapStyle.get() ?: return,
//            500,
//            300,
//        )?.url()
////        viewModel.mapImagesWithRouteAndMArker.set(staticImage.toString())
////        viewModel.mapImagesWithRouteAndMArker.set(getRouteImage())
//        viewModel.elevationGain.set(mapView?.elevation.toString())
//        viewModel.elevationLoss.set(mapView?.elevation.toString())

    }

    private fun getRouteImage(): String {
        return "https://api.mapbox.com/styles/v1/mapbox/${viewModel.mapViewType.get()}/static/geojson(%7B%22type%22:%22FeatureCollection%22,%22features%22:[%7B%22type%22:%22Feature%22,%22properties%22:%7B%22stroke%22:%22%23FCAC1D%22%7D,%22geometry%22:%7B%22type%22:%22LineString%22,%22coordinates%22:${
            createCoordinateString()
        }%7D%7D]%7D)/auto/600x450?access_token=${
            viewModel.dataManager.getResourceProvider().getString(R.string.mapbox_access_token)
        }"
    }

    private fun createCoordinateString(): String {
        val imageCoordinate = arrayListOf<String>()
        for (i in 0 until viewModel.coordinates.size) {
            imageCoordinate.add("[${viewModel.coordinates[i].longitude},${viewModel.coordinates[i].latitude}]")
        }
        return imageCoordinate.toString()
    }


    //calculate Route time
    private fun getTime(time: Long): String {
        var duration = time

        val hours: Long = duration / 3600

        val minutes: Long = (duration - (hours * 3600)) / 60


        val seconds: Long = (duration - (hours * 3600) - (minutes * 60))

        val HH = if (hours < 10) "0$hours" else "$hours"
        val MM = if (minutes < 10) "0$minutes" else "$minutes"
        val SS = if (seconds < 10) "0$seconds" else "$seconds"

        return "$HH:$MM:$SS"
    }

    //Draw route when route received form optimize route api
    private fun drawOptimizedRoute(style: Style, route: DirectionsRoute) {
        val optimizedLineSource =
            style.getSourceAs<GeoJsonSource>(viewModel.OPTIMIZED_ROUTE_SOURCE_ID)
        optimizedLineSource?.setGeoJson(
            FeatureCollection.fromFeature(
                Feature.fromGeometry(
                    LineString.fromPolyline(route.geometry()!!, Constants.PRECISION_6)
                )
            )
        )
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView!!.onSaveInstanceState(outState)
    }


    override fun onDestroy() {
        super.onDestroy()
        // Cancel the directions API request
        if (optimizedClient != null) {
            optimizedClient?.cancelCall()
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

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        mapView?.onSaveInstanceState(outState)
    }


    // used to reset mapbox
    override fun resetMapBox() {
        viewModel.stops.clear()
        if (mapboxMap != null) {
            val style = mapboxMap!!.style
            if (style != null) {
                resetDestinationMarkers(style)
                removeOptimizedRoute(style)
            }
        }
    }

    //used to navigate to search option
    override fun navigateToMapBoxSearch() {
        val intent = PlaceAutocomplete.IntentBuilder().accessToken(
            ((if (Mapbox.getAccessToken() != null) Mapbox.getAccessToken() else getString(R.string.mapbox_access_token))!!)
        ).placeOptions(
            PlaceOptions.builder().backgroundColor(Color.parseColor("#EEEEEE")).limit(10)
//                    .addInjectedFeature(home)
//                    .addInjectedFeature(work)
                .build(PlaceOptions.MODE_CARDS)
        ).build(this@CreateRouteActivity)

        startForSearchLocationResult.launch(intent)
    }


    //Set Route date when route is created
    override fun setDataToCreateRoute() {
        if (viewModel.coordinates.size > 1) {
            val intent = Intent()
            intent.putParcelableArrayListExtra("coordinates", viewModel.coordinates)
            intent.putExtra("distance", viewModel.distance.get())
            intent.putExtra("distanceUnit", viewModel.distanceUnit.get())
            intent.putExtra("elevationGain", viewModel.elevationGain.get())
            intent.putExtra("elevationLoss", viewModel.elevationLoss.get())
            intent.putExtra("expectedTime", viewModel.expectedTime.get())
            intent.putExtra("mapStyle", viewModel.mapStyle.get())
            intent.putExtra("imageUrl", getRouteImage())
            setResult(ROUTE_DATA, intent)
            finish() //finishing activity
        } else {
            Toast.makeText(this, "Please create route", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getCurrentLocation() {
        locationPermissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }


    private fun updateMapBoxView(isCurrentLocation: Boolean) {
        // Move map camera to the selected location
        mapboxMap?.animateCamera(
            CameraUpdateFactory.newCameraPosition(
                CameraPosition.Builder().target(
                    LatLng(
                        viewModel.origin.get()?.latitude() ?: return,
                        viewModel.origin.get()?.longitude() ?: return,
                    )
                ).zoom(15.0).build()
            ), 4000
        )
        if (isCurrentLocation) {
            mapboxMap?.setStyle(viewModel.mapStyle.get()) { style ->
                initMarkerIconSymbolLayer(style)
                initOptimizedRouteLineLayer(style)
            }
        }
    }


    //used to take mapbox screen shot
    private fun takeSnapshot(
        cameraPosition: CameraPosition, styleUrl: String, width: Int, height: Int
    ): MapboxStaticMap? {
        return MapboxStaticMap.builder().accessToken(getString(R.string.mapbox_access_token))
            .styleId(viewModel.mapViewType.get()!!).cameraPoint(
                Point.fromLngLat(
                    viewModel.origin.get()?.longitude()!!,
                    viewModel.origin.get()?.latitude()!!,
                ),
            ).cameraZoom(cameraPosition.zoom).cameraPitch(cameraPosition.tilt)
            .cameraBearing(cameraPosition.bearing).width(width).height(height).retina(true)
            .beforeLayer(viewModel.OPTIMIZED_ROUTE_SOURCE_ID).build()
    }


    private fun getLocation() {
        viewModel.fusedLocationProviderClient.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY,
            object : CancellationToken() {
                override fun onCanceledRequested(p0: OnTokenCanceledListener) =
                    CancellationTokenSource().token

                override fun isCancellationRequested() = false
            }).addOnSuccessListener { location: Location? ->
            if (location == null)
                Log.d("LOCATION", "Cannot get location.")
            else {
                val lat = location.latitude
                val lon = location.longitude

                getAddressFromLatlng(location.latitude, location.longitude)
                viewModel.origin.set(Point.fromLngLat(lon, lat))
                updateMapBoxView(true)
            }

        }

    }

    //used to get address form lat & lng
    private fun getAddressFromLatlng(latitude: Double, longitude: Double) {
        val geocoder = Geocoder(this, Locale.getDefault())
        val addressList: MutableList<Address>? = geocoder.getFromLocation(latitude, longitude, 1)
        var result: String? = null
        try {
            val addressList = geocoder.getFromLocation(latitude, longitude, 1)
            if (addressList != null && addressList.size > 0) {
                val address = addressList[0]
                val sb = StringBuilder()
                for (i in 0 until address.maxAddressLineIndex) {
                    sb.append(address.getAddressLine(i)) //.append("\n")
                }
                sb.append(address.subLocality).append(", ")
                sb.append(address.locality).append(", ")
                sb.append(address.countryName)
                result = sb.toString()
                viewModel.currentAddress.set(result)
            }
        } catch (e: IOException) {
            Log.e("Location Address Loader", "Unable connect to Geocoder", e)
        }
    }

//    override fun navigateToPopularRouteScreen() {
//        val extras = ChallengeExtras.challengeExtras {
//            challengeData = ChallengeModel(
//                3,
//                viewModel.challengeModel.selectedSportId,
//                viewModel.challengeModel.profileType,
//                viewModel.challengeModel.sportsName,
//                viewModel.challengeModel.startDate,
//                viewModel.challengeModel.endDate,
//                0,//for popular route
//                0,
//                "",
//                "",
//                arrayListOf(),
//                arrayListOf(),
//                "",
//                "",
//                "",
//                0.0,
//                0.0,
//                "",
//                "",
//                "",
//                arrayListOf(),
//                arrayListOf(),
//                "",
//                "",
//                "",
//                "",
//                "",
//                "",
//                "",
//                "",
//                "",
//                "",
//                "",
//                "",
//                "",
//                "",
//                "",
//                "",
//                arrayListOf<InviteMember>()
//            )
//        }
//        startActivity(
//            intentProviderFactory.create(
//                RouteListingActivity::class.java, extras, 0
//            )
//        )
//    }
//
//    override fun navigateToLatestRouteScreen() {
//        val extras = ChallengeExtras.challengeExtras {
//            challengeData = ChallengeModel(
//                3,
//                viewModel.challengeModel.selectedSportId,
//                viewModel.challengeModel.profileType,
//                viewModel.challengeModel.sportsName,
//                viewModel.challengeModel.startDate,
//                viewModel.challengeModel.endDate,
//                1,//for popular route
//                0,
//                "",
//                "",
//                arrayListOf(),
//                arrayListOf(),
//                "",
//                "",
//                "",
//                0.0,
//                0.0,
//                "",
//                "",
//                "",
//                arrayListOf(),
//                arrayListOf(),
//                "",
//                "",
//                "",
//                "",
//                "",
//                "",
//                "",
//                "",
//                "",
//                "",
//                "",
//                "",
//                "",
//                "",
//                "",
//                "",
//                arrayListOf<InviteMember>()
//            )
//
//        }
//        startActivity(
//            intentProviderFactory.create(
//                RouteListingActivity::class.java, extras, 0
//            )
//        )
//    }
//
//    override fun navigateToFavoriteRouteScreen() {
//        val extras = ChallengeExtras.challengeExtras {
//            challengeData = ChallengeModel(
//                3,
//                viewModel.challengeModel.selectedSportId,
//                viewModel.challengeModel.profileType,
//                viewModel.challengeModel.sportsName,
//                viewModel.challengeModel.startDate,
//                viewModel.challengeModel.endDate,
//                2,//for popular route
//                0,
//                "",
//                "",
//                arrayListOf(),
//                arrayListOf(),
//                "",
//                "",
//                "",
//                0.0,
//                0.0,
//                "",
//                "",
//                "",
//                arrayListOf(),
//                arrayListOf(),
//                "",
//                "",
//                "",
//                "",
//                "",
//                "",
//                "",
//                "",
//                "",
//                "",
//                "",
//                "",
//                "",
//                "",
//                "",
//                "",
//                arrayListOf<InviteMember>()
//            )
//
//        }
//        startActivity(
//            intentProviderFactory.create(
//                RouteListingActivity::class.java, extras, 0
//            )
//        )
//    }
}