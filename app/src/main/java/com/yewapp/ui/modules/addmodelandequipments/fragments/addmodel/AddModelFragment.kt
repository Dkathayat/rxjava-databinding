package com.yewapp.ui.modules.addmodelandequipments.fragments.addmodel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.yewapp.R
import com.yewapp.data.network.api.addmodelequipment.EquipmentData
import com.yewapp.data.network.api.addmodelequipment.SubSport
import com.yewapp.data.network.api.sports.*
import com.yewapp.databinding.FragmentAddModelBinding
import com.yewapp.ui.base.BaseFragment
import com.yewapp.ui.dialogs.addbrandmake.AddBrandPopUp
import com.yewapp.ui.dialogs.addmodel.AddModelPopUp
import com.yewapp.ui.dialogs.addyear.AddYearPopUp
import com.yewapp.ui.dialogs.challengepopupdialogs.ChallengePopUp
import com.yewapp.ui.dialogs.framesize.AddFrameSizePopUp
import com.yewapp.ui.dialogs.wheelsize.AddWheelSizePopup
import com.yewapp.ui.modules.addmodelandequipments.AddModelEquipmentDetailsActivity
import com.yewapp.ui.modules.dashboard.DashboardActivity
import java.util.*

class AddModelFragment(override val layoutId: Int = R.layout.fragment_add_model) :
    BaseFragment<AddModelNavigator, AddModelViewModel, FragmentAddModelBinding>(),
    AddModelNavigator {


    companion object {
        fun newInstance(extras: Bundle?): AddModelFragment {
            //can not send without bundle else will give null pointer error
            val fragment = AddModelFragment()
//            val args = Bundle()
            fragment.arguments = extras
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return bind(AddModelViewModel::class.java, inflater, container)
    }


    override fun onViewModelCreated(viewModel: AddModelViewModel) {
        viewModel.setNavigator(this)
    }

    override fun onViewBound(viewDataBinding: FragmentAddModelBinding) {
    }

    override fun onBackPress() {
    }



    override fun onAddIconClick() {
        hideKeyboard()
        val childCount: Int = viewDataBinding.modelLayoutContainer.childCount
//        if (childCount < (viewModel?.subSportList?.size ?: 0)) {
        if (((activity as AddModelEquipmentDetailsActivity?)?.modelEquipmentDataCount
                ?: 0) < 5
        ) {
            viewModel?.isEdit?.set(false)
            addModelLayout()
        } else {
            Toast.makeText(
                requireContext(),
                "You can not add more than 5 sub sports",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    var addModelViewList = arrayListOf<View>()
    private val subSportsList = HashMap<View, List<Sport>>()
    private val brandMakeList = HashMap<View, List<Brand>>()
    private val modelList = HashMap<View, List<Model>>()
    private val wheelSizeList = HashMap<View, List<WheelSize>>()
    private val frameSizeList = HashMap<View, List<FrameSize>>()
    private val yearList = HashMap<View, List<String>>()

    override fun addModelLayout() {
        if (((activity as AddModelEquipmentDetailsActivity?)?.modelEquipmentDataCount
                ?: 0) >= 5
        ) return

        (activity as AddModelEquipmentDetailsActivity?)?.modelEquipmentDataCount =
            (activity as AddModelEquipmentDetailsActivity?)?.modelEquipmentDataCount!! + 1


        val customView = layoutInflater.inflate(R.layout.item_add_sports_model, null)
        viewDataBinding.modelLayoutContainer.addView(customView)
        addModelViewList.add(customView)

        if (viewModel?.isEdit?.get()!! && viewModel?.editEquipmentData?.size == addModelViewList.size) {
            for (i in 0 until addModelViewList.size) {
                //init variable
                val deleteLayout = addModelViewList[i].findViewById<ImageView>(R.id.deleteLayout)
                val etSubSports =
                    addModelViewList[i].findViewById<TextInputEditText>(R.id.etSubSports)

                val tvBrandMake =
                    addModelViewList[i].findViewById<TextInputLayout>(R.id.tvBrandMake)
                val etBrandMake =
                    addModelViewList[i].findViewById<TextInputEditText>(R.id.etBrandMake)

                val tvModel = addModelViewList[i].findViewById<TextInputLayout>(R.id.tvModel)
                val etModel = addModelViewList[i].findViewById<TextInputEditText>(R.id.etModel)

                val tvWheelSize =
                    addModelViewList[i].findViewById<TextInputLayout>(R.id.tvWheelSize)
                val etWheelSize =
                    addModelViewList[i].findViewById<TextInputEditText>(R.id.etWheelSize)

                val tvFrame = addModelViewList[i].findViewById<TextInputLayout>(R.id.tvFrame)
                val etFrame = addModelViewList[i].findViewById<TextInputEditText>(R.id.etFrame)

                val etYear = addModelViewList[i].findViewById<TextInputEditText>(R.id.etYear)
                val etOther = addModelViewList[i].findViewById<TextInputEditText>(R.id.eOther)

                val yearListTemp = arrayListOf<String>()

                for (j in 0 until 20) {
                    yearListTemp.add("${Calendar.getInstance().get(Calendar.YEAR) - j}")
                }
                yearList[addModelViewList[i]] = yearListTemp
                subSportsList[addModelViewList[i]] = (viewModel?.subSportList ?: return)

                //TODO: DELETE LAYOUT
                deleteLayout?.setOnClickListener { it ->
                    viewDataBinding.modelLayoutContainer.removeView(addModelViewList[i])
                    viewModel?.addBrandMakeHashMap?.remove(addModelViewList[i])
                    viewModel?.addModelHashMap?.remove(addModelViewList[i])
                    viewModel?.addWheelSizeHashMap?.remove(addModelViewList[i])
                    viewModel?.addFrameHashMap?.remove(addModelViewList[i])
                    viewModel?.addYearHashMap?.remove(addModelViewList[i])

                    subSportsList.remove(addModelViewList[i])
                    brandMakeList.remove(addModelViewList[i])
                    modelList.remove(addModelViewList[i])
                    wheelSizeList.remove(addModelViewList[i])
                    frameSizeList.remove(addModelViewList[i])
                    yearList.remove(addModelViewList[i])
                }

                //TODO: Used to chooser Sub sports category
                etSubSports?.setOnClickListener { it ->
                    ChallengePopUp.Builder()
                        .addList(
                            subSportsList.get(addModelViewList.get(i)) ?: return@setOnClickListener
                        )
                        .setListener { subSports ->
                            etBrandMake.setText("")
                            etModel.setText("")
                            etWheelSize.setText("")
                            etFrame.setText("")
                            etYear.setText("")
                            etOther.setText("")

                            tvBrandMake.visibility = View.GONE
                            tvModel.visibility = View.GONE
                            tvWheelSize.visibility = View.GONE
                            tvFrame.visibility = View.GONE

                            val mappingID = addModelViewList[i]
                            viewModel?.addSubSportHashMap?.put(
                                mappingID, SubSport(
                                    subSports.gradeLevel ?: "",
                                    subSports.sportId ?: "",
                                    subSports.id ?: "",
                                    subSports.name ?: "",
                                )
                            )
                            etSubSports.setText(subSports.name.toString())
                            //initModel
                            brandMakeList.put(mappingID, subSports.brandList!!)
                            if (subSports.brandList.isNotEmpty()) {
                                tvBrandMake.visibility = View.VISIBLE
                            } else {
                                tvBrandMake.visibility = View.VISIBLE
                            }
                        }.build().show(it)
                }

                //TODO: Used to chooser Brand/Make
                etBrandMake?.setOnClickListener { it ->
                    AddBrandPopUp.Builder()
                        .addList(brandMakeList[addModelViewList[i]] ?: return@setOnClickListener)
                        .setListener { model ->
                            etModel.setText("")
                            etWheelSize.setText("")
                            etFrame.setText("")
                            etYear.setText("")
                            etOther.setText("")

                            tvModel.visibility = View.GONE
                            tvWheelSize.visibility = View.GONE
                            tvFrame.visibility = View.GONE

                            val mappingID = addModelViewList[i]
                            viewModel?.addBrandMakeHashMap?.put(mappingID, model)
                            etBrandMake.setText(model.brandName)

                            modelList.put(mappingID, model.modelList ?: return@setListener)
                            if (!model.modelList.isEmpty()) {
                                tvModel.visibility = View.VISIBLE
                            } else {
                                tvModel.visibility = View.GONE
                            }
                        }.build().show(it)
                }

                //TODO: Used to chooser Model
                etModel?.setOnClickListener { it ->
                    AddModelPopUp.Builder()
                        .addList(modelList.get(addModelViewList[i]) ?: return@setOnClickListener)
                        .setListener { model ->
                            etWheelSize.setText("")
                            etFrame.setText("")
                            etYear.setText("")
                            etOther.setText("")
                            etModel.setText(model.modelName)

                            val mappingID = addModelViewList[i]
                            viewModel?.addModelHashMap?.put(mappingID, model)

                            tvWheelSize.visibility = View.GONE
                            tvFrame.visibility = View.GONE

                            wheelSizeList.put(mappingID, model.wheelSizeList!!)
                            frameSizeList.put(mappingID, model.frameList!!)
                            if (model.wheelSizeList.isNotEmpty()) {
                                tvWheelSize.visibility = View.VISIBLE
                            } else {
                                tvWheelSize.visibility = View.GONE
                            }
                            if (!model.frameList.isEmpty()) {
                                tvFrame.visibility = View.VISIBLE
                            } else {
                                tvFrame.visibility = View.GONE
                            }
                        }.build().show(it)
                }

                //TODO: Used to chooser Wheel Size
                etWheelSize?.setOnClickListener { it ->
                    AddWheelSizePopup.Builder()
                        .addList(
                            wheelSizeList.get(addModelViewList[i]) ?: return@setOnClickListener
                        )
                        .setListener { wheel ->
                            etWheelSize.setText(wheel.wheelSize.toString())
                            val mappingID = addModelViewList[i]
                            viewModel?.addWheelSizeHashMap?.put(mappingID, wheel)
                        }.build().show(it)
                }

                //TODO: Used to chooser Frame Size
                etFrame?.setOnClickListener { it ->
                    AddFrameSizePopUp.Builder()
                        .addList(
                            frameSizeList.get(addModelViewList[i]) ?: return@setOnClickListener
                        )
                        .setListener { frameSize ->
                            etFrame.setText(frameSize.frameSize.toString())
                            val mappingID = addModelViewList[i]
                            viewModel?.addFrameHashMap?.put(mappingID, frameSize)
                        }.build().show(it)
                }

                //TODO: Used to chooser Year
                etYear?.setOnClickListener { it ->
                    AddYearPopUp.Builder()
                        .addList(yearList.get(addModelViewList[i]) ?: return@setOnClickListener)
                        .setListener { year ->
                            etYear.setText(year)
                            val mappingID = addModelViewList[i]
                            viewModel?.addYearHashMap?.put(mappingID, year)
                        }.build().show(it)
                }


                //Edit equipments
                if (viewModel?.isEdit?.get() ?: return) {
                    val mappingID = addModelViewList[i]

                    for (f in 0 until viewModel?.subSportList?.size!!) {
                        if (viewModel?.editSubSportData?.get(i)?.subSportId == viewModel?.subSportList?.get(
                                f
                            )?.id
                        ) {
                            val subSportsPreSelected = subSportsList.get(mappingID)?.get(f)
                            viewModel?.addSubSportHashMap?.put(
                                mappingID, SubSport(
                                    subSportsPreSelected?.gradeLevel ?: "",
                                    subSportsPreSelected?.sportId ?: "",
                                    subSportsPreSelected?.id ?: "",
                                    subSportsPreSelected?.name ?: ""
                                )
                            )
                            etSubSports.setText(subSportsPreSelected?.name)

                            brandMakeList.put(mappingID, subSportsPreSelected?.brandList ?: return)
                            for (j in 0 until subSportsPreSelected.brandList.size) {
                                if (viewModel?.editEquipmentData?.get(i)?.brandMakeId == subSportsPreSelected.brandList.get(
                                        j
                                    ).brandId
                                ) {
                                    val brandMake = subSportsPreSelected.brandList.get(j) ?: return
                                    tvBrandMake.visibility = View.VISIBLE
                                    etBrandMake.setText(brandMake.brandName)
                                    viewModel?.addBrandMakeHashMap?.put(mappingID, brandMake)
                                    modelList.put(mappingID, brandMake.modelList ?: return)

                                    for (k in 0 until brandMake.modelList.size) {
                                        if (viewModel?.editEquipmentData?.get(i)?.modelId == brandMake.modelList[k].modelId) {
                                            val model = brandMake.modelList[k]
                                            tvModel.visibility = View.VISIBLE
                                            etModel.setText(model.modelName)
                                            viewModel?.addModelHashMap?.put(mappingID, model)

                                            wheelSizeList.put(
                                                mappingID,
                                                model.wheelSizeList ?: return
                                            )
                                            for (l in 0 until model.wheelSizeList.size) {
                                                if (viewModel?.editEquipmentData?.get(i)?.wheelSize == model.wheelSizeList.get(
                                                        l
                                                    ).wheelSize
                                                ) {
                                                    val wheel = model.wheelSizeList.get(l)
                                                    tvWheelSize.visibility = View.VISIBLE
                                                    etWheelSize.setText(wheel.wheelSize)
                                                    viewModel?.addWheelSizeHashMap?.put(
                                                        mappingID,
                                                        wheel
                                                    )
                                                }
                                            }
                                            frameSizeList.put(mappingID, model.frameList ?: return)
                                            for (m in 0 until model.frameList.size) {
                                                if (viewModel?.editEquipmentData?.get(i)?.frame?.lowercase() == model.frameList.get(
                                                        m
                                                    ).frameSize?.lowercase()
                                                ) {
                                                    val frame = model.frameList.get(k)
                                                    tvFrame.visibility = View.VISIBLE
                                                    etFrame.setText(frame.frameSize)
                                                    viewModel?.addFrameHashMap?.put(
                                                        mappingID,
                                                        frame
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        for (k in 0 until yearList.get(mappingID)?.size!!) {
                            if (viewModel?.editEquipmentData?.get(f)?.year.equals(
                                    yearList.get(mappingID)
                                        ?.get(k)
                                )
                            ) {
                                val year = yearList.get(mappingID)?.get(k) ?: return
                                etYear.setText(year)
                                viewModel?.addYearHashMap?.put(mappingID, year)
                            }
                        }

                        etOther.setText(viewModel?.editEquipmentData?.get(f)?.other)
                        viewModel?.addOtherMap?.put(
                            mappingID,
                            viewModel?.editEquipmentData?.get(i)?.other ?: ""
                        )

                    }
                }
            }
        } else if (viewModel?.isEdit?.get() == false) {
            for (i in 0 until addModelViewList.size) {
                //init variable
                val deleteLayout = addModelViewList[i].findViewById<ImageView>(R.id.deleteLayout)
                val etSubSports =
                    addModelViewList[i].findViewById<TextInputEditText>(R.id.etSubSports)

                val tvBrandMake =
                    addModelViewList[i].findViewById<TextInputLayout>(R.id.tvBrandMake)
                val etBrandMake =
                    addModelViewList[i].findViewById<TextInputEditText>(R.id.etBrandMake)

                val tvModel = addModelViewList[i].findViewById<TextInputLayout>(R.id.tvModel)
                val etModel = addModelViewList[i].findViewById<TextInputEditText>(R.id.etModel)

                val tvWheelSize =
                    addModelViewList[i].findViewById<TextInputLayout>(R.id.tvWheelSize)
                val etWheelSize =
                    addModelViewList[i].findViewById<TextInputEditText>(R.id.etWheelSize)

                val tvFrame = addModelViewList[i].findViewById<TextInputLayout>(R.id.tvFrame)
                val etFrame = addModelViewList[i].findViewById<TextInputEditText>(R.id.etFrame)

                val etYear = addModelViewList[i].findViewById<TextInputEditText>(R.id.etYear)
                val etOther = addModelViewList[i].findViewById<TextInputEditText>(R.id.eOther)

                val yearListTemp = arrayListOf<String>()

                for (j in 0 until 20) {
                    yearListTemp.add("${Calendar.getInstance().get(Calendar.YEAR) - j}")
                }
                yearList[addModelViewList[i]] = yearListTemp
                subSportsList[addModelViewList[i]] = (viewModel?.subSportList ?: return)

                //TODO: DELETE LAYOUT
                deleteLayout?.setOnClickListener { it ->
                    viewDataBinding.modelLayoutContainer.removeView(addModelViewList[i])
                    viewModel?.addBrandMakeHashMap?.remove(addModelViewList[i])
                    viewModel?.addModelHashMap?.remove(addModelViewList[i])
                    viewModel?.addWheelSizeHashMap?.remove(addModelViewList[i])
                    viewModel?.addFrameHashMap?.remove(addModelViewList[i])
                    viewModel?.addYearHashMap?.remove(addModelViewList[i])

                    subSportsList.remove(addModelViewList[i])
                    brandMakeList.remove(addModelViewList[i])
                    modelList.remove(addModelViewList[i])
                    wheelSizeList.remove(addModelViewList[i])
                    frameSizeList.remove(addModelViewList[i])
                    yearList.remove(addModelViewList[i])
                }

                //TODO: Used to chooser Sub sports category
                etSubSports?.setOnClickListener { it ->
                    ChallengePopUp.Builder()
                        .addList(
                            subSportsList.get(addModelViewList.get(i)) ?: return@setOnClickListener
                        )
                        .setListener { subSports ->
                            etBrandMake.setText("")
                            etModel.setText("")
                            etWheelSize.setText("")
                            etFrame.setText("")
                            etYear.setText("")
                            etOther.setText("")

                            tvBrandMake.visibility = View.GONE
                            tvModel.visibility = View.GONE
                            tvWheelSize.visibility = View.GONE
                            tvFrame.visibility = View.GONE

                            val mappingID = addModelViewList[i]
                            viewModel?.addSubSportHashMap?.put(
                                mappingID, SubSport(
                                    subSports.gradeLevel ?: "",
                                    subSports.sportId ?: "",
                                    subSports.id ?: "",
                                    subSports.name ?: ""
                                )
                            )
                            etSubSports.setText(subSports.name.toString())
                            //initModel
                            brandMakeList.put(mappingID, subSports.brandList!!)
                            if (subSports.brandList.isNotEmpty()) {
                                tvBrandMake.visibility = View.VISIBLE
                            } else {
                                tvBrandMake.visibility = View.VISIBLE
                            }
                        }.build().show(it)
                }

                //TODO: Used to chooser Brand/Make
                etBrandMake?.setOnClickListener { it ->
                    AddBrandPopUp.Builder()
                        .addList(brandMakeList[addModelViewList[i]] ?: return@setOnClickListener)
                        .setListener { model ->
                            etModel.setText("")
                            etWheelSize.setText("")
                            etFrame.setText("")
                            etYear.setText("")
                            etOther.setText("")

                            tvModel.visibility = View.GONE
                            tvWheelSize.visibility = View.GONE
                            tvFrame.visibility = View.GONE

                            val mappingID = addModelViewList[i]
                            viewModel?.addBrandMakeHashMap?.put(mappingID, model)
                            etBrandMake.setText(model.brandName)

                            modelList.put(mappingID, model.modelList ?: return@setListener)
                            if (!model.modelList.isEmpty()) {
                                tvModel.visibility = View.VISIBLE
                            } else {
                                tvModel.visibility = View.GONE
                            }
                        }.build().show(it)
                }

                //TODO: Used to chooser Model
                etModel?.setOnClickListener { it ->
                    AddModelPopUp.Builder()
                        .addList(modelList.get(addModelViewList[i]) ?: return@setOnClickListener)
                        .setListener { model ->
                            etWheelSize.setText("")
                            etFrame.setText("")
                            etYear.setText("")
                            etOther.setText("")
                            etModel.setText(model.modelName)

                            val mappingID = addModelViewList[i]
                            viewModel?.addModelHashMap?.put(mappingID, model)

                            tvWheelSize.visibility = View.GONE
                            tvFrame.visibility = View.GONE

                            wheelSizeList.put(mappingID, model.wheelSizeList!!)
                            frameSizeList.put(mappingID, model.frameList!!)
                            if (model.wheelSizeList.isNotEmpty()) {
                                tvWheelSize.visibility = View.VISIBLE
                            } else {
                                tvWheelSize.visibility = View.GONE
                            }
                            if (!model.frameList.isEmpty()) {
                                tvFrame.visibility = View.VISIBLE
                            } else {
                                tvFrame.visibility = View.GONE
                            }
                        }.build().show(it)
                }

                //TODO: Used to chooser Wheel Size
                etWheelSize?.setOnClickListener { it ->
                    AddWheelSizePopup.Builder()
                        .addList(
                            wheelSizeList.get(addModelViewList[i]) ?: return@setOnClickListener
                        )
                        .setListener { wheel ->
                            etWheelSize.setText(wheel.wheelSize.toString())
                            val mappingID = addModelViewList[i]
                            viewModel?.addWheelSizeHashMap?.put(mappingID, wheel)
                        }.build().show(it)
                }

                //TODO: Used to chooser Frame Size
                etFrame?.setOnClickListener { it ->
                    AddFrameSizePopUp.Builder()
                        .addList(
                            frameSizeList.get(addModelViewList[i]) ?: return@setOnClickListener
                        )
                        .setListener { frameSize ->
                            etFrame.setText(frameSize.frameSize.toString())
                            val mappingID = addModelViewList[i]
                            viewModel?.addFrameHashMap?.put(mappingID, frameSize)
                        }.build().show(it)
                }

                //TODO: Used to chooser Year
                etYear?.setOnClickListener { it ->
                    AddYearPopUp.Builder()
                        .addList(yearList.get(addModelViewList[i]) ?: return@setOnClickListener)
                        .setListener { year ->
                            etYear.setText(year)
                            val mappingID = addModelViewList[i]
                            viewModel?.addYearHashMap?.put(mappingID, year)
                        }.build().show(it)
                }


                //Edit equipments
                if (viewModel?.isEdit?.get() ?: return) {
                    val mappingID = addModelViewList[i]

                    if (viewModel?.editSubSportData?.get(i)?.subSportId == viewModel?.subSportList?.get(
                            i
                        )?.id
                    ) {
                        val subSportsPreSelected = viewModel?.subSportList?.get(i)
                        viewModel?.addSubSportHashMap?.put(
                            mappingID, SubSport(
                                subSportsPreSelected?.gradeLevel ?: "",
                                subSportsPreSelected?.sportId ?: "",
                                subSportsPreSelected?.id ?: "",
                                subSportsPreSelected?.name ?: ""
                            )
                        )
                        etSubSports.setText(subSportsPreSelected?.name)
                    }
                    viewModel?.subSportList?.get(i)


                    brandMakeList.put(
                        mappingID,
                        viewModel?.subSportList?.get(i)?.brandList ?: return
                    )
                    for (j in 0 until brandMakeList.size) {
                        if (viewModel?.editEquipmentData?.get(i)?.brandMakeId == brandMakeList.get(
                                mappingID
                            )?.get(j)?.brandId
                        ) {
                            val brandMake = brandMakeList.get(mappingID)?.get(j) ?: return
                            tvBrandMake.visibility = View.VISIBLE
                            etBrandMake.setText(brandMake.brandName)
                            viewModel?.addBrandMakeHashMap?.put(mappingID, brandMake)
                            modelList.put(mappingID, brandMake.modelList ?: return)

                            for (k in 0 until brandMake.modelList.size) {
                                if (viewModel?.editEquipmentData?.get(i)?.modelId == brandMake.modelList[k].modelId) {
                                    val model = modelList.get(mappingID)?.get(k) ?: return
                                    tvModel.visibility = View.VISIBLE
                                    etModel.setText(model.modelName)
                                    viewModel?.addModelHashMap?.put(mappingID, model)

                                    wheelSizeList.put(mappingID, model.wheelSizeList ?: return)
                                    for (l in 0 until brandMake.modelList[k].wheelSizeList?.size!!) {
                                        if (viewModel?.editEquipmentData?.get(i)?.wheelSize == model.wheelSizeList.get(
                                                l
                                            ).wheelSize
                                        ) {
                                            val wheel = model.wheelSizeList.get(l)
                                            tvWheelSize.visibility = View.VISIBLE
                                            etWheelSize.setText(wheel.wheelSize)
                                            viewModel?.addWheelSizeHashMap?.put(mappingID, wheel)
                                        }
                                    }
                                    frameSizeList.put(mappingID, model.frameList ?: return)
                                    for (m in 0 until model.frameList.size) {
                                        if (viewModel?.editEquipmentData?.get(i)?.frame?.lowercase() == model.frameList.get(
                                                m
                                            ).frameSize?.lowercase()
                                        ) {
                                            val frame = model.frameList.get(k)
                                            tvFrame.visibility = View.VISIBLE
                                            etFrame.setText(frame.frameSize)
                                            viewModel?.addFrameHashMap?.put(mappingID, frame)
                                        }
                                    }
                                }
                            }
                        }
                    }

                    for (k in 0 until yearList.get(mappingID)?.size!!) {
                        if (viewModel?.editEquipmentData?.get(i)?.year.equals(
                                yearList.get(mappingID)
                                    ?.get(k)
                            )
                        ) {
                            val year = yearList.get(mappingID)?.get(k) ?: return
                            etYear.setText(year)
                            viewModel?.addYearHashMap?.put(mappingID, year)
                        }
                    }

                    etOther.setText(viewModel?.editEquipmentData?.get(i)?.other)
                    viewModel?.addOtherMap?.put(
                        mappingID,
                        viewModel?.editEquipmentData?.get(i)?.other ?: ""
                    )

                }
            }
        }
    }

//    //old start
//    override fun addModelLayout() {
//        if (((activity as AddModelEquipmentDetailsActivity?)?.modelEquipmentDataCount
//                ?: 0) >= 5
//        ) return
//        (activity as AddModelEquipmentDetailsActivity?)?.modelEquipmentDataCount =
//            (activity as AddModelEquipmentDetailsActivity?)?.modelEquipmentDataCount!! + 1
//
//        val customView = layoutInflater.inflate(R.layout.item_add_sports_model, null)
//        viewDataBinding.modelLayoutContainer.addView(customView)
//        addModelViewList.add(customView)
//
//        for (i in 0 until addModelViewList.size) {
//            //init variable
//            val deleteLayout = addModelViewList[i].findViewById<ImageView>(R.id.deleteLayout)
//            val etSubSports = addModelViewList[i].findViewById<TextInputEditText>(R.id.etSubSports)
//
//            val tvBrandMake = addModelViewList[i].findViewById<TextInputLayout>(R.id.tvBrandMake)
//            val etBrandMake = addModelViewList[i].findViewById<TextInputEditText>(R.id.etBrandMake)
//
//            val tvModel = addModelViewList[i].findViewById<TextInputLayout>(R.id.tvModel)
//            val etModel = addModelViewList[i].findViewById<TextInputEditText>(R.id.etModel)
//
//            val tvWheelSize = addModelViewList[i].findViewById<TextInputLayout>(R.id.tvWheelSize)
//            val etWheelSize = addModelViewList[i].findViewById<TextInputEditText>(R.id.etWheelSize)
//
//            val tvFrame = addModelViewList[i].findViewById<TextInputLayout>(R.id.tvFrame)
//            val etFrame = addModelViewList[i].findViewById<TextInputEditText>(R.id.etFrame)
//
//            val etYear = addModelViewList[i].findViewById<TextInputEditText>(R.id.etYear)
//            val etOther = addModelViewList[i].findViewById<TextInputEditText>(R.id.eOther)
//
//            val brandMakeList = arrayListOf<Brand>()
//            val modelList = arrayListOf<Model>()
//            val wheelSizeList = arrayListOf<WheelSize>()
//            val frameSizeList = arrayListOf<FrameSize>()
//            val yearList = arrayListOf<String>()
//
//            for (j in 0 until 20) {
//                yearList.add("${Calendar.getInstance().get(Calendar.YEAR) - j}")
//            }
//
//
//            //TODO: DELETE LAYOUT
//            deleteLayout?.setOnClickListener { it ->
//                viewDataBinding.modelLayoutContainer.removeView(addModelViewList[i])
//                viewModel?.addBrandMakeHashMap?.remove(addModelViewList[i])
//                viewModel?.addModelHashMap?.remove(addModelViewList[i])
//                viewModel?.addWheelSizeHashMap?.remove(addModelViewList[i])
//                viewModel?.addFrameHashMap?.remove(addModelViewList[i])
//                viewModel?.addYearHashMap?.remove(addModelViewList[i])
//            }
//
//            //TODO: Used to chooser Sub sports category
//            etSubSports?.setOnClickListener { it ->
//                ChallengePopUp.Builder()
//                    .addList(viewModel?.subSportList ?: return@setOnClickListener)
//                    .setListener { subSports ->
//                        etBrandMake.setText("")
//                        etModel.setText("")
//                        etWheelSize.setText("")
//                        etFrame.setText("")
//                        etYear.setText("")
//                        etOther.setText("")
//                        brandMakeList.clear()
//                        modelList.clear()
//                        wheelSizeList.clear()
//                        frameSizeList.clear()
//                        tvBrandMake.visibility = View.GONE
//                        tvModel.visibility = View.GONE
//                        tvWheelSize.visibility = View.GONE
//                        tvFrame.visibility = View.GONE
//
//                        val mappingID = addModelViewList[i]
//                        viewModel?.addSubSportHashMap?.put(
//                            mappingID, SubSport(
//                                subSports.gradeLevel ?: "",
//                                subSports.sportId ?: "",
//                                subSports.id ?: ""
//                            )
//                        )
//                        etSubSports.setText(subSports.name.toString())
//                        brandMakeList.clear()
//                        //initModel
//                        brandMakeList.addAll(subSports.brandList!!)
//                        if (subSports.brandList.isNotEmpty()) {
//                            tvBrandMake.visibility = View.VISIBLE
//                        } else {
//                            tvBrandMake.visibility = View.VISIBLE
//                        }
//                    }.build().show(it)
//            }
//
//            //TODO: Used to chooser Brand/Make
//            etBrandMake?.setOnClickListener { it ->
//                AddBrandPopUp.Builder().addList(brandMakeList).setListener { model ->
//                    etModel.setText("")
//                    etWheelSize.setText("")
//                    etFrame.setText("")
//                    etYear.setText("")
//                    etOther.setText("")
//                    modelList.clear()
//                    wheelSizeList.clear()
//                    frameSizeList.clear()
//                    tvModel.visibility = View.GONE
//                    tvWheelSize.visibility = View.GONE
//                    tvFrame.visibility = View.GONE
//                    val mappingID = addModelViewList[i]
//                    viewModel?.addBrandMakeHashMap?.put(mappingID, model)
//
//                    etBrandMake.setText(model.brandName)
//                    modelList.clear()
//                    modelList.addAll(model.modelList ?: return@setListener)
//                    if (!model.modelList.isEmpty()) {
//                        tvModel.visibility = View.VISIBLE
//                    } else {
//                        tvModel.visibility = View.GONE
//                    }
//                }.build().show(it)
//            }
//
//            //TODO: Used to chooser Model
//            etModel?.setOnClickListener { it ->
//                AddModelPopUp.Builder().addList(modelList).setListener { model ->
//                    etWheelSize.setText("")
//                    etFrame.setText("")
//                    etYear.setText("")
//                    etOther.setText("")
//                    wheelSizeList.clear()
//                    frameSizeList.clear()
//                    etModel.setText(model.modelName)
//                    wheelSizeList.clear()
//                    frameSizeList.clear()
//                    val mappingID = addModelViewList[i]
//                    viewModel?.addModelHashMap?.put(mappingID, model)
//
//                    tvWheelSize.visibility = View.GONE
//                    tvFrame.visibility = View.GONE
//
//                    wheelSizeList.addAll(model.wheelSizeList!!)
//                    frameSizeList.addAll(model.frameList!!)
//                    if (model.wheelSizeList.isNotEmpty()) {
//                        tvWheelSize.visibility = View.VISIBLE
//                    } else {
//                        tvWheelSize.visibility = View.GONE
//                    }
//                    if (!model.frameList.isEmpty()) {
//                        tvFrame.visibility = View.VISIBLE
//                    } else {
//                        tvFrame.visibility = View.GONE
//                    }
//                }.build().show(it)
//            }
//
//            //TODO: Used to chooser Wheel Size
//            etWheelSize?.setOnClickListener { it ->
//                AddWheelSizePopup.Builder().addList(wheelSizeList).setListener { wheel ->
//                    etWheelSize.setText(wheel.wheelSize.toString())
//                    val mappingID = addModelViewList[i]
//                    viewModel?.addWheelSizeHashMap?.put(mappingID, wheel)
//                }.build().show(it)
//            }
//
//            //TODO: Used to chooser Frame Size
//            etFrame?.setOnClickListener { it ->
//                AddFrameSizePopUp.Builder().addList(frameSizeList).setListener { frameSize ->
//                    etFrame.setText(frameSize.frameSize.toString())
//                    val mappingID = addModelViewList[i]
//                    viewModel?.addFrameHashMap?.put(mappingID, frameSize)
//                }.build().show(it)
//            }
//
//            //TODO: Used to chooser Year
//            etYear?.setOnClickListener { it ->
//                AddYearPopUp.Builder().addList(yearList).setListener { year ->
//                    etYear.setText(year)
//                    val mappingID = addModelViewList[i]
//                    viewModel?.addYearHashMap?.put(mappingID, year)
//                }.build().show(it)
//            }
//
//            //TODO: To show pre fill data if having
//            if (viewModel?.isEdit?.get()!! && viewModel?.editEquipmentData?.get(i)?.type.equals(
//                    "model",
//                    ignoreCase = true
//                )
//            ) {
//                val mappingID = addModelViewList[i]
//
//                if (viewModel?.editSubSportData?.get(i)?.subSportId == viewModel?.subSportList?.get(
//                        i
//                    )?.id
//                ) {
//                    val subSportsPreSelected = viewModel?.subSportList?.get(i)
//                    viewModel?.addSubSportHashMap?.put(
//                        mappingID, SubSport(
//                            subSportsPreSelected?.gradeLevel ?: "",
//                            subSportsPreSelected?.sportId ?: "",
//                            subSportsPreSelected?.id ?: ""
//                        )
//                    )
//                    etSubSports.setText(subSportsPreSelected?.name)
//                }
//                viewModel?.subSportList?.get(i)
//
//
//                brandMakeList.addAll(viewModel?.subSportList?.get(i)?.brandList ?: return)
//                for (m in 0 until brandMakeList.size) {
//                    if (viewModel?.editEquipmentData?.get(i)?.brandMakeId == brandMakeList[i].brandId) {
//                        val brandMake = brandMakeList[i]
//                        tvBrandMake.visibility = View.VISIBLE
//                        etBrandMake.setText(brandMake.brandName)
//                        viewModel?.addBrandMakeHashMap?.put(mappingID, brandMake)
//                    }
//                }
//                modelList.addAll(brandMakeList[i].modelList ?: return)
//                for (k in 0 until modelList.size) {
//                    if (viewModel?.editEquipmentData?.get(i)?.modelId == modelList[k].modelId) {
//                        val model = modelList[k]
//                        tvModel.visibility = View.VISIBLE
//                        etModel.setText(model.modelName)
//                        viewModel?.addModelHashMap?.put(mappingID, model)
//                    }
//                }
//
//                wheelSizeList.addAll(modelList[i].wheelSizeList ?: return)
//                for (k in 0 until wheelSizeList.size) {
//                    if (viewModel?.editEquipmentData?.get(i)?.wheelSize == wheelSizeList[k].wheelSize) {
//                        val wheel = wheelSizeList[k]
//                        tvWheelSize.visibility = View.VISIBLE
//                        etWheelSize.setText(wheel.wheelSize)
//                        viewModel?.addWheelSizeHashMap?.put(mappingID, wheel)
//                    }
//                }
//
//                frameSizeList.addAll(modelList[i].frameList ?: return)
//                for (k in 0 until frameSizeList.size) {
//                    if (viewModel?.editEquipmentData?.get(i)?.frame?.lowercase() == frameSizeList[k].frameSize?.lowercase()) {
//                        val frame = frameSizeList[k]
//                        tvFrame.visibility = View.VISIBLE
//                        etFrame.setText(frame.frameSize)
//                        viewModel?.addFrameHashMap?.put(mappingID, frame)
//                    }
//                }
//                for (k in 0 until yearList.size) {
//                    if (viewModel?.editEquipmentData?.get(i)?.year == yearList[k]) {
//                        val year = yearList[k]
//                        etYear.setText(year)
//                        viewModel?.addYearHashMap?.put(mappingID, year)
//                    }
//                }
//                etOther.setText(viewModel?.editEquipmentData?.get(i)?.other)
//                viewModel?.addOtherMap?.put(
//                    mappingID,
//                    viewModel?.editEquipmentData?.get(i)?.other ?: ""
//                )
//
//            }
//        }
//    }
////old end

    override fun filterDataRequestToSend() {
        for (i in 0 until addModelViewList.size) {
            viewModel?.requestSubSportData?.add(
                viewModel?.addSubSportHashMap?.get(addModelViewList[i]) ?: return
            )
            val sports = viewModel?.addSubSportHashMap?.get(addModelViewList[i])
            val brandMake = viewModel?.addBrandMakeHashMap?.get(addModelViewList[i])
            val model = viewModel?.addModelHashMap?.get(addModelViewList[i])
            val wheel = viewModel?.addWheelSizeHashMap?.get(addModelViewList[i])
            val frame = viewModel?.addFrameHashMap?.get(addModelViewList[i])
            val year = viewModel?.addYearHashMap?.get(addModelViewList[i])
            val other =
                (addModelViewList[i].findViewById<TextInputEditText>(R.id.eOther).text).toString()

            if (brandMake?.brandId != null)
                viewModel?.requestEquipmentData?.add(
                    EquipmentData(
                        brandMake.brandId,
                        brandMake.brandName ?: "",
                        "",
                        frame?.frameSize ?: "",
                        model?.modelId ?: "",
                        model?.modelName ?: "",
                        other,
                        sports?.sportId ?: "",
                        sports?.subSportId ?: "",
                        sports?.subSportName ?: "",
                        "model",
                        wheel?.wheelSize ?: "",
                        year ?: ""
                    )
                )
        }
        viewModel?.addModelEquipments()
    }

    override fun reDirectToProfile() {
        startActivity(
            intentProviderFactory.create(
                DashboardActivity::class.java, null, 0
            )
        )
    }
}