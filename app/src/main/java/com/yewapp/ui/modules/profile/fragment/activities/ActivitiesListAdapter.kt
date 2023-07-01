package com.yewapp.ui.modules.profile.fragment.activities

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yewapp.data.network.api.feed.Feed
import com.yewapp.data.network.api.profile.ActivitiesData
import com.yewapp.data.network.api.profile.Activity
import com.yewapp.databinding.ItemActivityBinding
import com.yewapp.ui.base.BaseRecyclerAdapter

class ActivitiesListAdapter(
    private val activityData: MutableList<ActivitiesData>,
    private val onActivityListner: OnActivityOptionClickListener,

    ) :
    BaseRecyclerAdapter<ActivitiesListAdapter.ViewHolder, ActivitiesData>(activityData) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemActivityBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(
            ItemActivityViewModel(
                position,
                activityData[position],
                onActivityListner
            )
        )
    }

    class ViewHolder(val binding: ItemActivityBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(viewModel: ItemActivityViewModel) {
            binding.viewModel = viewModel
        }
    }

    fun updateLikedItem(activitiesData: ActivitiesData,position: Int) {
        activityData[position] = activitiesData
        notifyItemChanged(position)
    }
}
