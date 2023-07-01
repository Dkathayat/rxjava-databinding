package com.yewapp.ui.modules.profile.fragment.mypoints

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.lifecycle.Observer
import com.yewapp.R
import com.yewapp.data.network.api.mypoint.MyPointsHistoryDetails
import com.yewapp.data.network.api.mypoint.UserPointSummaryData
import com.yewapp.databinding.FragmentMyPointsBinding
import com.yewapp.ui.base.BaseFragment
import com.yewapp.ui.dialogs.ConfirmationCommonDialog
import com.yewapp.ui.modules.profile.adapter.MyPointsHistoryAdapter
import com.yewapp.ui.modules.profile.fragment.mypoints.filteractivity.MyPointsFilterActivity
import com.yewapp.ui.modules.profile.navigator.MyPointsNavigator

class MyPointsFragment(override val layoutId: Int = R.layout.fragment_my_points) :
    BaseFragment<MyPointsNavigator, MyPointsViewModel, FragmentMyPointsBinding>(),
    MyPointsNavigator, OnActivityPointOptionClickListener {

    lateinit var adapter: MyPointsFragmentAdapter
    lateinit var pointsHistoryAdapter:MyPointsHistoryAdapter

    override fun onViewModelCreated(viewModel: MyPointsViewModel) {
        viewModel.setNavigator(this)
    }
    override fun onViewBound(viewDataBinding: FragmentMyPointsBinding) {
        addObserver()
        initializeAdapter()
        viewModel?.getUserSummary("","")

        viewDataBinding.deviceSyncSwitchCompat.setOnCheckedChangeListener { compoundButton: CompoundButton, isEnabled: Boolean ->
            if (isEnabled) {
                viewModel?.getUserPointHistoryData("","", listOf(), listOf())
                viewModel?.activityDataSync?.set(true)
                viewDataBinding.rvActivitiesPoints.visibility = View.VISIBLE
                viewDataBinding.rvActivities.visibility = View.GONE

            } else {
                viewModel?.activityDataSync?.set(false)
                viewDataBinding.rvActivitiesPoints.visibility = View.GONE
                viewDataBinding.rvActivities.visibility = View.VISIBLE
            }
        }

    }

    override fun onResume() {
        super.onResume()
        viewModel?.getProfileSportsList()
        if (isVisible && isAdded)
            viewModel?.getMyPointApi()
        addObserver()

    }

    private fun initializeAdapter(){
        val list = mutableListOf<UserPointSummaryData>()
        adapter = MyPointsFragmentAdapter(list, this)
        viewDataBinding.rvActivities.adapter = adapter

        val pointList = mutableListOf<MyPointsHistoryDetails>()
        pointsHistoryAdapter = MyPointsHistoryAdapter(pointList)
        viewDataBinding.rvActivitiesPoints.adapter = pointsHistoryAdapter


    }

    private fun addObserver() {
        viewModel?.myPointsData?.observe(this, Observer {
            adapter.clearItem()
            adapter.setItems(it)
            adapter.notifyDataSetChanged()


        })

        viewModel?.userPointsHistory?.observe(this, Observer {
            pointsHistoryAdapter.clearItem()
            pointsHistoryAdapter.setItems(it)
            pointsHistoryAdapter.notifyDataSetChanged()
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return bind(MyPointsViewModel::class.java, inflater, container)
    }

    override fun navigateToFilterActivity() {
        startActivity(
            intentProviderFactory.create(MyPointsFilterActivity::class.java, null, 0)
        )
    }

    override fun onBackPress() {

    }

    override fun onOptionMenuClick(
        option: String,
        filterResponse: UserPointSummaryData,
        position: Int
    ) {
        ConfirmationCommonDialog.Builder()
            .setDescription("Share your points activity in your feed")
            .setTitle("Share Feed")
            .addPositiveListener {
                viewModel?.sharePointsActivity(filterResponse)
            }.build().show(requireActivity())
    }

    override fun onCommentClick(id: Int, position: Int, filterResponse: UserPointSummaryData) {

    }

    override fun onShareFeedClick(id: Int) {

    }

    override fun onLikeClick(filterResponse: UserPointSummaryData) {

    }

    override fun onLikeLongClick(id: String, filterResponse: UserPointSummaryData, position: Int) {

    }

    override fun checkProfileCompletion(): Boolean {
        return true
    }

}