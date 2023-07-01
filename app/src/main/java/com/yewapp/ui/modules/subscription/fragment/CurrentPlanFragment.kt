package com.yewapp.ui.modules.subscription.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.yewapp.R
import com.yewapp.databinding.CurrentPlanFragmentBinding
import com.yewapp.ui.base.BaseFragment
import com.yewapp.ui.modules.subscription.SubscriptionActivity
import com.yewapp.ui.modules.subscription.adapter.CurrentPlanAdapter
import com.yewapp.ui.modules.subscription.navigator.CurrentPlanFragmentNavigtor
import com.yewapp.ui.modules.subscription.vm.CurrentPlanFragmentViewModel

class CurrentPlanFragment(override val layoutId: Int = R.layout.current_plan_fragment) :
    BaseFragment<CurrentPlanFragmentNavigtor, CurrentPlanFragmentViewModel, CurrentPlanFragmentBinding>(),
    CurrentPlanFragmentNavigtor {

    private lateinit var currentPlanAdapter: CurrentPlanAdapter
    override fun onViewModelCreated(viewModel: CurrentPlanFragmentViewModel) {
        viewModel.setNavigator(this)
    }

    override fun onViewBound(viewDataBinding: CurrentPlanFragmentBinding) {
        currentPlanAdapter = CurrentPlanAdapter(requireContext())
        with(viewDataBinding.currentPlanRecyler) {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = currentPlanAdapter
        }
    }

    override fun onStart() {
        super.onStart()
        addObserver()
    }

    private fun addObserver() {
        val viewModel = viewModel ?: return
        viewModel.subscriptionHistory.observe(this, Observer {
            currentPlanAdapter.differ.submitList(it.history.list)
        })
    }

    override fun navigateToPlansList() {
        (activity as SubscriptionActivity?)?.setCurrentItem(1, true)
    }

    override fun onBackPress() {

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return bind(CurrentPlanFragmentViewModel::class.java, inflater, container)

    }

}