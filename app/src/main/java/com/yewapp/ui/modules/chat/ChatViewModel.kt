package com.yewapp.ui.modules.chat

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import com.pubnub.api.PubNub
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.yewapp.R
import com.yewapp.data.network.DataManager
import com.yewapp.data.network.api.associate.ActiveDeActiveAssociateAccountRequest
import com.yewapp.data.network.api.associate.Associate
import com.yewapp.data.network.api.associate.RemoveAssociateAccountRequest
import com.yewapp.data.network.api.chat.*
import com.yewapp.data.network.api.signup.Profile
import com.yewapp.ui.base.BaseViewModel
import com.yewapp.ui.modules.chat.extras.ChatActivityExtras
import com.yewapp.utils.createNameWhenNoImage
import com.yewapp.utils.fromJson
import com.yewapp.utils.popup.PopupAssociateOptions
import com.yewapp.utils.pubnub.History
import com.yewapp.utils.pubnub.serialize
import com.yewapp.utils.rx.SchedulerProvider
import com.yewapp.utils.toJson
import java.util.*

class ChatViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) :
    BaseViewModel<ChatNavigator>(dataManager, schedulerProvider) {


    //    private var currentSpace: PNSpace? = null
    private var toUpdateSpace: Boolean = false
    var shortName = ObservableField<String>("")
    var isImageAvailable = ObservableField<Boolean>(false)


    var chatHistoryList = mutableListOf<TempMessage>()
    var mutableChatHistory = MutableLiveData<List<TempMessage>>()
    val chatHistoryListLiveData: LiveData<List<TempMessage>> get() = mutableChatHistory

    val lastMessage = ObservableField<String>("")
    var associateProfileImage = ObservableField<String>("")
    var associateName = ObservableField<String>("")
    var associateStatus = ObservableField<String>("")
    var message = ObservableField<String>("")
    var channelID = ObservableField<String>("")
    var isSendBtnActive = ObservableField<Boolean>(false)
    var sendMessageVisibility = ObservableField<Boolean>(true)
    var isFavorite = ObservableField<Boolean>(false)

    lateinit var associateDetails: Associate


    /**
     * @author: Narbir Singh
     * @description:CASE: 1 In Bundle we initialize Associate object with parent
     * details when come from associate account (CHAT WITH PARENT when login Associate user)
     * CASE: 2 We initialize Associate object with Associate
     * details when come from associate listing chat icon (When User Account login)
     */
    override fun setData(extras: Bundle?) {
        if (extras?.containsKey(ChatActivityExtras.ASSOCIATE_DETAILS) ?: return) {
            associateDetails =
                extras.getParcelable<Associate>(ChatActivityExtras.ASSOCIATE_DETAILS) ?: return
            associateProfileImage.set(associateDetails.profileImage)
            associateName.set(associateDetails.name)
            if (associateDetails.status?.equals("1", ignoreCase = true) ?: return)
                associateStatus.set("Online")
            else {
                associateStatus.set("Offline")
                sendMessageVisibility.set(false)
            }
            /**
             * @description: Used to create channel id when we can check the login user id is
             * less than the associate
             * CASE1: ChatActivityExtras.IS_ASSOCIATE== true and default is true on EXTRAS class
             * SYNTAX : UserID <Associate  cg_userId_associateID_messages
             * SYNTAX : UserID >Associate  cg_associateID_userId_messages
             * CASE2: ChatActivityExtras.IS_ASSOCIATE== false
             * SYNTAX : UserID <Associate  cg_sp_userId_associateID_messages
             * SYNTAX : UserID >Associate  cg_sp_associateID_userId_messages
             *
             *
             * @author: Narbir Singh
             */

            if (extras.getBoolean(ChatActivityExtras.IS_ASSOCIATE)) {
                if ((dataManager.getUser().userId ?: return) < (associateDetails.userId
                        ?: return)
                ) {
                    channelID.set("cg_${dataManager.getUser().userId}_${associateDetails.userId}_messages")
                } else {
                    channelID.set("cg_${associateDetails.userId}_${dataManager.getUser().userId}_messages")
                }
            } else {
                if ((dataManager.getUser().userId ?: return) < (associateDetails.userId
                        ?: return)
                ) {
                    channelID.set("cg_sp_${dataManager.getUser().userId}_${associateDetails.userId}_messages")
                } else {
                    channelID.set("cg_sp_${associateDetails.userId}_${dataManager.getUser().userId}_messages")
                }

            }
        }

        if (!associateDetails.profileImage.isNullOrEmpty()) {
            isImageAvailable.set(true)
        } else {
            isImageAvailable.set(false)
            shortName.set(createNameWhenNoImage(associateDetails.name ?: "YW"))
        }

    }

    /**
     * @author: Narbir Singh
     * @description:This method is used to Handle clicks of this screen
     */
    fun onItemClick(view: View) {
        when (view.id) {
            R.id.ivHeart -> {
                isFavorite.set(!isFavorite.get()!!)
            }
            R.id.ivBack -> onBackPressed(view)
            R.id.ivOptions -> {
                PopupAssociateOptions.showPopUp(view, associateDetails, associateDetails.userId) {
                    when (it) {
                        "Migrate Account" -> getNavigator()?.onMigrateAccountClicked(
                            associateDetails
                        )
                        "Remove Account" -> removeAssociateAccount(
                            associateDetails.userId ?: return@showPopUp
                        )
                        "Edit Account" -> getNavigator()?.onEditClicked(associateDetails)
                        "Manage Permission" -> getNavigator()?.onManagePermissionClicked(
                            associateDetails
                        )
                        "Activate Account" -> {
                            val updatedStatus = if (associateDetails.status?.toInt() == 1) 0 else 1
                            activateDeactivateAssociateAccount(
                                associateDetails.userId?.toInt() ?: return@showPopUp, updatedStatus
                            )
                        }
                        "Deactivate Account" -> {
                            val updatedStatus = if (associateDetails.status?.toInt() == 1) 0 else 1
                            activateDeactivateAssociateAccount(
                                associateDetails.userId?.toInt() ?: return@showPopUp, updatedStatus
                            )
                        }
                    }
                }
            }
            R.id.ivSend -> {
                getNavigator()?.sendMessageToAssociate()
            }
        }
    }

    fun getMessageObject(timeToken: Long): JsonObject? {
        return ChatMessage.newBuilder()
            ?.setChannelId(channelID.get())
            ?.setMessageId(UUID.randomUUID().toString())
            ?.setIsSender(true)
            ?.timetoken(timeToken)
            ?.setSender(getSenderUser() ?: return null)
            ?.setMessage(message.get())
            ?.setUser(getSenderUser() ?: return null)
            ?.build()
    }


    fun getSenderUser(): Profile? {
        val user = dataManager.getUser() ?: return null
        val userId = dataManager.getUser().userId ?: return null
//        val id = if (
//            user.userDetails.userType == UserType.DOCTOR_ASST.ordinal
//            || user.userDetails.userType == UserType.MEDICAL_ASSOC_ASST.ordinal
//        ) {
//            user.userDetails.AssistantOwner?.userId ?: user.userDetails.userId
//        } else {
//            user.userDetails.userId
//        }

        return user.apply {
            user.userId = userId
        }
    }


    fun handleNewMessage(messageResult: PNMessageResult, pubNub: PubNub): MutableList<TempMessage> {
        if (messageResult.channel != channelID.get()) return mutableListOf()

        val data = serialize(messageResult)

        val message = data as TempMessage
        chatHistoryList.add(message)

        History.chainMessages(chatHistoryList, chatHistoryList.size)
        return chatHistoryList
    }


    /**
     * TextChangeListener for Message field set using Data Binding Adapter
     */
    fun onMessageChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        if (count == 0)
            isSendBtnActive.set(false)
        else
            isSendBtnActive.set(true)
    }


//    fun updateSpace(pubNub: PubNub, timeToken: Long, toString: String) {
//        if(currentSpace == null) {
//            toUpdateSpace = true
//            getDirectSpace(channelId, pubNub)
//        } else {
//            val customData = currentSpace?.custom
//            if (lastMessage.get().isNullOrEmpty()) return
//
//            val spaceMetaData = updateSpaceMetaData(customData.toString(), pubNub, timeToken, MessageType.TEXT.name)
//            currentSpace?.custom = spaceMetaData
//            pubNub.updateSpace()
//                .space(currentSpace)
//                .async(object : PNCallback<PNUpdateSpaceResult>() {
//                    override fun onResponse(result: PNUpdateSpaceResult?, status: PNStatus) {
//                        if (!status.isError){
//
//                        } else {
//                            Log.i("SpaceErrorUpdateSpace", status.errorData.throwable.toString())
//                        }
//                    }
//                })
//        }
//    }


    private fun updateSpaceMetaData(
        custom: String, pubNub: PubNub,
        timeToken: Long,
        type: String
    ): JsonObject? {
        val spaceMetaData = getSpaceMetaDataUnformatted(custom)
        val participantList = spaceMetaData?.participants
        val sender = dataManager.getUser() ?: return null

        participantList?.forEach {
            if (it.id == sender.userId) {
                it.lastReadMessageTimeToken = timeToken.toString()
            }
        }

        spaceMetaData?.participants = participantList ?: return null

        val message = lastMessage.get()


        val newLastMessage = LastMessage(
            message ?: "",
            type.lowercase(),
            timeToken
        )

        spaceMetaData.lastMessage = newLastMessage

        return JsonObject().apply {
            addProperty("data", spaceMetaData.toJson())
        }
    }


    private fun getSpaceMetaDataUnformatted(spaceData: String): SpaceMetaData? {
        val spaceData = spaceData.fromJson<SpaceData>() ?: return null
        val spaceMetaData = spaceData.data.fromJson<SpaceMetaData>() ?: return null
        Log.i("SpaceMetaString", spaceMetaData.toString())
        return spaceMetaData
    }


    private fun onError(t: Throwable) {
        isLoading.set(false)
        getNavigator()?.onError(t, false)
    }

    /**
     * @author: Narbir Singh
     * @description:This method is used to Remove associate account
     */
    private fun removeAssociateAccount(associateID: Int) {
        if (isLoading.get()) return
        isLoading.set(true)
        compositeDisposable.add(
            dataManager.removeAssociateAccount(
                RemoveAssociateAccountRequest(associateID.toString())
            ).subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(this::onRemoveAssociateSuccess, this::onError)
        )
    }

    private fun onRemoveAssociateSuccess(message: String) {
        isLoading.set(false)
        getNavigator()?.onBackPress()
    }

    /**
     * @author: Narbir Singh
     * @description:This method is used to activate/deactivate associate account
     */
    private fun activateDeactivateAssociateAccount(associateID: Int, updatedStatus: Int) {
        if (isLoading.get()) return
        isLoading.set(true)

        compositeDisposable.add(
            dataManager.activateDeactivateAssociateAccount(
                ActiveDeActiveAssociateAccountRequest(associateID, updatedStatus)
            ).subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(
                    this::onActivateDeactivateAssociateAccountAssociateSuccess,
                    this::onError
                )
        )
    }

    private fun onActivateDeactivateAssociateAccountAssociateSuccess(message: String) {
        isLoading.set(false)
        if (associateDetails.status?.toInt() == 1) {
            associateDetails.status = "0"
            sendMessageVisibility.set(false)
        } else {
            associateDetails.status = "1"
            sendMessageVisibility.set(true)
        }
    }
}