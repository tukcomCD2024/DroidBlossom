package com.droidblossom.archive.domain.usecase

import com.droidblossom.archive.domain.repository.SMSMessageRepository
import javax.inject.Inject

class SMSMessageUseCase @Inject constructor(
    private val smsMessageRepository: SMSMessageRepository
){
}