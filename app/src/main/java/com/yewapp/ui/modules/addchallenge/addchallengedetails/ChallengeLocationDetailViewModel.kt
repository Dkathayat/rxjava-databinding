package com.yewapp.ui.modules.addchallenge.addchallengedetails

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CompoundButton
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yewapp.R
import com.yewapp.data.network.DataManager
import com.yewapp.data.network.api.addmodelequipment.EquipmentData
import com.yewapp.data.network.api.addmodelequipment.GetSavedModelEquipmentRequest
import com.yewapp.data.network.api.addmodelequipment.GetSavedModelEquipmentResponse
import com.yewapp.data.network.api.challenges.ChallengeModel
import com.yewapp.data.network.api.challenges.CreateChallengeResponse
import com.yewapp.data.network.api.createchallenge.CreateChallengeRequest
import com.yewapp.data.network.api.createchallenge.StaticMultipleSelection
import com.yewapp.data.network.api.emailchange.ChallengeNameRequest
import com.yewapp.data.network.api.emailchange.ChallengeNameResponse
import com.yewapp.data.network.api.sports.*
import com.yewapp.ui.base.BaseViewModel
import com.yewapp.ui.modules.listner.ChallengeExtras
import com.yewapp.utils.DateUtils
import com.yewapp.utils.rx.SchedulerProvider

class ChallengeLocationDetailViewModel(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider
) :
    BaseViewModel<ChallengeLocationDetailNavigator>(dataManager, schedulerProvider) {


    var subSportsVisibility = ObservableField<Boolean>(false)
    var isContinueActive = ObservableField<Boolean>(false)
    var isEquipmentAvailable = ObservableField<Boolean>(true)

    var challengeName = ObservableField<String>("")
    var description = ObservableField<String>("")
    val nameError = ObservableField<String>("")
    val descriptionError = ObservableField<String>("")
    val challengeVisibilityError = ObservableField<String>("")
    val sportLevelError = ObservableField<String>("")
    val ageGroupError = ObservableField<String>("")
    val ageGroup = ObservableField<String>("")
    val statusError = ObservableField<String>("")

    var challengeVisibilityArray = arrayOf("Public", "Private")
    var statusVisibilityArray = arrayOf("Active", "InActive")

    //    var sportLevelArray = arrayOf("Beginner", "Advance", "Pro")
//    var ageGroupArray = arrayOf("All", "0-10", "11-20", "21-30", "31-40", "41-50", ">50")
    var selectedChallengeVisibility = ObservableField<String>("")

    var selectedSportsLevel = arrayListOf<String>()
    var selectedSportsLevelDisplay = ObservableField<String>("")

    //    var selectedAgeGroupValue = ObservableField<String>("")
    var selectedAgeGroupValue = arrayListOf<String>()
    var challengeStatus = ObservableField<String>("InActive")

    var selectedSport = BooleanArray(3) { false }
    var sportList: ArrayList<Int> = ArrayList()
    var selectedAgeGroup = BooleanArray(7) { false }
    var subSportName = ObservableField<String>("")
    var subSportID = ObservableField<String>("")
    val subSportError = ObservableField<String>("")

    //  var ageGroupList: ArrayList<Int> = ArrayList()
    var subSportList = mutableListOf<Sport>()
    var _subSportsList = MutableLiveData<List<Sport>>()
    val subSportListLive: LiveData<List<Sport>> get() = _subSportsList

    var selectedSubSport: Sport? = null

    var selectedSubSportId = ObservableField<Int>(0)
    var modelValue = ObservableField<String>("")

    var modelList = mutableListOf<Model>()
    var _modelList = MutableLiveData<List<Model>>()
    val modelListLive: LiveData<List<Model>> get() = _modelList
    val isSubSportLoading = ObservableBoolean(false)
    val isModelLoading = ObservableBoolean(false)
    var challengeExtras: ChallengeExtras? = null

    var sportsLevelList = mutableListOf<StaticMultipleSelection>()
    private var _sportsLevelList = MutableLiveData<List<StaticMultipleSelection>>()
    val sportsLevelListLive: LiveData<List<StaticMultipleSelection>> get() = _sportsLevelList

    var sportsMainEquipmentList = mutableListOf<EquipmentData>()
    var sportsEquipmentList = mutableListOf<EquipmentData>()
    var _sportsEquipmentList = MutableLiveData<List<EquipmentData>>()
    val sportsEquipmentListLive: LiveData<List<EquipmentData>> get() = _sportsEquipmentList

    var ageGroupList = mutableListOf<StaticMultipleSelection>()
    lateinit var challengeModel: ChallengeModel


    override fun setData(extras: Bundle?) {
        challengeModel =
            extras?.getParcelable(ChallengeExtras.CHALLENGE_DATA) ?: return

        Log.d("STEP 4:", "${challengeModel}")


        getSubSportsList(
            challengeModel.selectedSportId ?: return
        )//pass selected sports od
        getSavedSportsEquipments(challengeModel.selectedSportId ?: return)


//        CoroutineScope(Dispatchers.Main).launch {
//            val subSportsListAsync = async {
//                getSubSportsList(
//                    challengeModel.selectedSportId ?: return@async
//                )//pass selected sports od
//            }
//            val equipmentAsync = async {
//                getSavedSportsEquipments(challengeModel.selectedSportId ?: return@async)
//            }
//            subSportsListAsync.await()
//            equipmentAsync.await()
//            if (challengeModel.isEdit) {
//                initlizeDataForEdit()
//            }
//
//        }
    }


    init {
        //initialize default Grade level group
        sportsLevelList.add(StaticMultipleSelection("Beginner", false))
        sportsLevelList.add(StaticMultipleSelection("Advance", false))
        sportsLevelList.add(StaticMultipleSelection("Pro", false))

        //initialize default age group
        ageGroupList.add(StaticMultipleSelection("All", false))
        ageGroupList.add(StaticMultipleSelection("0-10", false))
        ageGroupList.add(StaticMultipleSelection("11-20", false))
        ageGroupList.add(StaticMultipleSelection("21-30", false))
        ageGroupList.add(StaticMultipleSelection("41-50", false))
        ageGroupList.add(StaticMultipleSelection(">50", false))

    }

    private fun getSubSportsList(selectedSportId: Int) {
        isSubSportLoading.set(true)
        compositeDisposable.add(
            dataManager.getSubSportsList(selectedSportId)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(this::success, this::failure)
        )
    }

    private fun success(response: List<Sport>) {
        isSubSportLoading.set(false)
        // clearLists()
        if (response.isNotEmpty()) {
            subSportList.addAll(response)
            _subSportsList.value = subSportList
        }
    }

    private fun failure(error: Throwable) {
        isSubSportLoading.set(false)
        getNavigator()?.onError(error, false)
    }


    fun getSportModelList() {
        isModelLoading.set(true)
        compositeDisposable.add(
            dataManager.getSubSportsModelList(selectedSubSportId.get()!!)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(this::modelSuccess, this::modelFailure)
        )
    }

    private fun modelFailure(error: Throwable) {
        isModelLoading.set(false)
        getNavigator()?.onError(error, false)
    }

    private fun modelSuccess(response: List<Model>) {
        isModelLoading.set(false)
        // clearLists()
        if (response.isNotEmpty()) {
            modelList.addAll(response)
            _modelList.value = modelList

        }
    }


    fun onCheckedChange(button: CompoundButton?, check: Boolean) {
        if (check) {
            subSportsVisibility.set(true)
        } else {
            subSportsVisibility.set(false)
        }
    }


    fun validate(): Boolean {
        return when {
            challengeName.get().toString().trim().isEmpty() -> {
                nameError.set(
                    dataManager.getResourceProvider().getString(R.string.empty_name_error)
                )
                false
            }
            challengeName.get().toString().trim().length < 4 -> {
                nameError.set(
                    dataManager.getResourceProvider().getString(R.string.challenge_name_length)
                )
                false
            }
            selectedChallengeVisibility.get()?.isEmpty() == true -> {
                challengeVisibilityError.set(
                    dataManager.getResourceProvider()
                        .getString(R.string.empty_challengevisibility_error)
                )
                false
            }
//            selectedSportsLevel.get()?.isEmpty() == true -> {OLD
            selectedSportsLevel.isEmpty() -> {
                sportLevelError.set(
                    dataManager.getResourceProvider().getString(R.string.empty_sportlevel_error)
                )
                false
            }
            selectedAgeGroupValue.isEmpty() == true -> {
                ageGroupError.set(
                    dataManager.getResourceProvider().getString(R.string.empty_age_group_error)
                )
                false
            }
            challengeStatus.get()?.isEmpty() == true -> {
                statusError.set(
                    dataManager.getResourceProvider().getString(R.string.empty_status_error)
                )
                false
            }
            description.get().toString().trim().isEmpty() -> {
                descriptionError.set(
                    dataManager.getResourceProvider().getString(R.string.empty_description_error)
                )
                false
            }
            subSportName.get().toString().trim().isEmpty() -> {
                descriptionError.set(
                    dataManager.getResourceProvider().getString(R.string.empty_description_error)
                )
                false
            }

            else -> true
        }
    }

    fun onChallengeName(s: CharSequence, start: Int, before: Int, count: Int) {
        nameError.set("")
        if (validate()) {
            isContinueActive.set(true)
        }
    }

    fun onDescription(s: CharSequence, start: Int, before: Int, count: Int) {
        descriptionError.set("")
        if (validate()) {
            isContinueActive.set(true)
        }
    }

    fun onChallengeVisibility(s: CharSequence, start: Int, before: Int, count: Int) {
        challengeVisibilityError.set("")
        if (validate()) {
            isContinueActive.set(true)
        }
    }

    fun onSportLevel(s: CharSequence, start: Int, before: Int, count: Int) {
        sportLevelError.set("")
        if (validate()) {
            isContinueActive.set(true)
        }
    }

    fun onSubSports(s: CharSequence, start: Int, before: Int, count: Int) {
        subSportError.set("")
        if (validate()) {
            isContinueActive.set(true)
        }
    }

    fun onAgeGroup(s: CharSequence, start: Int, before: Int, count: Int) {
        ageGroupError.set("")
        if (validate()) {
            isContinueActive.set(true)
        }
    }

    fun onStatusChange(s: CharSequence, start: Int, before: Int, count: Int) {
        statusError.set("")
        if (validate()) {
            isContinueActive.set(true)
        }
    }

    fun onActionClick(view: View) {
        when (view.id) {
            R.id.btn_continue -> if (validate())
                validateChallengeNameAvailable()
            R.id.challenge_visibility_edt -> getNavigator()?.onChallengeVisibilityClick(view)
            R.id.sports_level_edt -> getNavigator()?.onSportsLevelClick(view)
            R.id.age_group_edt -> getNavigator()?.onAgeGroupClick(view)
//            R.id.sub_sport_edt -> getNavigator()?.onSubSportClick(view)
//            R.id.model_edt -> getNavigator()?.onModelClick(view)
            R.id.tvSaveAsDraft -> saveAsDraft()
//            R.id.add_icon -> getNavigator()?.onAddIconClick()
            R.id.status_edt -> getNavigator()?.onStatusClick(view)
            R.id.subSports -> getNavigator()?.onSubSportClick(view)
        }
    }

    private fun validateChallengeNameAvailable() {
        if (isLoading.get()) return
        isLoading.set(true)
        compositeDisposable.add(
            dataManager.checkChallengeNameAvailable(
                ChallengeNameRequest(
                    challengeModel.challengeID ?: "",
                    challengeName.get() ?: return
                )
            )
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(
                    this::challengeNameSuccess,
                    this::failure
                )
        )
    }

    private fun challengeNameSuccess(response: ChallengeNameResponse) {
        isLoading.set(false)
        if (!response.isChallengeNameAvailable) {
            getNavigator()?.navigateToAdditionalScreen()
        } else {
            getNavigator()?.onError(Throwable("Challenge name already exists!"))
        }
    }


    fun getSavedSportsEquipments(selectedSportId: Int) {
        if (isLoading.get()) return
        isLoading.set(true)
        compositeDisposable.add(
            dataManager.getSavedSportsEquipments(
                GetSavedModelEquipmentRequest(
                    "",
                    selectedSportId
                )
            )
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(
                    this::getSavedSportsEquipmentsSuccess,
                    this::getSavedSportsEquipmentsFailure
                )
        )
    }

    private fun getSavedSportsEquipmentsSuccess(response: GetSavedModelEquipmentResponse) {
        isLoading.set(false)
        if (response.equipmentData.isNotEmpty()) {
            for (i in 0 until response.equipmentData.size) {
                if (response.equipmentData[i].type.equals("equipment", ignoreCase = true))
                    sportsMainEquipmentList.add(response.equipmentData[i])
            }
        }
        if (challengeModel.isEdit) {
            initlizeDataForEdit()
        }
    }

    private fun getSavedSportsEquipmentsFailure(error: Throwable) {
        isLoading.set(false)
    }

    fun filterEquipmentBySubSports(subSportID: Int) {
        sportsEquipmentList.clear()
        isEquipmentAvailable.set(false)
        for (i in 0 until sportsMainEquipmentList.size) {
            if (sportsMainEquipmentList[i].subSportId.toInt() == subSportID) {
                sportsEquipmentList.add(sportsMainEquipmentList[i])
                isEquipmentAvailable.set(true)
            }
        }
        _sportsEquipmentList.value = sportsEquipmentList

    }

    fun saveAsDraft() {
//        if (!validate()) return
        if (isLoading.get()) return
        isLoading.set(true)

        val selectedSportsEquipments = arrayListOf<EquipmentData>()
        for (i in 0 until sportsEquipmentList.size) {
            if (sportsEquipmentList[i].isSelected)
                selectedSportsEquipments.add(sportsEquipmentList[i])
        }


        compositeDisposable.add(
            dataManager.createChallenge(
                CreateChallengeRequest(
                    challengeModel.challengeID ?: "",
                    true,
                    challengeModel.selectedSportId,
                    selectedSubSport?.id ?: "",
                    challengeName.get() ?: "",
                    selectedSportsLevel,
                    selectedSportsEquipments,
                    selectedChallengeVisibility.get(),
                    description.get() ?: "",
                    "",
                    challengeModel.miles ?: "",
                    challengeModel.elevationGain ?: "",
                    "",
                    challengeModel.time ?: "",
                    0,
                    dataManager.getUser().countryId,//challengeModel.countryId?.toInt(),
                    dataManager.getUser().stateId,// challengeModel.stateId?.toInt(),
                    arrayListOf(),
                    arrayListOf(),
                    selectedAgeGroupValue,
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
                    challengeStatus.get(),
                    arrayListOf(),
                    "4",
                    "",
                    "", ""
                )
            ).subscribeOn(schedulerProvider.io()).observeOn(schedulerProvider.ui())
                .subscribe(this::saveAsDraftSuccess, this::saveAsDraftFailure)
        )


    }

    private fun saveAsDraftSuccess(response: CreateChallengeResponse) {
        isLoading.set(false)
        if (response.id != 0)
            challengeModel.challengeID = response.id.toString()
        getNavigator()?.onSuccess("challenge saved")
    }

    private fun saveAsDraftFailure(error: Throwable) {
        isLoading.set(false)
    }


    private fun initlizeDataForEdit() {
        subSportID.set(challengeModel.subSportTypeId)
        challengeName.set(challengeModel.challengeName)
        selectedChallengeVisibility.set(challengeModel.challengeVisibility)

        //add selected grade level data
        selectedSportsLevelDisplay.set("Sports Level")
        selectedSportsLevel.addAll(challengeModel.selectedSportsLevel)
        for (i in 0 until sportsLevelList.size) {
            for (j in 0 until challengeModel.selectedSportsLevel.size) {
                if (sportsLevelList[i].name?.equals(
                        challengeModel.selectedSportsLevel[j],
                        ignoreCase = true
                    ) ?: return
                ) {
                    sportsLevelList[i].status = true
                    getNavigator()?.addSportsLevelChips(sportsLevelList[i].name ?: return)
                }

            }
        }
        ageGroup.set("Age Group")
        selectedAgeGroupValue.addAll(challengeModel.ageGroup)
        //add Age Group data
        var count = 0
        for (i in 0 until ageGroupList.size) {
            for (j in 0 until challengeModel.ageGroup.size) {
                if (challengeModel.ageGroup[j].equals("51-100"))
                    challengeModel.ageGroup[j] = ">51"

                if (ageGroupList[i].name?.equals(
                        challengeModel.ageGroup[j],
                        ignoreCase = true
                    ) ?: return
                ) {
                    count++
                    ageGroupList[i].status = true
                    getNavigator()?.addChips(ageGroupList[i].name ?: return)
                }

            }
        }
        if (challengeModel.ageGroup.size == count) {
            for (i in 0 until ageGroupList.size) {
                ageGroupList[i].status = true
            }
        }

        //Challenge Status
        challengeStatus.set(challengeModel.challengeStatus)

        description.set(challengeModel.challengeDescription)


        for (i in 0 until subSportList.size) {
            if (subSportList[i].id.equals(challengeModel.subSportTypeId)) {
                selectedSubSport = subSportList[i]
                subSportName.set(subSportList[i].name)
                selectedSubSportId.set(subSportList[i].id?.toInt())
                filterEquipmentBySubSports(subSportList[i].id?.toInt() ?: return)
            }
        }

        var equipmentDataCount = 0
        for (i in 0 until sportsMainEquipmentList.size) {
            for (j in 0 until challengeModel.sportsEquipments.size) {
                challengeModel.sportsEquipments[j].isSelected = false
                if (sportsMainEquipmentList[i] == challengeModel.sportsEquipments[j]) {
                    sportsMainEquipmentList[i].isSelected = true
                    equipmentDataCount++
                }
            }
        }
        if (equipmentDataCount > 0)
            subSportsVisibility.set(true)
        _sportsEquipmentList.value = sportsEquipmentList


    }
}