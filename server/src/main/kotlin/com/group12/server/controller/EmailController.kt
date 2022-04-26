package com.group12.server.controller

import com.group12.server.service.impl.EmailServiceImpl
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class EmailController(val emailService: EmailServiceImpl) {

    @GetMapping("/email")
    fun validate() : ResponseEntity<Unit> {
        emailService.sendEmail("marco.ballario1997@gmail.com", "prova", "prova")
        return ResponseEntity(HttpStatus.OK)
    }

}