package com.droidblossom.archive.util

import android.content.ContentResolver
import android.content.Context
import android.provider.ContactsContract
import android.util.Log


object ContactsUtils {

    fun getContacts(context : Context) : List<String> {
        val resolver: ContentResolver = context.contentResolver
        val phoneUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        val projection = arrayOf(
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER
        )
        val numberList = mutableListOf<String>()
        val cursor = resolver.query(phoneUri, projection, null, null, null)
        if (cursor != null) {
            while (cursor.moveToNext()) {
                val nameIndex = cursor.getColumnIndex(projection[1])
                val numberIndex = cursor.getColumnIndex(projection[2])
                val name = cursor.getString(nameIndex)
                var number = cursor.getString(numberIndex)
                number = number.replace("-", "")
                numberList.add(number)
                Log.d("GetContact", "이름 : $name 번호 : $number")
            }
        }
        cursor!!.close()
        return numberList.toList()
    }
}