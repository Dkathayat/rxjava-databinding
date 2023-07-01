package com.yewapp.ui.modules.profile.fragment.mypoints

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.yewapp.data.network.api.mypoint.UserPointSummaryData
import com.yewapp.databinding.ItemActivityPointsBinding
import com.yewapp.ui.base.BaseRecyclerAdapter

class MyPointsFragmentAdapter(
    private val pointSummaryData: MutableList<UserPointSummaryData>,
    private val onActivityListener: OnActivityPointOptionClickListener
) : BaseRecyclerAdapter<MyPointsFragmentAdapter.ViewHolder, UserPointSummaryData>(pointSummaryData) {


    inner class ViewHolder(val binding: ItemActivityPointsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(viewModel: ItemActivityPointsViewModel) {
            binding.viewModel = viewModel
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemActivityPointsBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    fun clearItem(){
        pointSummaryData.clear()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(
            ItemActivityPointsViewModel(
                position,
                pointSummaryData[position],
                onActivityListener
            )
        )
    }
}