package com.yewapp.ui.modules.profilesubsportstype

import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.yewapp.R
import com.yewapp.data.network.api.sports.Sport
import com.yewapp.databinding.ActivityProfileSubSportTypeBinding
import com.yewapp.ui.base.BaseActivity
import com.yewapp.ui.dialogs.challengepopupdialogs.ChallengePopUp
import com.yewapp.ui.modules.addmodelandequipments.AddModelEquipmentDetailsActivity
import com.yewapp.ui.modules.addmodelandequipments.extras.AddModelEquipmentDetailsExtras

class ProfileSubSportsTypeActivity :
    BaseActivity<ProfileSubSportsTypeNavigator, ProfileSubSportsTypeViewModel, ActivityProfileSubSportTypeBinding>(),
    ProfileSubSportsTypeNavigator {
    override fun getLayout(): Int = R.layout.activity_profile_sub_sport_type
    override fun init() {
        bind(ProfileSubSportsTypeViewModel::class.java)
    }

    override fun onViewModelCreated(viewModel: ProfileSubSportsTypeViewModel) {
        viewModel.setNavigator(this)
    }

    override fun onViewBound(viewDataBinding: ActivityProfileSubSportTypeBinding) {
    }


    private var subSportsLayout = arrayListOf<View>()
    var subSportCount = 0
    override fun addSubSportLayout() {
        subSportCount++
        val customView = layoutInflater.inflate(R.layout.item_sub_sports_add, null)
        viewDataBinding.subSportsContainer.addView(customView)
        subSportsLayout.add(customView)


        for (i in 0 until subSportsLayout.size) {
            val deleteLayout = subSportsLayout[i].findViewById<ImageView>(R.id.deleteLayout)
            val etGradeLevel = subSportsLayout[i].findViewById<TextInputEditText>(R.id.etGradeLevel)


            deleteLayout?.setOnClickListener { view ->
                subSportCount--
                viewModel.sportsHashMap.remove(subSportsLayout[i])
                viewDataBinding.subSportsContainer.removeView(subSportsLayout[i])

                if (viewModel.sportsHashMap.size != 0)
                    viewModel.isContinueActive.set(true)
                else
                    viewModel.isContinueActive.set(false)
                subSportsLayout.removeAt(i)
            }

            //popup to Show sub sports respect of selected sports type
            val etSubSports = subSportsLayout[i].findViewById<TextInputEditText>(R.id.etSubSports)
            etSubSports?.setOnClickListener { view ->
                val tempList = mutableListOf<Sport>()
                for (j in 0 until viewModel.subSportList.size) {
                    if (!viewModel.sportsHashMap.values.contains(viewModel.subSportList[j]))
                        tempList.add(viewModel.subSportList[j])
                }

                if (tempList.size == 0 && tempList.size != viewModel.subSportList.size) {
                    tempList.addAll(viewModel.subSportListForDropDown)
                }

                if (viewModel.sportsHashMap.size != viewModel.subSportList.size) {
                    ChallengePopUp.Builder().addList(tempList).setListener { selectedSports ->
                        etSubSports.setText(selectedSports.name.toString())
                        etGradeLevel.setText(selectedSports.gradeLevel)

                        val mappingID = subSportsLayout[i]
                        viewModel.sportsHashMap[mappingID] = selectedSports

                        if (viewModel.sportsHashMap.size != 0)
                            viewModel.isContinueActive.set(true)
                        else
                            viewModel.isContinueActive.set(false)
                    }.build().show(view)
                } else {
                    Toast.makeText(this, "No subsport available", Toast.LENGTH_SHORT).show()
                }
            }

            if (viewModel.isEdit.get() ?: return ) {
                for (k in 0 until viewModel.subSportList.size) {
                    if (viewModel.editSubSportData[i].subSportId == viewModel.subSportList[k].id
                    ){
                        val mappingID = subSportsLayout[i]
                        val subSportsPreSelected = viewModel.subSportList[k]
                        viewModel.sportsHashMap[mappingID] = subSportsPreSelected
                        etSubSports.setText(subSportsPreSelected.name.toString())
                        etGradeLevel.setText(subSportsPreSelected.gradeLevel)
                    }

                }
            }
        }
    }

    override fun addModelInProfile() {
        val selectedSubSports = arrayListOf<Sport>()
        for (i in 0 until viewModel.sportsHashMap.size) {
            selectedSubSports.add(viewModel.sportsHashMap[subSportsLayout[i]] ?: return)
        }
        val extras = AddModelEquipmentDetailsExtras.sportsAndSubSportsExtras {
            this.isEdit = viewModel.selectedSportType.isModelAdded ?: false
            this.associateID = viewModel.associateID
            this.profileImage = viewModel.profileImage
            this.profileCoverImage = viewModel.profileCoverImage
            this.selectedSportType = viewModel.selectedSportType
            this.selectedSubSports = selectedSubSports
        }
        startActivity(
            intentProviderFactory.create(
                AddModelEquipmentDetailsActivity::class.java,
                extras,
                0
            )
        )
    }

    override fun onAddIconClick() {
        hideKeyboard()
        val childCount: Int = viewDataBinding.subSportsContainer.childCount
        if (childCount < viewModel.subSportList.size) {
            viewModel.isEdit.set(false)
            addSubSportLayout()
        } else {
            Toast.makeText(
                this,
                "You can not add more than ${viewModel.subSportList.size} sub sports",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}