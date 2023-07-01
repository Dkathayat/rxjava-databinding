package com.yewapp.ui.modules.follower.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.yewapp.R
import com.yewapp.data.network.api.follower.MyFollowing
import com.yewapp.databinding.FragmentMyFollowingBinding
import com.yewapp.ui.base.BaseFragment
import com.yewapp.ui.modules.follower.adapter.MyFollowingAdapter
import com.yewapp.ui.modules.follower.navigator.MyFollowingNavigator
import com.yewapp.ui.modules.follower.vm.MyFollowingViewModel


class MyFollowingFragment(override val layoutId: Int = R.layout.fragment_my_following) :
    BaseFragment<MyFollowingNavigator, MyFollowingViewModel, FragmentMyFollowingBinding>(),
    MyFollowingNavigator {

    private lateinit var adapter: MyFollowingAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return bind(MyFollowingViewModel::class.java, inflater, container)
    }

    override fun onBackPress() {

    }

    override fun onViewModelCreated(viewModel: MyFollowingViewModel) {
        viewModel.setNavigator(this)
        addObserver()
    }

    override fun onViewBound(viewDataBinding: FragmentMyFollowingBinding) {
        val list = mutableListOf<MyFollowing>()
        adapter = MyFollowingAdapter(list)
        adapter.setHasStableIds(true)
        viewDataBinding.followingRecycler.adapter = adapter
        viewDataBinding.followingRecycler.layoutManager = LinearLayoutManager(activity)
    }

    private fun addObserver() {
        val viewModel = viewModel ?: return
        viewModel.listLive.observe(viewLifecycleOwner, Observer {
            this.adapter.setItems(it)
        })
    }

}