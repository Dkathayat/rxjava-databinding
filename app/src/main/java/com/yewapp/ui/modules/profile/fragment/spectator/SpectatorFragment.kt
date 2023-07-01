package com.yewapp.ui.modules.profile.fragment.spectator

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.yewapp.R
import com.yewapp.data.network.api.associate.Associate
import com.yewapp.data.network.api.spectator.SpectatorMember
import com.yewapp.databinding.FragmentSpectatorBinding
import com.yewapp.ui.base.BaseFragment
import com.yewapp.ui.common.RemoveAccountBottomSheet
import com.yewapp.ui.modules.addspectator.AddSpectatorActivity
import com.yewapp.ui.modules.addspectator.extras.AddSpectatorExtras
import com.yewapp.ui.modules.chat.ChatActivity
import com.yewapp.ui.modules.chat.extras.ChatActivityExtras

class SpectatorFragment(override val layoutId: Int = R.layout.fragment_spectator) :
    BaseFragment<SpectatorNavigator, SpectatorViewModel, FragmentSpectatorBinding>(),
    SpectatorNavigator, ItemSpectator.OnSpectatorOptionClickListener {

    private lateinit var spectatorAdapter: SpectatorAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return bind(SpectatorViewModel::class.java, inflater, container)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onResume() {
        super.onResume()
        if (isVisible && isAdded)
            viewModel?.getSpectatorMember()
    }

    override fun onViewModelCreated(viewModel: SpectatorViewModel) {
        viewModel.setNavigator(this)
    }

    override fun onViewBound(viewDataBinding: FragmentSpectatorBinding) {
        adapterInitialize()
        addObserver()
    }

    private fun addObserver() {
        val viewModel = viewModel ?: return
        viewModel.memberListLiveData.observe(this, Observer {
            spectatorAdapter.setItems(it)
        })
    }


    private fun adapterInitialize() {
        val list = mutableListOf<SpectatorMember>()
        spectatorAdapter = SpectatorAdapter(
            list, this
        )
        viewDataBinding.spectatorRecycler.adapter = spectatorAdapter
//        viewDataBinding.popularRecycler!!.addOnScrollListener(viewModel!!.onScrollListener)
    }


    override fun onBackPress() {

    }

    override fun onRemoveAccountClicked(associateItem: SpectatorMember) {
        val title = viewModel?.dataManager?.getResourceProvider()
            ?.getString(R.string.remove_account)

        val message = viewModel?.dataManager?.getResourceProvider()
            ?.getString(R.string.remove_spectator_message)
        RemoveAccountBottomSheet.Builder()
            .setTitle(title ?: return)
            .setMessage(message ?: return)
            .setButtonRemove {
                viewModel?.removeSpectatorMember(associateItem)
            }

            .build().show(requireActivity())

    }

    override fun onChatClicked(item: SpectatorMember) {
        /**
         * @author: Narbir Singh
         * @description:
         * Case:1
         *         We Pass Spectator item data as Associate data object for common chat process
         */

        val extrasSpectatorDetails = ChatActivityExtras.associateDetailsExtras {
            associateDetails = Associate(
                "N/A",
                "N/A",
                item.email,
                "${item.city}, ${item.country}",
                item.fullName,
                item.profileImage,
                "Spectator",
                "1",
                item.userId
            )
            isAssociate = false
        }
        startActivity(
            intentProviderFactory.create(
                ChatActivity::class.java, extrasSpectatorDetails, 0
            )
        )
    }

    override fun navigateAddSpectator() {
        val extras = AddSpectatorExtras.addSpectatorExtras {
            spectatorCount = viewModel?.mutableMemberList?.size ?: return
        }
        startActivity(
            intentProviderFactory.create(
                AddSpectatorActivity::class.java, extras, 0
            )
        )
    }
}