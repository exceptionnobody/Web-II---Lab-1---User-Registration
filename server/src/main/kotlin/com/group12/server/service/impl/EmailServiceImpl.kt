package com.group12.server.service.impl

import com.group12.server.service.EmailService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Component

@Component
class EmailServiceImpl : EmailService {

    @Value("\${spring.mail.username}")
    lateinit var senderName: String
    @Autowired
    lateinit var emailSender: JavaMailSender

    override fun sendEmail(to: String, subject: String, text: String) {
        val message = SimpleMailMessage()
        message.setFrom(senderName)
        message.setTo(to)
        message.setSubject(subject)
        message.setText(text)
        emailSender.send(message)
    }

}