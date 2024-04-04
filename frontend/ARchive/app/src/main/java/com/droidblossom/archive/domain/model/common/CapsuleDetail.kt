package com.droidblossom.archive.domain.model.common

// 모든 캡슐 디테일로 통합될 예정 maybe
data class CapsuleDetail(
    val address: String,
    val capsuleSkinUrl: String,
    val content: String,
    val createdDate: String,
    val dueDate: String?,
    val profileUrl :String,
    val isOpened: Boolean,
    val imageUrls: List<String>?,
    val videoUrls : List<String>?,
    val nickname: String,
    val title: String,
    val capsuleType: String
){
    constructor() : this("","","","",null,"",false, listOf(), listOf(),"","","")
}
