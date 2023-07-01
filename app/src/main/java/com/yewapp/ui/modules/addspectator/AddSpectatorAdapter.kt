package com.yewapp.ui.modules.addspectator

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yewapp.data.network.api.spectator.YewMember
import com.yewapp.databinding.ItemAddSpectatorMemberBinding
import com.yewapp.ui.base.BaseRecyclerAdapter

class AddSpectatorAdapter(
    private val yewMemberList: MutableList<YewMember>,
    val onSpectatorOptionClickListener: ItemAddSpectatorMember.OnSpectatorOptionClickListener,
) :
    BaseRecyclerAdapter<AddSpectatorAdapter.ViewHolder, YewMember>(yewMemberList) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemAddSpectatorMemberBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(
            ItemAddSpectatorMember(
                yewMemberList[position],
                onSpectatorOptionClickListener,
                position
            )
        )

    }


    class ViewHolder(val binding: ItemAddSpectatorMemberBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(viewModel: ItemAddSpectatorMember) {
            binding.viewModel = viewModel
        }
    }

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }
}