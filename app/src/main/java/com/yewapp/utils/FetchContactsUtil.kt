package com.yewapp.utils

import android.content.ContentResolver
import android.content.ContentUris
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.provider.ContactsContract
import com.yewapp.MainApplication
import com.yewapp.R
import com.yewapp.data.network.api.refer.PhoneContacts
import java.io.IOException
import java.io.InputStream

data class Contact(val id: Any, val name: String, var number: String)

class FetchContactsUtil {
    companion object {


        suspend fun getPhoneContacts(): ArrayList<PhoneContacts> {
            val contactsList = ArrayList<PhoneContacts>()
            val contactsCursor = MainApplication.mContentResolver.query(
                ContactsContract.Contacts.CONTENT_URI,
                null,
                null,
                null,
                "UPPER(" + ContactsContract.Contacts.DISPLAY_NAME + ") ASC"
            )
            if (contactsCursor != null && contactsCursor.count > 0) {
                val idIndex = contactsCursor.getColumnIndex(ContactsContract.Contacts._ID)
                val nameIndex =
                    contactsCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
                while (contactsCursor.moveToNext()) {
                    val id = contactsCursor.getString(idIndex)
                    val name = contactsCursor.getString(nameIndex)
                    if (name != null) {
                        contactsList.add(PhoneContacts("", name, id, null, true, false))
                    }
                }
                contactsCursor.close()
            }
            return contactsList
        }

        suspend fun getContactNumbers(): HashMap<String, ArrayList<String>> {
            val contactsNumberMap = HashMap<String, ArrayList<String>>()
            val phoneCursor: Cursor? = MainApplication.mContentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                null,
                null,
                null
            )
            if (phoneCursor != null && phoneCursor.count > 0) {
                val contactIdIndex =
                    phoneCursor!!.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID)
                val numberIndex =
                    phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                while (phoneCursor.moveToNext()) {
                    val contactId = phoneCursor.getString(contactIdIndex)
                    val number: String = phoneCursor.getString(numberIndex)

                    val regex = "[^0-9]".toRegex()
                    //check if the map contains key or not, if not then create a new array list with number
                    if (contactsNumberMap.containsKey(contactId)) {
                        contactsNumberMap[contactId]?.add(regex.replace(number, ""))
                    } else {
                        contactsNumberMap[contactId] = arrayListOf(regex.replace(number, ""))
                    }
                }
                //contact contains all the number of a particular contact
                phoneCursor.close()
            }
            return contactsNumberMap
        }


         suspend fun retrieveContactPhoto(number: String?): Bitmap? {
            val contentResolver: ContentResolver = MainApplication.mContentResolver
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
            val context = MainApplication.mContext
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

        fun convertDrawableBitmap(sourceDrawable: Drawable?): Bitmap {
            return if (sourceDrawable is BitmapDrawable) {
                sourceDrawable.bitmap
            } else {
                val constantState = sourceDrawable?.constantState
                val drawable = constantState?.newDrawable()?.mutate()
                val bitmap: Bitmap = Bitmap.createBitmap(
                    drawable!!.intrinsicWidth, drawable.intrinsicHeight,
                    Bitmap.Config.ARGB_8888
                )
                val canvas = Canvas(bitmap)
                drawable.setBounds(0, 0, canvas.width, canvas.height)
                drawable.draw(canvas)
                bitmap
            }
        }


//        suspend fun getContactNumbers(): HashMap<String, ArrayList<String>> {
//            val contactsNumberMap = HashMap<String, ArrayList<String>>()
//            val phoneCursor: Cursor? = MainApplication.mContentResolver.query(
//                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
//                null,
//                null,
//                null,
//                "UPPER(" + ContactsContract.Contacts.DISPLAY_NAME + ") ASC"
//            )
//            if (phoneCursor != null && phoneCursor.count > 0) {
//                val contactIdIndex =
//                    phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID)
//                val numberIndex =
//                    phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
//
//                while (phoneCursor.moveToNext()) {
//                    val contactId = phoneCursor.getString(contactIdIndex)
//                    val number: String = phoneCursor.getString(numberIndex)
//
//                    val regex = "[^0-9]".toRegex()
//                    //check if the map contains key or not, if not then create a new array list with number
//                    if (contactsNumberMap.containsKey(contactId)) {
////                        contactsNumberMap[contactId]?.add(regex.replace(number, "") )
//                        contactsNumberMap[contactId]?.add(number)
//                    } else {
////                        contactsNumberMap[contactId] = arrayListOf(regex.replace(number, ""))
//                        contactsNumberMap[contactId]?.add(number)
//
//                    }
//                }
//                //contact contains all the number of a particular contact
//                phoneCursor.close()
//            }
//            return contactsNumberMap
//        }
    }

}