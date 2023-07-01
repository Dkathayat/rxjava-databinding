package com.yewapp.ui.modules.addchallenge.invitemember.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yewapp.data.network.api.invitemember.InviteMember
import com.yewapp.databinding.ItemInviteMemberBinding
import com.yewapp.ui.base.BaseRecyclerAdapter
import com.yewapp.ui.modules.addchallenge.invitemember.vm.ItemInviteMemberViewModel

class InviteMemberAdapter(
    private val routeList: MutableList<InviteMember>,
    val onItemClickListener: ItemInviteMemberViewModel.OnItemClickListener
) :
    BaseRecyclerAdapter<InviteMemberAdapter.ViewHolder, InviteMember>(routeList) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemInviteMemberBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(ItemInviteMemberViewModel(routeList[position], onItemClickListener, position))
    }


    class ViewHolder(val binding: ItemInviteMemberBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: ItemInviteMemberViewModel) {
            binding.viewModel = viewModel
        }
    }

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }

    fun updateItem(inviteMember: InviteMember, position: Int) {
        routeList[position] = inviteMember
        notifyItemChanged(position)
    }
}