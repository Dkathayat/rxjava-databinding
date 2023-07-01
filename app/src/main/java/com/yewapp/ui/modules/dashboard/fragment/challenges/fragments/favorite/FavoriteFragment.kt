package com.yewapp.ui.modules.dashboard.fragment.challenges.fragments.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.yewapp.R
import com.yewapp.data.network.api.challenges.ChallengesDetails
import com.yewapp.databinding.FragmentFavoriteBinding
import com.yewapp.ui.base.BaseFragment
import com.yewapp.ui.modules.challengeDetails.ChallengeDetailActivity
import com.yewapp.utils.setDivider

class FavoriteFragment(override val layoutId: Int = R.layout.fragment_favorite) :
    BaseFragment<FavoriteNavigator, FavoriteChallengeViewModel, FragmentFavoriteBinding>(),
    FavoriteNavigator {

    private lateinit var favoriteChallengesAdapter: FavoriteChallengeAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return bind(FavoriteChallengeViewModel::class.java, inflater, container)
    }

    override fun onResume() {
        super.onResume()
        viewModel?.getFavoriteChallengesList()
    }

    override fun onBackPress() {
    }

    override fun onViewModelCreated(viewModel: FavoriteChallengeViewModel) {
        viewModel.setNavigator(this)
    }

    override fun onViewBound(viewDataBinding: FragmentFavoriteBinding) {
        val list = mutableListOf<ChallengesDetails>()

        favoriteChallengesAdapter = FavoriteChallengeAdapter(list, true) {
            onItemClick()
        }


        favoriteChallengesAdapter.setHasStableIds(true)
        viewDataBinding.activeChallengeRecycler.adapter = favoriteChallengesAdapter
        viewDataBinding.activeChallengeRecycler.layoutManager = LinearLayoutManager(activity)
        viewDataBinding.activeChallengeRecycler.setDivider(R.drawable.divider)
        addObserver()
    }

    private fun addObserver() {
        val viewModel = viewModel ?: return
        viewModel.favoriteChallengeLiveList.observe(this, Observer {
            favoriteChallengesAdapter.setItems(it)
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