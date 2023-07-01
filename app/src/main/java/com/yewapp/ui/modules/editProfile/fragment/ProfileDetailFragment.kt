package com.yewapp.ui.modules.editProfile.fragment

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions
import com.yewapp.R
import com.yewapp.data.network.api.profile.City
import com.yewapp.data.network.api.profile.Country
import com.yewapp.data.network.api.profile.State
import com.yewapp.databinding.FragmentProfileDetailBinding
import com.yewapp.ui.base.BaseFragment
import com.yewapp.ui.common.DatePicker
import com.yewapp.ui.common.GenericListBottomSheet
import com.yewapp.ui.modules.dashboard.DashboardActivity
import com.yewapp.ui.modules.editProfile.extras.EditProfileExtras
import com.yewapp.ui.modules.editProfile.navigator.ProfileDetailNavigator
import com.yewapp.ui.modules.editProfile.vm.ProfileDetailViewModel
import com.yewapp.ui.modules.emailchange.EmailChangeActivity
import com.yewapp.ui.modules.emailchange.extras.EmailPhoneChangeExtras
import com.yewapp.utils.LocationHelper
import com.yewapp.utils.getDateTime
import com.yewapp.utils.is18
import com.yewapp.utils.parseToUTC
import java.text.SimpleDateFormat
import java.util.*

class ProfileDetailFragment(override val layoutId: Int = R.layout.fragment_profile_detail) :
    BaseFragment<ProfileDetailNavigator, ProfileDetailViewModel, FragmentProfileDetailBinding>(),
    ProfileDetailNavigator {


    companion object {
        fun newInstance(associateID: String): ProfileDetailFragment {
            //can not send without bundle else will give null pointer error
            val fragment = ProfileDetailFragment()
            val args = Bundle()
            args.putString(EditProfileExtras.ASSOCIATE_ID, associateID)

            fragment.arguments = args
            return fragment
        }
    }


    override fun onViewModelCreated(viewModel: ProfileDetailViewModel) {
        viewModel.setNavigator(this)
    }

    override fun onViewBound(viewDataBinding: FragmentProfileDetailBinding) {
        Mapbox.getInstance(requireActivity(), getString(R.string.mapbox_access_token))
    }

    override fun onCountryClicked() {
        showBottomSheet(
            getString(R.string.choose_country),
            viewModel?.countryList as MutableList<Country>
        ) {
            viewModel?.selectedCountryId = it.id
            viewModel?.selectedCountry?.set(it.name)
            viewModel?.getStates()
        }
    }

    override fun onStateClicked() {
        if (viewModel?.selectedCountry?.get().toString().isNotEmpty()) {
            showBottomSheet(
                getString(R.string.choose_state),
                viewModel?.stateList as MutableList<State>
            ) {
                viewModel?.selectedStateId = it.id
                viewModel?.selectedState?.set(it.name)
                viewModel?.getCities()
            }
        } else {
            Toast.makeText(
                requireActivity(),
                viewModel?.dataManager?.getResourceProvider()?.getString(R.string.country_id_error),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onCityClicked() {
        if (!viewModel?.selectedState?.get().isNullOrEmpty()) {
            showBottomSheet(
                getString(R.string.choose_city),
                viewModel?.cityList as MutableList<City>
            ) {

                viewModel?.selectedCityId = it.id
                viewModel?.selectedCity?.set(it.name)
            }
        } else {
            Toast.makeText(
                requireActivity(),
                viewModel?.dataManager?.getResourceProvider()?.getString(R.string.state_id_error),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onEmailClick() {
        startActivity(
            intentProviderFactory.create(
                EmailChangeActivity::class.java,
                EmailPhoneChangeExtras.emailPhoneChangeExtras {
                    isEmailChange = true
                    isPhoneChange = false
                },
                0
            )
        )
    }

    override fun onPhoneClick() {
        startActivity(
            intentProviderFactory.create(
                EmailChangeActivity::class.java,
                EmailPhoneChangeExtras.emailPhoneChangeExtras {
                    isEmailChange = false
                    isPhoneChange = true
                },
                0
            )
        )

    }

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
                if (!is18(
                        getDateTime(dobSelected),
                        getDateTime("")
                    )
                ) {
                    onError(Throwable(getString(R.string.only_18_users_are_allowed_to_create_a_yew_account)))
                    return@setmConfirmListener
                }
                viewModel?.dobDisplayed?.set(it.parseToUTC("dd/MM/yyyy"))//display data format
                viewModel?.dobSent = it.parseToUTC().format("yyyy-mm-dd")//sent date format
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

    override fun onAddressFieldClick() {
        val intent = PlaceAutocomplete.IntentBuilder()
            .accessToken(
                ((if (Mapbox.getAccessToken() != null) Mapbox.getAccessToken() else getString(R.string.mapbox_access_token))!!)
            )
            .placeOptions(
                PlaceOptions.builder()
                    .backgroundColor(Color.parseColor("#FFFFFF"))
                    .limit(10)
//                    .addInjectedFeature(home)
//                    .addInjectedFeature(work)
                    .build(PlaceOptions.MODE_CARDS)
            )
            .build(requireActivity())
        searchPlaceActivityResult.launch(intent)
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

    override fun onBackPress() {

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        return bind(ProfileDetailViewModel::class.java, inflater, container)
    }

    var searchPlaceActivityResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            val searchedAddress = PlaceAutocomplete.getPlace(it.data)
            viewModel?.address?.set(searchedAddress.placeName() ?: "")
            viewModel?.latitude = searchedAddress.center()?.latitude().toString()
            viewModel?.longitude = searchedAddress.center()?.longitude().toString()
            viewModel?.setPlaceData(
                LocationHelper(requireActivity()).getAddressFromLatLng(
                    viewModel?.latitude?.toDouble() ?: 0.0,
                    viewModel?.longitude?.toDouble() ?: 0.0
                )
            )
        }
    }
}