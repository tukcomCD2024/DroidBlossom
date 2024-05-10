package com.droidblossom.archive.util

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.provider.ContactsContract
import android.telephony.TelephonyManager
import android.util.Log
import com.droidblossom.archive.data.dto.friend.request.PhoneBooks


object ContactsUtils {

    @SuppressLint("MissingPermission")
    fun getContacts(context : Context) : List<PhoneBooks> {
        val resolver: ContentResolver = context.contentResolver
        val phoneUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI

        val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val projection = arrayOf(
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER
        )
        val numberList = mutableListOf<PhoneBooks>()
        val cursor = resolver.query(phoneUri, projection, null, null, null)
        if (cursor != null) {
            while (cursor.moveToNext()) {
                val nameIndex = cursor.getColumnIndex(projection[1])
                val numberIndex = cursor.getColumnIndex(projection[2])
                val name = cursor.getString(nameIndex)
                var number = cursor.getString(numberIndex)
                number = number.replace("-", "")
                numberList.add(PhoneBooks(number,name))
                Log.d("GetContact", "이름 : $name 번호 : $number")
            }
        }
        numberList.removeIf {
            it.originPhone == tm.line1Number.replace("+82","0")
        }
        cursor!!.close()
        return numberList.toList()
    }
}