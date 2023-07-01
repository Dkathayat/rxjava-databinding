package com.yewapp.ui.modules.chat

import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.pubnub.api.PubNub
import com.pubnub.api.callbacks.SubscribeCallback
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult
import com.yewapp.R
import com.yewapp.data.network.api.associate.Associate
import com.yewapp.data.network.api.chat.TempMessage
import com.yewapp.databinding.ActivityChatBinding
import com.yewapp.ui.base.BaseActivity
import com.yewapp.ui.modules.addassociatememberdetails.AddMembersDetailsActivity
import com.yewapp.ui.modules.addassociatepermission.AddAssociatePermissionActivity
import com.yewapp.ui.modules.addassociatepermission.AddAssociatePermissionExtras
import com.yewapp.ui.modules.migrateassociate.MigrateAssociateActivity
import com.yewapp.utils.pubnub.History
import org.joda.time.DateTime
import javax.inject.Inject


/**
 * @author: Narbir Singh
 * @description:
 *
 *          This screen is used to chat with one to one user
 *          A> Chat with Associate
 *          B> Chat with Associate Parent
 *          C> Chat with Spectator
 *
 *          For All these chat we  will git all object parse into Associate model for
 *          all one to one chat process
 */

class ChatActivity : BaseActivity<ChatNavigator, ChatViewModel, ActivityChatBinding>(),
    ChatNavigator {
    override fun getLayout(): Int = R.layout.activity_chat


    private lateinit var chatHistoryAdapter: ChatHistoryAdapter

    @Inject
    lateinit var pubNub: PubNub


    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            val firstCompletelyVisibleItemPosition =
                (recyclerView.layoutManager as LinearLayoutManager?)!!.findFirstCompletelyVisibleItemPosition()

            if (firstCompletelyVisibleItemPosition == History.TOP_ITEM_OFFSET && dy < 0) {
                fetchHistory()

            }
        }
    }

    private val pubNubListener = object : SubscribeCallback() {
        override fun status(pubnub: PubNub, pnStatus: PNStatus) {
            println("Status category: ${pnStatus.category}")
            // PNConnectedCategory, PNReconnectedCategory, PNDisconnectedCategory

            println("Status operation: ${pnStatus.operation}")
            // PNSubscribeOperation, PNHeartbeatOperation

            println("Status error: ${pnStatus.error}")
            // true or false
        }

        override fun presence(pubnub: PubNub, pnPresenceEventResult: PNPresenceEventResult) {
            println("Presence event: ${pnPresenceEventResult.event}")
            println("Presence channel: ${pnPresenceEventResult.channel}")
            println("Presence uuid: ${pnPresenceEventResult.uuid}")
            println("Presence timetoken: ${pnPresenceEventResult.timetoken}")
            println("Presence occupancy: ${pnPresenceEventResult.occupancy}")
        }

        override fun message(pubnub: PubNub, pnMessageResult: PNMessageResult) {
            println("Message payload: ${pnMessageResult.message}")
            println("Message channel: ${pnMessageResult.channel}")
            println("Message publisher: ${pnMessageResult.publisher}")
            println("Message timetoken: ${pnMessageResult.timetoken}")


            // Handle new message stored in message.message
            // Handle new message stored in message.message
            if (pnMessageResult.channel != null) {
                // Message has been received on channel group stored in
                // message.getChannel().
            } else {
                // Message has been received on channel stored in
                // message.getSubscription().
            }
            val receivedMessageObject: JsonElement = pnMessageResult.message
            println("Received message: $receivedMessageObject")
            // Extract desired parts of the payload, using Gson.
            // Extract desired parts of the payload, using Gson.
            //Receive user remote message
            runOnUiThread {
                val messages = viewModel.handleNewMessage(pnMessageResult, pubnub)
                if (messages.size == 0) return@runOnUiThread
                runOnUiThread {
                    chatHistoryAdapter.update(messages)
                    scrollChatToBottom()
                }
            }
        }
    }


    override fun init() {
        bind(ChatViewModel::class.java)
        pubNub.addListener(pubNubListener)
    }

    override fun onViewModelCreated(viewModel: ChatViewModel) {
        viewModel.setNavigator(this)
    }

    override fun onViewBound(viewDataBinding: ActivityChatBinding) {
        initializeAdapter()

        //Subscribe Channel
        pubNub.subscribe(channels = listOf(viewModel.channelID.get() ?: return))
        fetchHistory()
    }


    /**
     * @author: Narbir Singh
     * @description:This method is used to fetch history messages from pubnub
     */
    private fun fetchHistory() {
        History.getAllMessages(
            channelID = viewModel.channelID.get(),
            getEarliestTimestamp(),
            pubNub = pubNub,
            callback = object : History.CallbackSkeleton() {
                override fun handleResponse(messages: MutableList<TempMessage>) {
                    if (messages.isNotEmpty()) {
                        viewModel.chatHistoryList.addAll(0, messages)

                        History.chainMessages(
                            viewModel.chatHistoryList,
                            viewModel.chatHistoryList.size
                        )
                        runOnUiThread {
                            viewModel.mutableChatHistory.value = viewModel.chatHistoryList
                            scrollChatToBottom()
                        }
                    }
                }
            })
    }

    private fun initializeAdapter() {
        chatHistoryAdapter =
            ChatHistoryAdapter(mutableListOf(), viewModel.dataManager.getPreference())
        chatHistoryAdapter.setHasStableIds(true)
        viewDataBinding.chatRecycler.adapter = chatHistoryAdapter
        val layoutManager = LinearLayoutManager(this)
        layoutManager.stackFromEnd = true
        viewDataBinding.chatRecycler.layoutManager = layoutManager
        viewDataBinding.chatRecycler.addOnScrollListener(scrollListener)
        addObserver()
    }

    private fun addObserver() {
        viewModel.chatHistoryListLiveData.observe(this, Observer {
            chatHistoryAdapter.addItems(it)
        })
    }


    override fun onDestroy() {
        destroyPubNub()
        super.onDestroy()
    }

    private fun destroyPubNub() {
        pubNub.destroy()
    }


    /**
     * @author: Narbir Singh
     * @description:This method is used to send associate user into Yew independent user
     */
    override fun sendMessageToAssociate() {
        if (viewModel.message.get().isNullOrEmpty()) {
            Toast.makeText(this, "Please enter message", Toast.LENGTH_SHORT).show()
            return
        }

        val timeToken = DateTime.now().millis
        val message = viewModel.getMessageObject(timeToken = timeToken)
        sendMessageToPubNub(message)
        viewModel.lastMessage.set(viewModel.message.get() ?: "")
        viewModel.lastMessage.set(viewModel.message.get() ?: "")

        viewModel.message.set("")
    }


    private fun sendMessageToPubNub(message: JsonObject?) {
        pubNub.publish(
            channel = viewModel.channelID.get() ?: return,
            message = message ?: return,
            shouldStore = true
        ).async { result, status ->
            /*
              the result is always of a nullable type
              it's null if there were errors (status.error)
              otherwise it's usable
           */
            if (!status.error) {
//                println("Message timetoken: ${result!!.timetoken}")
                println("Message published")

//                viewModel.updateSpace(pubNub, timeToken * TIMESTAMP_DIVIDER, MessageType.TEXT.toString())
            } else {
                println("Could not published message")

                // handle error
                status.exception?.printStackTrace()
            }
        }

    }


    /**
     * @author: Narbir Singh
     * @description:Get Earliest time stamp
     */
    private fun getEarliestTimestamp(): Long? {
        return if (viewModel.chatHistoryList.isNotEmpty()) {
            viewModel.chatHistoryList[0].timeToken
        } else null
    }

    private fun scrollChatToBottom() {
        runOnUiThread {
            viewDataBinding.chatRecycler.scrollToPosition(viewModel.chatHistoryList.size - 1)
        }
    }


    /**
     * @author: Narbir Singh
     * @description:This method is used to redirect to Migrate associate user into Yew independent user
     */
    override fun onMigrateAccountClicked(associateDetails: Associate) {
        val editAssociateExtras = AddAssociatePermissionExtras.associateIDExtras {
            associateId = associateDetails.userId.toString()
        }
        startActivity(
            intentProviderFactory.create(
                MigrateAssociateActivity::class.java, editAssociateExtras, 0
            )
        )
    }

    /**
     * @author: Narbir Singh
     * @description:This method is used to redirect Edit associate member details
     */
    override fun onEditClicked(associateDetails: Associate) {
        val editAssociateExtras = AddAssociatePermissionExtras.associateIDExtras {
            associateId = associateDetails.userId.toString()
        }
        startActivity(
            intentProviderFactory.create(
                AddMembersDetailsActivity::class.java, editAssociateExtras, 0
            )
        )
    }

    /**
     * @author: Narbir Singh
     * @description:This method is used to redirect associate Permission screen
     */
    override fun onManagePermissionClicked(associateItem: Associate) {
        //TODO: Edit Associate Profile Permission
        val editAssociateExtras = AddAssociatePermissionExtras.associateIDExtras {
            associateId = associateItem.userId.toString()
        }
        startActivity(
            intentProviderFactory.create(
                AddAssociatePermissionActivity::class.java, editAssociateExtras, 0
            )
        )
    }
}