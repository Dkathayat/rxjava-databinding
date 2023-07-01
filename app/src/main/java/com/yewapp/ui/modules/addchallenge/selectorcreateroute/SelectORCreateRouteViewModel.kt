package com.yewapp.ui.modules.addchallenge.selectorcreateroute

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.ObservableField
import com.yewapp.R
import com.yewapp.data.network.DataManager
import com.yewapp.data.network.api.addmodelequipment.EquipmentData
import com.yewapp.data.network.api.challenges.*
import com.yewapp.data.network.api.createchallenge.CreateChallengeRequest
import com.yewapp.data.network.api.invitemember.InviteMember
import com.yewapp.data.network.api.routes.Coordinate
import com.yewapp.data.network.api.routes.Coordinates
import com.yewapp.data.network.api.routes.CreateRouteRequest
import com.yewapp.data.network.api.routes.CreateRouteResponse
import com.yewapp.ui.base.BaseViewModel
import com.yewapp.ui.modules.listner.ChallengeExtras
import com.yewapp.ui.modules.listner.ChallengeExtrasEdit
import com.yewapp.utils.DateUtils
import com.yewapp.utils.rx.SchedulerProvider

class SelectORCreateRouteViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) :
    BaseViewModel<SelectORCreateRouteNavigator>(dataManager, schedulerProvider) {
    //A client that handles connection / connection failures for Google locations

    lateinit var challengeModel: ChallengeModel

    var routeID = ObservableField<String>("")

    //    var sportsTypeID = ObservableField<String>("")
//    var sportsType = ObservableField<String>("")
    var riderType = ObservableField<String>("")
    var startDate = ObservableField<String>("")
    var endDate = ObservableField<String>("")


    var routeName = ObservableField<String>("")

    //    var sportTypeID = ObservableField<Int>(0)
//    var rideType = ObservableField<String>("biking")
    var expectedTime = ObservableField<String>("")
    var distance = ObservableField<Double>(0.0)
    var mapStyle = ObservableField<String>("")
    var isRouteCreated =
        ObservableField<Boolean>(false)//handle continue button clickable and create or edit route button

    var distanceUnit = ObservableField<String>("miles")
    var elevationGain = ObservableField<Double>(0.0)
    var elevationLoss = ObservableField<Double>(0.0)
    var coordinates = arrayListOf<Coordinate>()


    var provider: String? = null
    var challengeExtras: ChallengeExtras? = null

    //    val title = ObservableField<String>("")
    val routeNameError = ObservableField<String>("")
    val description = ObservableField<String>("")
    val descriptionError = ObservableField<String>("")
    val editChallenge = ObservableField<Boolean>(false)
    val mapImage =
        ObservableField<String>("https://api.mapbox.com/styles/v1/mapbox/streets-v11/static/geojson(%7B%22type%22:%22FeatureCollection%22,%22features%22:[%7B%22type%22:%22Feature%22,%22properties%22:%7B%22stroke%22:%22%23FCAC1D%22%7D,%22geometry%22:%7B%22type%22:%22LineString%22,%22coordinates%22:[[2.33221,48.87035],[2.33483,48.86496],[2.33569,48.86315]]%7D%7D]%7D)/auto/600x500?access_token=pk.eyJ1IjoieWV3YXBwIiwiYSI6ImNrd2t6d216dDF4MXUydnFpenIxa3ppa2YifQ.DQqCFvFfQPCrOMLvE6uRAQ")

    override fun setData(extras: Bundle?) {
        if (extras?.containsKey(ChallengeExtras.CHALLENGE_DATA) == true) {
            challengeModel =
                extras.getParcelable<ChallengeModel>(ChallengeExtras.CHALLENGE_DATA) ?: return
            editChallenge.set(false)
        } else {
            tempInitMOdel()
            getChallengeDetailsApi(extras?.getInt(ChallengeExtrasEdit.CHALLENGE_ID) ?: return)
            editChallenge.set(true)
        }


    }

    private fun tempInitMOdel() {
        challengeModel = ChallengeModel(
            false,
            "",
            3,
            0,
            "",
            "",
            "",
            "",
            "",
            0,//for popular route
            "",
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
            "",
            0,
            "",
            0,
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

    fun onActionClick(view: View) {
        if (isLoading.get()) return
        when (view.id) {
            R.id.tvSaveAsDraft -> {
                if (challengeModel.isEdit) {
                    saveAsDraftWhenEdit()
                } else
                    saveAsDraft()
            }
            R.id.tvSkip -> {
                getNavigator()?.skipCreateRoute()
            }
            R.id.popular_txt -> {
                getNavigator()?.navigateToPopularRouteScreen()
            }
            R.id.latest_txt -> {
                getNavigator()?.navigateToLatestRouteScreen()
            }
            R.id.favorites_txt -> {
                getNavigator()?.navigateToFavoriteRouteScreen()
            }
            R.id.popular_layout -> {
                getNavigator()?.navigateToPopularRouteScreen()
            }
            R.id.btn_continue -> {
                //when create route api updated
                if (isRouteCreated.get()!!) {
                    if (validateInput()) createRouteApi()
                }

//when route changes not done
//                if (challengeModel.isEdit)
//                    getNavigator()?.navigateToLocationAdditionalDetailActivity(challengeModel.isEdit)
//                else {
//                    if (isRouteCreated.get()!!) {
//                        if (validateInput()) createRouteApi()
//                    }
//                }
            }
            R.id.ivAddRoute -> {
                getNavigator()?.navigateToCreateRouteActivity()
            }

        }
    }


    private fun getChallengeDetailsApi(challengeID: Int?) {
        if (isLoading.get()) return
        isLoading.set(true)

        compositeDisposable.add(
            dataManager.getChallengeDetails(challengeID ?: return)
                .subscribeOn(schedulerProvider.io()).observeOn(schedulerProvider.ui())
                .subscribe(this::onChallengeDetailsSuccess, this::onError)
        )
    }

    private fun onChallengeDetailsSuccess(response: ChallengeDetailResponse) {
        isLoading.set(false)
        Log.d("STEP 3:", "${response}")
        initModelOnApiResponse(response.list)
    }

    private fun initModelOnApiResponse(challengeDetails: ChallengeDetails) {
        isLoading.set(false)
        if (challengeDetails.routeDetail != null)
            initRouteData(challengeDetails.routeDetail)

//        val challengeStatus = if (challengeDetails.activeStatus) 1 else 0
        val cityID = arrayListOf<Int>()
        for (i in 0 until challengeDetails.city.size) cityID.add(challengeDetails.city[i].id.toInt())

        val zipCode = arrayListOf<Int>()
        if (challengeDetails.zipCode.isNotEmpty()) {
            val zip = challengeDetails.zipCode.split(",")
            for (element in zip) zipCode.add(element.toInt())
        }


        val inviteMember = arrayListOf<InviteMember>()
        for (i in 0 until challengeDetails.inviteUserId.size) {
            inviteMember.add(
                InviteMember(
                    challengeDetails.inviteUserId[i].toInt(),
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    true
                )
            )
        }

        val routeId =
            if (challengeDetails.routeDetail?.id != null) challengeDetails.routeDetail.id.toString() else ""
        val latitude =
            if (!challengeDetails.locationLatitude.equals("")) challengeDetails.locationLatitude?.toDouble() else 0.0
        val longitude =
            if (!challengeDetails.locationLongitude.equals("")) challengeDetails.locationLongitude?.toDouble() else 0.0
        val countryId =
            if (challengeDetails.country?.id != null) challengeDetails.country.id.toInt() else 0
        val stateId =
            if (challengeDetails.state?.id != null) challengeDetails.state.id.toInt() else 0


        if (challengeDetails.challengeArea.equals("Radius Reach") || challengeDetails.challengeArea.equals(
                ""
            )
        ) {
            challengeModel = ChallengeModel(
                true,
                challengeDetails.id.toString(),
                challengeDetails.step.toInt(),
                challengeDetails.sportTypeId.toInt(),
                "",
                challengeDetails.routeDetail?.profileType,
                challengeDetails.sportName,
                DateUtils.getChallengeDateFromTimeStamp(challengeDetails.startDate.toLong()),
                DateUtils.getChallengeDateFromTimeStamp(challengeDetails.endDate.toLong()),
                0,//for popular route
                routeId,
                challengeDetails.name,//challengeName
                challengeDetails.visibility,//challenge visibility
                challengeDetails.activeStatus,//challenge visibility
                challengeDetails.sportGradeLevel as ArrayList<String>,//challenge selectedSportsLevel
                challengeDetails.ageGroups as ArrayList<String>,//challenge selectedAgeGroupValue
                challengeDetails.description,//challenge description
                challengeDetails.subSportTypeId,
                challengeDetails.equipments as ArrayList<EquipmentData>,
                //Step-5 A (for radius reach)
                challengeDetails.challengeArea,//radius reach / Extended
                challengeDetails.location,
                latitude,
                longitude,
                challengeDetails.radius,
                //Step-5 B (Extended reach)
                "",
                0,
                "",
                0,
//            challengeDetails.country.name,
//            challengeDetails.country.id,
//            challengeDetails.state.name,
//            challengeDetails.state.id,
                cityID,
                zipCode,
                //Step-6
                challengeDetails.minimumCalories,
                challengeDetails.minimumMiles,
                challengeDetails.minimumElevationGain,
                challengeDetails.minimumAvgWatt,
                challengeDetails.minimumTime,
                challengeDetails.maxUserLimit,
                //Step 7
                challengeDetails.winnerPickedMethod,
                challengeDetails.prize ?: "NO",
                challengeDetails.overview ?: "NO",
                challengeDetails.combinedWinnerPrizeValue ?: " winner value not available",
                challengeDetails.additionalInformation,
                challengeDetails.rule,
                challengeDetails.guidelines,
                challengeDetails.qualificationCriteria,
                challengeDetails.featureImage,
                challengeDetails.icon,
                inviteMember
            )
        } else {
            challengeModel = ChallengeModel(
                true,
                challengeDetails.id.toString(),
                challengeDetails.step.toInt(),
                challengeDetails.sportTypeId.toInt(),
                "",
                challengeDetails.routeDetail?.profileType,
                challengeDetails.sportName,
                DateUtils.getChallengeDateFromTimeStamp(challengeDetails.startDate.toLong()),
                DateUtils.getChallengeDateFromTimeStamp(challengeDetails.endDate.toLong()),
                0,//for popular route
                routeId,
                challengeDetails.name,//challengeName
                challengeDetails.visibility,//challenge visibility
                challengeDetails.activeStatus,//challenge visibility
                challengeDetails.sportGradeLevel as ArrayList<String>,//challenge selectedSportsLevel
                challengeDetails.ageGroups as ArrayList<String>,//challenge selectedAgeGroupValue
                challengeDetails.description,//challenge description
                challengeDetails.subSportTypeId,
                challengeDetails.equipments as ArrayList<EquipmentData>,
                //Step-5 A (for radius reach)
                challengeDetails.challengeArea,//radius reach / Extended
                challengeDetails.location,
                latitude,
                longitude,
                challengeDetails.radius,
                //step-5 B (Extended reach)
                challengeDetails.country?.name ?: "",
                countryId,
                challengeDetails.state?.name ?: "",
                stateId,
                cityID,
                zipCode,
                //Step-6
                challengeDetails.minimumCalories,
                challengeDetails.minimumMiles,
                challengeDetails.minimumElevationGain,
                challengeDetails.minimumAvgWatt,
                challengeDetails.minimumTime,
                challengeDetails.maxUserLimit,
                //Step 7
                challengeDetails.winnerPickedMethod,
                challengeDetails.prize ?: "NO",
                challengeDetails.overview ?: "NO",
                "",
                challengeDetails.additionalInformation,
                challengeDetails.rule,
                challengeDetails.guidelines ?: "",
                challengeDetails.qualificationCriteria ?: "",
                challengeDetails.featureImage ?: "",
                challengeDetails.icon ?: "",
                inviteMember
            )
        }
    }

    private fun initRouteData(routeDetails: RouteDetail?) {
        if (routeDetails?.id != null) {
            routeID.set("${routeDetails.id ?: ""}")
            isRouteCreated.set(true)
        }
        routeName.set(routeDetails?.name)
        description.set(routeDetails?.description)
        coordinates.addAll(routeDetails?.cordinates ?: arrayListOf())
        if (!routeDetails?.distance.equals(""))
            distance.set(routeDetails?.distance?.toDouble())
        if (!routeDetails?.unit.equals(""))
            distanceUnit.set(routeDetails?.unit)
        if (!routeDetails?.elevationGain.equals(""))
            elevationGain.set(routeDetails?.elevationGain?.toDouble())
        if (!routeDetails?.elevationLoss.equals(""))
            elevationLoss.set(routeDetails?.elevationLoss?.toDouble())
        if (!routeDetails?.expectationTime.equals(""))
            expectedTime.set(routeDetails?.expectationTime)
        if (!routeDetails?.mapStyle.equals(""))
            mapStyle.set(routeDetails?.mapStyle)
        if (!routeDetails?.staticMap.equals(""))
            mapImage.set(routeDetails?.staticMap)
    }


    private fun onError(error: Throwable) {
        isLoading.set(false)
    }


    private fun validateInput(): Boolean {
        return when {
            routeName.get()?.isEmpty() == true -> {
                routeNameError.set(
                    dataManager.getResourceProvider().getString(R.string.title_empity)
                )
                false
            }

            description.get()?.isEmpty() == true -> {
                descriptionError.set(
                    dataManager.getResourceProvider().getString(R.string.descpiption_empty)
                )
                false
            }
            else -> true
        }

    }


    fun onTitleChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        routeNameError.set("")
    }

    fun onDescriptionChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        descriptionError.set("")
    }


    private fun createRouteApi() {
        isLoading.set(true)
        compositeDisposable.add(
            dataManager.createRoute(
                CreateRouteRequest(
                    routeID.get() ?: "",
                    coordinates,
                    description.get() ?: return,
                    distance.get()?.toInt() ?: return,
                    distanceUnit.get() ?: return,
                    elevationGain.get() ?: return,
                    elevationLoss.get() ?: return,
                    expectedTime.get().toString(),
                    mapStyle.get() ?: return,
                    routeName.get() ?: return,
                    challengeModel.profileType ?: return,
                    challengeModel.selectedSportId ?: return,
                    mapImage.get() ?: return
                )
            ).subscribeOn(schedulerProvider.io()).observeOn(schedulerProvider.ui())
                .subscribe(this::createRouteSuccess, this::failure)
        )
    }

    private fun createRouteSuccess(response: CreateRouteResponse) {
        isLoading.set(false)
        routeID.set(response.routeId.toString())
        getNavigator()?.navigateToLocationAdditionalDetailActivity(challengeModel.isEdit)
    }

    private fun failure(error: Throwable) {
        isLoading.set(false)
        getNavigator()?.onError(error, false)
    }

    fun saveAsDraft() {
//        if (!validate()) return
        if (isLoading.get()) return
        isLoading.set(true)

        compositeDisposable.add(
            dataManager.createChallenge(
                CreateChallengeRequest(
                    challengeModel.challengeID ?: "",
                    true,
                    challengeModel.selectedSportId,
                    "",
                    "",
                    arrayListOf(),
                    arrayListOf(),
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    0,
                    dataManager.getUser().countryId,//challengeModel.countryId?.toInt(),
                    dataManager.getUser().stateId,// challengeModel.stateId?.toInt(),
                    arrayListOf(),
                    arrayListOf(),
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
                    if (challengeModel.selectedWinnerPrize.equals(
                            "yes", ignoreCase = true
                        )
                    ) 1 else 0,
                    "",
                    "Goal",
                    DateUtils.convertCreateChallengeDatesToApiFormat(
                        challengeModel.startDate ?: return
                    ),
                    DateUtils.convertCreateChallengeDatesToApiFormat(
                        challengeModel.endDate ?: return
                    ),
                    "",
                    "",
                    "",
                    "",
                    challengeModel.challengeStatus,
                    arrayListOf(),
                    "3",
                    "",
                    "", ""
                )
            ).subscribeOn(schedulerProvider.io()).observeOn(schedulerProvider.ui())
                .subscribe(this::saveAsDraftSuccess, this::saveAsDraftFailure)
        )


    }

    private fun saveAsDraftWhenEdit() {
        if (isLoading.get()) return
        isLoading.set(true)

        compositeDisposable.add(
            dataManager.createChallenge(
                CreateChallengeRequest(
                    challengeModel.challengeID ?: "",
                    challengeModel.isEdit,
                    challengeModel.selectedSportId,
                    challengeModel.subSportTypeId,
                    challengeModel.challengeName,
                    challengeModel.selectedSportsLevel,
                    challengeModel.sportsEquipments,
                    challengeModel.challengeVisibility,
                    challengeModel.challengeDescription,
                    challengeModel.calories,
                    challengeModel.miles,
                    challengeModel.elevationGain,
                    challengeModel.avgWatt,
                    challengeModel.time,
                    challengeModel.maxMember?.toInt(),
                    if (challengeModel.countryId == 0) dataManager.getUser().countryId
                    else challengeModel.countryId,
                    if (challengeModel.stateId == 0) dataManager.getUser().stateId
                    else challengeModel.stateId,
                    challengeModel.cityId,
                    challengeModel.zipCode,
                    challengeModel.ageGroup,
                    arrayListOf<String>(),
//                    challengeModel.inviteMembers,
                    challengeModel.location,
                    challengeModel.latitude.toString(),
                    challengeModel.longitude.toString(),
                    challengeModel.overViewValue,
                    challengeModel.additionalInfoValue,
                    challengeModel.rulesValue,
                    challengeModel.qualificationValue,
                    "prizeType Static",
                    challengeModel.winnerValue,
                    if (challengeModel.selectedWinnerPrize.equals(
                            "yes", ignoreCase = true
                        )
                    ) 1 else 0,
                    challengeModel.winnerPickedFrom,
                    "Goal",
                    DateUtils.convertCreateChallengeDatesToApiFormat(
                        challengeModel.startDate ?: return
                    ),
                    DateUtils.convertCreateChallengeDatesToApiFormat(
                        challengeModel.endDate ?: return
                    ),
                    challengeModel.routeID.toString(),
                    "12",
                    challengeModel.challengeImage,
                    challengeModel.bannerImage ?: "",
                    challengeModel.challengeStatus,
                    arrayListOf(),
                    "10",
                    challengeModel.challengeArea ?: "",
                    challengeModel.radius ?: "", ""
                )
            ).subscribeOn(schedulerProvider.io()).observeOn(schedulerProvider.ui())
                .subscribe(this::saveAsDraftSuccess, this::saveAsDraftFailure)
        )


    }

    private fun saveAsDraftSuccess(response: CreateChallengeResponse) {
        isLoading.set(false)
        challengeModel.challengeID = response.id.toString()
        getNavigator()?.onSuccess("challenge saved")
    }

    private fun saveAsDraftFailure(error: Throwable) {
        isLoading.set(false)
    }

}