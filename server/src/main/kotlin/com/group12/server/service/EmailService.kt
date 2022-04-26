package com.group12.server.service

interface EmailService {
    fun sendEmail(to: String, subject: String, text: String)
}