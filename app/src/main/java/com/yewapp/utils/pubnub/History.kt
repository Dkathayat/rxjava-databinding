package com.yewapp.utils.pubnub

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.JsonSyntaxException
import com.pubnub.api.PubNub
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.yewapp.data.network.api.chat.ChatMessage
import com.yewapp.data.network.api.chat.TempMessage
import com.yewapp.utils.toJson

class History {

    data class NEWData(
        val message: Int,
        val channelID: String)

    abstract class CallbackSkeleton {
        abstract fun handleResponse(messages: MutableList<TempMessage>)
    }


    companion object {
        const val TOP_ITEM_OFFSET = 3

        private val messageHelper = MessageHelper()


        fun getAllMessages(
            channelID: String?,
            start: Long?,
            pubNub: PubNub,
            callback: CallbackSkeleton
        ) {
            pubNub.history(
                channelID ?: return,
                includeTimetoken = true,
//                includeMeta = true
            )
                .async { result, status ->
                    val result = result ?: return@async
                    Thread(Runnable {
                        if (!status.error && result.messages.isNotEmpty()) {
                            val messages: MutableList<TempMessage> =
                                ArrayList()
                            for (message in result.messages) {
                                val msg: TempMessage? = serialize(message.entry)
                                msg ?: return@Runnable
                                messages.add(msg)
                            }
                            callback.handleResponse(messages)
                        } else {
                            callback.handleResponse(ArrayList())
                        }
                    }).start()

                }
//                }
        }


        fun chainMessages(list: List<TempMessage?>, count: Int) {
            var limit = count
            if (limit > list.size) {
                limit = list.size
            }
            for (i in 0 until limit) {
                val message = list[i]
                if (i > 0) {
//                    messageHelper.chain(message, list[i - 1])
                }
            }
        }

    }
}


fun serialize(message: JsonElement): TempMessage? {
    var chatMessageItem: TempMessage? = null
    println("TEST :: \t ${message}")

    try {
        chatMessageItem = Gson().fromJson(
            message,
            TempMessage::class.java
        )
    } catch (exception: JsonSyntaxException) {
        return chatMessageItem
    }
    println("TEST2 :: \t ${chatMessageItem.toJson()}")

//    message.timetoken = pnHistoryItemResult.timetoken?:return null
//    chatMessageItem.initializeCustomProperties()
    return chatMessageItem
}

fun serialize(pnMessageResult: PNMessageResult): Any? {
    var message: TempMessage? = null
    message = Gson().fromJson(
        pnMessageResult.message,
        TempMessage::class.java
    )

//    message.timetoken = pnMessageResult.timetoken!!
//    message.initializeCustomProperties()
    return message
}