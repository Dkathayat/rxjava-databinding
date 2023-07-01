package com.yewapp.ui.modules.addchallenge.addchallengedetails

import android.view.View
import androidx.lifecycle.Observer
import com.google.android.material.chip.Chip
import com.yewapp.R
import com.yewapp.data.network.api.addmodelequipment.EquipmentData
import com.yewapp.data.network.api.challenges.ChallengeModel
import com.yewapp.data.network.api.createchallenge.StaticMultipleSelection
import com.yewapp.databinding.ChallengeLocationDetailActivityBinding
import com.yewapp.ui.base.BaseActivity
import com.yewapp.ui.dialogs.challengepopupdialogs.*
import com.yewapp.ui.modules.addchallenge.addchallengedetails.adapter.ChallengeSportsEquipmentAdapter
import com.yewapp.ui.modules.addchallenge.addchallengedetails.adapter.ItemChallengeSportsEquipmentViewModel
import com.yewapp.ui.modules.addchallenge.challengeadditionaldetail.ChallengeAdditionalDetailActivity
import com.yewapp.ui.modules.listner.ChallengeExtras
import com.yewapp.ui.modules.listner.Repository

//Todo:Add Challenge Step 4
class ChallengeLocationDetailActivity :
    BaseActivity<ChallengeLocationDetailNavigator, ChallengeLocationDetailViewModel, ChallengeLocationDetailActivityBinding>(),
    ChallengeLocationDetailNavigator {

    lateinit var challengeSportsEquipmentAdapter: ChallengeSportsEquipmentAdapter

    private var subSportView: Sequence<View>? = null
    override fun getLayout(): Int {
        return R.layout.challenge_location_detail_activity
    }

    override fun init() {
        bind(ChallengeLocationDetailViewModel::class.java)
    }

    override fun onViewModelCreated(viewModel: ChallengeLocationDetailViewModel) {
        viewModel.setNavigator(this)
    }

    override fun onViewBound(viewDataBinding: ChallengeLocationDetailActivityBinding) {
        Repository.getInstance().getChallengeProgress().observe(this) {
            viewModel.challengeExtras = it
        }

        adapterInitialize()
        addObserver()
    }


    private fun addObserver() {
        val viewModel = viewModel ?: return
        viewModel.sportsEquipmentListLive.observe(this, Observer {
            challengeSportsEquipmentAdapter.setItems(it)
        })
    }


    private fun adapterInitialize() {
        val list = mutableListOf<EquipmentData>()
        challengeSportsEquipmentAdapter =
            ChallengeSportsEquipmentAdapter(
                list,
                object : ItemChallengeSportsEquipmentViewModel.OnItemClickListener {

                    override fun onClickItem(item: EquipmentData, index: Int) {
                        viewModel.sportsEquipmentList[index] = item
                        viewModel._sportsEquipmentList.value = viewModel.sportsEquipmentList
                    }
                })
        challengeSportsEquipmentAdapter.setHasStableIds(true)
        viewDataBinding.sportsEquipmentRecycler.adapter = challengeSportsEquipmentAdapter
    }


//    private fun addSubSportLayout() {
//        val customView =
//            layoutInflater.inflate(R.layout.item_challenge_specefic_sports_equipment, null)
//        viewDataBinding.sportsLayoutContainer.addView(customView)
//        specificSportEquipmentViewList.add(customView)
//
//
//        for (i in 0 until specificSportEquipmentViewList.size) {
//            val deleteLayout =
//                specificSportEquipmentViewList[i].findViewById<ImageView>(R.id.deleteLayout)
//            deleteLayout?.setOnClickListener { it ->
//                viewDataBinding.sportsLayoutContainer.removeView(specificSportEquipmentViewList[i])
//            }
//
//            val etBrandMake =
//                specificSportEquipmentViewList[i].findViewById<TextInputEditText>(R.id.etBrandMake)
//            etBrandMake?.setOnClickListener { it ->
//                ChallengePopUp.Builder().addList(viewModel.subSportList).setListener {
//                    etBrandMake.setText(it.name.toString())
//                }.build().show(it)
//            }
//
//            val etModel =
//                specificSportEquipmentViewList[i].findViewById<TextInputEditText>(R.id.etModel)
//            etModel?.setOnClickListener { it ->
//                ChallengePopUp.Builder().addList(viewModel.subSportList).setListener {
//                    etBrandMake.setText(it.name.toString())
//                }.build().show(it)
//            }
//
//            val etWheelSize =
//                specificSportEquipmentViewList[i].findViewById<TextInputEditText>(R.id.etWheelSize)
//            etWheelSize?.setOnClickListener { it ->
//                ChallengePopUp.Builder().addList(viewModel.subSportList).setListener {
//                    etBrandMake.setText(it.name.toString())
//                }.build().show(it)
//            }
//            val etFrame =
//                specificSportEquipmentViewList[i].findViewById<TextInputEditText>(R.id.etFrame)
//            etFrame?.setOnClickListener { it ->
//                ChallengePopUp.Builder().addList(viewModel.subSportList).setListener {
//                    etBrandMake.setText(it.name.toString())
//                }.build().show(it)
//            }
//            val etYear =
//                specificSportEquipmentViewList[i].findViewById<TextInputEditText>(R.id.etYear)
//            etYear?.setOnClickListener { it ->
//                ChallengePopUp.Builder().addList(viewModel.subSportList).setListener {
//                    etBrandMake.setText(it.name.toString())
//                }.build().show(it)
//            }
//            val eOther =
//                specificSportEquipmentViewList[i].findViewById<TextInputEditText>(R.id.eOther)
//
//        }
//
//    }

    override fun navigateToAdditionalScreen() {
        val selectedSportsEquipments = arrayListOf<EquipmentData>()
        for (i in 0 until viewModel.sportsEquipmentList.size) {
            if (viewModel.sportsEquipmentList[i].isSelected)
                selectedSportsEquipments.add(viewModel.sportsEquipmentList[i])
        }

        if (viewModel.challengeModel.isEdit) {//true to edit challenge
            val extras =
                if (viewModel.subSportsVisibility.get()!! && viewModel.isEquipmentAvailable.get()!!)
                    ChallengeExtras.challengeExtras {
                        challengeData = ChallengeModel(
                            viewModel.challengeModel.isEdit,
                            viewModel.challengeModel.challengeID ?: "",
                            4,
                            viewModel.challengeModel.selectedSportId,
                            viewModel.challengeModel.selectedSportImage,
                            viewModel.challengeModel.profileType,
                            viewModel.challengeModel.sportsName,
                            viewModel.challengeModel.startDate,
                            viewModel.challengeModel.endDate,
                            2,//for popular route
                            viewModel.challengeModel.routeID,
                            viewModel.challengeName.get(),//challengeName
                            viewModel.selectedChallengeVisibility.get(),//challenge visibility
                            viewModel.challengeStatus.get(),//TODO:   challenge Status 1= active
                            viewModel.selectedSportsLevel,//challenge selectedSportsLevel
                            viewModel.selectedAgeGroupValue,//challenge selectedAgeGroupValue
                            viewModel.description.get(),//challenge description
                            viewModel.subSportID.get(),
                            selectedSportsEquipments,//selected sports Equipments
                            viewModel.challengeModel.challengeArea,
                            viewModel.challengeModel.location,
                            viewModel.challengeModel.latitude,
                            viewModel.challengeModel.longitude,
                            viewModel.challengeModel.radius,
                            viewModel.challengeModel.country,
                            viewModel.challengeModel.countryId,
                            viewModel.challengeModel.state,
                            viewModel.challengeModel.stateId,
                            viewModel.challengeModel.cityId,
                            viewModel.challengeModel.zipCode,
                            viewModel.challengeModel.calories,
                            viewModel.challengeModel.miles,
                            viewModel.challengeModel.elevationGain,
                            viewModel.challengeModel.avgWatt,
                            viewModel.challengeModel.time,
                            viewModel.challengeModel.maxMember,
                            viewModel.challengeModel.winnerPickedFrom,
                            viewModel.challengeModel.selectedWinnerPrize,
                            viewModel.challengeModel.overViewValue,
                            viewModel.challengeModel.winnerValue,
                            viewModel.challengeModel.additionalInfoValue,
                            viewModel.challengeModel.rulesValue,
                            viewModel.challengeModel.guidelinesValue,
                            viewModel.challengeModel.qualificationValue,
                            viewModel.challengeModel.bannerImage,
                            viewModel.challengeModel.challengeImage,
                            viewModel.challengeModel.inviteMembers
                        )
                    }
                else
                    ChallengeExtras.challengeExtras {
                        challengeData = ChallengeModel(
                            viewModel.challengeModel.isEdit,
                            viewModel.challengeModel.challengeID ?: "",
                            4,
                            viewModel.challengeModel.selectedSportId,
                            viewModel.challengeModel.selectedSportImage,
                            viewModel.challengeModel.profileType,
                            viewModel.challengeModel.sportsName,
                            viewModel.challengeModel.startDate,
                            viewModel.challengeModel.endDate,
                            2,//for popular route
                            viewModel.challengeModel.routeID,
                            viewModel.challengeName.get(),//challengeName
                            viewModel.selectedChallengeVisibility.get(),//challenge visibility
                            viewModel.challengeStatus.get(),//challenge Status value 1 or 0
                            viewModel.selectedSportsLevel,//challenge selectedSportsLevel
                            viewModel.selectedAgeGroupValue,//challenge selectedAgeGroupValue
                            viewModel.description.get(),//challenge description
                            viewModel.subSportID.get(),
                            arrayListOf(),
                            viewModel.challengeModel.challengeArea,
                            viewModel.challengeModel.location,
                            viewModel.challengeModel.latitude,
                            viewModel.challengeModel.longitude,
                            viewModel.challengeModel.radius,
                            viewModel.challengeModel.country,
                            viewModel.challengeModel.countryId,
                            viewModel.challengeModel.state,
                            viewModel.challengeModel.stateId,
                            viewModel.challengeModel.cityId,
                            viewModel.challengeModel.zipCode,
                            viewModel.challengeModel.calories,
                            viewModel.challengeModel.miles,
                            viewModel.challengeModel.elevationGain,
                            viewModel.challengeModel.avgWatt,
                            viewModel.challengeModel.time,
                            viewModel.challengeModel.maxMember,
                            viewModel.challengeModel.winnerPickedFrom,
                            viewModel.challengeModel.selectedWinnerPrize,
                            viewModel.challengeModel.overViewValue,
                            viewModel.challengeModel.winnerValue,
                            viewModel.challengeModel.additionalInfoValue,
                            viewModel.challengeModel.rulesValue,
                            viewModel.challengeModel.guidelinesValue,
                            viewModel.challengeModel.qualificationValue,
                            viewModel.challengeModel.bannerImage,
                            viewModel.challengeModel.challengeImage,
                            viewModel.challengeModel.inviteMembers
                        )
                    }

            startActivity(
                intentProviderFactory.create(
                    ChallengeAdditionalDetailActivity::class.java, extras, 0
                )
            )

        } else {
            val extras =
                if (viewModel.subSportsVisibility.get()!! && viewModel.isEquipmentAvailable.get()!!)
                    ChallengeExtras.challengeExtras {
                        challengeData = ChallengeModel(
                            viewModel.challengeModel.isEdit,
                            viewModel.challengeModel.challengeID ?: "",
                            4,
                            viewModel.challengeModel.selectedSportId,
                            viewModel.challengeModel.selectedSportImage,
                            viewModel.challengeModel.profileType,
                            viewModel.challengeModel.sportsName,
                            viewModel.challengeModel.startDate,
                            viewModel.challengeModel.endDate,
                            2,//for popular route
                            viewModel.challengeModel.routeID,
                            viewModel.challengeName.get(),//challengeName
                            viewModel.selectedChallengeVisibility.get(),//challenge visibility
                            viewModel.challengeStatus.get(),//TODO:   challenge Status 1= active
                            viewModel.selectedSportsLevel,//challenge selectedSportsLevel
                            viewModel.selectedAgeGroupValue,//challenge selectedAgeGroupValue
                            viewModel.description.get(),//challenge description
                            viewModel.subSportID.get(),
                            selectedSportsEquipments,//selected sports Equipments
                            "",
                            "",
                            0.0,
                            0.0,
                            "",
                            viewModel.challengeModel.country,
                            viewModel.challengeModel.countryId,
                            viewModel.challengeModel.state,
                            viewModel.challengeModel.stateId,
                            arrayListOf(),
                            arrayListOf(),
                            viewModel.challengeModel.calories,
                            viewModel.challengeModel.miles,
                            viewModel.challengeModel.elevationGain,
                            "",
                            viewModel.challengeModel.time,
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            arrayListOf()
                        )
                    }
                else
                    ChallengeExtras.challengeExtras {
                        challengeData = ChallengeModel(
                            viewModel.challengeModel.isEdit,
                            viewModel.challengeModel.challengeID ?: "",
                            4,
                            viewModel.challengeModel.selectedSportId,
                            viewModel.challengeModel.selectedSportImage,
                            viewModel.challengeModel.profileType,
                            viewModel.challengeModel.sportsName,
                            viewModel.challengeModel.startDate,
                            viewModel.challengeModel.endDate,
                            2,//for popular route
                            viewModel.challengeModel.routeID,
                            viewModel.challengeName.get(),//challengeName
                            viewModel.selectedChallengeVisibility.get(),//challenge visibility
                            viewModel.challengeStatus.get(),//challenge Status value active or Inactive
                            viewModel.selectedSportsLevel,//challenge selectedSportsLevel
                            viewModel.selectedAgeGroupValue,//challenge selectedAgeGroupValue
                            viewModel.description.get(),//challenge description
                            viewModel.subSportID.get(),
                            arrayListOf(),
                            "",
                            "",
                            0.0,
                            0.0,
                            "",
                            viewModel.challengeModel.country,
                            viewModel.challengeModel.countryId,
                            viewModel.challengeModel.state,
                            viewModel.challengeModel.stateId,
                            arrayListOf(),
                            arrayListOf(),
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            arrayListOf()
                        )
                    }

            startActivity(
                intentProviderFactory.create(
                    ChallengeAdditionalDetailActivity::class.java, extras, 0
                )
            )
        }
    }

    override fun onChallengeVisibilityClick(view: View) {
        hideKeyboard()
        // showChallengeVisibilityPopUp()
        ChallengeSingleSelectionPopUp.Builder().addList(viewModel.challengeVisibilityArray.toList())
            .setListener {
                viewModel.selectedChallengeVisibility.set(it)
            }.build().show(view)
    }

    override fun onSportsLevelClick(view: View) {
        hideKeyboard()
        //  showSportLevelPopUp()
        ChallengeMultipleSelectionPopUp.Builder().addList(viewModel.sportsLevelList)
            .setUpdatedListener { updateReceivedList ->

                viewModel.selectedSportsLevel.clear()
                viewModel.sportsLevelList =
                    updateReceivedList as MutableList<StaticMultipleSelection>
                viewModel.selectedSportsLevelDisplay.set("Sports Level")

//                for (i in updateReceivedList.indices)
//                    if (updateReceivedList[i].status) {
////                        addChips(updateReceivedList[i].name.toString())
//                        viewModel.selectedSportsLevel.add(updateReceivedList[i].name.toString())
//                    }
                viewDataBinding.chipSpotsLevel.removeAllViews()//remove all views of chips

                for (i in updateReceivedList.indices) {
                    if (updateReceivedList[i].status) {
                        viewModel.selectedSportsLevel.add(updateReceivedList[i].name.toString())
                        addSportsLevelChips(
                            updateReceivedList[i].name ?: return@setUpdatedListener
                        )
                    }
                }
            }.build().show(view)


//        TODO:Sport level single Selection
//        ChallengeSingleSelectionPopUp.Builder()
//            .addList(viewModel.sportLevelArray.toList())
//            .setListener {
//                viewModel.selectedSportsLevel.set(it)
//            }
//            .build()
//            .show(view)

    }

    override fun onAgeGroupClick(view: View) {
        hideKeyboard()
        //Multiple selection with select all options
        ChallengeMultipleSelectionPopUpWIthSelectAll.Builder().addList(viewModel.ageGroupList)
            .setUpdatedListener {
                viewModel.selectedAgeGroupValue.clear()
                viewModel.ageGroupList = it as MutableList<StaticMultipleSelection>
                viewModel.ageGroup.set(
                    viewModel.dataManager.getResourceProvider().getString(R.string.age_group)
                )

                viewDataBinding.chipGroup.removeAllViews()//remove all views of chips
                if (it[0].status) {
                    for (element in it) {
                        if (!element.name?.contains("All")!!)
                            viewModel.selectedAgeGroupValue.add(
                                element.name.replace(">50", "50")
                            )
                        addChips(element.name ?: return@setUpdatedListener)
                    }
                } else {
                    for (i in it.indices) {
                        if (i > 0 && it[i].status) {
                            viewModel.selectedAgeGroupValue.add(
                                it.get(i).name ?: return@setUpdatedListener
                            )
                            addChips(it[i].name ?: return@setUpdatedListener)
                        }
                    }
                }
            }.build().show(view)
        //showAgeGroupPopUp()
    }

    override fun onSubSportClick(view: View) {
        hideKeyboard()
        ChallengePopUp.Builder().addList(viewModel.subSportList).setListener {
            viewModel.selectedSubSport = it
            viewModel.subSportName.set(it.name)
            viewModel.subSportID.set(it.id)
            //TODO": Get saved user sports equipment based on selected sub sports
            viewModel.filterEquipmentBySubSports(it.id?.toInt() ?: return@setListener)
        }.build().show(view)
    }

    override fun onModelClick(view: View) {
        hideKeyboard()
        ChallengeModelPopUp.Builder().addList(viewModel.modelList).setListener {
            viewModel.modelValue.set(it.modelName)
        }.build().show(view)
        //showModelPopUp()
    }

    override fun onStatusClick(view: View) {
        hideKeyboard()
        ChallengeSingleSelectionPopUp.Builder().addList(viewModel.statusVisibilityArray.toList())
            .setListener {
                viewModel.challengeStatus.set(it)

            }.build().show(view)
    }

//    override fun saveAsDraft() {
//        hideKeyboard()
//        viewModel.saveAsDraft()
//    }

//    override fun onAddIconClick() {
//        hideKeyboard()
//        var childcount: Int = viewDataBinding.sportsLayoutContainer.childCount
//        if (childcount < 6) {
//            addSubSportLayout()
//        } else {
//            Toast.makeText(this, "You can not add more than 5 sub sports", Toast.LENGTH_LONG).show()
//        }
//    }

/*
    private fun showSportLevelPopUp() {
        // Initialize alert dialog
        val builder =
            AlertDialog.Builder(this@ChallengeLocationDetailActivity, R.style.AlertDialogStyle)
        // set title
        builder.setTitle("Select Sports Level");
        // set dialog non cancelable
        builder.setCancelable(true);
        builder.setMultiChoiceItems(
            viewModel.sportLevelArray,
            viewModel.selectedSport,
            object : DialogInterface.OnMultiChoiceClickListener {

                override fun onClick(p0: DialogInterface?, position: Int, b: Boolean) {
                    if (b) {
                        // when checkbox selected
                        // Add position  in lang list
                        viewModel.sportList.add(position)
                        // Sort array list
                        viewModel.sportList.sort()
                    } else {
                        // when checkbox unselected
                        // Remove position from langList
                        viewModel.sportList.remove(Integer.valueOf(position));
                    }
                }

            })
        builder.setPositiveButton("OK", object : DialogInterface.OnClickListener {
            override fun onClick(p0: DialogInterface?, position: Int) {
                // Initialize string builder
                val stringBuilder = StringBuilder()
                // use for loop
                for (j in 0 until viewModel.sportList.size) {
                    // concat array value
                    stringBuilder.append(viewModel.sportLevelArray[viewModel.sportList[j]])
                    // check condition
                    if (j != viewModel.sportList.size - 1) {
                        // When j value  not equal
                        // to lang list size - 1
                        // add comma
                        stringBuilder.append(", ")
                    }
                }
                viewModel.selectedSportsLevel.set(stringBuilder.toString())
            }

        })

        builder.setNegativeButton("Cancel", object : DialogInterface.OnClickListener {
            override fun onClick(dialogInterface: DialogInterface?, position: Int) {
                dialogInterface?.dismiss()
            }
        })
        // show dialog
        builder.show()
    }
*/


/*
    private fun showChallengeVisibilityPopUp() {
        // Initialize alert dialog
        val builder =
            AlertDialog.Builder(this@ChallengeLocationDetailActivity, R.style.AlertDialogStyle)
        // set title
        builder.setTitle("Select Challenge Visibility");
        // set dialog non cancelable
        builder.setCancelable(true)
        builder.setItems(viewModel.challengeVisibilityArray,
            object : DialogInterface.OnClickListener {
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    viewModel.selectedChallengeVisibility.set(viewModel.challengeVisibilityArray[p1])
                }
            })
        // show dialog
        builder.show()
    }
*/

/*
    private fun showAgeGroupPopUp() {
        // Initialize alert dialog
        val builder =
            AlertDialog.Builder(this@ChallengeLocationDetailActivity, R.style.AlertDialogStyle)
        // set title
        builder.setTitle("Select Age Group")
        // set dialog non cancelable
        builder.setCancelable(true)
        builder.setMultiChoiceItems(
            viewModel.ageGroupArray,
            viewModel.selectedAgeGroup,
            object : DialogInterface.OnMultiChoiceClickListener {

                override fun onClick(p0: DialogInterface?, position: Int, b: Boolean) {
                    if (b) {
                        // when checkbox selected
                        // Add position  in lang list
                        viewModel.ageGroupList.add(position)
                        // Sort array list
                        viewModel.ageGroupList.sort()

                    } else {
                        // when checkbox unselected
                        // Remove position from langList
                        viewModel.ageGroupList.remove(Integer.valueOf(position));
                    }
                }

            })
        builder.setPositiveButton("OK", object : DialogInterface.OnClickListener {
            override fun onClick(p0: DialogInterface?, position: Int) {
                // Initialize string builder
                val stringBuilder = StringBuilder()
                // use for loop
                for (j in 0 until viewModel.ageGroupList.size) {
                    // concat array value
                    stringBuilder.append(viewModel.ageGroupArray[viewModel.ageGroupList[j]])
                    // check condition
                    if (j != viewModel.ageGroupList.size - 1) {
                        // When j value  not equal
                        // to lang list size - 1
                        // add comma
                        stringBuilder.append(", ")
                    }
                    // viewDataBinding.chipGroup.removeAllViews()
                    addChips(viewModel.ageGroupArray[viewModel.ageGroupList[j]])
                }
                viewModel.selectedAgeGroupValue.set(stringBuilder.toString())
            }

        })

        builder.setNegativeButton("Cancel", object : DialogInterface.OnClickListener {
            override fun onClick(dialogInterface: DialogInterface?, position: Int) {
                dialogInterface?.dismiss()
            }
        })
        // show dialog
        builder.show()
    }
*/

    //For Sports Grade
    override fun addSportsLevelChips(name: String) {
        val newChip = this.layoutInflater.inflate(R.layout.food_chip_item, null, false) as Chip
        newChip.text = name
//        newChip.isCloseIconEnabled = true
//        newChip.isClickable = false
//        newChip.isFocusable = true
        viewDataBinding.chipSpotsLevel.addView(newChip)

    }

    //For age
    override fun addChips(name: String) {
        val newChip = this.layoutInflater.inflate(R.layout.food_chip_item, null, false) as Chip
        newChip.text = name
//        newChip.isCloseIconEnabled = true
//        newChip.isClickable = false
//        newChip.isFocusable = true
        viewDataBinding.chipGroup.addView(newChip)

    }

//    private fun showSubSportPopUp() {
//
//        // Initialize alert dialog
//        val builder =
//            AlertDialog.Builder(this@ChallengeLocationDetailActivity, R.style.AlertDialogStyle)
//        // set title
//        builder.setTitle("Select Sub Sport");
//        // set dialog non cancelable
//        builder.setCancelable(true)
//        // converting arraylist to array
//        var subsportList: ArrayList<String> = ArrayList()
//        val subsportListArray = arrayOfNulls<String>(viewModel.subSportList.size)
//
//        for (i in 0 until viewModel.subSportList.size) {
//            subsportListArray[i] = viewModel.subSportList[i].name.toString()
//        }
//        builder.setItems(subsportListArray, object : DialogInterface.OnClickListener {
//            override fun onClick(p0: DialogInterface?, p1: Int) {
//                viewModel.subSportName.set(subsportListArray[p1].toString())
//                viewModel.getSportModelList()
//            }
//        })
//        // show dialog
//        builder.show()
//    }
//
//    private fun showModelPopUp() {
//        // Initialize alert dialog
//        val builder =
//            AlertDialog.Builder(this@ChallengeLocationDetailActivity, R.style.AlertDialogStyle)
//        // set title
//        builder.setTitle("Select Model");
//        // set dialog non cancelable
//        builder.setCancelable(true)
//        // converting arraylist to array
//        val subsportListArray = arrayOfNulls<String>(viewModel.modelList.size)
//
//        for (i in 0 until viewModel.modelList.size) {
//            subsportListArray[i] = viewModel.modelList[i].modelName.toString()
//        }
//        builder.setItems(subsportListArray, object : DialogInterface.OnClickListener {
//            override fun onClick(p0: DialogInterface?, p1: Int) {
//                viewModel.modelValue.set(subsportListArray[p1].toString())
//            }
//        })
//        // show dialog
//        builder.show()
//    }


    var addModelViewList = arrayListOf<View>()

//    override fun addModelLayout() {
////        val customView = layoutInflater.inflate(R.layout.item_add_sports_model, null)
////        viewDataBinding.sportsLayoutContainer.addView(customView)
////        addModelViewList.add(customView)
////
////        for (i in 0 until addModelViewList.size) {
////            //init variable
////            val deleteLayout = addModelViewList[i].findViewById<ImageView>(R.id.deleteLayout)
////            val etSubSports = addModelViewList[i].findViewById<TextInputEditText>(R.id.etSubSports)
////
////            val tvBrandMake = addModelViewList[i].findViewById<TextInputLayout>(R.id.tvBrandMake)
////            val etBrandMake = addModelViewList[i].findViewById<TextInputEditText>(R.id.etBrandMake)
////
////            val tvModel = addModelViewList[i].findViewById<TextInputLayout>(R.id.tvModel)
////            val etModel = addModelViewList[i].findViewById<TextInputEditText>(R.id.etModel)
////
////            val tvWheelSize = addModelViewList[i].findViewById<TextInputLayout>(R.id.tvWheelSize)
////            val etWheelSize = addModelViewList[i].findViewById<TextInputEditText>(R.id.etWheelSize)
////
////            val tvFrame = addModelViewList[i].findViewById<TextInputLayout>(R.id.tvFrame)
////            val etFrame = addModelViewList[i].findViewById<TextInputEditText>(R.id.etFrame)
////
////            val etYear = addModelViewList[i].findViewById<TextInputEditText>(R.id.etYear)
////            val etOther = addModelViewList[i].findViewById<TextInputEditText>(R.id.eOther)
////
////            val brandMakeList = arrayListOf<Brand>()
////            val modelList = arrayListOf<Model>()
////            val wheelSizeList = arrayListOf<WheelSize>()
////            val frameSizeList = arrayListOf<FrameSize>()
////            val yearList = arrayListOf<String>()
////
////            for (j in 0 until 20) {
////                yearList.add("${Calendar.getInstance().get(Calendar.YEAR) - j}")
////            }
////
////
////            //TODO: DELETE LAYOUT
////            deleteLayout?.setOnClickListener { it ->
////                viewDataBinding.sportsLayoutContainer.removeView(addModelViewList[i])
////                viewModel.addBrandMakeHashMap.remove(addModelViewList[i])
////                viewModel.addModelHashMap.remove(addModelViewList[i])
////                viewModel.addWheelSizeHashMap.remove(addModelViewList[i])
////                viewModel.addFrameHashMap.remove(addModelViewList[i])
////                viewModel.addYearHashMap.remove(addModelViewList[i])
////            }
////
////            //TODO: Used to chooser Sub sports category
////            etSubSports?.setOnClickListener { it ->
////                ChallengePopUp.Builder()
////                    .addList(viewModel.subSportList ?: return@setOnClickListener)
////                    .setListener { subSports ->
////                        etBrandMake.setText("")
////                        etModel.setText("")
////                        etWheelSize.setText("")
////                        etFrame.setText("")
////                        etYear.setText("")
////                        etOther.setText("")
////                        brandMakeList.clear()
////                        modelList.clear()
////                        wheelSizeList.clear()
////                        frameSizeList.clear()
////                        tvBrandMake.visibility = View.GONE
////                        tvModel.visibility = View.GONE
////                        tvWheelSize.visibility = View.GONE
////                        tvFrame.visibility = View.GONE
////
////                        val mappingID = addModelViewList[i]
////                        viewModel.addSubSportHashMap.put(
////                            mappingID, SubSport(
////                                subSports.gradeLevel ?: "",
////                                subSports.sportId ?: "",
////                                subSports.id ?: ""
////                            )
////                        )
////                        etSubSports.setText(subSports.name.toString())
////                        brandMakeList.clear()
////                        //initModel
////                        brandMakeList.addAll(subSports.brandList!!)
////                        if (subSports.brandList.isNotEmpty()) {
////                            tvBrandMake.visibility = View.VISIBLE
////                        } else {
////                            tvBrandMake.visibility = View.VISIBLE
////                        }
////                    }.build().show(it)
////            }
////
////            //TODO: Used to chooser Brand/Make
////            etBrandMake?.setOnClickListener { it ->
////                AddBrandPopUp.Builder().addList(brandMakeList).setListener { model ->
////                    etModel.setText("")
////                    etWheelSize.setText("")
////                    etFrame.setText("")
////                    etYear.setText("")
////                    etOther.setText("")
////                    modelList.clear()
////                    wheelSizeList.clear()
////                    frameSizeList.clear()
////                    tvModel.visibility = View.GONE
////                    tvWheelSize.visibility = View.GONE
////                    tvFrame.visibility = View.GONE
////                    val mappingID = addModelViewList[i]
////                    viewModel.addBrandMakeHashMap.put(mappingID, model)
////
////                    etBrandMake.setText(model.brandName)
////                    modelList.clear()
////                    modelList.addAll(model.modelList ?: return@setListener)
////                    if (!model.modelList.isEmpty()) {
////                        tvModel.visibility = View.VISIBLE
////                    } else {
////                        tvModel.visibility = View.GONE
////                    }
////                }.build().show(it)
////            }
////
////            //TODO: Used to chooser Model
////            etModel?.setOnClickListener { it ->
////                AddModelPopUp.Builder().addList(modelList).setListener { model ->
////                    etWheelSize.setText("")
////                    etFrame.setText("")
////                    etYear.setText("")
////                    etOther.setText("")
////                    wheelSizeList.clear()
////                    frameSizeList.clear()
////                    etModel.setText(model.modelName)
////                    wheelSizeList.clear()
////                    frameSizeList.clear()
////                    val mappingID = addModelViewList[i]
////                    viewModel.addModelHashMap.put(mappingID, model)
////
////                    tvWheelSize.visibility = View.GONE
////                    tvFrame.visibility = View.GONE
////
////                    wheelSizeList.addAll(model.wheelSizeList!!)
////                    frameSizeList.addAll(model.frameList!!)
////                    if (model.wheelSizeList.isNotEmpty()) {
////                        tvWheelSize.visibility = View.VISIBLE
////                    } else {
////                        tvWheelSize.visibility = View.GONE
////                    }
////                    if (!model.frameList.isEmpty()) {
////                        tvFrame.visibility = View.VISIBLE
////                    } else {
////                        tvFrame.visibility = View.GONE
////                    }
////                }.build().show(it)
////            }
////
////            //TODO: Used to chooser Wheel Size
////            etWheelSize?.setOnClickListener { it ->
////                AddWheelSizePopup.Builder().addList(wheelSizeList).setListener { wheel ->
////                    etWheelSize.setText(wheel.wheelSize.toString())
////                    val mappingID = addModelViewList[i]
////                    viewModel.addWheelSizeHashMap.put(mappingID, wheel)
////                }.build().show(it)
////            }
////
////            //TODO: Used to chooser Frame Size
////            etFrame?.setOnClickListener { it ->
////                AddFrameSizePopUp.Builder().addList(frameSizeList).setListener { frameSize ->
////                    etFrame.setText(frameSize.frameSize.toString())
////                    val mappingID = addModelViewList[i]
////                    viewModel.addFrameHashMap.put(mappingID, frameSize)
////                }.build().show(it)
////            }
////
////            //TODO: Used to chooser Year
////            etYear?.setOnClickListener { it ->
////                AddYearPopUp.Builder().addList(yearList).setListener { year ->
////                    etYear.setText(year)
////                    val mappingID = addModelViewList[i]
////                    viewModel.addYearHashMap.put(mappingID, year)
////                }.build().show(it)
////            }
////
////            //To show pre fill data if having
//////            if (viewModel?.isEdit?.get()!! && viewModel?.editEquipmentData?.get(i)?.type.equals(
//////                    "model",
//////                    ignoreCase = true
//////                )
//////            ) {
//////                val mappingID = addModelViewList[i]
//////                val subSportsPreSelected =
//////                    if (viewModel?.editSubSportData?.get(i)?.subSportId == viewModel?.subSportList?.get(
//////                            i
//////                        )?.id
//////                    )
//////                        viewModel?.subSportList?.get(i)
//////                    else
//////                        return
//////                viewModel?.addSubSportHashMap?.put(
//////                    mappingID, SubSport(
//////                        subSportsPreSelected?.gradeLevel ?: "",
//////                        subSportsPreSelected?.sportId ?: "",
//////                        subSportsPreSelected?.id ?: ""
//////                    )
//////                )
//////                etSubSports.setText(subSportsPreSelected?.name)
//////                brandMakeList.addAll(viewModel?.subSportList?.get(i)?.brandList ?: return)
//////                for (m in 0 until brandMakeList.size) {
//////                    val brandMake =
//////                        if (viewModel?.editEquipmentData?.get(i)?.brandMakeId == brandMakeList[i].brandId)
//////                            brandMakeList[i]
//////                        else
//////                            return
//////                    tvBrandMake.visibility = View.VISIBLE
//////                    etBrandMake.setText(brandMake.brandName)
//////                    viewModel?.addBrandMakeHashMap?.put(mappingID, brandMake)
//////
//////                }
//////                modelList.addAll(brandMakeList[i].modelList ?: return)
//////                for (k in 0 until modelList.size) {
//////                    val model =
//////                        if (viewModel?.editEquipmentData?.get(i)?.modelId == modelList[k].modelId)
//////                            modelList[k]
//////                        else
//////                            return
//////
//////                    tvModel.visibility = View.VISIBLE
//////                    etModel.setText(model.modelName)
//////                    viewModel?.addModelHashMap?.put(mappingID, model)
//////
//////                }
//////                wheelSizeList.addAll(modelList[i].wheelSizeList ?: return)
//////                for (k in 0 until wheelSizeList.size) {
//////                    val wheel =
//////                        if (viewModel?.editEquipmentData?.get(i)?.wheelSize == wheelSizeList[k].wheelSize)
//////                            wheelSizeList[k]
//////                        else
//////                            return
//////                    tvWheelSize.visibility = View.VISIBLE
//////                    etWheelSize.setText(wheel.wheelSize)
//////                    viewModel?.addWheelSizeHashMap?.put(mappingID, wheel)
//////                }
//////
//////                frameSizeList.addAll(modelList[i].frameList ?: return)
//////                for (k in 0 until frameSizeList.size) {
//////                    val frame =
//////                        if (viewModel?.editEquipmentData?.get(i)?.frame == frameSizeList[k].frameSize)
//////                            frameSizeList[k]
//////                        else
//////                            return
//////
//////                    tvFrame.visibility = View.VISIBLE
//////                    etFrame.setText(frame.frameSize)
//////                    viewModel?.addFrameHashMap?.put(mappingID, frame)
//////                }
//////                for (k in 0 until yearList.size) {
//////                    val year =
//////                        if (viewModel?.editEquipmentData?.get(i)?.year == yearList[k])
//////                            yearList[k]
//////                        else
//////                            return
//////                    etYear.setText(year)
//////                    viewModel?.addYearHashMap?.put(mappingID, year)
//////                }
//////                etOther.setText(viewModel?.editEquipmentData?.get(i)?.other)
//////                viewModel?.addOtherMap?.put(
//////                    mappingID,
//////                    viewModel?.editEquipmentData?.get(i)?.other ?: ""
//////                )
//////
//////            }
////        }
//    }

    override fun filterDataRequestToSend() {
//        for (i in 0 until addModelViewList.size) {
//            viewModel.requestSubSportData.add(
//                viewModel.addSubSportHashMap.get(addModelViewList[i]) ?: return
//            )
//            val sports = viewModel.addSubSportHashMap.get(addModelViewList[i])
//            val brandMake = viewModel.addBrandMakeHashMap.get(addModelViewList[i])
//            val model = viewModel.addModelHashMap.get(addModelViewList[i])
//            val wheel = viewModel.addWheelSizeHashMap.get(addModelViewList[i])
//            val frame = viewModel.addFrameHashMap.get(addModelViewList[i])
//            val year = viewModel.addYearHashMap.get(addModelViewList[i])
//            val other =
//                (addModelViewList[i].findViewById<TextInputEditText>(R.id.eOther).text).toString()
//
//            viewModel.requestEquipmentData.add(
//                EquipmentData(
//                    brandMake?.brandId ?: return,
//                    "",
//                    frame?.frameSize ?: "",
//                    model?.modelId ?: "",
//                    other,
//                    sports?.sportId ?: "",
//                    sports?.subSportId ?: "",
//                    "model",
//                    wheel?.wheelSize ?: "",
//                    year ?: ""
//                )
//            )
//        }
//        viewModel?.addModelEquipments()
    }

}