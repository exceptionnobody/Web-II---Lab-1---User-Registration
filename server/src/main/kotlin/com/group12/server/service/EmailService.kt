package com.group12.server.service

import org.springframework.mail.SimpleMailMessage

interface EmailService {
    fun sendEmail(receiverEmail: String, receiverNickname: String, activationCode: String) : SimpleMailMessage
}