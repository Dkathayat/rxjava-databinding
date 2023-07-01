package com.yewapp.ui.modules.dashboard.fragment.challenges.fragments.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yewapp.data.network.api.challenges.ChallengesDetails
import com.yewapp.databinding.LayoutFavoriteChallengeItemBinding
import com.yewapp.ui.base.BaseRecyclerAdapter

class FavoriteChallengeAdapter(
    val list: MutableList<ChallengesDetails>,
    val flag: Boolean,
    val listener: () -> Unit?
) : BaseRecyclerAdapter<FavoriteChallengeAdapter.ChallengesFavoriteViewHolder, ChallengesDetails>(
    list
) {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ChallengesFavoriteViewHolder =
        ChallengesFavoriteViewHolder(
            LayoutFavoriteChallengeItemBinding
                .inflate(
                    LayoutInflater
                        .from(parent.context), parent, false
                )
        )

    override fun onBindViewHolder(holder: ChallengesFavoriteViewHolder, position: Int) {
        holder.bind(ItemFavoriteChallengeViewModel(list[position], flag, listener))
    }


    class ChallengesFavoriteViewHolder(
        private val binding: LayoutFavoriteChallengeItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: ItemFavoriteChallengeViewModel) {
            binding.viewModel = viewModel

            //Setting the user progress in challenge6
            val userDistance = (viewModel.activeChallengeDetails.userDistance)?.toInt() ?: return
            val distance = (viewModel.activeChallengeDetails.minimumMiles)?.toInt() ?: return
            binding.progressHorizontal.progress = (userDistance * 100) / distance

        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
}