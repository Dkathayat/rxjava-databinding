package com.yewapp.ui.modules.addchallenge.addchallengedetails.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yewapp.data.network.api.addmodelequipment.EquipmentData
import com.yewapp.data.network.api.sports.Sport
import com.yewapp.databinding.ItemChallengeSportsEquipmentBinding
import com.yewapp.ui.base.BaseRecyclerAdapter

class ChallengeSportsEquipmentAdapter(
    private val sportList: MutableList<EquipmentData>,
    val onItemClickListener: ItemChallengeSportsEquipmentViewModel.OnItemClickListener
) :
    BaseRecyclerAdapter<ChallengeSportsEquipmentAdapter.ViewHolder, EquipmentData>(sportList) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemChallengeSportsEquipmentBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(
            ItemChallengeSportsEquipmentViewModel(
                sportList[position],
                onItemClickListener,
                position
            )
        )

    }

    class ViewHolder(val binding: ItemChallengeSportsEquipmentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: ItemChallengeSportsEquipmentViewModel) {
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