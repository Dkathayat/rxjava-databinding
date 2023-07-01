package com.yewapp.ui.modules.addassociatememberdetails.fragment.profiledetails

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.yewapp.R
import com.yewapp.databinding.FragmentAssociateProfileDetailsBinding
import com.yewapp.ui.base.BaseFragment
import com.yewapp.ui.common.DatePicker
import com.yewapp.ui.common.GenericListBottomSheet
import com.yewapp.ui.modules.addassociatememberdetails.AddMembersDetailsActivity
import com.yewapp.ui.modules.addassociatememberdetails.AddMembersDetailsViewModel
import com.yewapp.ui.modules.addassociatepermission.AddAssociatePermissionActivity
import com.yewapp.ui.modules.addassociatepermission.AddAssociatePermissionExtras
import com.yewapp.ui.modules.dashboard.DashboardActivity
import com.yewapp.utils.parseToUTC
import java.text.SimpleDateFormat
import java.util.*

class AssociateProfileDetailsFragment(override val layoutId: Int = R.layout.fragment_associate_profile_details) :
    BaseFragment<AssociateProfileDetailsNavigator, AssociateProfileDetailsViewModel, FragmentAssociateProfileDetailsBinding>(),
    AssociateProfileDetailsNavigator {

//    var searchPlaceActivityResult = registerForActivityResult(
//        ActivityResultContracts.StartActivityForResult()
//    ) {
//        if (it.resultCode == Activity.RESULT_OK) {
//            val searchedAddress = PlaceAutocomplete.getPlace(it.data)
//            viewModel?.address?.set(searchedAddress.placeName() ?: "")
//            viewModel?.latitude = searchedAddress.center()?.latitude().toString()
//            viewModel?.longitude = searchedAddress.center()?.longitude().toString()
//            viewModel?.setPlaceData(
//                LocationHelper(requireActivity()).getAddressFromLatLng(
//                    viewModel?.latitude?.toDouble() ?: 0.0,
//                    viewModel?.longitude?.toDouble() ?: 0.0
//                )
//            )
//        }
//    }


    lateinit var addMembersDetailsViewModel: AddMembersDetailsViewModel


    companion object {
        fun newInstance(extras: Bundle): AssociateProfileDetailsFragment {
            //can not send without bundle else will give null pointer error
            val fragment = AssociateProfileDetailsFragment()
            var args = Bundle()
            args = extras
            fragment.arguments = args
            return fragment
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return bind(AssociateProfileDetailsViewModel::class.java, inflater, container)

    }

    override fun onViewModelCreated(viewModel: AssociateProfileDetailsViewModel) {
        viewModel.setNavigator(this)
        //NEW Updated method of shared viewModel
        addMembersDetailsViewModel =
            ViewModelProvider(requireActivity())[AddMembersDetailsViewModel::class.java]
        //OLD
//            ViewModelProviders().of(requireActivity())[AddMembersDetailsViewModel::class.java]
    }

    override fun onViewBound(viewDataBinding: FragmentAssociateProfileDetailsBinding) {
    }

//    override fun onCountryClicked() {
//        showBottomSheet(
//            getString(R.string.choose_country),
//            viewModel?.countryList as MutableList<Country>
//        ) {
//            viewModel?.selectedCountryId = it.id
//            viewModel?.selectedCountry?.set(it.name)
//            viewModel?.getStates()
//        }
//    }
//
//    override fun onStateClicked() {
//        if (viewModel?.selectedCountry?.get().toString().isNotEmpty()) {
//            showBottomSheet(
//                getString(R.string.choose_state),
//                viewModel?.stateList as MutableList<State>
//            ) {
//                viewModel?.selectedStateId = it.id
//                viewModel?.selectedState?.set(it.name)
//                viewModel?.getCities()
//            }
//        } else {
//            Toast.makeText(
//                requireActivity(),
//                viewModel?.dataManager?.getResourceProvider()?.getString(R.string.country_id_error),
//                Toast.LENGTH_LONG
//            ).show()
//        }
//    }
//
//    override fun onCityClicked() {
//        if (!viewModel?.selectedState?.get().isNullOrEmpty()) {
//            showBottomSheet(
//                getString(R.string.choose_city),
//                viewModel?.cityList as MutableList<City>
//            ) {
//
//                viewModel?.selectedCityId = it.id
//                viewModel?.selectedCity?.set(it.name)
//            }
//        } else {
//            Toast.makeText(
//                requireActivity(),
//                viewModel?.dataManager?.getResourceProvider()?.getString(R.string.state_id_error),
//                Toast.LENGTH_LONG
//            ).show()
//        }
//    }

//    override fun onEmailClick() {
//        startActivity(
//            intentProviderFactory.create(
//                EmailChangeActivity::class.java,
//                EmailPhoneChangeExtras.emailPhoneChangeExtras {
//                    isEmailChange = true
//                    isPhoneChange = false
//                },
//                0
//            )
//        )
//    }

//    override fun onPhoneClick() {
//        startActivity(
//            intentProviderFactory.create(
//                EmailChangeActivity::class.java,
//                EmailPhoneChangeExtras.emailPhoneChangeExtras {
//                    isEmailChange = false
//                    isPhoneChange = true
//                },
//                0
//            )
//        )
//
//    }

    override fun onDobClicked() {
        DatePicker.Builder()
            .isRangeCalendar(false)
            .setmConfirmListener {
                if (it.isNullOrEmpty()) return@setmConfirmListener
                val dobSelected = it.parseToUTC("dd/MM/yyyy")
                val ca = Calendar.getInstance().time
                println("Current time => $ca")
                val df = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val currentDate = df.format(ca)
                println("DOB TIME$dobSelected")
                println("formattedDate TIME$currentDate")
//                if (!is18(
//                        getDateTime(dobSelected),
//                        getDateTime("")
//                    )
//                ) {
//                    onError(Throwable(getString(R.string.only_18_users_are_allowed_to_create_a_yew_account)))
//                    return@setmConfirmListener
//                }
                viewModel?.dobDisplayed?.set(it.parseToUTC("dd/MM/yyyy"))//display data format
//                viewModel?.dobSent?.set(it.parseToUTC().format("yyyy-mm-dd"))//sent date format
//                viewModel?.dobSent = it.parseToUTC()//sent date format
            }
            .build()
            .show(childFragmentManager)
    }

    override fun onGenderClicked() {
        showBottomSheet(
            getString(R.string.gender),
            mutableListOf("Male", "Female")
        ) {
            viewModel?.gender?.set(it) ?: return@showBottomSheet
        }
    }

    override fun onStatusClicked() {
        showBottomSheet(
            getString(R.string.gender),
            mutableListOf("Active", "Inactive")
        ) {
            viewModel?.yewStatus?.set(it) ?: return@showBottomSheet
            viewModel?.yewStatusSent?.set(it) ?: return@showBottomSheet
        }
    }

    override fun navigateToDashboard() {
        startActivity(
            intentProviderFactory.create(
                DashboardActivity::class.java,
                null,
                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            )
        )
        activity?.finish()
    }


    /**
     * @description: This method is used to redirect user to add associate permission with associate ID
     * @author: Narbir Singh
     */
    override fun navigateNextScreen() {
        val extras = AddAssociatePermissionExtras.associateIDExtras {
            this.associateId = viewModel?.associateId?.get() ?: return
            this.isUpdate = false
        }
        startActivity(
            intentProviderFactory.create(
                AddAssociatePermissionActivity::class.java,
                extras,
                0
            )
        )
    }

    /**
     * @description: This method is used to to show update associate profile success
     * OR Redirect to add or update associate permissions
     * @author: Narbir Singh
     */
    override fun updateAssociateProfileSuccess(updateMessage: String) {

        Toast.makeText(requireContext(), updateMessage, Toast.LENGTH_SHORT).show()
        activity?.finish()
//        val extras = AddAssociatePermissionExtras.associateIDExtras {
//            this.associateId = viewModel?.associateId?.get() ?: return
//            this.isUpdate = true
//        }
//        startActivity(
//            intentProviderFactory.create(
//                AddAssociatePermissionActivity::class.java,
//                extras,
//                0
//            )
//        )
    }


    /**
     * @description: This method is used to update Associate ID on shared/common viewModel
     * Update associate ID
     * @author: Narbir Singh
     */
    override fun updateAssociateID(associateId: String) {
        addMembersDetailsViewModel.associateID.set(associateId.toInt())
    }


//    override fun onAddressFieldClick() {
//        val intent = PlaceAutocomplete.IntentBuilder()
//            .accessToken(
//                ((if (Mapbox.getAccessToken() != null) Mapbox.getAccessToken() else getString(R.string.mapbox_access_token))!!)
//            )
//            .placeOptions(
//                PlaceOptions.builder()
//                    .backgroundColor(Color.parseColor("#FFFFFF"))
//                    .limit(10)
////                    .addInjectedFeature(home)
////                    .addInjectedFeature(work)
//                    .build(PlaceOptions.MODE_CARDS)
//            )
//            .build(requireActivity())
//        searchPlaceActivityResult.launch(intent)
//    }

    override fun onBackPress() {
    }



    fun <T> showBottomSheet(title: String, list: MutableList<T>, listener: (T) -> Unit) {
        GenericListBottomSheet.Builder<T>()
            .setTitleText(title)
            .setDataList(list)
            .setClickListener {
                listener(it)
            }
            .build()
            .show(requireActivity())
    }

}