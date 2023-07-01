package com.yewapp.ui.modules.dashboard.fragment.challenges.fragments.created

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yewapp.data.network.api.challenges.ChallengesDetails
import com.yewapp.databinding.LayoutCreatedChallengeItemBinding
import com.yewapp.ui.base.BaseRecyclerAdapter

class CreatedByMeChallengesAdapter(
    val list: MutableList<ChallengesDetails>, val flag: Boolean,
    val listener: (Boolean,ChallengesDetails) -> Unit?
) :
    BaseRecyclerAdapter<CreatedByMeChallengesAdapter.ChallengesCreatedViewHolder, ChallengesDetails>(
        list
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChallengesCreatedViewHolder =
        ChallengesCreatedViewHolder(
            LayoutCreatedChallengeItemBinding
                .inflate(
                    LayoutInflater
                        .from(parent.context), parent, false
                )
        )


    override fun onBindViewHolder(holder: ChallengesCreatedViewHolder, position: Int) {
        holder.bind(ItemCreatedChallengeViewModel(list[position], flag, listener))
    }

    class ChallengesCreatedViewHolder(
        private val binding: LayoutCreatedChallengeItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: ItemCreatedChallengeViewModel) {
            binding.viewModel = viewModel

        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
}


