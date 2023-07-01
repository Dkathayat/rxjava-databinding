package com.yewapp.ui.modules.dashboard.fragment.challenges.fragments.past

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ObservableField
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.yewapp.R
import com.yewapp.data.network.api.challenges.ChallengesDetails
import com.yewapp.databinding.FragmentPastChallengeBinding
import com.yewapp.ui.base.BaseFragment
import com.yewapp.ui.modules.challengeDetails.ChallengeDetailActivity
import com.yewapp.utils.setDivider


class PastChallengeFragment(override val layoutId: Int = R.layout.fragment_past_challenge) :
    BaseFragment<PastChallengeNavigator, PastChallengeViewModel, FragmentPastChallengeBinding>(),
    PastChallengeNavigator {


    val noData = ObservableField<Boolean>(false)

    private lateinit var pastChallengeAdapter: PastChallengesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return bind(PastChallengeViewModel::class.java, inflater, container)
    }


    override fun onResume() {
        super.onResume()
        viewModel?.getPastChallengesList()
    }

    override fun onBackPress() {
    }

    override fun onViewModelCreated(viewModel: PastChallengeViewModel) {
        viewModel.setNavigator(this)
    }

    override fun onViewBound(viewDataBinding: FragmentPastChallengeBinding) {
        initializeAdapter()
    }

    private fun initializeAdapter() {
        val list = mutableListOf<ChallengesDetails>()
        pastChallengeAdapter = PastChallengesAdapter(list, true) {
            onItemClick()
        }
        pastChallengeAdapter.setHasStableIds(true)
        viewDataBinding.recyclerview.adapter = pastChallengeAdapter
        viewDataBinding.recyclerview.layoutManager = LinearLayoutManager(activity)
        viewDataBinding.recyclerview.setDivider(R.drawable.divider)
        addObserver()
    }

    private fun addObserver() {
        val viewModel = viewModel ?: return
        viewModel.pastChallengeLiveList.observe(this, Observer {
            pastChallengeAdapter.setItems(it)
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