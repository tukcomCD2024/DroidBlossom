package com.droidblossom.archive.domain.model.group

data class GroupPage<T>(
    val groups: List<T>,
    val hasNext: Boolean
)
