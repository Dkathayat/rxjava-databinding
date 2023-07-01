package com.yewapp.ui.modules.addassociatememberdetails.fragment.sportsimages

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yewapp.data.network.api.profile.SportType
import com.yewapp.databinding.ItemAssociateSportsTypeBinding
import com.yewapp.ui.base.BaseRecyclerAdapter

class AssociateSportsTypeAdapter(
    val sportList: MutableList<SportType>,
    val listener: ItemAssociateSportTypeViewModel.OnItemClickListener
) : BaseRecyclerAdapter<AssociateSportsTypeAdapter.ViewHolder, SportType>(sportList) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemAssociateSportsTypeBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(ItemAssociateSportTypeViewModel(sportList[position], position, listener))
    }

    fun addItems(list: List<SportType>) {
        sportList.clear()
        sportList.addAll(list)
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ItemAssociateSportsTypeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: ItemAssociateSportTypeViewModel) {
            binding.viewModel = viewModel
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
}