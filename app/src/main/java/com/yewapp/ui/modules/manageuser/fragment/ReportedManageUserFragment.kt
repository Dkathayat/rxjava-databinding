package com.yewapp.ui.modules.manageuser.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.yewapp.R
import com.yewapp.data.network.api.UserList
import com.yewapp.databinding.ReportedManageUserFragmentBinding
import com.yewapp.ui.base.BaseFragment
import com.yewapp.ui.modules.managefeeds.navigator.ReportedFragmentNavigator
import com.yewapp.ui.modules.manageuser.adapter.ManageUserListAdapter
import com.yewapp.ui.modules.manageuser.vm.ItemManageUserViewModel
import com.yewapp.ui.modules.manageuser.vm.ReportedManageUserFragmentViewModel
import com.yewapp.ui.modules.addchallenge.challengeroutes.ManageUserEnum

class ReportedManageUserFragment(override val layoutId: Int = R.layout.reported_manage_user_fragment) :
    BaseFragment<ReportedFragmentNavigator, ReportedManageUserFragmentViewModel, ReportedManageUserFragmentBinding>(),
    ReportedFragmentNavigator {

    private lateinit var blockListAdapter: ManageUserListAdapter

    override fun onViewModelCreated(viewModel: ReportedManageUserFragmentViewModel) {
        viewModel.setNavigator(this)
    }

    override fun onViewBound(viewDataBinding: ReportedManageUserFragmentBinding) {
        viewModel?.getReportedUserList()
        adapterInitialize()
    }

    override fun onBackPress() {

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return bind(ReportedManageUserFragmentViewModel::class.java, inflater, container)
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
        var list = mutableListOf<UserList>()
        blockListAdapter =
            ManageUserListAdapter(list, object : ItemManageUserViewModel.OnItemClickListener {

                override fun onClickItem(item: UserList) {

                }
            }, ManageUserEnum.REPORTED.Type)
        viewDataBinding.rvMuted.adapter = blockListAdapter.apply {
            setHasStableIds(true)
        }
        viewDataBinding.swipeRefreshListener.setOnRefreshListener {
            viewModel?.clearList()
            blockListAdapter.clearItems()
            viewModel?.getReportedUserList()
        }
    }
}