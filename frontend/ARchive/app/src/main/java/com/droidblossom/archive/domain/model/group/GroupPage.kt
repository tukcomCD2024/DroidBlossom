package com.droidblossom.archive.domain.model.group

data class GroupPage(
    val groups: List<GroupSummary>,
    val hasNext: Boolean
)
