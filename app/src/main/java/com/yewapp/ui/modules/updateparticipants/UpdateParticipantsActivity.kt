package com.yewapp.ui.modules.updateparticipants

import android.content.Intent
import androidx.lifecycle.Observer
import com.yewapp.R
import com.yewapp.data.network.api.invitemember.InviteMember
import com.yewapp.databinding.ActivityUpdateParticiapantsBinding
import com.yewapp.ui.base.BaseActivity
import com.yewapp.ui.modules.addchallenge.challengepreview.extras.UpdateParticipantsExtras
import com.yewapp.ui.modules.addchallenge.invitemember.adapter.InviteMemberAdapter
import com.yewapp.ui.modules.addchallenge.invitemember.vm.ItemInviteMemberViewModel

class UpdateParticipantsActivity :
    BaseActivity<UpdateParticipantsNavigator, UpdateParticipantsViewModel, ActivityUpdateParticiapantsBinding>(),
    UpdateParticipantsNavigator {
    private lateinit var inviteMemberAdapter: InviteMemberAdapter


    override fun getLayout(): Int = R.layout.activity_update_particiapants

    override fun init() {
        bind(UpdateParticipantsViewModel::class.java)
    }

    override fun onViewModelCreated(viewModel: UpdateParticipantsViewModel) {
        viewModel.setNavigator(this)
    }

    override fun onViewBound(viewDataBinding: ActivityUpdateParticiapantsBinding) {
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
                    viewModel.isUpdateActive.set(false)
                    item.addStatus = !item.addStatus
                    viewModel.memberList[position].addStatus = item.addStatus
                    inviteMemberAdapter.updateItem(item, position)
                    for (i in 0 until viewModel.memberList.size) {
                        if (viewModel.memberList[i].addStatus) {
                            viewModel.isUpdateActive.set(true)
                        }
                    }
                }
            })
        viewDataBinding.rvParticipants.adapter = inviteMemberAdapter
    }

    override fun onUpdateClicked() {
        val selectedUser = arrayListOf<InviteMember>()
        for (i in 0 until viewModel.memberList.size) {
            if (viewModel.memberList[i].addStatus)
                selectedUser.add(viewModel.memberList[i])
        }
        val intent = Intent()
        intent.putParcelableArrayListExtra(UpdateParticipantsExtras.PARTICIPANTS_LIST, selectedUser)
        setResult(100108, intent)
        finish() //finishing activity
    }
}