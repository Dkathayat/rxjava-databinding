package com.yewapp.ui.modules.createroute

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.yewapp.data.network.api.challenges.ChallengesDetails
import com.yewapp.data.network.api.routes.MapViewTypeModel
import com.yewapp.databinding.ItemMapViewTypeBinding
import com.yewapp.ui.base.BaseRecyclerAdapter
import com.yewapp.ui.modules.createroute.MapBoxViewTypeAdapter.MapBoxViewHolder

class MapBoxViewTypeAdapter(
    val mapViewTypeList: MutableList<MapViewTypeModel>,
    val mapbox_access_token: String?,
    val onClickListener: ItemMapTypeViewModel.OnItemClickListener

) : BaseRecyclerAdapter<MapBoxViewHolder, MapViewTypeModel>(mapViewTypeList) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        MapBoxViewHolder(
            ItemMapViewTypeBinding
                .inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
        )


    override fun onBindViewHolder(holder: MapBoxViewHolder, position: Int) {
        holder.bind(
            ItemMapTypeViewModel(
                mapViewTypeList[position],
                onClickListener,
                mapbox_access_token
            )
        )
    }


    class MapBoxViewHolder(val binding: ItemMapViewTypeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(viewModel: ItemMapTypeViewModel) {
            binding.viewModel = viewModel
        }
    }

}