package com.droidblossom.archive.domain.model.friend

data class FriendsPage(
    val friends: List<Friend>,
    val hasNext: Boolean
)