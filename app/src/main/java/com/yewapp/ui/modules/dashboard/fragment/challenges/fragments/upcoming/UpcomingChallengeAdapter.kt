package com.yewapp.ui.modules.dashboard.fragment.challenges.fragments.upcoming

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yewapp.data.network.api.challenges.ChallengesDetails
import com.yewapp.databinding.LayoutUpcomingChallengeItemBinding
import com.yewapp.ui.base.BaseRecyclerAdapter

class UpcomingChallengeAdapter(
    val list: MutableList<ChallengesDetails>,
    val flag: Boolean,
    val listener: () -> Unit?
) : BaseRecyclerAdapter<UpcomingChallengeAdapter.ChallengesUpcomingViewHolder, ChallengesDetails>(
    list
) {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ChallengesUpcomingViewHolder =
        ChallengesUpcomingViewHolder(
            LayoutUpcomingChallengeItemBinding
                .inflate(
                    LayoutInflater
                        .from(parent.context), parent, false
                )
        )

    override fun onBindViewHolder(holder: ChallengesUpcomingViewHolder, position: Int) {
        holder.bind(ItemUpcomingChallengeViewModel(list[position], flag, listener))
    }


    class ChallengesUpcomingViewHolder(
        private val binding: LayoutUpcomingChallengeItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: ItemUpcomingChallengeViewModel) {
            binding.viewModel = viewModel

//            //Setting the user progress in challenge6
//            val userDistance = (viewModel.activeChallengeDetails.userDistance)?.toInt() ?: return
//            val distance = (viewModel.activeChallengeDetails.minimumMiles)?.toInt() ?: return
//            binding.progressHorizontal.progress = (userDistance * 100) / distance

        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
}