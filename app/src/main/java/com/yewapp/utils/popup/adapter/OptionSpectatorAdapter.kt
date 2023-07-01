package com.yewapp.utils.popup.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yewapp.R
import com.yewapp.data.network.api.spectator.SpectatorMember
import com.yewapp.databinding.ItemSpectatorOptionsBinding
import com.yewapp.ui.base.BaseRecyclerAdapter
import com.yewapp.utils.popup.vm.ItemSpectatorOptionViewModel

class OptionSpectatorAdapter(val options: MutableList<String>,
                             val spectatorItem: SpectatorMember,
                             val listener: (String) -> Unit,
) : BaseRecyclerAdapter<OptionSpectatorAdapter.ViewHolder, String>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemSpectatorOptionsBinding.bind(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_spectator_options, parent, false
                )
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(
            ItemSpectatorOptionViewModel(
                options[position],
                position,
                spectatorItem,
                listener
            )
        )
    }

    class ViewHolder(val binding: ItemSpectatorOptionsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: ItemSpectatorOptionViewModel) {
            binding.viewModel = viewModel
        }
    }
}