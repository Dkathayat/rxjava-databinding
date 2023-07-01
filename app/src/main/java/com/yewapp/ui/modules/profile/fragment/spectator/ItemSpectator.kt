package com.yewapp.ui.modules.profile.fragment.spectator

import android.view.View
import androidx.databinding.ObservableField
import com.yewapp.R
import com.yewapp.data.network.api.spectator.SpectatorMember
import com.yewapp.utils.DateUtils
import com.yewapp.utils.createNameWhenNoImage
import com.yewapp.utils.popup.PopupSpectatorOptions

class ItemSpectator(
    val item: SpectatorMember,
    val listener: OnSpectatorOptionClickListener,
    val position: Int
) {

    var shortName = ObservableField<String>("")
    val isImageAvailable = ObservableField<Boolean>(false)
    val chatOptionVisibility = ObservableField<Boolean>(false)

    val cityCountryOrMobileNumber = ObservableField<String>("")
    val createdOn = ObservableField<String>("")

    init {
        if (!item.userFrom.equals("phoneContact",ignoreCase = true)) {
            cityCountryOrMobileNumber.set("${item.city}, ${item.country}")
            chatOptionVisibility.set(true)
        } else {
            cityCountryOrMobileNumber.set(item.phoneContact)
            chatOptionVisibility.set(false)
        }

        if (!item.profileImage.isNullOrEmpty()) {
            isImageAvailable.set(true)
        } else {
            isImageAvailable.set(false)
            shortName.set(createNameWhenNoImage(item.fullName ?: item.firstName ?: "YW"))
        }
        createdOn.set("Created ${DateUtils.getDateFromUTCDateTime(item.createdOn, "dd/MM/yyyy")}")
    }


    fun onItemClick(view: View) {
        when (view.id) {
            R.id.ivChat -> {
                listener.onChatClicked(item)
            }
            R.id.ivOptions -> {
                PopupSpectatorOptions.showPopUp(view, item, item.userId) {
                    when (it) {
//                        "Migrate Account" -> listener.onMigrateAccountClicked(item)
                        "Remove Account" -> listener.onRemoveAccountClicked(item)
//                        "Edit Account" -> listener.onEditClicked(item)
//                        "Manage Permission" -> listener.onManagePermissionClicked(item)
//                        "Activate Account" -> listener.onDeActiveAccountClicked(item)
//                        "Deactivate Account" -> listener.onDeActiveAccountClicked(item)
                    }
                }

            }

        }
    }

    interface OnSpectatorOptionClickListener {
        //        fun onMigrateAccountClicked(associateItem: Associate)
        fun onRemoveAccountClicked(associateItem: SpectatorMember)

        //        fun onEditClicked(associateItem: Associate)
//        fun onDeActiveAccountClicked(associateItem: Associate)
//        fun onManagePermissionClicked(associateItem: Associate)
        fun onChatClicked(associateItem: SpectatorMember)
    }


}