package com.yewapp.ui.modules.addchallenge.addchallengesportstype

import android.widget.Toast
import androidx.lifecycle.Observer
import com.yewapp.R
import com.yewapp.data.network.api.challenges.ChallengeModel
import com.yewapp.data.network.api.invitemember.InviteMember
import com.yewapp.data.network.api.sports.Sport
import com.yewapp.databinding.ActivityAddChallengeBinding
import com.yewapp.ui.base.BaseActivity
import com.yewapp.ui.modules.addchallenge.addchallengesportstype.adapter.SportsListAdapter
import com.yewapp.ui.modules.addchallenge.addchallengesportstype.vm.ItemSportListViewModel
import com.yewapp.ui.modules.listner.ChallengeExtras
import com.yewapp.ui.modules.addchallenge.addchallengeselectrangedate.SelectRangeDateActivity

//Todo:Add Challenge Step 1
class AddChallengeActivity :
    BaseActivity<AddChallengeNavigator, AddChallengeViewModel, ActivityAddChallengeBinding>(),
    AddChallengeNavigator {

    private lateinit var sportsListAdapter: SportsListAdapter


    override fun getLayout(): Int {
        return R.layout.activity_add_challenge
    }

    override fun init() {
        bind(AddChallengeViewModel::class.java)
    }

    override fun onViewModelCreated(viewModel: AddChallengeViewModel) {
        viewModel.setNavigator(this)
    }

    override fun onViewBound(viewDataBinding: ActivityAddChallengeBinding) {
        adapterInitialize()
    }


    override fun navigateToastMessage() {
        Toast.makeText(this, R.string.please_select_challenge_before_proceed, Toast.LENGTH_LONG)
            .show()
    }

    override fun saveAsDraft() {
        if (viewModel.selectedSportId != 0) {
            viewModel.saveAsDraft()
        } else {
            navigateToastMessage()
        }

    }

    override fun onStart() {
        super.onStart()
        addObserver()
    }

    private fun addObserver() {
        val viewModel = viewModel ?: return
        viewModel.sportListLive.observe(this, Observer {
            sportsListAdapter.setItems(it)
        })
    }

    private fun adapterInitialize() {
        val list = mutableListOf<Sport>()
        sportsListAdapter =
            SportsListAdapter(list, object : ItemSportListViewModel.OnItemClickListener {

                override fun onClickItem(item: Sport, index: Int) {
                    viewModel.selectedSportId = item.id?.toInt() ?: return

                    //Set Data on Item click
                    viewModel.challengeModel =
                        ChallengeModel(
                            false,
                            "",
                            1,
                            item.id.toInt(),
                            item.icon,
                            item.profileType,
                            item.name,
                            "",
                            "",
                            0,
                            item.id,// Parent Sports id
                            "", "", "InActive", arrayListOf(), arrayListOf(), "",
                            "", arrayListOf(), "", "", 0.0, 0.0, "",
                            "", 0, "", 0, arrayListOf(), arrayListOf(),
                            "", "", "", "", "",
                            "", "", "", "", "",
                            "", "", "", "", "", "",
                            arrayListOf<InviteMember>()
                        )

                    viewModel.isBtnActive.set(true)
                    for (i in viewModel.sportList.indices) {
                        viewModel.sportList[i].isSelected = index == i
                    }
                    viewModel._sportsList.value = viewModel.sportList

                }
            })
        sportsListAdapter.setHasStableIds(true)
        viewDataBinding.sportsRecycler.adapter = sportsListAdapter
    }

    override fun navigateToSelectDateRange() {
        startActivity(
            intentProviderFactory.create(
                SelectRangeDateActivity::class.java,
                ChallengeExtras.challengeExtras {
                    challengeData = viewModel.challengeModel
                },
                0
            )
        )
//        val challengeExtras = ChallengeExtras()
//        challengeExtras.setChallengeId(1)
//        Repository.getInstance().getChallengeProgress().value = challengeExtras

    }
}