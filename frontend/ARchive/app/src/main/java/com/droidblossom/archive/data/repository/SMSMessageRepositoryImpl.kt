package com.droidblossom.archive.data.repository

import com.droidblossom.archive.data.source.remote.api.SMSMessageService
import com.droidblossom.archive.domain.repository.SMSMessageRepository
import javax.inject.Inject

class SMSMessageRepositoryImpl @Inject constructor(
    private val smsMessageService: SMSMessageService
) : SMSMessageRepository {
}