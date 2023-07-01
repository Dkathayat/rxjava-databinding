package com.yewapp.ui.modules.addspectator

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.ContactsContract
import android.util.Log
import android.util.SparseBooleanArray
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.yewapp.R
import com.yewapp.data.network.api.associate.Associate
import com.yewapp.data.network.api.refer.PhoneContacts
import com.yewapp.data.network.api.spectator.YewMember
import com.yewapp.databinding.ActivityAddSpectatorBinding
import com.yewapp.ui.base.BaseActivity
import com.yewapp.ui.dialogs.challengepopupdialogs.ChallengeSingleSelectionPopUp
import com.yewapp.ui.modules.chat.ChatActivity
import com.yewapp.ui.modules.chat.extras.ChatActivityExtras
import com.yewapp.ui.modules.refer.adapter.ChooseContactsAdapter
import com.yewapp.ui.modules.refer.vm.ItemChooseContactsViewModel
import com.yewapp.utils.READ_CONTACTS
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import java.io.InputStream

class AddSpectatorActivity :
    BaseActivity<AddSpectatorNavigator, AddSpectatorViewModel, ActivityAddSpectatorBinding>(),
    AddSpectatorNavigator, ItemAddSpectatorMember.OnSpectatorOptionClickListener {

    var list = ArrayList<PhoneContacts>()
    var chooseContactsAdapter: ChooseContactsAdapter? = null


    private lateinit var addSpectatorMember: AddSpectatorAdapter


    override fun getLayout(): Int = R.layout.activity_add_spectator

    override fun init() {
        bind(AddSpectatorViewModel::class.java)
    }

    override fun onViewModelCreated(viewModel: AddSpectatorViewModel) {
        viewModel.setNavigator(this)
    }

    override fun onViewBound(viewDataBinding: ActivityAddSpectatorBinding) {
        adapterInitialize()
    }

    private fun adapterInitialize() {
        val list = mutableListOf<YewMember>()
        addSpectatorMember = AddSpectatorAdapter(
            list, this
        )
        viewDataBinding.rvSpectatorMembers.adapter = addSpectatorMember
//        viewDataBinding.popularRecycler!!.addOnScrollListener(viewModel!!.onScrollListener)
        addObserver()
    }

    private fun addObserver() {
        val viewModel = viewModel ?: return
        viewModel.yewMemberList.observe(this, Observer {
            addSpectatorMember.setItems(it)
        })
    }

    private fun initializeContactAdapter() {
        chooseContactsAdapter =
            ChooseContactsAdapter(
                list,
                SparseBooleanArray(),
                object : ItemChooseContactsViewModel.OnItemClickListener {
                    override fun onAddItem(phoneContacts: PhoneContacts) {
                        val allowLimitToAdd =
                            viewModel.dataManager.getSubscription().maxSpectatorLimit.toInt() - viewModel.spectatorCount.get()!!
                        if (viewModel.checkList.size > allowLimitToAdd) {
                            Toast.makeText(
                                this@AddSpectatorActivity,
                                "Please upgrade your subscription",
                                Toast.LENGTH_SHORT
                            ).show()
                            viewModel.isBtnActive.set(false)
                            return
                        } else {
                            viewModel.checkList.add(
                                phoneContacts
                            )
                            var count = 0
                            for (i in 0 until viewModel.checkList.size) {
                                if (viewModel.checkList[i].isSelected)
                                    count++
                            }
                            if (count == 0)
                                viewModel.isBtnActive.set(true)
                            else
                                viewModel.isBtnActive.set(false)
                        }
                    }

                    override fun onRemoveItem(phoneContacts: PhoneContacts) {
                        viewModel.checkList.remove(
                            phoneContacts
                        )
                        var count = 0
                        for (i in 0 until viewModel.checkList.size) {
                            if (viewModel.checkList[i].isSelected)
                                count++
                        }
                        if (count == 0)
                            viewModel.isBtnActive.set(false)
                        else
                            viewModel.isBtnActive.set(true)
                    }
                })
        chooseContactsAdapter?.setHasStableIds(true)
        viewDataBinding.rvContactNumber.adapter = chooseContactsAdapter
        viewDataBinding.rvContactNumber.layoutManager = LinearLayoutManager(this)

        viewModel.isLoading.set(true)

        lifecycleScope.launch(Dispatchers.Default) {


            checkPermission()


            /*    searchEditText?.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    }

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                    }

                    override fun afterTextChanged(p0: Editable?) {
                        // filter(p0.toString());
                    }

                })*/
        }
    }


    override fun chooseUserType(view: View) {
        ChallengeSingleSelectionPopUp.Builder().addList(viewModel.userTypeArray.toList())
            .setListener {
                viewModel.userFrom.set(it)
                viewModel.mutableYewMemberList.clear()
                viewModel.yewMemberList.value = mutableListOf()
                viewModel.isBtnActive.set(false)
                if (viewModel.userFrom.get() == viewModel.userTypeArray[0]) {
                    viewModel.userFromYew.set(true)
                    viewModel.getYewMemberList()
                } else {
                    viewModel.isLoading.set(true)
                    viewModel.userFromYew.set(false)
                    initializeContactAdapter()
                    lifecycleScope.launch(Dispatchers.Default) {
                        checkPermission()
                    }
                }
            }.build().show(view)

    }

    override fun navigateToSpectatorListing(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        onBackPress()
    }


    //Handle yew member click
    override fun onClickItem(item: YewMember, position: Int) {
        var count = 0
        viewModel.mutableYewMemberList[position] = item
        for (i in 0 until viewModel.mutableYewMemberList.size) {
            if (viewModel.mutableYewMemberList[i].isSelected)
                count++
        }
        if (count == 0) {
            viewModel.isBtnActive.set(false)
        } else {
            viewModel.isBtnActive.set(true)
        }
        val allowLimitToAdd =
            viewModel.dataManager.getSubscription().maxSpectatorLimit.toInt() - viewModel.spectatorCount.get()!!
        if (count > allowLimitToAdd) {
            Toast.makeText(
                this@AddSpectatorActivity,
                "Please upgrade your subscription",
                Toast.LENGTH_SHORT
            ).show()
            viewModel.isBtnActive.set(false)
            return
        }
    }

    override fun onChatClicked(item: YewMember, position: Int) {
        /**
         * @author: Narbir Singh
         * @description:
         * Case:1
         *         We Pass YEW MEMBER(Spectator) item data as Associate data object for common chat process
         */

        val extrasSpectatorDetails =
            ChatActivityExtras.associateDetailsExtras {
                associateDetails = Associate(
                    "N/A", "N/A", item.email, "${item.city}, ${item.country}",
                    item.fullName, item.profileImage, "Spectator", "1",
                    item.userId
                )
                isAssociate = false
            }
        startActivity(
            intentProviderFactory.create(
                ChatActivity::class.java, extrasSpectatorDetails, 0
            )
        )
    }


    private fun checkPermission() {
        requestPermissions(
            arrayOf(android.Manifest.permission.READ_CONTACTS),
            getString(R.string.read_contact_permission),
            READ_CONTACTS
        ) {
            getAllPhoneContacts()
        }
    }


    @SuppressLint("Range")
    private fun getAllPhoneContacts(): ArrayList<PhoneContacts> {
        Log.d("START", "Getting all Contacts")
        val arrContacts: ArrayList<PhoneContacts> = ArrayList<PhoneContacts>()
        var phoneContactInfo: PhoneContacts? = null

        // create uri
        val uri: Uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        val contentResolver = this.contentResolver
        val cursor: Cursor? = contentResolver.query(
            uri,
            arrayOf(
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone._ID,
                ContactsContract.CommonDataKinds.Phone.PHOTO_URI
            ),
            null,
            null,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
        )
        cursor!!.moveToFirst()
        while (!cursor.isAfterLast) {
            val contactNumber =
                cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
            val contactName =
                cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
            val phoneContactID =
                cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID))


            // receiving photo
            var bitmap = retrieveContactPhoto(this, contactNumber)
            if (bitmap == null) {
                phoneContactInfo =
                    PhoneContacts(contactNumber, contactName, phoneContactID, bitmap, true, false)
            } else {
                phoneContactInfo =
                    PhoneContacts(contactNumber, contactName, phoneContactID, bitmap, false, false)

                val conf = Bitmap.Config.ARGB_8888 // see other conf types
                Bitmap.createBitmap(1, 1, conf)
            }
            arrContacts.add(phoneContactInfo)
            cursor.moveToNext()
        }
        cursor.close()
        list = arrContacts

        runOnUiThread(Runnable {
            chooseContactsAdapter?.addItems(list, viewModel.selectedItems)
            viewModel.isLoading.set(false)
        })


        //Log.d("END", "Got all Contacts")
        return arrContacts
    }

    private fun retrieveContactPhoto(context: Context, number: String?): Bitmap? {
        val contentResolver: ContentResolver = context.contentResolver
        var contactId: String? = null
        val uri: Uri = Uri.withAppendedPath(
            ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
            Uri.encode(number)
        )
        val projection =
            arrayOf(
                ContactsContract.PhoneLookup.DISPLAY_NAME,
                ContactsContract.PhoneLookup.NUMBER,
                ContactsContract.PhoneLookup._ID,
                ContactsContract.PhoneLookup.PHOTO_URI
            )
        val cursor: Cursor? = contentResolver.query(
            uri,
            projection,
            null,
            null,
            null
        )
        if (cursor != null) {
            while (cursor.moveToNext()) {
                contactId =
                    cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.PhoneLookup._ID))
            }
            cursor.close()
        }

        // create bitmap for image
        var photo = BitmapFactory.decodeResource(
            context.resources,
            R.drawable.ic_add_friend
        )
        try {
            if (contactId != null) {
                val inputStream: InputStream? =
                    ContactsContract.Contacts.openContactPhotoInputStream(
                        context.contentResolver,
                        ContentUris.withAppendedId(
                            ContactsContract.Contacts.CONTENT_URI,
                            contactId.toLong()
                        )
                    )
                when (inputStream) {
                    null -> {
                        return null
                    }
                    else -> {
                        photo = BitmapFactory.decodeStream(inputStream)
                    }
                }
                inputStream.close()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return photo
    }


    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        when (requestCode) {
            READ_CONTACTS -> {
                lifecycleScope.launch(Dispatchers.Default) {
                    checkPermission()
                }
            }
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
    }
}