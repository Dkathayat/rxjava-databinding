package com.yewapp.ui.modules.routedetail

import android.graphics.Color
import android.os.Bundle
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.style.layers.CircleLayer
import com.mapbox.mapboxsdk.style.layers.LineLayer
import com.mapbox.mapboxsdk.style.layers.Property
import com.mapbox.mapboxsdk.style.layers.PropertyFactory
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import com.yewapp.R
import com.yewapp.databinding.RouteDetailActivityBinding
import com.yewapp.ui.base.BaseActivity
import com.yewapp.ui.modules.addchallenge.addchallengedetails.ChallengeLocationDetailActivity
import com.yewapp.ui.modules.listner.ChallengeExtras
import dagger.android.AndroidInjection


class RouteDetailActivity :
    BaseActivity<RouteDetailNavigator, RouteDetailViewModel, RouteDetailActivityBinding>(),
    RouteDetailNavigator, MapboxMap.OnMapClickListener {

    private var mapView: MapView? = null
    private var mapboxMap: MapboxMap? = null
    private val CIRCLE_COLOR: Int = Color.WHITE
    private val CIRCLE_STROKE_COLOR: Int = Color.BLACK
    private val LINE_COLOR: Int = Color.parseColor("#FCAC1D")
    private val CIRCLE_RADIUS = 6f
    private val LINE_WIDTH = 4f
    private val SOURCE_ID = "SOURCE_ID"
    private val CIRCLE_LAYER_ID = "CIRCLE_LAYER_ID"
    private val LINE_LAYER_ID = "LINE_LAYER_ID"

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token))
        super.onCreate(savedInstanceState)
        setContentView(getLayout())
        init()
        mapView = findViewById(R.id.detail_mapView)

        mapView?.getMapAsync(OnMapReadyCallback { mapboxMap ->

            this.mapboxMap = mapboxMap
            mapboxMap.setStyle(
                com.mapbox.mapboxsdk.maps.Style.Builder()
                    .fromUri(Style.MAPBOX_STREETS) // Add the source to the map
                    .withSource(GeoJsonSource(SOURCE_ID)) // Style and add the CircleLayer to the map
                    .withLayer(
                        CircleLayer(CIRCLE_LAYER_ID, SOURCE_ID).withProperties(
                            PropertyFactory.circleColor(CIRCLE_COLOR),
                            PropertyFactory.circleStrokeColor(CIRCLE_STROKE_COLOR),
                            PropertyFactory.circleStrokeWidth(5f),
                            PropertyFactory.circleRadius(CIRCLE_RADIUS)
                        )
                    ) // Style and add the LineLayer to the map. The LineLayer is placed below the CircleLayer.
                    .withLayerBelow(
                        LineLayer(LINE_LAYER_ID, SOURCE_ID).withProperties(
                            PropertyFactory.lineColor(LINE_COLOR),
                            PropertyFactory.lineWidth(LINE_WIDTH),
                            PropertyFactory.lineJoin(Property.LINE_JOIN_ROUND)
                        ), CIRCLE_LAYER_ID
                    )
            ) {
                this@RouteDetailActivity.mapboxMap!!.addOnMapClickListener(this@RouteDetailActivity)

            }
        })


    }

    override fun getLayout(): Int {
        return R.layout.route_detail_activity
    }

    override fun onResume() {
        super.onResume()
        viewModel.getRouteDetail()
    }

    override fun init() {
        bind(RouteDetailViewModel::class.java)
    }

    override fun onViewModelCreated(viewModel: RouteDetailViewModel) {
        viewModel.setNavigator(this)
    }

    override fun onViewBound(viewDataBinding: RouteDetailActivityBinding) {
//        viewModel.getRouteDetail()
    }

    override fun onMapClick(point: LatLng): Boolean {
        return true
    }

    override fun navigateToLocationAdditionalDetailActivity() {
        val extras = ChallengeExtras.challengeExtras {
            challengeData = viewModel.challengeModel
        }

        startActivity(
            intentProviderFactory.create(
                ChallengeLocationDetailActivity::class.java,
                extras,
                0
            )
        )
    }

    override fun addCoordinates() {
    }

/*
    override fun addCoordinates() {
        val client = MapboxMapMatching.builder()
            .accessToken(MAPBOX_ACCESS_TOKEN)
            .profile(PROFILE_DRIVING)
            .coordinates(viewModel.pointList)
            .annotations(ANNOTATION_SPEED)
            .overview(OVERVIEW_FULL)
            .steps(false)
            .build()

        client.enqueueCall(object : Callback<MapMatchingResponse> {


            override fun onFailure(call: Call<MapMatchingResponse>, t: Throwable) {

            }

            override fun onResponse(
                call: Call<MapMatchingResponse>,
                response: Response<MapMatchingResponse>
            ) {

                var mapMatchedPoints: ArrayList<LatLng> = ArrayList()
               // mapMatchedPoints.addAll(pointList.)
                var b = 0
                for (i in 0 until response.body().getMatchedPoints().size()) {
                    mapMatchedPoints.add(
                        LatLng(
                            response.body().getMatchedPoints().get(i).getLatitude(),
                            response.body().getMatchedPoints().get(i).getLongitude()
                        )
                    )
                }
//                 Add the map matched route to your Mapbox map
                mapboxMap?.addPolyline(
                    PolylineOptions()
                        .addAll(mapMatchedPoints)
                        .color(Color.parseColor("#3bb2d0"))
                        .width(4f)
                )

            }


        })
    }
*/


}