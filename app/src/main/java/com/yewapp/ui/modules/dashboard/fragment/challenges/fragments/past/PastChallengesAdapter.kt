package com.yewapp.ui.modules.dashboard.fragment.challenges.fragments.past

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yewapp.data.network.api.challenges.ChallengesDetails
import com.yewapp.databinding.LayoutPastchallengeItemBinding
import com.yewapp.ui.base.BaseRecyclerAdapter

class PastChallengesAdapter(
    val list: MutableList<ChallengesDetails>,
    val flag: Boolean,
    val listener: () -> Unit?
) : BaseRecyclerAdapter<PastChallengesAdapter.ChallengesPastViewHolder, ChallengesDetails>(
    list
) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChallengesPastViewHolder =
        ChallengesPastViewHolder(
            LayoutPastchallengeItemBinding
                .inflate(
                    LayoutInflater
                        .from(parent.context), parent, false
                )
        )


    override fun onBindViewHolder(holder: ChallengesPastViewHolder, position: Int) {
        holder.bind(ItemPastChallengeViewModel(list[position], flag, listener))
    }

    class ChallengesPastViewHolder(
        private val binding: LayoutPastchallengeItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: ItemPastChallengeViewModel) {
            binding.viewModel = viewModel

        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
}
