package com.yewapp.ui.modules.refer.vm

import android.os.Bundle
import android.util.Log
import android.util.SparseBooleanArray
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.yewapp.data.network.DataManager
import com.yewapp.data.network.api.refer.PhoneContacts
import com.yewapp.ui.base.BaseViewModel
import com.yewapp.ui.modules.refer.navigator.ChooseContactsNavigator
import com.yewapp.utils.FetchContactsUtil
import com.yewapp.utils.rx.SchedulerProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ChooseContactsViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) :
    BaseViewModel<ChooseContactsNavigator>(dataManager, schedulerProvider) {

    var checkList = mutableListOf<PhoneContacts>()
    val selectedItems = SparseBooleanArray()

    val addedContactList = mutableListOf<PhoneContacts>()
    val mutableContactList = mutableListOf<PhoneContacts>()
    private val mutableContactLiveData = MutableLiveData<List<PhoneContacts>>()
    val contactList: LiveData<List<PhoneContacts>> get() = mutableContactLiveData

//    val mutableContactList = mutableListOf<PhoneContacts>()
//    private val mutableContactLiveData = MutableLiveData<List<PhoneContacts>>()
//    val contactList: LiveData<List<PhoneContacts>      nbmhg> get() = mutableContactLiveData

    val formattedContactList = mutableListOf<PhoneContacts>()


    fun onSendRequestClick(view: View) {
        if (isLoading.get()) return
        isLoading.set(true)
        compositeDisposable.add(
            dataManager.sendRefer(
                checkList
            ).subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(this::onSuccess, this::onError)
        )

    }

    fun onSuccess(response: String) {
        isLoading.set(false)
        getNavigator()?.onSuccess(response)
    }

    fun onError(t: Throwable) {
        isLoading.set(false)
        getNavigator()?.onError(t, false)
    }

    override fun setData(extras: Bundle?) {

    }


    fun fetchContacts() {
        isLoading.set(true)
        viewModelScope.launch {
            val contactsListAsync = async { FetchContactsUtil.getPhoneContacts() }
            val contactNumbersAsync = async { FetchContactsUtil.getContactNumbers() }

            val contacts = contactsListAsync.await()
            val contactNumbers = contactNumbersAsync.await()

            contacts.forEachIndexed { index, it ->
                contactNumbers[it.id]?.let { numbers ->
                    it.phone = numbers[0]
                    for (contact in addedContactList) {
                        if (contact.phone?.contains(it.phone ?: return@launch) ?: return@launch) {
                            selectedItems.put(index, true)
                            it.isSelected = true
                        }
                    }
                }
            }
            mutableContactLiveData.value = (contacts.sortedBy { it.name }).toMutableList()
            formattedContactList.addAll((contacts.sortedBy { it.name }).toMutableList())
            Log.i("fetchContacts: ", contacts.toString().toString())
            launch(Dispatchers.Main) {
                isLoading.set(false)
            }
        }
    }


}