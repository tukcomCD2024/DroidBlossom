package com.droidblossom.archive.data.dto.kakao.response

data class Address(
    val address_name: String,
    val main_address_no: String,
    val mountain_yn: String,
    val region_1depth_name: String,
    val region_2depth_name: String,
    val region_3depth_name: String,
    val sub_address_no: String,
    val zip_code: String
)