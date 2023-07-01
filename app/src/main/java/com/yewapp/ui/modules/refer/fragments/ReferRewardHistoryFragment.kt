package com.yewapp.ui.modules.refer.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yewapp.R
import com.yewapp.data.network.api.refer.ReferRewardHistory
import com.yewapp.databinding.FragmentReferRewardHistoryBinding
import com.yewapp.ui.base.BaseFragment
import com.yewapp.ui.modules.refer.adapter.ReferRewardHistoryAdapter
import com.yewapp.ui.modules.refer.navigator.ReferRewardHistoryNavigator
import com.yewapp.ui.modules.refer.vm.ReferRewardHistoryViewModel

class ReferRewardHistoryFragment(override val layoutId: Int = com.yewapp.R.layout.fragment_refer_reward_history) :
    BaseFragment<ReferRewardHistoryNavigator, ReferRewardHistoryViewModel, FragmentReferRewardHistoryBinding>(),
    ReferRewardHistoryNavigator {

    private lateinit var referRewardHistoryAdapter: ReferRewardHistoryAdapter
    var layoutManager = LinearLayoutManager(activity)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return bind(ReferRewardHistoryViewModel::class.java, inflater, container)
    }


    override fun onViewModelCreated(viewModel: ReferRewardHistoryViewModel) {
        viewModel.setNavigator(this)
    }

    override fun onViewBound(viewDataBinding: FragmentReferRewardHistoryBinding) {
        initializeAdapter()
        addObserver()
    }

    private fun initializeAdapter() {
        val list = mutableListOf<ReferRewardHistory>()
        referRewardHistoryAdapter = ReferRewardHistoryAdapter(
            list
        )
        viewDataBinding.referRewardHistoryRecycler.adapter = referRewardHistoryAdapter.apply {
            setHasStableIds(true)
        }
        viewDataBinding.referRewardHistoryRecycler.layoutManager = layoutManager

        val decorator = DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
        decorator.setDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_line_1)!!)

        viewDataBinding.referRewardHistoryRecycler.addItemDecoration(decorator)//add Divider b/w items
        viewDataBinding.referRewardHistoryRecycler.addOnScrollListener(
            referRewardHistoryScrollListener
        )
    }

    private fun addObserver() {
        viewModel?.liveDataReferRewardHistory?.observe(this, Observer {
            referRewardHistoryAdapter.setItems(it)
        })
    }

    override fun onBackPress() {
    }

    private val referRewardHistoryScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val viewModel = viewModel ?: return
            val visibleItemCount: Int = layoutManager.childCount
            val totalItemCount: Int = layoutManager.itemCount
            val firstVisibleItemPosition: Int = layoutManager.findFirstVisibleItemPosition()
            val isLastPage = viewModel.currentPage > viewModel.lastPage

            if (!viewModel.isLoading.get() && !isLastPage) {
                // if (!viewModel.isLoading.get() && viewModel.currentPage <  viewModel.lastPage){
                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                    && firstVisibleItemPosition >= 0 && totalItemCount >= viewModel.perPage
                ) {
                    viewModel.currentPage++
                    viewModel.getReferRewardHistoryList()
                }
            }
        }
    }

}