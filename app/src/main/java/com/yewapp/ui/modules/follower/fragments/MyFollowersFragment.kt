package com.yewapp.ui.modules.follower.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.yewapp.R
import com.yewapp.databinding.FragmentMyFollowersBinding
import com.yewapp.ui.base.BaseFragment
import com.yewapp.ui.modules.dashboard.fragment.feeds.UserAction
import com.yewapp.ui.modules.follower.adapter.MyFollowersAdapter
import com.yewapp.ui.modules.follower.navigator.MyFollowersNavigator
import com.yewapp.ui.modules.follower.vm.MyFollowersViewModel
import com.yewapp.ui.modules.report.SendReportActivity

enum class UserAction {
    BLOCK, MUTE, FOLLOW, FAVOURITE
}

class MyFollowersFragment(override val layoutId: Int = R.layout.fragment_my_followers) :
    BaseFragment<MyFollowersNavigator, MyFollowersViewModel, FragmentMyFollowersBinding>(),
    MyFollowersNavigator {

    var layoutManager = LinearLayoutManager(activity)
    val adapter = MyFollowersAdapter(mutableListOf()) { option, myFollowers, position ->
        viewModel?.onFollowOptionSelected(option, myFollowers, position)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        return bind(MyFollowersViewModel::class.java, inflater, container)
    }


    override fun userBlockedSuccess() {
        adapter.updateItem(
            viewModel?.selectedFollowers ?: return,
            viewModel?.selectedOptionMyFollowerPosition ?: return,
            UserAction.BLOCK
        )
    }

    override fun userFavouriteSuccess() {
        adapter.updateItem(
            viewModel?.selectedFollowers ?: return,
            viewModel?.selectedOptionMyFollowerPosition ?: return,
            UserAction.FAVOURITE
        )
    }

    override fun userMutedSuccess() {
        adapter.updateItem(
            viewModel?.selectedFollowers ?: return,
            viewModel?.selectedOptionMyFollowerPosition ?: return,
            UserAction.MUTE
        )
    }

    override fun userFollowedSuccess() {
        adapter.updateItem(
            viewModel?.selectedFollowers ?: return,
            viewModel?.selectedOptionMyFollowerPosition ?: return,
            UserAction.FOLLOW
        )
    }

    override fun launchReportActivity() {
        startActivity(
            intentProviderFactory.create(
                SendReportActivity::class.java,
                null,
                0
            )
        )
    }

    override fun onBackPress() {

    }

    override fun onViewModelCreated(viewModel: MyFollowersViewModel) {
        viewModel.setNavigator(this)
        addObserver()
        viewModel.followerList.observe(this, Observer {
            adapter.addItems(it)
        })
    }


    override fun onViewBound(viewDataBinding: FragmentMyFollowersBinding) {
        //    adapter.setHasStableIds(true)
        viewDataBinding.followerRecycler.adapter = adapter
        viewDataBinding.followerRecycler.layoutManager = LinearLayoutManager(activity)
    }

    private fun addObserver() {
        val viewModel = viewModel ?: return
        viewModel.followerList.observe(viewLifecycleOwner, Observer {
            this.adapter.setItems(it)
        })
    }
}
