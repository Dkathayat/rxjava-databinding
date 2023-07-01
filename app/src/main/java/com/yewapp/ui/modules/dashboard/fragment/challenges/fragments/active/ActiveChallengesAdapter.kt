package com.yewapp.ui.modules.dashboard.fragment.challenges.fragments.active

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yewapp.data.network.api.challenges.ChallengesDetails
import com.yewapp.databinding.LayoutActivechallengeItemBinding
import com.yewapp.ui.base.BaseRecyclerAdapter

class ActiveChallengesAdapter(
    val list: MutableList<ChallengesDetails>,
    val flag: Boolean,
    val listener: () -> Unit?
) :
    BaseRecyclerAdapter<ActiveChallengesAdapter.ChallengesActiveViewHolder, ChallengesDetails>(
        list
    ) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ChallengesActiveViewHolder =
        ChallengesActiveViewHolder(
            LayoutActivechallengeItemBinding
                .inflate(
                    LayoutInflater
                        .from(parent.context), parent, false
                )
        )

    override fun onBindViewHolder(holder: ChallengesActiveViewHolder, position: Int) {
        holder.bind(ItemActiveChallengeViewModel(list[position], flag, listener))
    }


    class ChallengesActiveViewHolder(
        private val binding: LayoutActivechallengeItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: ItemActiveChallengeViewModel) {
            binding.viewModel = viewModel

            val userDistance = (viewModel.activeChallengeDetails.userDistance)?.toInt() ?: return
            val distance = (viewModel.activeChallengeDetails.minimumMiles)?.toInt() ?: return
//            val userChallengeProgress =
            binding.progressHorizontal.progress = (userDistance * 100) / distance

        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
}