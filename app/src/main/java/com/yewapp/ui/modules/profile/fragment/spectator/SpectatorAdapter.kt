package com.yewapp.ui.modules.profile.fragment.spectator

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yewapp.data.network.api.spectator.SpectatorMember
import com.yewapp.databinding.ItemSpectatorBinding
import com.yewapp.ui.base.BaseRecyclerAdapter


class SpectatorAdapter(
    private val spectatorList: MutableList<SpectatorMember>,
    val onSpectatorOptionClickListener: ItemSpectator.OnSpectatorOptionClickListener,
//    val onItemClickListener: ItemAssociateMember.OnItemClickListener
) :
    BaseRecyclerAdapter<SpectatorAdapter.ViewHolder, SpectatorMember>(spectatorList) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemSpectatorBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(
            ItemSpectator(
                spectatorList[position],
                onSpectatorOptionClickListener,
                position
            )
        )

    }


    class ViewHolder(val binding: ItemSpectatorBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(viewModel: ItemSpectator) {
            binding.viewModel = viewModel
        }
    }

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }
}