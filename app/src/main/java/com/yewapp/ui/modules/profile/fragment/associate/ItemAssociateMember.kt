package com.yewapp.ui.modules.profile.fragment.associate

import android.view.View
import androidx.databinding.ObservableField
import com.yewapp.R
import com.yewapp.data.network.api.associate.Associate
import com.yewapp.utils.DateUtils
import com.yewapp.utils.createNameWhenNoImage
import com.yewapp.utils.popup.PopupAssociateOptions

class ItemAssociateMember(
    val item: Associate,
    val listener: OnAssociateOptionClickListener,
//    val listener: ItemAssociateMember.OnItemClickListener,
    var index: Int
) {

    var ageRelation = ObservableField<String>("")
    val createdOn = ObservableField<String>("")
    val shortName = ObservableField<String>("")
    val isImageAvailable = ObservableField<Boolean>(false)


    init {
        ageRelation.set("${item.age}, ${item.relation}")
        if (!item.profileImage.isNullOrEmpty()) {
            isImageAvailable.set(true)
        } else {
            isImageAvailable.set(false)
            shortName.set(createNameWhenNoImage(item.name ?: "YW"))
        }

        createdOn.set("Created ${DateUtils.getDateTimeFromTimeStamp(item.createdOn)}")
    }


//    interface OnItemClickListener {
//        fun onClickItem(item: Associate)
//    }

    fun onItemClick(view: View) {
        when (view.id) {
            R.id.ivChat -> {
                listener.onChatClicked(item)
            }
            R.id.ivOptions -> {
//                listener.onClickItem(item)

                PopupAssociateOptions.showPopUp(view, item, item.userId) {
                    when (it) {
                        "Migrate Account" -> listener.onMigrateAccountClicked(item)
                        "Remove Account" -> listener.onRemoveAccountClicked(item)
                        "Edit Account" -> listener.onEditClicked(item)
                        "Manage Permission" -> listener.onManagePermissionClicked(item)
                        "Activate Account" -> listener.onDeActiveAccountClicked(item)
                        "Deactivate Account" -> listener.onDeActiveAccountClicked(item)
                    }
                }

            }
        }
    }

    interface OnAssociateOptionClickListener {
        //                fun onOptionMenuClick(option: String, feed: Feed, position: Int)
        fun onMigrateAccountClicked(associateItem: Associate)
        fun onRemoveAccountClicked(associateItem: Associate)
        fun onEditClicked(associateItem: Associate)
        fun onDeActiveAccountClicked(associateItem: Associate)
        fun onManagePermissionClicked(associateItem: Associate)
        fun onChatClicked(associateItem: Associate)
    }


}


