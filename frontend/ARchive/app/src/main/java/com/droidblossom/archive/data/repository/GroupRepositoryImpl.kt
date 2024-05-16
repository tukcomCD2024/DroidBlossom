package com.droidblossom.archive.data.repository

import com.droidblossom.archive.data.source.remote.api.GroupService
import com.droidblossom.archive.domain.repository.GroupRepository
import javax.inject.Inject

class GroupRepositoryImpl @Inject constructor(
    private val api: GroupService
) : GroupRepository {

}