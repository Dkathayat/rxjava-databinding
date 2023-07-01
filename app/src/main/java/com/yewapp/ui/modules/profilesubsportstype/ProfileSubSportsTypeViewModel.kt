package com.yewapp.ui.modules.profilesubsportstype

import android.os.Bundle
import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yewapp.R
import com.yewapp.data.network.DataManager
import com.yewapp.data.network.api.addmodelequipment.EquipmentData
import com.yewapp.data.network.api.addmodelequipment.GetSavedModelEquipmentRequest
import com.yewapp.data.network.api.addmodelequipment.GetSavedModelEquipmentResponse
import com.yewapp.data.network.api.addmodelequipment.SubSport
import com.yewapp.data.network.api.profile.SportType
import com.yewapp.data.network.api.sports.Sport
import com.yewapp.ui.base.BaseViewModel
import com.yewapp.ui.modules.profilesubsportstype.extras.ProfileSubSportsTypeExtras
import com.yewapp.utils.rx.SchedulerProvider
import kotlinx.coroutines.*

class ProfileSubSportsTypeViewModel(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider
) :
    BaseViewModel<ProfileSubSportsTypeNavigator>(dataManager, schedulerProvider) {


    var subSportsTitle = ObservableField<String>("Sub Sports Type")
    var gradeLevel = ObservableField<String>("Beginner")
    var isContinueActive = ObservableField<Boolean>(false)

    //    var subSportTypeForSave = arrayListOf<Sport>()//used to get selected sub sports type
    var sportsHashMap = HashMap<View, Sport>()

    var subSportListForDropDown =
        mutableListOf<Sport>()//used only for dropdown selected sub sports
    var subSportList = mutableListOf<Sport>()
    var _subSportsList = MutableLiveData<List<Sport>>()
    val subSportListLive: LiveData<List<Sport>> get() = _subSportsList

    var isEdit = ObservableField<Boolean>(false)
    var associateID = ""
    var profileImage = ""
    var profileCoverImage = ""
    var sportID = ""
    lateinit var selectedSportType: SportType

    val editEquipmentData = arrayListOf<EquipmentData>()
    val editSubSportData = arrayListOf<SubSport>()

    override fun setData(extras: Bundle?) {
        associateID = extras?.getString(ProfileSubSportsTypeExtras.ASSOCIATE_ID) ?: ""

        profileImage = extras?.getString(ProfileSubSportsTypeExtras.PROFILE_IMAGE) ?: return
        profileCoverImage =
            extras.getString(ProfileSubSportsTypeExtras.PROFILE_COVER_IMAGE) ?: return

        selectedSportType =
            extras.getParcelable<SportType>(ProfileSubSportsTypeExtras.SPORTS_TYPE_DATA) ?: return
        subSportsTitle.set("${selectedSportType.name} Sub Sports Type")
        sportID =
            extras.getParcelable<SportType>(ProfileSubSportsTypeExtras.SPORTS_TYPE_DATA)?.sportId
                ?: return

        isEdit.set(extras.getBoolean(ProfileSubSportsTypeExtras.IS_EDIT))


        if (isEdit.get() ?: return) {
            isContinueActive.set(true)
            CoroutineScope(Dispatchers.IO).launch {
                //used to get all available subSports
                val sportTypeApi = async { getSubSportsList(sportID.toInt()) }
                print(sportTypeApi.await())
                //used to get saved sports
                delay(1000)
                val saveSportsApi = async { getSavedSportsEquipments() }
                print(saveSportsApi.await())
            }
        } else {
            //used to get all available subSports
            getSubSportsList(sportID.toInt())
        }
    }


    private fun getSubSportsList(selectedSportId: Int) {
        isLoading.set(true)
        compositeDisposable.add(
            dataManager.getSubSportsList(selectedSportId)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(this::success, this::failure)
        )
    }

    private fun success(response: List<Sport>) {
        isLoading.set(false)
        // clearLists()
        if (response.isNotEmpty()) {
            subSportList.addAll(response)
            subSportListForDropDown.addAll(response)
            _subSportsList.value = subSportList
            if (isEdit.get() == false)
                getNavigator()?.addSubSportLayout()
        }
    }

    private fun failure(error: Throwable) {
        isLoading.set(false)
//        getNavigator()?.onError(error, false)
    }


    private fun getSavedSportsEquipments() {
        isLoading.set(true)
        compositeDisposable.add(
            dataManager.getSavedSportsEquipments(
                GetSavedModelEquipmentRequest(
                    associateID,
                    sportID.toInt()
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
            editEquipmentData.addAll(response.equipmentData)
            for (i in 0 until response.subSport.size)
                getNavigator()?.addSubSportLayout()
        }

        // clearLists()
//        if (response.isNotEmpty()) {
//            subSportList.addAll(response)
//            subSportListForDropDown.addAll(response)
//            _subSportsList.value = subSportList
//            getNavigator()?.addSubSportLayout()
//        }
    }

    private fun getSavedSportsEquipmentsFailure(error: Throwable) {
        isLoading.set(false)
//        getNavigator()?.onError(error, false)
    }


    fun onActionClick(view: View) {
        when (view.id) {
            R.id.btnContinue -> {
                getNavigator()?.addModelInProfile()
            }

            R.id.add_icon -> {
                getNavigator()?.onAddIconClick()
            }
        }

    }
}