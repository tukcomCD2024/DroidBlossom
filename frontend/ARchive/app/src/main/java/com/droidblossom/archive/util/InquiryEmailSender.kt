package com.droidblossom.archive.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast

object InquiryEmailSender {
    private const val EMAIL_ADDRESS = "archivetimecapsule@gmail.com"
    private const val EMAIL_SUBJECT = "<ARchive 문의사항 보내기>"
    private const val DEFAULT_BODY = "회원가입된 계정으로 이메일을 보내주세요."

    fun sendEmail(context: Context, body: String = DEFAULT_BODY) {
        val emailSelectorIntent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
        }

        val emailIntent = Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_EMAIL, arrayOf(EMAIL_ADDRESS))
            putExtra(Intent.EXTRA_SUBJECT, EMAIL_SUBJECT)
            putExtra(Intent.EXTRA_TEXT, body)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            selector = emailSelectorIntent
        }

        try {
            context.startActivity(emailIntent)
        } catch (e: Exception) {
            Toast.makeText(context, "메일을 연결할 앱이 없습니다.", Toast.LENGTH_SHORT).show()
        }
    }
}