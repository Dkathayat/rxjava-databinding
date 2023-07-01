package com.yewapp.ui.modules.profile.fragment.associate

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yewapp.data.network.api.associate.Associate
import com.yewapp.databinding.ItemAssociateMemberBinding
import com.yewapp.ui.base.BaseRecyclerAdapter

class AssociateMemberAdapter(
    var routeList: MutableList<Associate>,
    val onAssociateOptionClickListener: ItemAssociateMember.OnAssociateOptionClickListener,
//    val onItemClickListener: ItemAssociateMember.OnItemClickListener
) :
    BaseRecyclerAdapter<AssociateMemberAdapter.ViewHolder, Associate>(routeList) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemAssociateMemberBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(
            ItemAssociateMember(
                routeList[position],
                onAssociateOptionClickListener,
                position
            )
        )
    }


    class ViewHolder(val binding: ItemAssociateMemberBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(viewModel: ItemAssociateMember) {
            binding.viewModel = viewModel
        }
    }


    fun addItem(list: List<Associate>) {
        routeList.clear()
        routeList.addAll(list)
        Log.d("ASSOCIATE", "addItem ${list.size}")
        notifyItemRangeChanged(0,list.size)
    }

    override fun getItemCount(): Int {
        return routeList.size
    }
}