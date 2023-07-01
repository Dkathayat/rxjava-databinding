package com.yewapp.ui.modules.dashboard.fragment.challenges.fragments.active

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.yewapp.R
import com.yewapp.data.network.api.challenges.ChallengesDetails
import com.yewapp.databinding.FragmentActiveBinding
import com.yewapp.ui.base.BaseFragment
import com.yewapp.ui.modules.challengeDetails.ChallengeDetailActivity
import com.yewapp.utils.setDivider


class ActiveFragment(override val layoutId: Int = R.layout.fragment_active) :
    BaseFragment<ActiveNavigator, ActiveChallengeViewModel, FragmentActiveBinding>(),
    ActiveNavigator {

    private lateinit var activeChallengesAdapter: ActiveChallengesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return bind(ActiveChallengeViewModel::class.java, inflater, container)
    }

    override fun onResume() {
        super.onResume()
        viewModel?.getActiveChallengesList()
    }

    override fun onBackPress() {
    }


    override fun onViewModelCreated(viewModel: ActiveChallengeViewModel) {
        viewModel.setNavigator(this)
    }

    override fun onViewBound(viewDataBinding: FragmentActiveBinding) {
        val list = mutableListOf<ChallengesDetails>()

        activeChallengesAdapter = ActiveChallengesAdapter(list, true) {
            onItemClick()
        }


        activeChallengesAdapter.setHasStableIds(true)
        viewDataBinding.activeChallengeRecycler.adapter = activeChallengesAdapter
        viewDataBinding.activeChallengeRecycler.layoutManager = LinearLayoutManager(activity)
        viewDataBinding.activeChallengeRecycler.setDivider(R.drawable.divider)
        addObserver()
    }

    private fun addObserver() {
        val viewModel = viewModel ?: return
        viewModel.activeChallengeLiveList.observe(this, Observer {
            activeChallengesAdapter.setItems(it)
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