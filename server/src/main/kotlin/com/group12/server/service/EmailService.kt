package com.group12.server.service

interface EmailService {
    fun sendEmail(receiverEmail: String, receiverNickname: String, activationCode: String)
}