package com.droidblossom.archive.domain.model.friend

data class FriendsPage<T>(
    val friends: List<T>,
    val hasNext: Boolean
)