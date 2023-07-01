package com.yewapp.ui.modules.addchallenge.invitemember

import androidx.lifecycle.Observer
import com.yewapp.R
import com.yewapp.data.network.api.challenges.ChallengeModel
import com.yewapp.data.network.api.invitemember.InviteMember
import com.yewapp.databinding.ActivityInviteMemberBinding
import com.yewapp.ui.base.BaseActivity
import com.yewapp.ui.modules.addchallenge.challengepreview.ChallengePreviewActivity
import com.yewapp.ui.modules.addchallenge.invitemember.adapter.InviteMemberAdapter
import com.yewapp.ui.modules.addchallenge.invitemember.vm.ItemInviteMemberViewModel
import com.yewapp.ui.modules.listner.ChallengeExtras

//Todo:Add Challenge Step 9
class InviteMemberActivity :
    BaseActivity<InviteMemberNavigator, InviteMemberViewModel, ActivityInviteMemberBinding>(),
    InviteMemberNavigator {

    private lateinit var inviteMemberAdapter: InviteMemberAdapter

    override fun getLayout(): Int {
        return R.layout.activity_invite_member
    }

    override fun init() {
        bind(InviteMemberViewModel::class.java)
    }

    override fun onViewModelCreated(viewModel: InviteMemberViewModel) {
        viewModel.setNavigator(this)
    }

    override fun onViewBound(viewDataBinding: ActivityInviteMemberBinding) {
        adapterInitialize()
    }

    override fun onStart() {
        super.onStart()
        addObserver()
    }

    private fun addObserver() {
        val viewModel = viewModel
        viewModel.inviteMemberList.observe(this, Observer {
            inviteMemberAdapter.setItems(it)
        })
    }

    private fun adapterInitialize() {
        val list = mutableListOf<InviteMember>()

        inviteMemberAdapter =
            InviteMemberAdapter(list, object : ItemInviteMemberViewModel.OnItemClickListener {
                override fun onClickItem(item: InviteMember, position: Int) {
                    viewModel.isContinueActive.set(false)
                    item.addStatus = !item.addStatus
                    viewModel.memberList[position].addStatus = item.addStatus
                    inviteMemberAdapter.updateItem(item, position)
                    for (i in 0 until viewModel.memberList.size) {
                        if (viewModel.memberList[i].addStatus) {
                            viewModel.isContinueActive.set(true)
                        }
                    }
                }
            })
        viewDataBinding.inviteMemberRecycler.adapter = inviteMemberAdapter
    }

    override fun onContinueClick() {
//        Repository.getInstance().getChallengeProgress().value = viewModel.challengeExtras
        val inviteMembers = arrayListOf<InviteMember>()

        for (i in 0 until viewModel.memberList.size) {
            if (viewModel.memberList[i].addStatus) {
                inviteMembers.add(viewModel.memberList[i])
            }
        }


        val extras = ChallengeExtras.challengeExtras {
            challengeData = ChallengeModel(
                viewModel.challengeModel.isEdit,
                viewModel.challengeModel.challengeID ?: "",
                9,
                viewModel.challengeModel.selectedSportId,
                viewModel.challengeModel.selectedSportImage,
                viewModel.challengeModel.profileType,
                viewModel.challengeModel.sportsName,
                viewModel.challengeModel.startDate,
                viewModel.challengeModel.endDate,
                2,//for popular route
                viewModel.challengeModel.routeID,
                viewModel.challengeModel.challengeName,//challengeName
                viewModel.challengeModel.challengeVisibility,//challenge visibility
                viewModel.challengeModel.challengeStatus,//challenge visibility
                viewModel.challengeModel.selectedSportsLevel,//challenge selectedSportsLevel
                viewModel.challengeModel.ageGroup,//challenge selectedAgeGroupValue
                viewModel.challengeModel.challengeDescription,//challenge description
                viewModel.challengeModel.subSportTypeId,
                viewModel.challengeModel.sportsEquipments,
                //step-5 A (for radius reach)
                viewModel.challengeModel.challengeArea,//radius reach / Extended
                viewModel.challengeModel.location,//radius reach / Extended
                viewModel.challengeModel.latitude,
                viewModel.challengeModel.longitude,
                viewModel.challengeModel.radius,

                //step-5 B (Extended reach)
                viewModel.challengeModel.country,
                viewModel.challengeModel.countryId,
                viewModel.challengeModel.state,
                viewModel.challengeModel.stateId,
                viewModel.challengeModel.cityId,
                viewModel.challengeModel.zipCode,
                //step-6
                viewModel.challengeModel.calories,
                viewModel.challengeModel.miles,
                viewModel.challengeModel.elevationGain,
                viewModel.challengeModel.avgWatt,
                viewModel.challengeModel.time,
                viewModel.challengeModel.maxMember,

                //Step 7
                viewModel.challengeModel.winnerPickedFrom,
                viewModel.challengeModel.selectedWinnerPrize,
                viewModel.challengeModel.overViewValue,
                viewModel.challengeModel.winnerValue,
                viewModel.challengeModel.additionalInfoValue,
                viewModel.challengeModel.rulesValue,
                viewModel.challengeModel.guidelinesValue,
                viewModel.challengeModel.qualificationValue,
                //STEP 8
                viewModel.challengeModel.bannerImage,
                viewModel.challengeModel.challengeImage,
                inviteMembers
            )
        }
        startActivity(
            intentProviderFactory.create(
                ChallengePreviewActivity::class.java,
                extras,
                0
            )
        )
    }

    override fun skipScreen() {
        if(viewModel.challengeModel.isEdit){
            onContinueClick()
        }else{
            val extras = ChallengeExtras.challengeExtras {
                challengeData = ChallengeModel(
                    viewModel.challengeModel.isEdit,
                    viewModel.challengeModel.challengeID ?: "",
                    9,
                    viewModel.challengeModel.selectedSportId,
                    viewModel.challengeModel.selectedSportImage,
                    viewModel.challengeModel.profileType,
                    viewModel.challengeModel.sportsName,
                    viewModel.challengeModel.startDate,
                    viewModel.challengeModel.endDate,
                    2,//for popular route
                    viewModel.challengeModel.routeID,
                    viewModel.challengeModel.challengeName,//challengeName
                    viewModel.challengeModel.challengeVisibility,//challenge visibility
                    viewModel.challengeModel.challengeStatus,//challenge visibility
                    viewModel.challengeModel.selectedSportsLevel,//challenge selectedSportsLevel
                    viewModel.challengeModel.ageGroup,//challenge selectedAgeGroupValue
                    viewModel.challengeModel.challengeDescription,//challenge description
                    viewModel.challengeModel.subSportTypeId,
                    viewModel.challengeModel.sportsEquipments,
                    //step-5 A (for radius reach)
                    viewModel.challengeModel.challengeArea,//radius reach / Extended
                    viewModel.challengeModel.location,//radius reach / Extended
                    viewModel.challengeModel.latitude,
                    viewModel.challengeModel.longitude,
                    viewModel.challengeModel.radius,

                    //step-5 B (Extended reach)
                    viewModel.challengeModel.country,
                    viewModel.challengeModel.countryId,
                    viewModel.challengeModel.state,
                    viewModel.challengeModel.stateId,
                    viewModel.challengeModel.cityId,
                    viewModel.challengeModel.zipCode,
                    //step-6
                    viewModel.challengeModel.calories,
                    viewModel.challengeModel.miles,
                    viewModel.challengeModel.elevationGain,
                    viewModel.challengeModel.avgWatt,
                    viewModel.challengeModel.time,
                    viewModel.challengeModel.maxMember,

                    //Step 7
                    viewModel.challengeModel.winnerPickedFrom,
                    viewModel.challengeModel.selectedWinnerPrize,
                    viewModel.challengeModel.overViewValue,
                    viewModel.challengeModel.winnerValue,
                    viewModel.challengeModel.additionalInfoValue,
                    viewModel.challengeModel.rulesValue,
                    viewModel.challengeModel.guidelinesValue,
                    viewModel.challengeModel.qualificationValue,
                    //STEP 8
                    viewModel.challengeModel.bannerImage,
                    viewModel.challengeModel.challengeImage,
                    arrayListOf()
                )
            }
            startActivity(
                intentProviderFactory.create(
                    ChallengePreviewActivity::class.java,
                    extras,
                    0
                )
            )
        }
    }
}