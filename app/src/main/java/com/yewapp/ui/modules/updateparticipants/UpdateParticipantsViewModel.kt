package com.yewapp.ui.modules.updateparticipants

import android.database.Observable
import android.os.Bundle
import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yewapp.R
import com.yewapp.data.network.DataManager
import com.yewapp.data.network.api.invitemember.InviteMember
import com.yewapp.data.network.api.invitemember.InviteMemberListResponse
import com.yewapp.ui.base.BaseViewModel
import com.yewapp.ui.modules.addchallenge.challengepreview.extras.UpdateParticipantsExtras
import com.yewapp.utils.rx.SchedulerProvider

class UpdateParticipantsViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) :
    BaseViewModel<UpdateParticipantsNavigator>(dataManager, schedulerProvider) {

    var isUpdateActive = ObservableField<Boolean>(false)
    var lastPage: Int = 1
    var currentPage: Int = 1
    var perPage: Int = 10

    val memberList = mutableListOf<InviteMember>()
    val _inviteMemberList = MutableLiveData<List<InviteMember>>()
    val inviteMemberList: LiveData<List<InviteMember>> get() = _inviteMemberList
    var noData = ObservableField<Boolean>(false)

//    val selectedMemberList = arrayListOf<InviteMember>()


    override fun setData(extras: Bundle?) {
        memberList.addAll(
            extras?.getParcelableArrayList<InviteMember>(
                UpdateParticipantsExtras.PARTICIPANTS_LIST
            ) ?: return
        )
        _inviteMemberList.value = memberList
    }


//    private fun getMemberList() {
//        if (isLoading.get()) return
//        isLoading.set(true)
//        compositeDisposable.add(
//            dataManager.getInviteMemberList().subscribeOn(schedulerProvider.io())
//                .observeOn(schedulerProvider.ui()).subscribe(this::onSuccess, this::onFailure)
//        )
//    }
//
//    private fun onSuccess(response: InviteMemberListResponse) {
//        isLoading.set(false)
//        if (response.list.isNotEmpty()) {
//
//            for(i in 0 until selectedMemberList.size){
//                if (response.list.contains(selectedMemberList[i]))
//                    memberList.add(response.list)
//
//            }
//            _inviteMemberList.value =memberList
//
//            noData.set(false)
//        } else {
//            noData.set(true)
//        }
//        currentPage = response.pageData?.current_page!!
//        perPage = response.pageData.per_page!!
//        lastPage = response.pageData.last_page!!
//    }
//
//    private fun onFailure(t: Throwable) {
//        isLoading.set(false)
//        getNavigator()?.onError(t)
//    }


    fun onActionClick(view: View) {
        when (view.id) {
            R.id.btnUpdate -> getNavigator()?.onUpdateClicked()
        }
    }
}