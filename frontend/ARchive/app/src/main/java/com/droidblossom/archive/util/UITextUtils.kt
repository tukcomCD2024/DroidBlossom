package com.droidblossom.archive.util

object UITextUtils {

    fun bigDecimalUIFormat(num : Int) : String{
        if (num < 1000) {
            return num.toString()
        }else{
            return  "${num / 1000}K"
        }
    }
}