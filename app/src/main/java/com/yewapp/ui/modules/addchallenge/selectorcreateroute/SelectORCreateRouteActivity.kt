package com.yewapp.ui.modules.addchallenge.selectorcreateroute

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.yewapp.R
import com.yewapp.data.network.api.challenges.ChallengeModel
import com.yewapp.data.network.api.invitemember.InviteMember
import com.yewapp.data.network.api.routes.Coordinate
import com.yewapp.data.network.api.routes.Coordinates
import com.yewapp.databinding.ActivitySelectOrCreateRouteBinding
import com.yewapp.ui.base.BaseActivity
import com.yewapp.ui.modules.addchallenge.addchallengedetails.ChallengeLocationDetailActivity
import com.yewapp.ui.modules.createroute.CreateRouteActivity
import com.yewapp.ui.modules.createroute.extras.CreateRouteExtras
import com.yewapp.ui.modules.listner.ChallengeExtras
import com.yewapp.ui.modules.addchallenge.challengeroutes.RouteListingActivity
import com.yewapp.utils.ROUTE_DATA

//Todo:Add Challenge Step 3
class SelectORCreateRouteActivity :
    BaseActivity<SelectORCreateRouteNavigator, SelectORCreateRouteViewModel, ActivitySelectOrCreateRouteBinding>(),
    SelectORCreateRouteNavigator {
    override fun getLayout(): Int = R.layout.activity_select_or_create_route

    override fun init() {
        bind(SelectORCreateRouteViewModel::class.java)
    }

    override fun onViewModelCreated(viewModel: SelectORCreateRouteViewModel) {
        viewModel.setNavigator(this)
    }

    override fun onViewBound(viewDataBinding: ActivitySelectOrCreateRouteBinding) {
//        setupFocusOutside(findViewById(android.R.id.content))
    }


    override fun navigateToPopularRouteScreen() {
        val extras = ChallengeExtras.challengeExtras {
//            riderType = viewModel.riderType.get() ?: return
//            sportsTypeID = viewModel.sportsTypeID.get() ?: return
//            sportsType = viewModel.sportsType.get() ?: return
//            startDate = viewModel.startDate.get() ?: return
//            startDate = viewModel.endDate.get() ?: return
//            routeId = viewModel.endDate.get() ?: return
//            navigateTO = 0

            challengeData = ChallengeModel(
                viewModel.challengeModel.isEdit,
                viewModel.challengeModel.challengeID ?: "",
                3,
                viewModel.challengeModel.selectedSportId,
                viewModel.challengeModel.selectedSportImage,
                viewModel.challengeModel.profileType,
                viewModel.challengeModel.sportsName,
                viewModel.challengeModel.startDate,
                viewModel.challengeModel.endDate,
                0,//for popular route
                viewModel.challengeModel.routeID,
                "",
                "",
                viewModel.challengeModel.challengeStatus,
                arrayListOf(),
                arrayListOf(),
                "",
                "",
                arrayListOf(),
                "",
                "",
                0.0,
                0.0,
                "",
                viewModel.challengeModel.country,
                viewModel.challengeModel.countryId,
                viewModel.challengeModel.state,
                viewModel.challengeModel.stateId,
                arrayListOf(),
                arrayListOf(),
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                arrayListOf<InviteMember>()
            )
        }
        startActivity(
            intentProviderFactory.create(
                RouteListingActivity::class.java, extras, 0
            )
        )
    }

    override fun navigateToLatestRouteScreen() {
        val extras = ChallengeExtras.challengeExtras {
            challengeData = ChallengeModel(
                viewModel.challengeModel.isEdit,
                viewModel.challengeModel.challengeID ?: "",
                3,
                viewModel.challengeModel.selectedSportId,
                viewModel.challengeModel.selectedSportImage,
                viewModel.challengeModel.profileType,
                viewModel.challengeModel.sportsName,
                viewModel.challengeModel.startDate,
                viewModel.challengeModel.endDate,
                1,//for popular route
                viewModel.challengeModel.routeID,
                "",
                "",
                "InActive",
                arrayListOf(),
                arrayListOf(),
                "",
                "",
                arrayListOf(),
                "",
                "",
                0.0,
                0.0,
                "",
                viewModel.challengeModel.country,
                viewModel.challengeModel.countryId,
                viewModel.challengeModel.state,
                viewModel.challengeModel.stateId,
                arrayListOf(),
                arrayListOf(),
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                arrayListOf<InviteMember>()
            )

        }
        startActivity(
            intentProviderFactory.create(
                RouteListingActivity::class.java, extras, 0
            )
        )
    }

    override fun navigateToFavoriteRouteScreen() {
        val extras = ChallengeExtras.challengeExtras {
            challengeData = ChallengeModel(
                viewModel.challengeModel.isEdit,
                viewModel.challengeModel.challengeID ?: "",
                3,
                viewModel.challengeModel.selectedSportId,
                viewModel.challengeModel.selectedSportImage,
                viewModel.challengeModel.profileType,
                viewModel.challengeModel.sportsName,
                viewModel.challengeModel.startDate,
                viewModel.challengeModel.endDate,
                2,//for popular route
                viewModel.challengeModel.routeID,
                "",
                "",
                "InActive",
                arrayListOf(),
                arrayListOf(),
                "",
                "",
                arrayListOf(),
                "",
                "",
                0.0,
                0.0,
                "",
                viewModel.challengeModel.country,
                viewModel.challengeModel.countryId,
                viewModel.challengeModel.state,
                viewModel.challengeModel.stateId,
                arrayListOf(),
                arrayListOf(),
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                arrayListOf<InviteMember>()
            )
        }
        startActivity(
            intentProviderFactory.create(
                RouteListingActivity::class.java, extras, 0
            )
        )
    }

    override fun navigateToLocationAdditionalDetailActivity(isChallengeEdit: Boolean) {
        if (isChallengeEdit) {
            callingActivityForEditChallenge()
        } else {
            callingActivityForCreateChallenge()
        }
    }

    /**
     * @author: Narbir Singh
     * @description: This method is used to call next activity for edit challenge
     */
    private fun callingActivityForEditChallenge() {
        val extras = ChallengeExtras.challengeExtras {
            this.challengeData = ChallengeModel(
                viewModel.challengeModel.isEdit,
                viewModel.challengeModel.challengeID ?: "",
                viewModel.challengeModel.step,
                viewModel.challengeModel.selectedSportId,
                viewModel.challengeModel.selectedSportImage,
                viewModel.challengeModel.profileType,
                viewModel.challengeModel.sportsName,
                viewModel.challengeModel.startDate,
                viewModel.challengeModel.endDate,
                0,//for popular route
                viewModel.challengeModel.routeID ?: "",
                viewModel.challengeModel.challengeName ?: "",
                viewModel.challengeModel.challengeVisibility ?: "",
                viewModel.challengeModel.challengeStatus,
                viewModel.challengeModel.selectedSportsLevel ?: arrayListOf(),
                viewModel.challengeModel.ageGroup ?: arrayListOf(),
                viewModel.challengeModel.challengeDescription ?: "",
                viewModel.challengeModel.subSportTypeId ?: "",
                viewModel.challengeModel.sportsEquipments ?: arrayListOf(),
                viewModel.challengeModel.challengeArea ?: "",
                viewModel.challengeModel.location ?: "",
                viewModel.challengeModel.longitude ?: 0.0,
                viewModel.challengeModel.latitude ?: 0.0,
                viewModel.challengeModel.radius ?: "",
                viewModel.challengeModel.country ?: "",
                viewModel.challengeModel.countryId ?: 0,
                viewModel.challengeModel.state ?: "",
                viewModel.challengeModel.stateId ?: 0,
                viewModel.challengeModel.cityId ?: arrayListOf(),
                viewModel.challengeModel.zipCode ?: arrayListOf(),
                viewModel.challengeModel.calories,
                viewModel.distance.get().toString() ?: "",
                viewModel.elevationGain.get().toString() ?: " ",
                viewModel.challengeModel.avgWatt ?: "",
                viewModel.expectedTime.get() ?: "",
                viewModel.challengeModel.maxMember ?: "0",
                viewModel.challengeModel.winnerPickedFrom ?: "",
                viewModel.challengeModel.selectedWinnerPrize ?: "",
                viewModel.challengeModel.overViewValue ?: "",
                viewModel.challengeModel.winnerValue ?: "",
                viewModel.challengeModel.additionalInfoValue ?: "",
                viewModel.challengeModel.rulesValue ?: "",
                viewModel.challengeModel.guidelinesValue ?: "",
                viewModel.challengeModel.qualificationValue ?: "",
                viewModel.challengeModel.bannerImage ?: "",
                viewModel.challengeModel.challengeImage ?: "",
                viewModel.challengeModel.inviteMembers ?: arrayListOf<InviteMember>()
            )
        }
        startActivity(
            intentProviderFactory.create(
                ChallengeLocationDetailActivity::class.java, extras, 0
            )
        )
    }

    /**
     * @author: Narbir Singh
     * @description: This method is used to call next activity for add challenge details to create
     */
    private fun callingActivityForCreateChallenge() {
        val extras = ChallengeExtras.challengeExtras {
            challengeData = ChallengeModel(
                viewModel.challengeModel.isEdit,
                viewModel.challengeModel.challengeID ?: "",
                3,
                viewModel.challengeModel.selectedSportId,
                viewModel.challengeModel.selectedSportImage,
                viewModel.challengeModel.profileType,
                viewModel.challengeModel.sportsName,
                viewModel.challengeModel.startDate,
                viewModel.challengeModel.endDate,
                2,//for popular route
                viewModel.routeID.get() ?: "",
                "",
                "",
                viewModel.challengeModel.challengeStatus,
                arrayListOf(),
                arrayListOf(),
                "",
                "",
                arrayListOf(),
                "",
                "",
                0.0,
                0.0,
                "",
                viewModel.challengeModel.country,
                viewModel.challengeModel.countryId,
                viewModel.challengeModel.state,
                viewModel.challengeModel.stateId,
                arrayListOf(),
                arrayListOf(),
                "",
                "${viewModel.distance.get()}",
                "${viewModel.elevationGain.get()}",
                "",
                "${viewModel.expectedTime.get()}",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                arrayListOf<InviteMember>()
            )
        }
        startActivity(
            intentProviderFactory.create(
                ChallengeLocationDetailActivity::class.java, extras, 0
            )
        )
    }


    override fun navigateToCreateRouteActivity() {
        //used come from back-pressed and route is already created
        if (viewModel.isRouteCreated.get()!!) {
            val extras = CreateRouteExtras.createEditRouteExtras {
                routeID = 1
                profileType = viewModel.challengeModel.profileType ?: return
                mapStyle = viewModel.mapStyle.get() ?: return
                coordinates = viewModel.coordinates
            }
            val createRouteIntent = intentProviderFactory.create(
                CreateRouteActivity::class.java, extras, 0
            )
            startForResult.launch(createRouteIntent)

        } else {
            val extras = CreateRouteExtras.createEditRouteExtras {
                profileType = viewModel.challengeModel.profileType ?: return
            }
            val createRouteIntent = intentProviderFactory.create(
                CreateRouteActivity::class.java, extras, 0
            )
            startForResult.launch(createRouteIntent)

//            viewModel.isRouteCreated.set(false)
//            val extras = ChallengeExtras.challengeExtras {
//                challengeData = viewModel.challengeModel
//            }
//            val createRouteIntent = intentProviderFactory.create(
//                CreateRouteActivity::class.java, extras, 0
//            )
//            startForResult.launch(createRouteIntent)
        }
//        viewModel.isRouteCreated.set(false)
//        viewModel.routeID.set(0)
    }

    @SuppressLint("LongLogTag", "LogNotTimber")
    var startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.data != null) {
                if (result.resultCode == ROUTE_DATA) {
                    Log.d("SelectORCreateRouteActivity", "From Activity2:    RESULT_OK\n")
                    setRouteData(result.data)
                }
                if (result.resultCode == Activity.RESULT_CANCELED) {
                    Log.d("SelectORCreateRouteActivity", "From Activity2:    CANCELLED\n")
                }
            } else {
                Log.d("SelectORCreateRouteActivity", "From Activity2:    NO DATA\n")
            }

        }

    //Set recivied result from create route activity
    private fun setRouteData(data: Intent?) {
        viewModel.coordinates.clear()
        viewModel.isRouteCreated.set(true)
        viewModel.coordinates.addAll(data?.getParcelableArrayListExtra<Coordinates>("coordinates") as List<Coordinate>)
        viewModel.distance.set(data.getDoubleExtra("distance", 0.0))
        viewModel.distanceUnit.set(data.getStringExtra("distanceUnit"))
        viewModel.elevationGain.set(data.getDoubleExtra("elevationGain", 0.0))
        viewModel.elevationLoss.set(data.getDoubleExtra("elevationLoss", 0.0))
        viewModel.expectedTime.set(data.getStringExtra("expectedTime"))
        viewModel.mapStyle.set(data.getStringExtra("mapStyle"))
        viewModel.mapImage.set(data.getStringExtra("imageUrl"))//Route Image

        //Call for getting elevation from mapbox api
//        viewModel.getRouteElevation()
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

    override fun skipCreateRoute() {
        if (viewModel.challengeModel.isEdit) {
            callingActivityForEditChallenge()
        } else {
            val extras = ChallengeExtras.challengeExtras {
                challengeData = ChallengeModel(
                    false,
                    viewModel.challengeModel.challengeID ?: "",
                    3,
                    viewModel.challengeModel.selectedSportId,
                    viewModel.challengeModel.selectedSportImage,
                    viewModel.challengeModel.profileType,
                    viewModel.challengeModel.sportsName,
                    viewModel.challengeModel.startDate,
                    viewModel.challengeModel.endDate,
                    0,//for popular route
                    viewModel.challengeModel.routeID,
                    "",
                    "",
                    viewModel.challengeModel.challengeStatus,
                    arrayListOf(),
                    arrayListOf(),
                    "",
                    "",
                    arrayListOf(),
                    "",
                    "",
                    0.0,
                    0.0,
                    "",
                    viewModel.challengeModel.country,
                    viewModel.challengeModel.countryId,
                    viewModel.challengeModel.state,
                    viewModel.challengeModel.stateId,
                    arrayListOf(),
                    arrayListOf(),
                    "",
                    "${viewModel.distance.get()}",
                    "${viewModel.elevationGain.get()}",
                    "",
                    "${viewModel.expectedTime.get()}",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    arrayListOf<InviteMember>()
                )
            }

            startActivity(
                intentProviderFactory.create(
                    ChallengeLocationDetailActivity::class.java, extras, 0
                )
            )
        }

    }
}