package com.yewapp.ui.modules.profile.fragment.activities

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.yewapp.R
import com.yewapp.data.network.api.profile.*
import com.yewapp.databinding.FragmentActivitiesBinding
import com.yewapp.ui.base.BaseFragment
import com.yewapp.ui.dialogs.ConfirmationCommonDialog
import com.yewapp.ui.modules.dashboard.fragment.feeds.FeedsLikedActivity
import com.yewapp.ui.modules.dashboard.fragment.feeds.extras.FeedExtras
import com.yewapp.ui.modules.profile.fragment.bottomsheet.FilterActivityBottomSheet
import com.yewapp.ui.modules.profile.fragment.bottomsheet.DateFilterActivitesBottomSheet
import com.yewapp.ui.modules.profile.navigator.ActivitiesNavigator
import com.yewapp.utils.DateUtils
import com.yewapp.utils.toJson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ActivitiesFragment(override val layoutId: Int = R.layout.fragment_activities) :
    BaseFragment<ActivitiesNavigator, ActivitiesViewModel, FragmentActivitiesBinding>(),
    ActivitiesNavigator, OnActivityOptionClickListener {

    private val chart: LineChart by lazy {
        viewDataBinding.chart
    }
    lateinit var adapter: ActivitiesListAdapter

    override fun onViewModelCreated(viewModel: ActivitiesViewModel) {
        viewModel.setNavigator(this)
        viewModel.selectedFilterDate.set("Filter :Start Date - End Date")

//        lifecycleScope.launch(Dispatchers.IO){
//            delay(500)
//            withContext(Dispatchers.Main){
//                configureLineChart()
//            }
//
//        }

    }

    override fun onViewBound(viewDataBinding: FragmentActivitiesBinding) {
//        viewModel?.sendFilterData("","")//narbir
        addObserver()
        val list = mutableListOf<ActivitiesData>()
        adapter = ActivitiesListAdapter(
            list, this,
        )
        viewDataBinding.rvActivities.adapter = adapter
    }

    override fun configureLineChart() {

        val xvalue = ArrayList<String>()

        for (i in viewModel!!.yvalueChart){
            xvalue.add(DateUtils.getDateFromTimeStamp(i.toLong()).substring(0,6))
            //xvalue.add(i.toString().substring(3,5))
        }

        val lineEntry = ArrayList<Entry>()

        for ((index, value) in viewModel?.xvalueChart!!.withIndex()) {
            lineEntry.add(Entry(value.toFloat(), index))

        }


        val linerDataSet = LineDataSet(lineEntry, "DataSet")
        linerDataSet.color = ContextCompat.getColor(requireContext(), R.color.black)
        linerDataSet.circleRadius = 7f
        linerDataSet.setDrawFilled(true)
        linerDataSet.fillFormatter
        linerDataSet.setDrawCircles(true)
        linerDataSet.setDrawCubic(true)
        linerDataSet.setCircleColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
        linerDataSet.cubicIntensity = 0.2f
        linerDataSet.setDrawCircleHole(false)
        linerDataSet.lineWidth = 1f

        linerDataSet.fillColor =
            ContextCompat.getColor(requireContext(), R.color.challenge_selected_bg)


        val data = LineData(xvalue, linerDataSet)
        chart.data = data
        chart.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
        chart.animateXY(3000, 3000)
    }

    override fun onFeedLikeSuccess(activitiesData: ActivitiesData) {
        adapter.updateLikedItem(activitiesData,viewModel?.selectedItem ?: return)
    }

    override fun onSuccessActivityShared(message: String) {
        onSuccess(message)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return bind(ActivitiesViewModel::class.java, inflater, container)

    }

    private fun addObserver() {
        viewModel?.activityData?.observe(this, Observer {
            if (it != null) {
                adapter.setItems(it)
            }

        })
    }


    override fun navigateToDateRangePicker() {
        viewModel?.filterList?.let {
            DateFilterActivitesBottomSheet.Builder(requireContext())
                .setTitle("Choose a Start & End Date")
                .setFilterList(it)
                .setFilterButton { startDate, endDate ->
                    viewModel!!.startDate.set(startDate)
                    viewModel!!.endDate.set(endDate)
                    viewModel?.sendFilterData(startDate, endDate)
                    viewModel!!.selectedFilterDate.set("Filter : $startDate - $endDate")
                }
                .build().show(requireContext())
        }

    }

    override fun navigateToApplyFilter() {

        FilterActivityBottomSheet.Builder()
            .setTitle("Filter")
            .setFilterList(viewModel?.filterList ?: return)
            .setFilterButton {
                for (i in it) {
                    if (i.filterDatatype == "all") {
                        viewModel?.filterType?.add("all")
                    } else if (i.isSelected) {
                        viewModel?.filterType?.add(i.filterDatatype)
                    }
                }
                viewModel!!.sendFilterData("", "")
                viewModel?.selectedFilterDate?.set("Filter :Start Date - End Date")
            }.build().show(requireContext())

    }

    override fun onBackPress() {

    }

    override fun onResume() {
        super.onResume()
        viewModel?.sendFilterData("", "")
        viewModel?.selectedFilterDate?.set("Filter :Start Date - End Date")
        viewModel?.startDate?.set(null)
        viewModel?.endDate?.set(null)
        //configureLineChart()
    }

    override fun onPause() {
        super.onPause()
        viewModel?.yvalueChart?.clear()
        viewModel?.yvalueChart?.clear()
    }

    override fun onOptionMenuClick(option: String, filterResponse: ActivitiesData, position: Int) {
        ConfirmationCommonDialog.Builder()
            .setDescription("Share the activity in your feed")
            .setTitle("Share Feed")
            .addPositiveListener {
                viewModel?.shareActivity(filterResponse)
            }.build().show(requireActivity())

    }

    override fun onCommentClick(id: Int, position: Int, filterResponse: ActivitiesData) {

    }

    override fun onShareFeedClick(id: Int) {

    }

    override fun onLikeClick(filterResponse: ActivitiesData) {
//        if (filterResponse.likeCount >= 1) {
//            startActivity(
//                intentProviderFactory.create(
//                    FeedsLikedActivity::class.java,
//                    FeedExtras.feedExtras {
//                        feedId = filterResponse.activityId.toInt()
//                    },
//                    0
//                )
//            )
//        }

    }

    override fun onLikeLongClick(id: String, filterResponse: ActivitiesData, position: Int) {
//        viewModel?.selectedItem = position
//        adapter.updateLikedItem(filterResponse,position)
//        viewModel?.postActivityLike(filterResponse,"U+1F603")

    }

}
