package com.yewapp.ui.modules.addchallenge.challengeroutes.popular

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.yewapp.R
import com.yewapp.data.network.api.challenges.ChallengeModel
import com.yewapp.data.network.api.invitemember.InviteMember
import com.yewapp.data.network.api.routes.Route
import com.yewapp.databinding.PopularFragmentBinding
import com.yewapp.ui.base.BaseFragment
import com.yewapp.ui.modules.listner.ChallengeExtras
import com.yewapp.ui.modules.routedetail.RouteDetailActivity
import com.yewapp.ui.modules.addchallenge.challengeroutes.popular.adapter.PopularRoutesAdapter
import com.yewapp.ui.modules.addchallenge.challengeroutes.popular.vm.ItemPopularRouteViewModel

class PopularRoutesFragment(override val layoutId: Int = R.layout.popular_fragment) :
    BaseFragment<PopularRoutesNavigator, PopularRoutesViewModel, PopularFragmentBinding>(),
    PopularRoutesNavigator {

    private lateinit var popularRoutesAdapter: PopularRoutesAdapter


    companion object {
        fun newInstance(challengeModel: ChallengeModel): PopularRoutesFragment {
            //can not send without bundle else will give null pointer error
            val fragment = PopularRoutesFragment()
            val args = Bundle()
            args.putParcelable(ChallengeExtras.CHALLENGE_DATA, challengeModel)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel?.getPopularRoutesList()
    }

    override fun onViewModelCreated(viewModel: PopularRoutesViewModel) {
        viewModel.setNavigator(this)
    }

    override fun onViewBound(viewDataBinding: PopularFragmentBinding) {
        adapterInitialize()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return bind(PopularRoutesViewModel::class.java, inflater, container)
    }

    override fun onBackPress() {
    }


    override fun onStart() {
        super.onStart()
        addObserver()
    }

    private fun addObserver() {
        val viewModel = viewModel ?: return
        viewModel.popularRouteList.observe(this, Observer {
            popularRoutesAdapter.setItems(it)
        })
    }

    private fun adapterInitialize() {
        var list = mutableListOf<Route>()
        popularRoutesAdapter =
            PopularRoutesAdapter(list, object : ItemPopularRouteViewModel.OnItemClickListener {

                override fun onClickItem(item: Route) {
//                    val extras = RouteExtras.routeExtras {
//                        id = item.id!!
//                    }

                    val extra = ChallengeExtras.challengeExtras {
                        challengeData = ChallengeModel(
                            viewModel?.challengeModel?.isEdit?:false,
                            viewModel?.challengeModel?.challengeID ?:"",
                            3,
                            viewModel?.challengeModel?.selectedSportId,
                            viewModel?.challengeModel?.selectedSportImage,
                            viewModel?.challengeModel?.profileType,
                            viewModel?.challengeModel?.sportsName,
                            viewModel?.challengeModel?.startDate,
                            viewModel?.challengeModel?.endDate,
                            2,//for popular route
                            item.id.toString(),// route id
                            "",
                            "",
                            "",
                            arrayListOf(),
                            arrayListOf(),
                            "",
                            "",
                            arrayListOf(),
                            "",
                            "",
                            0.0,
                            0.0,
                            "",
                            viewModel?.challengeModel?.country?:"",
                            viewModel?.challengeModel?.countryId?:0,
                            viewModel?.challengeModel?.state?:"",
                            viewModel?.challengeModel?.stateId?:0,
                            arrayListOf(),
                            arrayListOf(),
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            arrayListOf<InviteMember>()
                        )
                    }

                    startActivity(
                        intentProviderFactory.create(
                            RouteDetailActivity::class.java, extra, 0
                        )
                    )
                }
            })
        viewDataBinding.popularRecycler.adapter = popularRoutesAdapter
//        viewDataBinding.popularRecycler!!.addOnScrollListener(viewModel!!.onScrollListener)
    }

}