package com.yewapp.ui.modules.addmodelandequipments.fragments.addequipments

import android.os.Bundle
import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yewapp.R
import com.yewapp.data.network.DataManager
import com.yewapp.data.network.api.addmodelequipment.*
import com.yewapp.data.network.api.profile.SportType
import com.yewapp.data.network.api.sports.*
import com.yewapp.ui.base.BaseViewModel
import com.yewapp.ui.modules.addmodelandequipments.extras.AddModelEquipmentDetailsExtras
import com.yewapp.utils.rx.SchedulerProvider

class AddEquipmentsViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) :
    BaseViewModel<AddEquipmentsNavigator>(dataManager, schedulerProvider) {

    var addSubSportHashMap = HashMap<View, SubSport>()
    var addBrandMakeHashMap = HashMap<View, Brand>()
    var addModelHashMap = HashMap<View, Model>()
    var addTypeHashMap = HashMap<View, Type>()
//    var addFrameHashMap = HashMap<View, FrameSize>()
//    var addEquipmentType = HashMap<View, String>()
//    var addYearHashMap = HashMap<View, String>()
//    var addEquipmentTypeMap = HashMap<View, String>()
    var addOtherMap = HashMap<View, String>()


    var subSportList = mutableListOf<Sport>()
    var _subSportsList = MutableLiveData<List<Sport>>()
    val subSportListLive: LiveData<List<Sport>> get() = _subSportsList

    val editEquipmentData = arrayListOf<EquipmentData>()
    val editSubSportData = arrayListOf<SubSport>()


    var isUpdateDetailsActive = ObservableField<Boolean>(true)
    lateinit var selectedSportsType: SportType
    var associateID = ""
    var profileImage = ""
    var profileCoverImage = ""
    var isEdit = ObservableField<Boolean>(false)
    var requestSubSportData = arrayListOf<SubSport>()
    var requestEquipmentData = arrayListOf<EquipmentData>()

    override fun setData(extras: Bundle?) {
        isEdit.set(extras?.getBoolean(AddModelEquipmentDetailsExtras.IS_EDIT))
        associateID = extras?.getString(AddModelEquipmentDetailsExtras.ASSOCIATE_ID) ?: ""
        profileImage = extras?.getString(AddModelEquipmentDetailsExtras.PROFILE_IMAGE) ?: return
        profileCoverImage =
            extras.getString(AddModelEquipmentDetailsExtras.PROFILE_COVER_IMAGE) ?: return
        selectedSportsType =
            extras.getParcelable<SportType>(AddModelEquipmentDetailsExtras.SELECTED_SPORTS_TYPE)
                ?: return
        subSportList.addAll(
            extras.getParcelableArrayList<Sport>(
                AddModelEquipmentDetailsExtras.SELECTED_SUB_SPORTS
            ) ?: return
        )
        _subSportsList.value = subSportList

        if (isEdit.get()!!) {
            getSavedSportsEquipments()
        } else {
            getNavigator()?.addModelLayout()
        }
    }

    fun onActionClick(view: View) {
        when (view.id) {
            R.id.addModel -> {
                getNavigator()?.onAddIconClick()
            }
            R.id.btnContinue -> {
                getNavigator()?.filterDataRequestToSend()
            }
        }
    }

    private fun getSavedSportsEquipments() {
        isLoading.set(true)
        compositeDisposable.add(
            dataManager.getSavedSportsEquipments(
                GetSavedModelEquipmentRequest(
                    associateID,
                    selectedSportsType.sportId.toInt()
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
            editSubSportData.addAll(response.subSport)
            for (i in 0 until response.equipmentData.size) {
                if (response.equipmentData[i].type.equals("equipment", ignoreCase = true))
                    editEquipmentData.add(response.equipmentData[i])
            }
            for (i in 0 until editEquipmentData.size)
                getNavigator()?.addModelLayout()
        }
    }

    private fun getSavedSportsEquipmentsFailure(error: Throwable) {
        isLoading.set(false)
    }


    fun addModelEquipments() {
        if (isLoading.get()) return
        isLoading.set(true)
        compositeDisposable.add(
            dataManager.saveUserSportsEquipments(
                AddModelEquipmentRequest(
                    associateID,
                    profileImage,
                    profileCoverImage,
                    requestSubSportData,
                    requestEquipmentData
                )
            ).subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(
                    this::saveUserSportsEquipmentsSuccess,
                    this::saveUserSportsEquipmentsFailure
                )
        )
    }

    private fun saveUserSportsEquipmentsSuccess(response: List<AddModelEquipmentResponse>) {
        getNavigator()?.onSuccess("Model added successfully")
        getNavigator()?.reDirectToProfile()
    }

    private fun saveUserSportsEquipmentsFailure(error: Throwable) {
        getNavigator()?.onError(error)
    }
}