package com.yewapp.ui.modules.profile.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yewapp.data.network.api.mypoint.MyPointsHistoryDetails
import com.yewapp.databinding.ItemMypointsHistoryBinding
import com.yewapp.ui.base.BaseRecyclerAdapter
import com.yewapp.utils.DateUtils.Companion.getDateFromUTCDateTime
import com.yewapp.utils.getDateTime
import com.yewapp.utils.getFeedTime

class MyPointsHistoryAdapter(
    private val pointHistoryData: MutableList<MyPointsHistoryDetails>
) : BaseRecyclerAdapter<MyPointsHistoryAdapter.ViewHolder, MyPointsHistoryDetails>(pointHistoryData) {

    inner class ViewHolder(val binding: ItemMypointsHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
            fun bind(pointsHistoryDetails: MyPointsHistoryDetails){
                binding.title.text = pointsHistoryDetails.title
                binding.description.text = pointsHistoryDetails.description
                binding.date.text = pointsHistoryDetails.created_at
                binding.totalPoints.text = pointsHistoryDetails.points

            }

    }

    fun clearItem(){
        pointHistoryData.clear()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemMypointsHistoryBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(pointHistoryData[position])
    }
}