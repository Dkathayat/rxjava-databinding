package com.yewapp.ui.modules.manageuser.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.yewapp.R
import com.yewapp.data.network.api.UserList
import com.yewapp.databinding.MutedManageUserFragmentBinding
import com.yewapp.ui.base.BaseFragment
import com.yewapp.ui.modules.manageuser.adapter.ManageUserListAdapter
import com.yewapp.ui.modules.manageuser.navigator.MutedFragmentNavigator
import com.yewapp.ui.modules.manageuser.vm.ItemManageUserViewModel
import com.yewapp.ui.modules.manageuser.vm.MutedFragmentViewModel
import com.yewapp.ui.modules.addchallenge.challengeroutes.ManageUserEnum

class MutedFragment(override val layoutId: Int = R.layout.muted_manage_user_fragment) :
    BaseFragment<MutedFragmentNavigator, MutedFragmentViewModel, MutedManageUserFragmentBinding>(),
    MutedFragmentNavigator {

    private lateinit var blockListAdapter: ManageUserListAdapter

    override fun onViewModelCreated(viewModel: MutedFragmentViewModel) {
        viewModel.setNavigator(this)
    }

    override fun onViewBound(viewDataBinding: MutedManageUserFragmentBinding) {
        viewModel?.getMutedUserList()
        adapterInitialize()
    }

    override fun onBackPress() {

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return bind(MutedFragmentViewModel::class.java, inflater, container)
    }

    override fun onStart() {
        super.onStart()
        addObserver()
    }

    private fun addObserver() {
        val viewModel = viewModel ?: return
        viewModel._userList.observe(this, Observer {
            viewDataBinding.swipeRefreshListener.isRefreshing = false
            blockListAdapter.setItems(it!!)
        })
    }

    private fun adapterInitialize() {
        val list = mutableListOf<UserList>()
        blockListAdapter =
            ManageUserListAdapter(list, object : ItemManageUserViewModel.OnItemClickListener {
                override fun onClickItem(item: UserList) {
                    viewModel?.unMuteUser(item.userId!!)
                }
            }, ManageUserEnum.MUTED.Type)
        viewDataBinding.rvMuted.adapter = blockListAdapter.apply {
            setHasStableIds(true)
        }
        viewDataBinding.swipeRefreshListener.setOnRefreshListener {
            viewModel?.clearList()
            blockListAdapter.clearItems()
            viewModel?.getMutedUserList()
        }
    }
}