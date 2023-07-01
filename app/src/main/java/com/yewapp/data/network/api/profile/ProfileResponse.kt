package com.yewapp.data.network.api.profile

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class CountryListResponse(
    val countryList: List<Country>
)

data class Country(
    val id: String,
    val name: String,
    @SerializedName("country_code")
    @Expose
    val countryCode: String
)

data class State(
    val id: String,
    val name: String,
    @SerializedName("country_Id")
    @Expose
    val countryId: String
)

data class City(
    val id: String,
    val name: String,

    @SerializedName("state_id") @Expose val cityId: String,
    @Transient var isSelected: Boolean = false
)

data class SportsLevel(
    val name: String
)

data class SportsVisibility(
    val name: String
)

@Parcelize
data class SportType(
    @SerializedName("id") @Expose val sportId: String,
    val parentId: String?,
    val name: String?,
    val icon: String?,
    @SerializedName("background_image") @Expose val backgroundImage: String?,
    val description: String?,
//    val equipList: List<Any>,
    val gradeLevel: String?,
    var isChecked: Boolean?,
    val isModelAdded: Boolean?,
//    val modelList: List<Any>,
    val profileType: String?,

    ) : Parcelable

data class CountryCode(val countryCodeList: List<CountryCodeList>?)

data class CountryCodeList(val countryId: Number?, val countryCode: String?)


