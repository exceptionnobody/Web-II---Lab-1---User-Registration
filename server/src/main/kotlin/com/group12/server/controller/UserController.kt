package com.group12.server.controller

import com.group12.server.dto.ActivationDTO
import com.group12.server.dto.RegistrationDTO
import com.group12.server.dto.TokenDTO
import com.group12.server.dto.UserDTO
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
class UserController {

    fun checkPwd(pwd: String) : Boolean{
        var hasUpper = false
        var hasLower = false
        var hasNumber = false
        var hasSpecial =false
        for(c in pwd) {
            if(c.isWhitespace())
                return false
            if(c.isDigit())
                hasNumber=true
            if(c.isUpperCase())
                hasUpper=true
            if(c.isLowerCase())
                hasLower=true
            if(!c.isLetterOrDigit())
                hasSpecial=true
        }
        return hasUpper &&  hasLower &&  hasNumber &&  hasSpecial
    }

    @PostMapping("/user/register")
    fun register(
        @RequestBody
        @Valid
         body :  RegistrationDTO,
        br: BindingResult) : ResponseEntity<ActivationDTO> {
        if(br.hasErrors())
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        if(!checkPwd(body.password))
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        val res = ActivationDTO("a-b-c",body.email)
        return ResponseEntity(res,HttpStatus.OK)
    }

    @PostMapping("/user/validate")
    fun validate(@RequestBody body: TokenDTO): ResponseEntity<UserDTO> {
    return ResponseEntity(HttpStatus.OK);
    }
}