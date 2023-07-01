package com.yewapp.ui.modules.addmodelandequipments.fragments.addequipments

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
import com.yewapp.data.network.api.sports.Brand
import com.yewapp.data.network.api.sports.Model
import com.yewapp.data.network.api.sports.Sport
import com.yewapp.data.network.api.sports.Type
import com.yewapp.databinding.FragmentAddEquipmentsBinding
import com.yewapp.ui.base.BaseFragment
import com.yewapp.ui.dialogs.addbrandmake.AddBrandPopUp
import com.yewapp.ui.dialogs.addmodel.AddModelPopUp
import com.yewapp.ui.dialogs.challengepopupdialogs.ChallengePopUp
import com.yewapp.ui.dialogs.type.AddTypePopup
import com.yewapp.ui.modules.addmodelandequipments.AddModelEquipmentDetailsActivity
import com.yewapp.ui.modules.dashboard.DashboardActivity

class AddEquipmentsFragment(override val layoutId: Int = R.layout.fragment_add_equipments) :
    BaseFragment<AddEquipmentsNavigator, AddEquipmentsViewModel, FragmentAddEquipmentsBinding>(),
    AddEquipmentsNavigator {


    companion object {
        fun newInstance(extras: Bundle?): AddEquipmentsFragment {
            //can not send without bundle else will give null pointer error
            val fragment = AddEquipmentsFragment()
//           val args = Bundle()
            fragment.arguments = extras
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return bind(AddEquipmentsViewModel::class.java, inflater, container)
    }


    override fun onViewModelCreated(viewModel: AddEquipmentsViewModel) {
        viewModel.setNavigator(this)
    }

    override fun onViewBound(viewDataBinding: FragmentAddEquipmentsBinding) {
    }

    override fun onAddIconClick() {
        hideKeyboard()
        val childCount: Int = viewDataBinding.modelLayoutContainer.childCount
//        if (childCount < (viewModel?.subSportList?.size ?: 0)) { OLD
        if (((activity as AddModelEquipmentDetailsActivity?)?.modelEquipmentDataCount
                ?: 0) < 5
        ) {//NEW
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
    private val typeList = HashMap<View, List<Type>>()


    override fun addModelLayout() {
        if (((activity as AddModelEquipmentDetailsActivity?)?.modelEquipmentDataCount
                ?: 0) >= 5
        ) return

        (activity as AddModelEquipmentDetailsActivity?)?.modelEquipmentDataCount =
            (activity as AddModelEquipmentDetailsActivity?)?.modelEquipmentDataCount!! + 1


        val customView = layoutInflater.inflate(R.layout.item_add_sports_equipment, null)
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

                val tvType = addModelViewList[i].findViewById<TextInputLayout>(R.id.tvType)
                val etType = addModelViewList[i].findViewById<TextInputEditText>(R.id.etType)

                val etOther = addModelViewList[i].findViewById<TextInputEditText>(R.id.eOther)


                subSportsList[addModelViewList[i]] = (viewModel?.subSportList ?: return)

                //TODO: DELETE LAYOUT
                deleteLayout?.setOnClickListener { it ->
                    viewDataBinding.modelLayoutContainer.removeView(addModelViewList[i])
                    viewModel?.addBrandMakeHashMap?.remove(addModelViewList[i])
                    viewModel?.addModelHashMap?.remove(addModelViewList[i])
                    viewModel?.addTypeHashMap?.remove(addModelViewList[i])

                    subSportsList.remove(addModelViewList[i])
                    brandMakeList.remove(addModelViewList[i])
                    modelList.remove(addModelViewList[i])
                    typeList.remove(addModelViewList[i])
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
                            etType.setText("")
                            etOther.setText("")

                            tvBrandMake.visibility = View.GONE
                            tvModel.visibility = View.GONE
                            tvType.visibility = View.GONE

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
                            etType.setText("")
                            etOther.setText("")

                            tvModel.visibility = View.GONE
                            tvType.visibility = View.GONE

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
                            etType.setText("")
                            etOther.setText("")
                            etModel.setText(model.modelName)

                            val mappingID = addModelViewList[i]
                            viewModel?.addModelHashMap?.put(mappingID, model)

                            tvType.visibility = View.GONE

                            typeList.put(mappingID, model.typeList!!)
                            if (model.typeList.isNotEmpty()) {
                                tvType.visibility = View.VISIBLE
                            } else {
                                tvType.visibility = View.GONE
                            }

                        }.build().show(it)
                }

                //TODO: Used to chooser Wheel Size
                etType?.setOnClickListener { it ->
                    AddTypePopup.Builder()
                        .addList(
                            typeList.get(addModelViewList[i]) ?: return@setOnClickListener
                        )
                        .setListener { type ->
                            etType.setText(type.type)
                            val mappingID = addModelViewList[i]
                            viewModel?.addTypeHashMap?.put(mappingID, type)
                        }.build().show(it)
                }

                //Edit equipments
                if (viewModel?.isEdit?.get() ?: return) {
                    val mappingID = addModelViewList[i]
                    etOther.setText(viewModel?.editEquipmentData?.get(i)?.other)
                    viewModel?.addOtherMap?.put(
                        mappingID,
                        viewModel?.editEquipmentData?.get(i)?.other ?: ""
                    )

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

                                            typeList.put(
                                                mappingID,
                                                model.typeList ?: return
                                            )
                                            for (l in 0 until model.typeList.size) {
                                                if (viewModel?.editEquipmentData?.get(i)?.equipmentType == model.typeList.get(
                                                        l
                                                    ).type
                                                ) {
                                                    val type = model.typeList.get(l)
                                                    tvType.visibility = View.VISIBLE
                                                    etType.setText(type.type)
                                                    viewModel?.addTypeHashMap?.put(
                                                        mappingID,
                                                        type
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }

//                        etOther.setText(viewModel?.editEquipmentData?.get(f)?.other)
//                        viewModel?.addOtherMap?.put(
//                            mappingID,
//                            viewModel?.editEquipmentData?.get(i)?.other ?: ""
//                        )

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

                val tvType = addModelViewList[i].findViewById<TextInputLayout>(R.id.tvType)
                val etType = addModelViewList[i].findViewById<TextInputEditText>(R.id.etType)

                val etOther = addModelViewList[i].findViewById<TextInputEditText>(R.id.eOther)


                subSportsList[addModelViewList[i]] = (viewModel?.subSportList ?: return)

                //TODO: DELETE LAYOUT
                deleteLayout?.setOnClickListener { it ->
                    viewDataBinding.modelLayoutContainer.removeView(addModelViewList[i])
                    viewModel?.addBrandMakeHashMap?.remove(addModelViewList[i])
                    viewModel?.addModelHashMap?.remove(addModelViewList[i])
                    viewModel?.addTypeHashMap?.remove(addModelViewList[i])

                    subSportsList.remove(addModelViewList[i])
                    brandMakeList.remove(addModelViewList[i])
                    modelList.remove(addModelViewList[i])
                    typeList.remove(addModelViewList[i])

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
                            etType.setText("")
                            etOther.setText("")

                            tvBrandMake.visibility = View.GONE
                            tvModel.visibility = View.GONE
                            tvType.visibility = View.GONE

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
                            etType.setText("")

                            etOther.setText("")

                            tvModel.visibility = View.GONE
                            tvType.visibility = View.GONE

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
                            etType.setText("")
                            etOther.setText("")
                            etModel.setText(model.modelName)

                            val mappingID = addModelViewList[i]
                            viewModel?.addModelHashMap?.put(mappingID, model)

                            tvType.visibility = View.GONE

                            typeList.put(mappingID, model.typeList!!)
                            if (model.typeList.isNotEmpty()) {
                                tvType.visibility = View.VISIBLE
                            } else {
                                tvType.visibility = View.GONE
                            }

                        }.build().show(it)
                }

                //TODO: Used to chooser Wheel Size
                etType?.setOnClickListener { it ->
                    AddTypePopup.Builder()
                        .addList(
                            typeList.get(addModelViewList[i]) ?: return@setOnClickListener
                        )
                        .setListener { type ->
                            etType.setText(type.type.toString())
                            val mappingID = addModelViewList[i]
                            viewModel?.addTypeHashMap?.put(mappingID, type)
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

                                    typeList.put(mappingID, model.typeList ?: return)
                                    for (l in 0 until brandMake.modelList[k].typeList?.size!!) {
                                        if (viewModel?.editEquipmentData?.get(i)?.equipmentType == model.typeList.get(
                                                l
                                            ).type
                                        ) {
                                            val type = model.typeList.get(l)
                                            tvType.visibility = View.VISIBLE
                                            etType.setText(type.type)
                                            viewModel?.addTypeHashMap?.put(mappingID, type)
                                        }
                                    }

                                }
                            }
                        }
                    }
                }
            }
        }
    }

//    var addModelViewList = arrayListOf<View>()
//    private val subSportsList = java.util.HashMap<View, List<Sport>>()
//    private val brandMakeList = HashMap<View, List<Brand>>()
//    private val modelList = HashMap<View, List<Model>>()
//    private val typeList = HashMap<View, List<Type>>()
//
//    //    private val frameSizeList = HashMap<View, List<FrameSize>>()
////    private val yearList = HashMap<View, List<String>>()
//    override fun addModelLayout() {
//        if (((activity as AddModelEquipmentDetailsActivity?)?.modelEquipmentDataCount
//                ?: 0) >= 5
//        ) return
//
//        (activity as AddModelEquipmentDetailsActivity?)?.modelEquipmentDataCount =
//            (activity as AddModelEquipmentDetailsActivity?)?.modelEquipmentDataCount!! + 1
//
//
//        val customView = layoutInflater.inflate(R.layout.item_add_sports_equipment, null)
//        viewDataBinding.modelLayoutContainer.addView(customView)
//        addModelViewList.add(customView)
//
//        if (viewModel?.isEdit?.get()!! && viewModel?.editEquipmentData?.size == addModelViewList.size) {
//            for (i in 0 until addModelViewList.size) {
//                //init variable
//                val deleteLayout =
//                    addModelViewList[i].findViewById<ImageView>(R.id.deleteLayout)
//                val etSubSports =
//                    addModelViewList[i].findViewById<TextInputEditText>(R.id.etSubSports)
//
//                val tvBrandMake =
//                    addModelViewList[i].findViewById<TextInputLayout>(R.id.tvBrandMake)
//                val etBrandMake =
//                    addModelViewList[i].findViewById<TextInputEditText>(R.id.etBrandMake)
//
//                val tvModel = addModelViewList[i].findViewById<TextInputLayout>(R.id.tvModel)
//                val etModel = addModelViewList[i].findViewById<TextInputEditText>(R.id.etModel)
//
//                val tvType = addModelViewList[i].findViewById<TextInputLayout>(R.id.tvType)
//                val etType = addModelViewList[i].findViewById<TextInputEditText>(R.id.etType)
//
//                val etOther = addModelViewList[i].findViewById<TextInputEditText>(R.id.eOther)
//
//                val yearListTemp = arrayListOf<String>()
//
//                for (j in 0 until 20) {
//                    yearListTemp.add("${Calendar.getInstance().get(Calendar.YEAR) - j}")
//                }
//                subSportsList[addModelViewList[i]] = (viewModel?.subSportList ?: return)
//
//                //TODO: DELETE LAYOUT
//                deleteLayout?.setOnClickListener { it ->
//                    viewDataBinding.modelLayoutContainer.removeView(addModelViewList[i])
//                    viewModel?.addBrandMakeHashMap?.remove(addModelViewList[i])
//                    viewModel?.addModelHashMap?.remove(addModelViewList[i])
//                    viewModel?.addTypeHashMap?.remove(addModelViewList[i])
//
//
//                    subSportsList.remove(addModelViewList[i])
//                    brandMakeList.remove(addModelViewList[i])
//                    modelList.remove(addModelViewList[i])
//                    typeList.remove(addModelViewList[i])
//                }
//
//                //TODO: Used to chooser Sub sports category
//                etSubSports?.setOnClickListener { it ->
//                    ChallengePopUp.Builder()
//                        .addList(
//                            subSportsList.get(addModelViewList.get(i))
//                                ?: return@setOnClickListener
//                        )
//                        .setListener { subSports ->
//                            etBrandMake.setText("")
//                            etModel.setText("")
//                            etType.setText("")
//                            etOther.setText("")
//
//
//                            val mappingID = addModelViewList[i]
//                            viewModel?.addSubSportHashMap?.put(
//                                mappingID, SubSport(
//                                    subSports.gradeLevel ?: "",
//                                    subSports.sportId ?: "",
//                                    subSports.id ?: ""
//                                )
//                            )
//                            etSubSports.setText(subSports.name.toString())
//                            //initModel
//                            brandMakeList.put(mappingID, subSports.brandList!!)
//                            if (subSports.brandList.isNotEmpty()) {
//                                tvBrandMake.visibility = View.VISIBLE
//                            } else {
//                                tvBrandMake.visibility = View.VISIBLE
//                            }
//                        }.build().show(it)
//                }
//
//                //TODO: Used to chooser Brand/Make
//                etBrandMake?.setOnClickListener { it ->
//                    AddBrandPopUp.Builder()
//                        .addList(
//                            brandMakeList[addModelViewList[i]] ?: return@setOnClickListener
//                        )
//                        .setListener { model ->
//                            etModel.setText("")
//                            etType.setText("")
//                            etOther.setText("")
//                            val mappingID = addModelViewList[i]
//                            viewModel?.addBrandMakeHashMap?.put(mappingID, model)
//                            etBrandMake.setText(model.brandName)
//
//                            modelList.put(mappingID, model.modelList ?: return@setListener)
//                        }.build().show(it)
//                }
//
//                //TODO: Used to chooser Model
//                etModel?.setOnClickListener { it ->
//                    AddModelPopUp.Builder()
//                        .addList(
//                            modelList.get(addModelViewList[i]) ?: return@setOnClickListener
//                        )
//                        .setListener { model ->
//                            etType.setText("")
//                            etOther.setText("")
//                            etModel.setText(model.modelName)
//
//                            val mappingID = addModelViewList[i]
//                            viewModel?.addModelHashMap?.put(mappingID, model)
//
//
//                            typeList.put(mappingID, model.typeList!!)
//
//                        }.build().show(it)
//                }
//
//                //TODO: Used to chooser Wheel Size
//                etType?.setOnClickListener { it ->
//                    AddTypePopup.Builder()
//                        .addList(
//                            typeList.get(addModelViewList[i]) ?: return@setOnClickListener
//                        )
//                        .setListener { type ->
//                            etType.setText(type.type)
//                            val mappingID = addModelViewList[i]
//                            viewModel?.addTypeHashMap?.put(mappingID, type)
//                        }.build().show(it)
//                }
//
//
//                //Edit equipments
//                if (viewModel?.isEdit?.get() ?: return) {
//                    for (f in 0 until viewModel?.subSportList?.size!!) {
//                        val mappingID = addModelViewList[f]
//                        if (viewModel?.editSubSportData?.get(i)?.subSportId == viewModel?.subSportList?.get(f)?.id) {
//                            val subSportsPreSelected = subSportsList.get(mappingID)?.get(f)
//                            viewModel?.addSubSportHashMap?.put(
//                                mappingID, SubSport(
//                                    subSportsPreSelected?.gradeLevel ?: "",
//                                    subSportsPreSelected?.sportId ?: "",
//                                    subSportsPreSelected?.id ?: ""
//                                )
//                            )
//                            etSubSports.setText(subSportsPreSelected?.name)
//
//                            brandMakeList.put(
//                                mappingID,
//                                subSportsPreSelected?.brandList ?: return
//                            )
//                            for (j in 0 until subSportsPreSelected.brandList.size) {
//                                if (viewModel?.editEquipmentData?.get(f)?.brandMakeId == subSportsPreSelected.brandList.get(
//                                        j
//                                    ).brandId
//                                ) {
//                                    val brandMake =
//                                        subSportsPreSelected.brandList.get(j) ?: return
//                                    tvBrandMake.visibility = View.VISIBLE
//                                    etBrandMake.setText(brandMake.brandName)
//                                    viewModel?.addBrandMakeHashMap?.put(mappingID, brandMake)
//                                    modelList.put(mappingID, brandMake.modelList ?: return)
//
//                                    for (k in 0 until brandMake.modelList.size) {
//                                        if (viewModel?.editEquipmentData?.get(f)?.modelId == brandMake.modelList[k].modelId) {
//                                            val model = brandMake.modelList[k]
//                                            tvModel.visibility = View.VISIBLE
//                                            etModel.setText(model.modelName)
//                                            viewModel?.addModelHashMap?.put(mappingID, model)
//
//                                            typeList.put(
//                                                mappingID,
//                                                model.typeList ?: return
//                                            )
//                                            for (l in 0 until model.typeList.size) {
//                                                if (viewModel?.editEquipmentData?.get(f)?.equipmentType == model.typeList.get(
//                                                        l
//                                                    ).type
//                                                ) {
//                                                    val type = model.typeList.get(l)
//                                                    etType.setText(type.type)
//                                                    viewModel?.addTypeHashMap?.put(
//                                                        mappingID,
//                                                        type
//                                                    )
//                                                }
//                                            }
//                                        }
//                                    }
//                                }
//                            }
//                        }
//
//                        etOther.setText(viewModel?.editEquipmentData?.get(f)?.other)
//                        viewModel?.addOtherMap?.put(
//                            mappingID,
//                            viewModel?.editEquipmentData?.get(f)?.other ?: ""
//                        )
//
//                    }
//                }
//            }
//        }
//        else if (viewModel?.isEdit?.get() == false) {
//            for (i in 0 until addModelViewList.size) {
//                //init variable
//                val deleteLayout =
//                    addModelViewList[i].findViewById<ImageView>(R.id.deleteLayout)
//                val etSubSports =
//                    addModelViewList[i].findViewById<TextInputEditText>(R.id.etSubSports)
//
//                val tvBrandMake =
//                    addModelViewList[i].findViewById<TextInputLayout>(R.id.tvBrandMake)
//                val etBrandMake =
//                    addModelViewList[i].findViewById<TextInputEditText>(R.id.etBrandMake)
//                val etType =
//                    addModelViewList[i].findViewById<TextInputEditText>(R.id.etType)
//
//                val tvModel = addModelViewList[i].findViewById<TextInputLayout>(R.id.tvModel)
//                val etModel = addModelViewList[i].findViewById<TextInputEditText>(R.id.etModel)
//                val etOther = addModelViewList[i].findViewById<TextInputEditText>(R.id.eOther)
//
//                subSportsList[addModelViewList[i]] = (viewModel?.subSportList ?: return)
//
//                //TODO: DELETE LAYOUT
//                deleteLayout?.setOnClickListener { it ->
//                    viewDataBinding.modelLayoutContainer.removeView(addModelViewList[i])
//                    viewModel?.addBrandMakeHashMap?.remove(addModelViewList[i])
//                    viewModel?.addModelHashMap?.remove(addModelViewList[i])
//                    viewModel?.addTypeHashMap?.remove(addModelViewList[i])
//
//                    subSportsList.remove(addModelViewList[i])
//                    brandMakeList.remove(addModelViewList[i])
//                    modelList.remove(addModelViewList[i])
//                    typeList.remove(addModelViewList[i])
//                }
//
//                //TODO: Used to chooser Sub sports category
//                etSubSports?.setOnClickListener { it ->
//                    ChallengePopUp.Builder()
//                        .addList(
//                            subSportsList.get(addModelViewList.get(i))
//                                ?: return@setOnClickListener
//                        )
//                        .setListener { subSports ->
//                            etBrandMake.setText("")
//                            etModel.setText("")
//                            etType.setText("")
//                            etOther.setText("")
//
//                            val mappingID = addModelViewList[i]
//                            viewModel?.addSubSportHashMap?.put(
//                                mappingID, SubSport(
//                                    subSports.gradeLevel ?: "",
//                                    subSports.sportId ?: "",
//                                    subSports.id ?: ""
//                                )
//                            )
//                            etSubSports.setText(subSports.name.toString())
//                            //initModel
//                            brandMakeList.put(mappingID, subSports.brandList!!)
//                            if (subSports.brandList.isNotEmpty()) {
//                                tvBrandMake.visibility = View.VISIBLE
//                            } else {
//                                tvBrandMake.visibility = View.VISIBLE
//                            }
//                        }.build().show(it)
//                }
//
//                //TODO: Used to chooser Brand/Make
//                etBrandMake?.setOnClickListener { it ->
//                    AddBrandPopUp.Builder()
//                        .addList(
//                            brandMakeList[addModelViewList[i]] ?: return@setOnClickListener
//                        )
//                        .setListener { model ->
//                            etModel.setText("")
//                            etType.setText("")
//                            etOther.setText("")
//
//                            val mappingID = addModelViewList[i]
//                            viewModel?.addBrandMakeHashMap?.put(mappingID, model)
//                            etBrandMake.setText(model.brandName)
//
//                            modelList.put(mappingID, model.modelList ?: return@setListener)
//                            if (!model.modelList.isEmpty()) {
//                                tvModel.visibility = View.VISIBLE
//                            } else {
//                                tvModel.visibility = View.GONE
//                            }
//                        }.build().show(it)
//                }
//
//                //TODO: Used to chooser Model
//                etModel?.setOnClickListener { it ->
//                    AddModelPopUp.Builder()
//                        .addList(
//                            modelList.get(addModelViewList[i]) ?: return@setOnClickListener
//                        )
//                        .setListener { model ->
//                            etType.setText("")
//                            etOther.setText("")
//                            etModel.setText(model.modelName)
//
//                            val mappingID = addModelViewList[i]
//                            viewModel?.addModelHashMap?.put(mappingID, model)
//
//                            typeList.put(mappingID, model.typeList!!)
//                        }.build().show(it)
//                }
//
//                //TODO: Used to chooser Wheel Size
//                etType?.setOnClickListener { it ->
//                    AddTypePopup.Builder()
//                        .addList(
//                            typeList.get(addModelViewList[i]) ?: return@setOnClickListener
//                        )
//                        .setListener { type ->
//                            etType.setText(type.type)
//                            val mappingID = addModelViewList[i]
//                            viewModel?.addTypeHashMap?.put(mappingID, type)
//                        }.build().show(it)
//                }
//
//
//                //Edit equipments
//                if (viewModel?.isEdit?.get() ?: return) {
//                    val mappingID = addModelViewList[i]
//
//                    if (viewModel?.editSubSportData?.get(i)?.subSportId == viewModel?.subSportList?.get(
//                            i
//                        )?.id
//                    ) {
//                        val subSportsPreSelected = viewModel?.subSportList?.get(i)
//                        viewModel?.addSubSportHashMap?.put(
//                            mappingID, SubSport(
//                                subSportsPreSelected?.gradeLevel ?: "",
//                                subSportsPreSelected?.sportId ?: "",
//                                subSportsPreSelected?.id ?: ""
//                            )
//                        )
//                        etSubSports.setText(subSportsPreSelected?.name)
//                    }
//                    viewModel?.subSportList?.get(i)
//
//
//                    brandMakeList.put(
//                        mappingID,
//                        viewModel?.subSportList?.get(i)?.brandList ?: return
//                    )
//                    for (j in 0 until brandMakeList.size) {
//                        if (viewModel?.editEquipmentData?.get(i)?.brandMakeId == brandMakeList.get(
//                                mappingID
//                            )?.get(j)?.brandId
//                        ) {
//                            val brandMake = brandMakeList.get(mappingID)?.get(j) ?: return
//                            tvBrandMake.visibility = View.VISIBLE
//                            etBrandMake.setText(brandMake.brandName)
//                            viewModel?.addBrandMakeHashMap?.put(mappingID, brandMake)
//                            modelList.put(mappingID, brandMake.modelList ?: return)
//
//                            for (k in 0 until brandMake.modelList.size) {
//                                if (viewModel?.editEquipmentData?.get(i)?.modelId == brandMake.modelList[k].modelId) {
//                                    val model = modelList.get(mappingID)?.get(k) ?: return
//                                    tvModel.visibility = View.VISIBLE
//                                    etModel.setText(model.modelName)
//                                    viewModel?.addModelHashMap?.put(mappingID, model)
//
//                                    typeList.put(mappingID, model.typeList ?: return)
//                                    for (l in 0 until brandMake.modelList[k].wheelSizeList?.size!!) {
//                                        if (viewModel?.editEquipmentData?.get(i)?.equipmentType == model.typeList.get(
//                                                l
//                                            ).type
//                                        ) {
//                                            val type = model.typeList.get(l)
//
//                                            etType.setText(type.type)
//                                            viewModel?.addTypeHashMap?.put(mappingID, type)
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                    }
//
//
//                    etOther.setText(viewModel?.editEquipmentData?.get(i)?.other)
//                    viewModel?.addOtherMap?.put(
//                        mappingID,
//                        viewModel?.editEquipmentData?.get(i)?.other ?: ""
//                    )
//
//                }
//            }
//        }
//    }

    override fun filterDataRequestToSend() {
        for (i in 0 until addModelViewList.size) {
            viewModel?.requestSubSportData?.add(
                viewModel?.addSubSportHashMap?.get(addModelViewList[i]) ?: return
            )
            val sports = viewModel?.addSubSportHashMap?.get(addModelViewList[i])
            val brandMake = viewModel?.addBrandMakeHashMap?.get(addModelViewList[i])
            val model = viewModel?.addModelHashMap?.get(addModelViewList[i])
            val equipmentType = viewModel?.addTypeHashMap?.get(addModelViewList[i])?.type
//            val frame = viewModel?.addFrameHashMap?.get(addModelViewList[i])
//            val year = viewModel?.addYearHashMap?.get(addModelViewList[i])
//            val equipmentType =
//                (addModelViewList[i].findViewById<TextInputEditText>(R.id.etEquipment).text).toString()
            val other =
                (addModelViewList[i].findViewById<TextInputEditText>(R.id.eOther).text).toString()

            if (brandMake?.brandId != null)
                viewModel?.requestEquipmentData?.add(
                    EquipmentData(
                        brandMake.brandId,
                        brandMake.brandName ?: "",
                        equipmentType ?: "",
                        "",
                        model?.modelId ?: "",
                        model?.modelName ?: "",
                        other,
                        sports?.sportId ?: "",
                        sports?.subSportId ?: "",
                        sports?.subSportName ?: "",
                        "equipment",
                        "",
                        ""
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

    override fun onBackPress() {

    }


}