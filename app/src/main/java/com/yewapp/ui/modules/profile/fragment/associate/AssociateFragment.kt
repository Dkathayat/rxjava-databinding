package com.yewapp.ui.modules.profile.fragment.associate

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.yewapp.R
import com.yewapp.data.network.api.associate.Associate
import com.yewapp.databinding.FragmentAssociateBinding
import com.yewapp.ui.base.BaseFragment
import com.yewapp.ui.common.RemoveAccountBottomSheet
import com.yewapp.ui.modules.addassociatemember.AddMembersActivity
import com.yewapp.ui.modules.addassociatememberdetails.AddMembersDetailsActivity
import com.yewapp.ui.modules.addassociatepermission.AddAssociatePermissionActivity
import com.yewapp.ui.modules.addassociatepermission.AddAssociatePermissionExtras
import com.yewapp.ui.modules.chat.ChatActivity
import com.yewapp.ui.modules.chat.extras.ChatActivityExtras
import com.yewapp.ui.modules.editProfile.EditProfileActivity
import com.yewapp.ui.modules.editProfile.extras.EditProfileExtras
import com.yewapp.ui.modules.migrateassociate.MigrateAssociateActivity


class AssociateFragment(override val layoutId: Int = R.layout.fragment_associate) :
    BaseFragment<AssociateNavigator, AssociateViewModel, FragmentAssociateBinding>(),
    AssociateNavigator, ItemAssociateMember.OnAssociateOptionClickListener {

    private lateinit var associateMemberAdapter: AssociateMemberAdapter


    override fun onResume() {
        super.onResume()
        if (isVisible && isAdded)
            viewModel?.getAssociateMember()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        Log.d("ASSOCIATE", "onCreateView")

        return bind(AssociateViewModel::class.java, inflater, container)
    }


    override fun navigateAddMembersAssociate() {
        if (viewModel?.inCompleteAssociateId?.get() == 0) {
            //TODO: Create Associate Profile
            startActivity(
                intentProviderFactory.create(
                    AddMembersActivity::class.java, null, 0
                )
            )
        } else {
            //TODO: Edit Associate Profile
            val editAssociateExtras = AddAssociatePermissionExtras.associateIDExtras {
                associateId = viewModel?.inCompleteAssociateId?.get().toString()
            }
            startActivity(
                intentProviderFactory.create(
                    AddAssociatePermissionActivity::class.java, editAssociateExtras, 0
                )
            )

            //used to call AddMembersDetailsActivity
//            startActivity(
//                intentProviderFactory.create(
//                    AddMembersDetailsActivity::class.java,
//                    editAssociateExtras,
//                    0
//                )
//            )

        }

    }

    override fun onBackPress() {
    }


    override fun onViewModelCreated(viewModel: AssociateViewModel) {
        viewModel.setNavigator(this)
        Log.d("ASSOCIATE", "onViewModelCreated")
    }

    override fun onViewBound(viewDataBinding: FragmentAssociateBinding) {
        Log.d("ASSOCIATE", "onViewBound")
        adapterInitialize()
        addObserver()
    }

    private fun addObserver() {
        val viewModel = viewModel ?: return
        viewModel.memberListLiveData.observe(this, Observer {
            Log.d("ASSOCIATE", "memberListLiveData ${viewModel.mutableMemberList.size}")
            associateMemberAdapter.addItem(it)
        })
    }


    private fun adapterInitialize() {
        val list = mutableListOf<Associate>()
        associateMemberAdapter = AssociateMemberAdapter(
            list, this
//                ,object : ItemAssociateMember.OnItemClickListener {
//                override fun onClickItem(item: Associate) {
//                }}


        )
        viewDataBinding.memberRecycler.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        viewDataBinding.memberRecycler.adapter = associateMemberAdapter
//        viewDataBinding.popularRecycler!!.addOnScrollListener(viewModel!!.onScrollListener)
        Log.d("ASSOCIATE", "adapterInitialize")

    }


    /**
     * @author: Narbir Singh
     * @description:This method is used to redirect to Migrate associate user into Yew independent user
     */
    override fun onMigrateAccountClicked(associateItem: Associate) {
        val editAssociateExtras = AddAssociatePermissionExtras.associateIDExtras {
            associateId = associateItem.userId.toString()
        }
        startActivity(
            intentProviderFactory.create(
                MigrateAssociateActivity::class.java, editAssociateExtras, 0
            )
        )
    }

    /**
     * @author: Narbir Singh
     * @description:This method is used to remove associate member account
     */
    override fun onRemoveAccountClicked(associateItem: Associate) {
        val title = viewModel?.dataManager?.getResourceProvider()
            ?.getString(R.string.remove_account)

        val message = viewModel?.dataManager?.getResourceProvider()
            ?.getString(R.string.are_you_sure_you_want_to_remove_this_associate_account_once_removed_you_will_not_bew_able_to_recover_any_of_it_s_data_like_the_earned_points_stats_and_followers_etc)
        RemoveAccountBottomSheet.Builder()
            .setTitle(title ?: return)
            .setMessage(message ?: return)
            .setButtonRemove {
                viewModel?.removeAssociateAccount(associateItem.userId ?: return@setButtonRemove)
            }

            .build().show(requireActivity())


//        RemoveAccountBottomSheet.newInstance(
//            message?:return,
//            object : RemoveAccountBottomSheet.OnRemoveClicked {
//                override fun onDismissClicked() {
//                    TODO("Not yet implemented")
//                }
//
//                override fun onRemoveClicked() {
//                    viewModel?.removeAssociateAccount(associateItem.userId ?: return)
//                }
//
//            },
//        ).show(activity?.supportFragmentManager ?: return, "Remove Associate")

    }

    /**
     * @author: Narbir Singh
     * @description:This method is used to redirect Edit associate member details
     */
    override fun onEditClicked(associateItem: Associate) {
//        val extras = EditProfileExtras.editProfileExtras {
//            this.isSignUp = false
//            this.assoicateID = ""
//        }
//        startActivity(
//            intentProviderFactory.create(
//                EditProfileActivity::class.java, extras, 0
//            )
//        )
        val editAssociateExtras = AddAssociatePermissionExtras.associateIDExtras {
            associateId = associateItem.userId.toString()
        }
        startActivity(
            intentProviderFactory.create(
                AddMembersDetailsActivity::class.java, editAssociateExtras, 0
            )
        )
    }

    /**
     * @author: Narbir Singh
     * @description:This method is used to activate or deactivate associate account
     * 1 : activate
     * 0 :Deactivate
     */
    override fun onDeActiveAccountClicked(associateItem: Associate) {
        val updatedStatus = if (associateItem.status?.toInt() == 1) 0 else 1
        viewModel?.activateDeactivateAssociateAccount(associateItem.userId ?: return, updatedStatus)
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


    /**
     * @author: Narbir Singh
     * @description:This method is used show message associate is removed
     */
    override fun removeAssociateSuccess(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        viewModel?.getAssociateMember()
    }


    override fun onChatClicked(associateItem: Associate) {
        /**
         * @author: Narbir Singh
         * @description:
         * Case:1
         *         We Pass Associate item data as Associate data object for common chat process
         */
        val extrasAssociateDetails =
            ChatActivityExtras.associateDetailsExtras { associateDetails = associateItem }
        startActivity(
            intentProviderFactory.create(
                ChatActivity::class.java, extrasAssociateDetails, 0
            )
        )
    }


//    private fun initializePubNub() {
//        val pnConfiguration = PNConfiguration(UserId("myUserId")).apply {
//            subscribeKey = "pub-c-55ebd3d7-dd61-4e1d-9f2b-8d1f9f7a0e64"
//            publishKey = "sub-c-8a4228bd-7282-4891-af93-198b26ee58aa"
//            secretKey = "sec-c-ZmIxZmRlNDItYWJjYS00NTVhLThjNTMtOWU2YzJlMjhhOTQ4"
//
//            secure = true
//        }
//        val pubnub = PubNub(pnConfiguration)
//
//        val myChannel = "awesomeChannel"
//        val myMessage = JsonObject().apply {
//            addProperty("msg", "Hello, world")
//        }
//
//        println("Message to send: $myMessage")
//
//        pubnub.addListener(object : SubscribeCallback() {
//
//            override fun status(pubnub: PubNub, status: PNStatus) {
//                when (status.category) {
//                    PNStatusCategory.PNConnectedCategory -> {
//                        // Connect event. You can do stuff like publish, and know you'll get it.
//                        // Or just use the connected event to confirm you are subscribed for
//                        // UI / internal notifications, etc.
//                    }
//                    PNStatusCategory.PNReconnectedCategory -> {
//                        // Happens as part of our regular operation.
//                        // This event happens when radio / connectivity is lost, then regained.
//                    }
//                    PNStatusCategory.PNUnexpectedDisconnectCategory -> {
//                        // This event happens when radio / connectivity is lost.
//                    }
//                    else -> {}
//                }
//            }
//
//            override fun message(pubnub: PubNub, pnMessageResult: PNMessageResult) {
//                if (pnMessageResult.channel == myChannel) {
//                    println("Received message ${pnMessageResult.message.asJsonObject}")
//                }
//            }
//
//            override fun presence(
//                pubnub: PubNub,
//                pnPresenceEventResult: PNPresenceEventResult
//            ) {
//                // handle presence
//            }
//        })
//
////        pubnub.subscribe(
////            channels = listOf(myChannel)
////        )
////
////        pubnub.publish(
////            channel = myChannel,
////            message = myMessage
////        ).async { result, status ->
////            println(status)
////            if (!status.error) {
////                println("Message sent, timetoken: ${result!!.timetoken}")
////            } else {
////                println("Error while publishing")
////                status.exception?.printStackTrace()
////            }
////        }
//    }

}