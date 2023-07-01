package com.yewapp.ui.modules.profile.fragment.mypoints.filteractivity

import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yewapp.R
import com.yewapp.data.network.api.mypoint.MyPointsHistoryDetails
import com.yewapp.data.network.api.refer.PhoneContacts
import com.yewapp.databinding.ItemUserPointsFilterActivityBinding
import com.yewapp.ui.base.BaseRecyclerAdapter
import com.yewapp.ui.modules.profile.fragment.mypoints.MyPointsViewModel

class UserPointsFilterAdapter(
    val list: MutableList<MyPointsHistoryDetails>,
    val listner: ItemMyPointsFilterViewModel.OnFilterItemClickListener
) : BaseRecyclerAdapter<UserPointsFilterAdapter.ViewHolder, MyPointsHistoryDetails>(list) {


    inner class ViewHolder(val binding: ItemUserPointsFilterActivityBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(viewModel: ItemMyPointsFilterViewModel) {
            binding.viewModel = viewModel
            binding.tvNameFilter.text = viewModel.myPointsHistoryDetails.title
        }

    }
    override fun getItemCount(): Int {
        return list.size
    }

    fun clearItem(){
        for(i in list){
            i.isSelected =false
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemUserPointsFilterActivityBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(
            ItemMyPointsFilterViewModel(
                list[position],
                listner
            )
        )
    }
}