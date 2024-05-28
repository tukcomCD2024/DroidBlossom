package com.droidblossom.archive.domain.model.group

import com.droidblossom.archive.presentation.model.mypage.detail.GroupProfileData
import com.droidblossom.archive.util.UITextUtils

data class GroupDetail (
    val groupName: String,
    val groupProfileUrl: String,
    val groupDescription: String,
    val createdAt: String,
    val groupCapsuleTotalCount: Int,
    val canGroupEdit: Boolean,
    val members: List<GroupMember>,
)

fun GroupDetail.toGroupProfileData() = GroupProfileData(
    groupName = groupName,
    groupDescription = groupDescription,
    groupProfileUrl = groupProfileUrl,
    hasEditPermission = canGroupEdit,
    groupCapsuleNum = UITextUtils.bigDecimalUIFormat(groupCapsuleTotalCount),
    groupMemberNum = (members.size + 1).toString(),
    groupCreateTime = createdAt
)