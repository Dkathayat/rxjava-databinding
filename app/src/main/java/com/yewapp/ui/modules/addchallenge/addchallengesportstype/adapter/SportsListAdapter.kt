package com.yewapp.ui.modules.addchallenge.addchallengesportstype.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yewapp.data.network.api.sports.Sport
import com.yewapp.databinding.ItemSportListBinding
import com.yewapp.ui.base.BaseRecyclerAdapter
import com.yewapp.ui.modules.addchallenge.addchallengesportstype.vm.ItemSportListViewModel

class SportsListAdapter(
    private val sportList: MutableList<Sport>,
    val onItemClickListener: ItemSportListViewModel.OnItemClickListener
) :
    BaseRecyclerAdapter<SportsListAdapter.ViewHolder, Sport>(sportList) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemSportListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(ItemSportListViewModel(sportList[position], onItemClickListener, position))

    }

    class ViewHolder(val binding: ItemSportListBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: ItemSportListViewModel) {
            binding.viewModel = viewModel
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return sportList.size
    }
}