package com.yewapp.ui.modules.dashboard.fragment.challenges.fragments.upcoming

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.yewapp.R
import com.yewapp.data.network.api.challenges.ChallengesDetails
import com.yewapp.databinding.FragmentUpcomingBinding
import com.yewapp.ui.base.BaseFragment
import com.yewapp.ui.modules.challengeDetails.ChallengeDetailActivity
import com.yewapp.utils.setDivider

class UpcomingFragment(override val layoutId: Int = R.layout.fragment_upcoming) :
    BaseFragment<UpcomingNavigator, UpcomingChallengeViewModel, FragmentUpcomingBinding>(),
    UpcomingNavigator {

    private lateinit var upcomingChallengeAdapter: UpcomingChallengeAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return bind(UpcomingChallengeViewModel::class.java, inflater, container)
    }

    override fun onResume() {
        super.onResume()
        viewModel?.getUpcomingChallengesList()
    }

    override fun onBackPress() {
    }

    override fun onViewModelCreated(viewModel: UpcomingChallengeViewModel) {
        viewModel.setNavigator(this)
    }

    override fun onViewBound(viewDataBinding: FragmentUpcomingBinding) {
        val list = mutableListOf<ChallengesDetails>()

        upcomingChallengeAdapter = UpcomingChallengeAdapter(list, true) {
            onItemClick()
        }


        upcomingChallengeAdapter.setHasStableIds(true)
        viewDataBinding.activeChallengeRecycler.adapter = upcomingChallengeAdapter
        viewDataBinding.activeChallengeRecycler.layoutManager = LinearLayoutManager(activity)
        viewDataBinding.activeChallengeRecycler.setDivider(R.drawable.divider)
        addObserver()
    }

    private fun addObserver() {
        val viewModel = viewModel ?: return
        viewModel.upcomingChallengeLiveList.observe(this, Observer {
            upcomingChallengeAdapter.setItems(it)
        })
    }


    fun onItemClick() {
        startActivity(
            intentProviderFactory.create(
                ChallengeDetailActivity::class.java,
                null,
                0
            )
        )
    }

}