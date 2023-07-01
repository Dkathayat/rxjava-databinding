package com.yewapp.ui.modules.manageuser.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.yewapp.R
import com.yewapp.data.network.api.UserList
import com.yewapp.databinding.BlockedManageUserFragmentBinding
import com.yewapp.ui.base.BaseFragment
import com.yewapp.ui.modules.manageuser.adapter.ManageUserListAdapter
import com.yewapp.ui.modules.manageuser.navigator.BlockFragmentNavigator
import com.yewapp.ui.modules.manageuser.vm.BlockedFragmentViewModel
import com.yewapp.ui.modules.manageuser.vm.ItemManageUserViewModel
import com.yewapp.ui.modules.addchallenge.challengeroutes.ManageUserEnum

class BlockedFragment(override val layoutId: Int = R.layout.blocked_manage_user_fragment) :
    BaseFragment<BlockFragmentNavigator, BlockedFragmentViewModel, BlockedManageUserFragmentBinding>(),
    BlockFragmentNavigator {

    private lateinit var blockListAdapter: ManageUserListAdapter

    override fun onViewModelCreated(viewModel: BlockedFragmentViewModel) {
        viewModel.setNavigator(this)
    }

    override fun onViewBound(viewDataBinding: BlockedManageUserFragmentBinding) {
        viewModel?.getBlockedUserList()
        adapterInitialize()
    }

    private fun adapterInitialize() {
        val list = mutableListOf<UserList>()
        blockListAdapter =
            ManageUserListAdapter(list, object : ItemManageUserViewModel.OnItemClickListener {
                override fun onClickItem(item: UserList) {
                    viewModel?.unBlockUser(item.userId!!)

                }
            }, ManageUserEnum.BLOCKED.Type)
        viewDataBinding.rvBlock.adapter = blockListAdapter.apply {
            setHasStableIds(true)
        }
        viewDataBinding.swipeRefreshListener.setOnRefreshListener {
            viewModel?.clearList()
            blockListAdapter.clearItems()
            viewModel?.getBlockedUserList()
        }
    }
    override fun onBackPress() {

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return bind(BlockedFragmentViewModel::class.java, inflater, container)
    }

    override fun onStart() {
        super.onStart()
        addObserver()
    }

    private fun addObserver() {
        val viewModel = viewModel ?: return
        viewModel._userList.observe(this, Observer {

            blockListAdapter.setItems(it!!)
        })
    }
}