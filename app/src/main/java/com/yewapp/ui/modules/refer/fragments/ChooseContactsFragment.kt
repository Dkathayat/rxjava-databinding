package com.yewapp.ui.modules.refer.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.yewapp.R
import com.yewapp.data.network.api.refer.PhoneContacts
import com.yewapp.databinding.FragmentChooseContactsBinding
import com.yewapp.ui.base.BaseFragment
import com.yewapp.ui.modules.refer.adapter.ChooseContactsAdapter
import com.yewapp.ui.modules.refer.navigator.ChooseContactsNavigator
import com.yewapp.ui.modules.refer.vm.ChooseContactsViewModel
import com.yewapp.ui.modules.refer.vm.ItemChooseContactsViewModel
import com.yewapp.utils.CustomItemAnimator
import com.yewapp.utils.READ_CONTACTS
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pub.devrel.easypermissions.EasyPermissions


class ChooseContactsFragment(override val layoutId: Int = R.layout.fragment_choose_contacts) :
    BaseFragment<ChooseContactsNavigator, ChooseContactsViewModel, FragmentChooseContactsBinding>(),
    ChooseContactsNavigator, EasyPermissions.PermissionCallbacks {

    var adapter: ChooseContactsAdapter? = null
//    var timer: Timer? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return bind(ChooseContactsViewModel::class.java, inflater, container)
    }

    private fun checkPermission() {
        requestPermissions(
            arrayOf(android.Manifest.permission.READ_CONTACTS),
            getString(R.string.read_contact_permission),
            READ_CONTACTS
        ) {
            viewModel?.fetchContacts()
        }
    }


//    @SuppressLint("Range")
//    private fun getAllPhoneContacts(): ArrayList<PhoneContacts> {
//        Log.d("START", "Getting all Contacts")
//        val arrContacts: ArrayList<PhoneContacts> = ArrayList<PhoneContacts>()
//        var phoneContactInfo: PhoneContacts? = null
//
//        // create uri
//        val uri: Uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
//        val contentResolver = requireActivity().contentResolver
//        var cursor: Cursor? = contentResolver.query(
//            uri,
//            arrayOf(
//                ContactsContract.CommonDataKinds.Phone.NUMBER,
//                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
//                ContactsContract.CommonDataKinds.Phone._ID,
//                ContactsContract.CommonDataKinds.Phone.PHOTO_URI
//            ),
//            null,
//            null,
//            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
//        )
//        cursor!!.moveToFirst()
//        while (!cursor.isAfterLast) {
//            val contactNumber =
//                cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
//            val contactName =
//                cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
//            val phoneContactID =
//                cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID))
//
//            // receiving photo
//            var bitmap = retrieveContactPhoto(requireContext(), contactNumber)
//            if (bitmap == null) {
//                phoneContactInfo =
//                    PhoneContacts(contactNumber, contactName, phoneContactID, bitmap, true, false)
//            } else {
//                phoneContactInfo =
//                    PhoneContacts(contactNumber, contactName, phoneContactID, bitmap, false, false)
//
//                val conf = Bitmap.Config.ARGB_8888 // see other conf types
//                Bitmap.createBitmap(1, 1, conf)
//            }
//            arrContacts.add(phoneContactInfo)
//            cursor.moveToNext()
//        }
//        cursor.close()
//        list = arrContacts
//
//        requireActivity().runOnUiThread(Runnable {
//            adapter?.addItems(list)
//            viewModel!!.isLoading.set(false)
//        })
//
//
//        //Log.d("END", "Got all Contacts")
//        return arrContacts
//    }
//
//    private fun retrieveContactPhoto(context: Context, number: String?): Bitmap? {
//        val contentResolver: ContentResolver = context.contentResolver
//        var contactId: String? = null
//        val uri: Uri = Uri.withAppendedPath(
//            ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
//            Uri.encode(number)
//        )
//        val projection =
//            arrayOf(
//                ContactsContract.PhoneLookup.DISPLAY_NAME,
//                ContactsContract.PhoneLookup.NUMBER,
//                ContactsContract.PhoneLookup._ID,
//                ContactsContract.PhoneLookup.PHOTO_URI
//            )
//        val cursor: Cursor? = contentResolver.query(
//            uri,
//            projection,
//            null,
//            null,
//            null
//        )
//        if (cursor != null) {
//            while (cursor.moveToNext()) {
//                contactId =
//                    cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.PhoneLookup._ID))
//            }
//            cursor.close()
//        }
//
//        // create bitmap for image
//        var photo = BitmapFactory.decodeResource(
//            context.resources,
//            R.drawable.ic_add_friend
//        )
//        try {
//            if (contactId != null) {
//                val inputStream: InputStream? =
//                    ContactsContract.Contacts.openContactPhotoInputStream(
//                        context.contentResolver,
//                        ContentUris.withAppendedId(
//                            ContactsContract.Contacts.CONTENT_URI,
//                            contactId.toLong()
//                        )
//                    )
//                when (inputStream) {
//                    null -> {
//                        return null
//                    }
//                    else -> {
//                        photo = BitmapFactory.decodeStream(inputStream)
//                    }
//                }
//                inputStream.close()
//            }
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//        return photo
//    }

    override fun onBackPress() {
    }

    override fun onViewModelCreated(viewModel: ChooseContactsViewModel) {
        viewModel.setNavigator(this)
    }

    override fun onViewBound(viewDataBinding: FragmentChooseContactsBinding) {
        adapter = ChooseContactsAdapter(arrayListOf(),
            SparseBooleanArray(),
            object : ItemChooseContactsViewModel.OnItemClickListener {
                override fun onAddItem(phoneContacts: PhoneContacts) {
                    phoneContacts.isSelected = true
                    viewModel?.checkList?.add(
                        phoneContacts
                    )
                }

                override fun onRemoveItem(phoneContacts: PhoneContacts) {
                    phoneContacts.isSelected = false
                    viewModel?.checkList?.remove(
                        phoneContacts
                    )
                }
            })
        adapter?.setHasStableIds(true)
        viewDataBinding.recyclerChoose.setItemViewCacheSize(500)
        viewDataBinding.recyclerChoose.itemAnimator = CustomItemAnimator()
        viewDataBinding.recyclerChoose.adapter = adapter
        viewDataBinding.recyclerChoose.layoutManager = LinearLayoutManager(activity)

        viewModel?.isLoading?.set(true)
        addObserver()

        lifecycleScope.launch(Dispatchers.Default) {
            checkPermission()
            viewDataBinding.searchEdittext.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//                    if (timer != null) {
//                        timer?.cancel();
//                    }
                }

                override fun afterTextChanged(p0: Editable?) {
//                    timer = Timer()
//                    timer?.schedule(object : TimerTask() {
//                        override fun run() {
//                        }
//                    }, 1000)
                    adapter?.filter?.filter(p0.toString())
                }

            })
        }
    }

    private fun addObserver() {
        viewModel?.contactList?.observe(this, Observer {
            viewModel?.isLoading?.set(false)
            adapter?.addItems(it, viewModel?.selectedItems ?: return@Observer)
        })
    }
    /* private fun filter(text: String) {
         //new array list that will hold the filtered data
         val filterdNames: ArrayList<String> = ArrayList()

         //looping through existing elements
         for (s in list)
         {
             //if the existing elements contains the search input
             if (s.toString().contains(text.lowercase())) {
                 //adding the element to filtered list
                 filterdNames.add(s.toString())
             }
         }

         //calling a method of the adapter class and passing the filtered list
         adapter?.filterList(filterdNames)
     }
 */

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        when (requestCode) {
            READ_CONTACTS -> {
                checkPermission()
            }
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
    }
}
