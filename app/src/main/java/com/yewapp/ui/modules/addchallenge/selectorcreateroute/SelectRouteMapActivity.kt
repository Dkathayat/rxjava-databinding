//package com.yewapp.ui.modules.selectroutemap
//
////import com.mapbox.maps.MapView
//
//import android.annotation.SuppressLint
//import android.app.Activity
//import android.content.Intent
//import android.content.pm.PackageManager
//import android.graphics.Color
//import android.location.LocationManager
//import android.os.Bundle
//import android.view.View
//import android.view.ViewGroup
//import android.widget.EditText
//import android.widget.TextView
//import androidx.annotation.NonNull
//import androidx.core.app.ActivityCompat
//import androidx.core.content.ContextCompat
//import com.google.android.gms.location.LocationServices
//import com.mapbox.geojson.Feature
//import com.mapbox.geojson.LineString
//import com.mapbox.geojson.Point
//import com.mapbox.mapboxsdk.Mapbox
//import com.mapbox.mapboxsdk.geometry.LatLng
//import com.mapbox.mapboxsdk.maps.MapView
//import com.mapbox.mapboxsdk.maps.MapboxMap
//import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
//import com.mapbox.mapboxsdk.maps.Style
//import com.mapbox.mapboxsdk.style.layers.CircleLayer
//import com.mapbox.mapboxsdk.style.layers.LineLayer
//import com.mapbox.mapboxsdk.style.layers.Property.LINE_JOIN_ROUND
//import com.mapbox.mapboxsdk.style.layers.PropertyFactory.*
//import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
//import com.mapbox.turf.TurfConstants
//import com.mapbox.turf.TurfConstants.UNIT_KILOMETERS
//import com.mapbox.turf.TurfMeasurement
//import com.yewapp.R
//import com.yewapp.databinding.ActivitySelectRouteMapBinding
//import com.yewapp.ui.base.BaseActivity
//import com.yewapp.ui.dialogs.PermissionsDialog
//import com.yewapp.ui.modules.addchallengelocationdetails.ChallengeLocationDetailActivity
//import com.yewapp.ui.modules.createroute.CreateRouteActivity
//import com.yewapp.ui.modules.listner.Repository
//import com.yewapp.ui.modules.routes.RouteListingActivity
//import com.yewapp.ui.modules.searchlocation.SearchLocationActivity
//import com.yewapp.utils.INTENT_CODE_LOCATION_SEARCH_RESULT
//import com.yewapp.utils.PERMISSION_ACCESS_FINE_LOCATION
//import dagger.android.AndroidInjection
//
//
//class SelectRouteMapActivity :
//    BaseActivity<SelectRouteMapNavigator, SelectRouteMapViewModel, ActivitySelectRouteMapBinding>(),
//    SelectRouteMapNavigator, MapboxMap.OnMapClickListener {
//
//    private var mapView: MapView? = null
//    private val pointList: ArrayList<Point> = ArrayList()
//    private var mapboxMap: MapboxMap? = null
//    private var lineLengthTextView: TextView? = null
//    private var totalLineDistance = 0.0
//
//    private val SOURCE_ID = "SOURCE_ID"
//    private val CIRCLE_LAYER_ID = "CIRCLE_LAYER_ID"
//    private val LINE_LAYER_ID = "LINE_LAYER_ID"
//
//    // Adjust private static final variables below to change the example's UI
//    private val STYLE_URI = "mapbox://styles/mapbox/cjv6rzz4j3m4b1fqcchuxclhb"
//    private val CIRCLE_COLOR: Int = Color.WHITE
//    private val CIRCLE_STROKE_COLOR: Int = Color.BLACK
//    private val LINE_COLOR: Int = Color.parseColor("#FCAC1D")
//    private val CIRCLE_RADIUS = 6f
//    private val LINE_WIDTH = 4f
//    private val DISTANCE_UNITS: String = UNIT_KILOMETERS
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        AndroidInjection.inject(this)
//        Mapbox.getInstance(this, getString(R.string.mapbox_access_token))
//        super.onCreate(savedInstanceState)
//        setContentView(getLayout())
//        setupFocusOutside(findViewById(android.R.id.content))
//        init()
//        Repository.getInstance().getChallengeProgress().observe(this) {
//            viewModel.challengeExtras = it
//        }
//        viewModel.fusedLocationProviderClient =
//            LocationServices.getFusedLocationProviderClient(this)
//
//        mapView = findViewById(R.id.mapView)
//        mapView?.getMapAsync(OnMapReadyCallback { mapboxMap ->
//            lineLengthTextView = findViewById(R.id.line_length_textView)
//            // DISTANCE_UNITS must be equal to a String found in the TurfConstants class
//            lineLengthTextView?.text = String.format(
//                getString(R.string.line_distance_textview),
//                DISTANCE_UNITS, totalLineDistance.toString()
//            )
//            var distance = String.format(
//                getString(R.string.line_distance_textview),
//                DISTANCE_UNITS,
//                totalLineDistance.toString()
//            )
//            // Toast.makeText(this,distance,Toast.LENGTH_LONG).show()
//            this.mapboxMap = mapboxMap
//            mapboxMap.setStyle(
//                com.mapbox.mapboxsdk.maps.Style.Builder()
//                    .fromUri(Style.MAPBOX_STREETS) // Add the source to the map
//                    .withSource(GeoJsonSource(SOURCE_ID)) // Style and add the CircleLayer to the map
//                    .withLayer(
//                        CircleLayer(CIRCLE_LAYER_ID, SOURCE_ID).withProperties(
//                            circleColor(CIRCLE_COLOR),
//                            circleStrokeColor(CIRCLE_STROKE_COLOR),
//                            circleStrokeWidth(5f),
//                            circleRadius(CIRCLE_RADIUS)
//                        )
//                    ) // Style and add the LineLayer to the map. The LineLayer is placed below the CircleLayer.
//                    .withLayerBelow(
//                        LineLayer(LINE_LAYER_ID, SOURCE_ID).withProperties(
//                            lineColor(LINE_COLOR),
//                            lineWidth(LINE_WIDTH),
//                            lineJoin(LINE_JOIN_ROUND)
//                        ), CIRCLE_LAYER_ID
//                    )
//            ) {
//                this@SelectRouteMapActivity.mapboxMap!!.addOnMapClickListener(this@SelectRouteMapActivity)
//            }
//        })
//    }
//
//    @SuppressLint("Lifecycle")
//    override fun onStart() {
//        super.onStart()
//        mapView?.onStart()
//    }
//
//    @SuppressLint("Lifecycle")
//    override fun onStop() {
//        super.onStop()
//        mapView?.onStop()
//    }
//
//    @SuppressLint("Lifecycle")
//    override fun onLowMemory() {
//        super.onLowMemory()
//        mapView?.onLowMemory()
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        if (mapboxMap != null) {
//            mapboxMap!!.removeOnMapClickListener(this)
//        }
//        mapView!!.onDestroy()
//    }
//
//    override fun onMapClick(point: LatLng): Boolean {
//        addClickPointToLine(point)
//        return true
//    }
//
//    /**
//     * Handle the map click location and re-draw the circle and line data.
//     *
//     * @param clickLatLng where the map was tapped on.
//     */
//    private fun addClickPointToLine(@NonNull clickLatLng: LatLng) {
//        mapboxMap!!.getStyle { style ->
//            // Get the source from the map's style
//            val geoJsonSource: GeoJsonSource? = style.getSourceAs(SOURCE_ID)
//            if (geoJsonSource != null) {
//                pointList.add(Point.fromLngLat(clickLatLng.longitude, clickLatLng.latitude))
//                val pointListSize = pointList.size
//                var distanceBetweenLastAndSecondToLastClickPoint = 0.0
//
//                // Make the Turf calculation between the last tap point and the second-to-last tap point.
//                if (pointList.size >= 2) {
//                    distanceBetweenLastAndSecondToLastClickPoint = TurfMeasurement.distance(
//                        pointList[pointListSize - 2],
//                        pointList[pointListSize - 1],
//                        TurfConstants.UNIT_KILOMETERS
//                    )
//                }
//                // Re-draw the new GeoJSON data
//                if (pointListSize >= 2 && distanceBetweenLastAndSecondToLastClickPoint > 0) {
//
//                    // Add the last TurfMeasurement#distance calculated distance to the total line distance.
//                    totalLineDistance += distanceBetweenLastAndSecondToLastClickPoint
//
//                    // Adjust the TextView to display the new total line distance.
//                    // DISTANCE_UNITS must be equal to a String found in the TurfConstants class
//                    lineLengthTextView!!.text = String.format(
//                        getString(R.string.line_distance_textview),
//                        DISTANCE_UNITS,
//                        totalLineDistance.toString()
//                    )
//                    var distance = String.format(
//                        getString(R.string.line_distance_textview),
//                        DISTANCE_UNITS,
//                        totalLineDistance.toString()
//                    )
//                    //  Toast.makeText(this,distance,Toast.LENGTH_LONG).show()
//
//                    // Set the specific source's GeoJSON data
//                    geoJsonSource.setGeoJson(
//                        Feature.fromGeometry(
//                            LineString.fromLngLats(
//                                pointList
//                            )
//                        )
//                    )
//                }
//            }
//        }
//    }
//
//    //Removes the focus from text fields when the user click on the screen
//    private fun setupFocusOutside(view: View) {
//        if (view !is EditText) {
//            view.setOnTouchListener { _, _ ->
//                hideKeyboard()
//                false
//            }
//        }
//
//        if (view is ViewGroup) {
//            for (i in 0 until view.childCount) {
//                val innerView = view.getChildAt(i)
//                setupFocusOutside(innerView)
//            }
//        }
//    }
//    //Hides the Keyboard when the User clicks on the screen
//
//
//    override fun getLayout(): Int {
//        return R.layout.activity_select_route_map
//    }
//
//    override fun init() {
//        bind(SelectRouteMapViewModel::class.java)
//    }
//
//    override fun onViewModelCreated(viewModel: SelectRouteMapViewModel) {
//        viewModel.setNavigator(this)
//    }
//
//    override fun onViewBound(viewDataBinding: ActivitySelectRouteMapBinding) {
//    }
//
//    private fun isLocationEnabled(): Boolean {
//        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
//        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
//            LocationManager.NETWORK_PROVIDER
//        )
//
//
//    }
//
//    private fun getLocation() {
//        if (ContextCompat.checkSelfPermission(
//                this,
//                android.Manifest.permission.ACCESS_FINE_LOCATION
//            )
//            == PackageManager.PERMISSION_GRANTED
//        )
////            fetchLocation()
//
//        else {
//            if (ActivityCompat.shouldShowRequestPermissionRationale(
//                    this,
//                    android.Manifest.permission.ACCESS_FINE_LOCATION
//                )
//            ) {
//                PermissionsDialog.Builder()
//                    .setTitle(getString(R.string.location_permission_title))
//                    .setDescription(getString(R.string.location_permission_description))
//                    .setNegativeText(getString(R.string.app_settings))
//                    .setPositiveText(getString(R.string.not_now))
//                    .build()
//                    .show(this)
//            } else {
//                requestLocationPermission()
//            }
//        }
//
//    }
//
//    private fun requestLocationPermission() {
//        ActivityCompat.requestPermissions(
//            this,
//            arrayOf(
//                android.Manifest.permission.ACCESS_FINE_LOCATION
//            ),
//            PERMISSION_ACCESS_FINE_LOCATION
//        )
//        val abc = ::add
//    }
//
//    fun add(): Boolean {
//        return true
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (resultCode == INTENT_CODE_LOCATION_SEARCH_RESULT) {
//            if (resultCode == Activity.RESULT_OK) {
//                getCurrentLocation()
//            }
//        }
//    }
////    private fun getLastLocation() {
////        if (((ActivityCompat.checkSelfPermission(
////                this, Manifest.permission.ACCESS_FINE_LOCATION
////            ) != PackageManager.PERMISSION_GRANTED) && (ActivityCompat.checkSelfPermission(
////                this, Manifest.permission.ACCESS_COARSE_LOCATION
////            ) != PackageManager.PERMISSION_GRANTED))
////        ) {
////            ActivityCompat.requestPermissions(
////                this,
////                arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION),
////                REQUEST_CODE_FOR_LOCATION
////            )
////            return
////        }
////        viewModel.mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
////        val mSettingsClient = LocationServices.getSettingsClient(this)
////        viewModel.mLocationCallback = object : LocationCallback() {
////            override fun onLocationResult(result: LocationResult?) {
////                if (result != null) {
////                    super.onLocationResult(result)
////                }
////                //mCurrentLocation = locationResult.getLastLocation();
////                viewModel.mCurrentLocation = result!!.locations[0]
////
////                if (viewModel.mCurrentLocation != null) {
////                    viewModel.currentLocation = viewModel.mCurrentLocation as Location
////
////                    Log.e("Location(Lat)==", "" + viewModel.mCurrentLocation?.latitude)
////                    Log.e("Location(Long)==", "" + viewModel.mCurrentLocation?.longitude)
////                    val geocode = Geocoder(this@SelectRouteMapActivity)
////                    try {
////                        val addressList: List<Address>? =
////                            geocode.getFromLocation(viewModel.mCurrentLocation?.latitude!!,
////                                viewModel.mCurrentLocation?.longitude!!, 1)
////                        if (addressList != null && addressList.isNotEmpty()) {
////                            val locality: String = addressList[0].getAddressLine(0)
////                            val country: String = addressList[0].countryName ?: ""
////                            if (locality.isNotEmpty() && country.isNotEmpty()) {
////                                viewModel.currentAddress.set("$locality  $country")
////                            }
////                        }
////                    } catch (e: IOException) {
////                        e.printStackTrace()
////                    }
//////                    val supportMapFragment =
//////                        supportFragmentManager.findFragmentById(com.google.android.gms.location.R.id.map) as SupportMapFragment
//////                    assert(supportMapFragment != null)
//////                    supportMapFragment.getMapAsync(onMapReadyMethod)
//////                    configureCameraIdle()
////
////                } else {
////                    //  viewModel.location.value = mCurrentLocation
////                }
////                /**
////                 * To get location information consistently
////                 * mLocationRequest.setNumUpdates(1) Commented out
////                 * Uncomment the co de below
////                 */
////                viewModel.mFusedLocationClient?.removeLocationUpdates(viewModel.mLocationCallback)
////            }
////
////            //Locatio nMeaning that all relevant information is available
////            override fun onLocationAvailability(availability: LocationAvailability?) {
////                //boolean isLocation = availability.isLocationAvailable();
////            }
////        }
////        viewModel.mLocationRequest = LocationRequest()
////        viewModel.mLocationRequest?.interval = 5000
////        viewModel.mLocationRequest?.fastestInterval = 5000
////        //To get location information only once here
////        viewModel.mLocationRequest?.numUpdates = 3
////        if (viewModel.provider.equals(LocationManager.GPS_PROVIDER, ignoreCase = true)) {
////            //Accuracy is a top priority regardless of battery consumption
////            viewModel.mLocationRequest?.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
////        } else {
////            //Acquired location information based on balance of battery and accuracy (somewhat higher accuracy)
////            viewModel.mLocationRequest?.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
////        }
////        val builder = LocationSettingsRequest.Builder()
////        builder.addLocationRequest(viewModel.mLocationRequest!!)
////        /**
////         * Stores the type of location service the client wants to use. Also used for positioning.
////         */
////        val mLocationSettingsRequest = builder.build()
////
////        val locationResponse = mSettingsClient.checkLocationSettings(mLocationSettingsRequest)
////        locationResponse.addOnSuccessListener(this, OnSuccessListener<LocationSettingsResponse> {
////            Log.e("Response", "Successful acquisition of location information!!")
////            //
////            if (ActivityCompat.checkSelfPermission(
////                    this,
////                    Manifest.permission.ACCESS_FINE_LOCATION
////                ) != PackageManager.PERMISSION_GRANTED
////            ) {
////                return@OnSuccessListener
////            }
////            viewModel.mFusedLocationClient?.requestLocationUpdates(
////                viewModel.mLocationRequest!!,
////                viewModel.mLocationCallback,
////                Looper.myLooper()
////            )
////        })
////        //When the location information is not set and acquired, callback
////        locationResponse.addOnFailureListener(this,
////            OnFailureListener { e ->
////                when ((e as ApiException).statusCode) {
////                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> Log.e(
////                        "onFailure",
////                        "Location environment check"
////                    )
////                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
////                        val errorMessage = "Check location setting"
////                        Log.e("onFailure", errorMessage)
////                    }
////                }
////            })
////
////    }
//
//    override fun getCurrentLocation() {
//        getLocation()
//    }
//
//    override fun navigateToPopularRouteScreen() {
//        startActivity(
//            intentProviderFactory.create(
//                RouteListingActivity::class.java,
//                null,
//                0
//            )
//        )
//    }
//
//    override fun navigateToLocationAdditionalDetailActivity() {
//        startActivity(
//            intentProviderFactory.create(
//                ChallengeLocationDetailActivity::class.java,
//                null,
//                0
//            )
//        )
//        Repository.getInstance().getChallengeProgress().value = viewModel.challengeExtras
//    }
//
//    override fun navigateToSearchLocationActivity() {
//        startActivity(
//            intentProviderFactory.create(
//                SearchLocationActivity::class.java,
//                null,
//                0
//            )
//        )
//    }
//
//    override fun navigateToCreateRouteActivity() {
//        startActivity(
//            intentProviderFactory.create(
//                CreateRouteActivity::class.java,
//                null,
//                0
//            )
//        )
//    }
//
//}