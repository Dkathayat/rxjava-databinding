package com.yewapp.ui.modules.refer.vm

import android.graphics.Bitmap
import android.widget.CompoundButton
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.yewapp.MainApplication
import com.yewapp.R
import com.yewapp.data.network.api.refer.PhoneContacts
import com.yewapp.utils.FetchContactsUtil

class ItemChooseContactsViewModel(
    val phoneContacts: PhoneContacts,
    val listener: OnItemClickListener
) : ViewModel() {

    var contactName = ObservableField<String>("")
    var number = ObservableField<String>("")
    var bitmap = ObservableField<Bitmap>()
    val profileImageUrl = ObservableField<String>("")
    var checkBox = ObservableField(false)


    init {
        if (phoneContacts.isBitmap) {
            bitmap.set(
                FetchContactsUtil.convertDrawableBitmap(
                    MainApplication.mContext.getDrawable(
                        R.drawable.ic_profile
                    )
                )
            )
        } else {
//            CoroutineScope(Dispatchers.IO).launch {
//                val bitmapAsync = async {
//                    val bitmap =
//                        FetchContactsUtil.retrieveContactPhoto(phoneContacts.phone)
//                    bitmap
//                }
//                val bitmapIMage = bitmapAsync.await()
//                launch(Dispatchers.Main) {
//                    bitmap.set(bitmapIMage)
//                }
//            }
        }

    }
    interface OnItemClickListener {
        fun onAddItem(phoneContacts: PhoneContacts)
        fun onRemoveItem(phoneContacts: PhoneContacts)
    }

    fun onCheckedChange(button: CompoundButton?, check: Boolean) {
        phoneContacts.isSelected = check
        if (check) {
            listener.onAddItem(phoneContacts)
        } else {
            return listener.onRemoveItem(phoneContacts)
        }
    }
}
