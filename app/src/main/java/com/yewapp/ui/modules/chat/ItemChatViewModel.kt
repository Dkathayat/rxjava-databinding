package com.yewapp.ui.modules.chat

import androidx.databinding.ObservableField
import com.yewapp.data.local.PreferenceKeys
import com.yewapp.data.local.PreferencesHelper
import com.yewapp.data.network.api.chat.TIMESTAMP_DIVIDER
import com.yewapp.data.network.api.chat.TempMessage
import com.yewapp.data.network.api.signup.Profile
import com.yewapp.utils.pubnub.Helper
import org.joda.time.DateTime

class ItemChatViewModel(
    val chatHistory: TempMessage,
    position: Int,
    val preferencesHelper: PreferencesHelper
) {


    val isSender = ObservableField<Boolean>(false)
    val isTimeStampVisible = ObservableField<Boolean>(false)
    val commonTimeStampVisible = ObservableField<Boolean>(false)
    val timeToken = ObservableField<String>("")

    init {
        setTimeToken()
        handleMessageViewType()
        isSender.set(
            chatHistory.senderID == preferencesHelper.getObject(
                PreferenceKeys.USER_DATA,
                Profile::class.java
            ).userId

//            chatHistory.senderID == preferencesHelper?.getObject(
//                PreferenceKeys.USER_DATA,
//                Profile::class.java
//            )?.userId
        )
    }

    private fun handleMessageViewType() {
//        when (viewType) {
//            ChatItem.TYPE_OWN_HEADER_FULL,
//            ChatItem.TYPE_REC_HEADER_FULL -> {
//                isAvatarVisible.set(true)
//                isTimeStampVisible.set(true)
//            }
//
//            ChatItem.TYPE_OWN_HEADER_SERIES,
//            ChatItem.TYPE_REC_HEADER_SERIES -> {
//                isAvatarVisible.set(true)
//                isTimeStampVisible.set(false)
//            }
//
//            ChatItem.TYPE_OWN_MIDDLE,
//            ChatItem.TYPE_REC_MIDDLE -> {
//                isAvatarVisible.set(false)
//                isTimeStampVisible.set(false)
//            }
//
//            ChatItem.TYPE_OWN_END,
//            ChatItem.TYPE_REC_END -> {
//                isAvatarVisible.set(false)
//                isTimeStampVisible.set(true)
//            }
//        }

    }


    private fun setTimeToken() {
        val pnTimeStamp = (chatHistory.timeToken)
        val mTimeStamp = pnTimeStamp?.div(TIMESTAMP_DIVIDER)
        val curTimeStamp = DateTime.now().millis
        val viewDate = Helper.parseDateTime(pnTimeStamp?:return, "hh:mm aa")
        val date = Helper.parseTime(mTimeStamp?:return)

        if (mTimeStamp in (curTimeStamp - 100000)..curTimeStamp)
            timeToken.set("now")
        else
            timeToken.set(viewDate)
    }

}