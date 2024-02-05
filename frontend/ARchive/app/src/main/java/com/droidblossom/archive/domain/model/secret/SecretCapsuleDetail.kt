package com.droidblossom.archive.domain.model.secret

// 모든 캡슐 디테일로 통합될 예정 maybe
data class SecretCapsuleDetail(
    val address: String,
    val capsuleSkinUrl: String,
    val content: String,
    val createdDate: String,
    val dueDate: String?,
    val isOpened: Boolean,
    val imageUrls: List<String>?,
    val videoUrls : List<String>?,
    val nickname: String,
    val title: String
){
    constructor() : this("","","","",null,false, listOf(), listOf(),"","")
}
