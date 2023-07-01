package com.yewapp.ui.modules.addchallenge.challengeroutes.popular.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yewapp.data.network.api.routes.Route
import com.yewapp.databinding.RouteListingItemBinding
import com.yewapp.ui.base.BaseRecyclerAdapter
import com.yewapp.ui.modules.addchallenge.challengeroutes.popular.vm.ItemPopularRouteViewModel

class PopularRoutesAdapter(
    private val routeList: MutableList<Route>,
    val onItemClickListener: ItemPopularRouteViewModel.OnItemClickListener
) :
    BaseRecyclerAdapter<PopularRoutesAdapter.ViewHolder, Route>(routeList) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            RouteListingItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(ItemPopularRouteViewModel(routeList[position], onItemClickListener, position))

    }


    class ViewHolder(val binding: RouteListingItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: ItemPopularRouteViewModel) {
            binding.viewModel = viewModel
        }
    }

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }
}