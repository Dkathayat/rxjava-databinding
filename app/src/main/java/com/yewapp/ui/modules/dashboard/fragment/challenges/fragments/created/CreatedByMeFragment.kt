package com.yewapp.ui.modules.dashboard.fragment.challenges.fragments.created

//import com.yewapp.utils.RecyclerViewItemDivider
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.yewapp.R
import com.yewapp.data.network.api.challenges.ChallengeModel
import com.yewapp.data.network.api.challenges.ChallengesDetails
import com.yewapp.data.network.api.invitemember.InviteMember
import com.yewapp.databinding.FragmentCreatedbymeBinding
import com.yewapp.ui.base.BaseFragment
import com.yewapp.ui.modules.addchallenge.addchallengesportstype.AddChallengeActivity
import com.yewapp.ui.modules.addchallenge.challengepreview.ChallengePreviewActivity
import com.yewapp.ui.modules.addchallenge.selectorcreateroute.SelectORCreateRouteActivity
import com.yewapp.ui.modules.challengeDetails.ChallengeDetailActivity
import com.yewapp.ui.modules.listner.ChallengeExtras
import com.yewapp.ui.modules.listner.ChallengeExtrasEdit
import com.yewapp.utils.setDivider

class CreatedByMeFragment(override val layoutId: Int = R.layout.fragment_createdbyme) :
    BaseFragment<CreatedByMeNavigator, CreatedByMeViewModel, FragmentCreatedbymeBinding>(),
    CreatedByMeNavigator {

    private lateinit var createdByMeChallengesAdapter: CreatedByMeChallengesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return bind(CreatedByMeViewModel::class.java, inflater, container)

    }

    override fun onResume() {
        super.onResume()
        viewModel?.getCreatedChallengesList()
    }

    override fun onBackPress() {

    }


    override fun onViewModelCreated(viewModel: CreatedByMeViewModel) {
        viewModel.setNavigator(this)
    }

    override fun onViewBound(viewDataBinding: FragmentCreatedbymeBinding) {
        initializeAdapter()
    }

    private fun initializeAdapter() {
        val list = mutableListOf<ChallengesDetails>()
        createdByMeChallengesAdapter = CreatedByMeChallengesAdapter(list, false)
        { isDraftClicked, challengeItem ->
            if (isDraftClicked)
                onDraftItemClick(challengeItem)
            else
                onItemClick()
        }

        createdByMeChallengesAdapter.setHasStableIds(true)
        viewDataBinding.recyclerview.adapter = createdByMeChallengesAdapter
        viewDataBinding.recyclerview.layoutManager = LinearLayoutManager(activity)
        viewDataBinding.recyclerview.setDivider(R.drawable.divider)
        addObserver()
    }

    private fun addObserver() {
        val viewModel = viewModel ?: return
        viewModel.createdChallengeLiveList.observe(this, Observer {
            createdByMeChallengesAdapter.setItems(it)
        })
    }


    fun onItemClick() {
        startActivity(
            intentProviderFactory.create(
                ChallengeDetailActivity::class.java,
                null,
                0
            )
        )
    }

    private fun onDraftItemClick(challengesDetails: ChallengesDetails) {
        val extras = ChallengeExtrasEdit.challengeExtras {
            this.challengeID = challengesDetails.id
        }
        startActivity(
            intentProviderFactory.create(
                SelectORCreateRouteActivity::class.java, extras, 0
            )
        )
    }
}
