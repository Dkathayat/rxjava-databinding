package com.yewapp.ui.modules.searchlocation

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.annotation.NonNull
import com.google.gson.JsonObject
import com.mapbox.api.geocoding.v5.models.CarmenFeature
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
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
import com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage
import com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset
import com.mapbox.mapboxsdk.style.layers.SymbolLayer
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import com.yewapp.R
import com.yewapp.databinding.ActivitySearchLocationBinding
import com.yewapp.ui.base.BaseActivity
import com.yewapp.ui.modules.addchallenge.addchallengedetails.ChallengeLocationDetailActivity
import dagger.android.AndroidInjection


class SearchLocationActivity :
    BaseActivity<SearchLocationNavigator, SearchLocationViewModel, ActivitySearchLocationBinding>(),
    SearchLocationNavigator, MapboxMap.OnMapClickListener, OnMapReadyCallback {

    override fun getLayout(): Int {
        return R.layout.activity_search_location
    }

    private var mapView: MapView? = null
    private var mapboxMap: MapboxMap? = null
    private var home: CarmenFeature? = null
    private var work: CarmenFeature? = null
    private val geojsonSourceLayerId = "geojsonSourceLayerId"
    private val symbolIconId = "symbolIconId"
    private val REQUEST_CODE_AUTOCOMPLETE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token))
        super.onCreate(savedInstanceState)
        setContentView(getLayout())
        init()
        mapView = findViewById(R.id.mapView)
        mapView?.getMapAsync(OnMapReadyCallback { mapboxMap ->

            this.mapboxMap = mapboxMap
            mapboxMap.setStyle(
                Style.Builder()
                    .fromUri(Style.MAPBOX_STREETS) // Add the source to the map
            ) {
                this@SearchLocationActivity.mapboxMap!!.addOnMapClickListener(this@SearchLocationActivity)
            }
        })

    }



    override fun init() {
        bind(SearchLocationViewModel::class.java)
    }

    override fun onViewModelCreated(viewModel: SearchLocationViewModel) {
        viewModel.setNavigator(this)
    }

    override fun onViewBound(viewDataBinding: ActivitySearchLocationBinding) {
        viewDataBinding.searchEditTxt.setOnClickListener {
            val intent = PlaceAutocomplete.IntentBuilder()
                .accessToken(
                    (if (Mapbox.getAccessToken() != null) Mapbox.getAccessToken() else getString(
                        R.string.mapbox_access_token
                    ))!!
                )
                .placeOptions(
                    PlaceOptions.builder()
                        .backgroundColor(Color.parseColor("#EEEEEE"))
                        .limit(10)
//                        .addInjectedFeature(home)
//                        .addInjectedFeature(work)
                        .build(PlaceOptions.MODE_CARDS)
                )
                .build(this@SearchLocationActivity)
            startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE)
        }
    }

    override fun onMapClick(point: LatLng): Boolean {
        return true
    }

    override fun navigateToLocationAdditionalDetailActivity() {
        startActivity(
            intentProviderFactory.create(
                ChallengeLocationDetailActivity::class.java,
                null,
                0
            )
        )
    }

    override fun onMapReady(mapboxMap: MapboxMap) {

        this.mapboxMap = mapboxMap
        mapboxMap.setStyle(
            Style.MAPBOX_STREETS
        ) { style ->
            initSearchFab()
            addUserLocations()

// Add the symbol layer icon to map for future use
            style.addImage(
                symbolIconId, BitmapFactory.decodeResource(
                    this@SearchLocationActivity.resources, R.drawable.map_default_map_marker
                )
            )

// Create an empty GeoJSON source using the empty feature collection
            setUpSource(style)

// Set up a new symbol layer for displaying the searched location's feature coordinates
            setupLayer(style)
        }
    }

    private fun initSearchFab() {
        findViewById<View>(R.id.search_EditTxt).setOnClickListener {
            val intent = PlaceAutocomplete.IntentBuilder()

                .accessToken(
                    (if (Mapbox.getAccessToken() != null) Mapbox.getAccessToken() else getString(
                        R.string.mapbox_access_token
                    ))!!
                )
                .placeOptions(
                    PlaceOptions.builder()
                        .backgroundColor(Color.parseColor("#EEEEEE"))
                        .limit(10)
                        .addInjectedFeature(home)
                        .addInjectedFeature(work)
                        .build(PlaceOptions.MODE_CARDS)
                )
                .build(this@SearchLocationActivity)
            startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE)
        }
    }

    private fun addUserLocations() {
        home = CarmenFeature.builder().text("Mapbox SF Office")
            .geometry(Point.fromLngLat(-122.3964485, 37.7912561))
            .placeName("50 Beale St, San Francisco, CA")
            .id("mapbox-sf")

            .properties(JsonObject())
            .build()
        work = CarmenFeature.builder().text("Mapbox DC Office")
            .placeName("740 15th Street NW, Washington DC")
            .geometry(Point.fromLngLat(-77.0338348, 38.899750))
            .id("mapbox-dc")
            .properties(JsonObject())
            .build()
    }

    private fun setUpSource(@NonNull loadedMapStyle: Style) {
        loadedMapStyle.addSource(GeoJsonSource(geojsonSourceLayerId))
    }

    private fun setupLayer(@NonNull loadedMapStyle: Style) {
        loadedMapStyle.addLayer(
            SymbolLayer("SYMBOL_LAYER_ID", geojsonSourceLayerId).withProperties(
                iconImage(symbolIconId),
                iconOffset(arrayOf(0f, -8f))
            )
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_AUTOCOMPLETE) {

// Retrieve selected location's CarmenFeature
            val selectedCarmenFeature = PlaceAutocomplete.getPlace(data)

// Create a new FeatureCollection and add a new Feature to it using selectedCarmenFeature above.
// Then retrieve and update the source designated for showing a selected location's symbol layer icon
            if (mapboxMap != null) {
                val style = mapboxMap!!.style
                if (style != null) {
                    val source = style.getSourceAs<GeoJsonSource>(geojsonSourceLayerId)
                    source?.setGeoJson(
                        FeatureCollection.fromFeatures(
                            arrayOf<Feature>(
                                Feature.fromJson(
                                    selectedCarmenFeature.toJson()
                                )
                            )
                        )
                    )

// Move map camera to the selected location
                    mapboxMap!!.animateCamera(
                        CameraUpdateFactory.newCameraPosition(
                            CameraPosition.Builder()
                                .target(
                                    LatLng(
                                        (selectedCarmenFeature.geometry() as Point?)!!.latitude(),
                                        (selectedCarmenFeature.geometry() as Point?)!!.longitude()
                                    )
                                )
                                .zoom(14.0)
                                .build()
                        ), 4000
                    )
                }
            }
        }
    }

    // Add the mapView lifecycle to the activity's lifecycle methods
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

    override fun onLowMemory() {
        super.onLowMemory()
        mapView!!.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView!!.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView!!.onSaveInstanceState(outState)
    }

}