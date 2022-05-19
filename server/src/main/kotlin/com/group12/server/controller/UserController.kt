package com.group12.server.controller

import com.group12.server.dto.*
import com.group12.server.service.impl.UserServiceImpl
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import javax.annotation.PostConstruct
import javax.servlet.http.HttpServletResponse
import javax.validation.Valid

@RestController
class UserController(val userService: UserServiceImpl) {

    @Value("\${http.header.name}")
    lateinit var httpHeaderName: String
    @Value("\${bearer.prefix}")
    lateinit var bearerPrefix: String

    @PostMapping("/user/register")
    fun register(
        @RequestBody
        @Valid
        body: RegistrationDTO,
        br: BindingResult
    ) : ResponseEntity<ActivationDTO> {
        if (br.hasErrors() ||
            !userService.isValidPwd(body.password) ||
            !userService.isValidEmail(body.email) ||
            !userService.isValidNickname(body.nickname))
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        val reg = RegistrationDTO(body.nickname, body.password, body.email)
        val res = userService.userReg(reg)
        return ResponseEntity(res, HttpStatus.ACCEPTED)
    }

    @PostMapping("/user/validate")
    fun validate(@RequestBody body: TokenDTO): ResponseEntity<UserDTO> {
        val tempUserDto = userService.completedReg(TokenDTO(body.provisional_id, body.activation_code))
        return if (tempUserDto == null)
            ResponseEntity(HttpStatus.NOT_FOUND)
        else
            ResponseEntity(tempUserDto, HttpStatus.CREATED)
    }

    @PostMapping("/user/login")
    fun login(@RequestBody credentials: LoginDTO, response: HttpServletResponse) : ResponseEntity<String> {
        val token : String? = userService.login(credentials.username, credentials.password)
        return if (token == null) {
             ResponseEntity(HttpStatus.BAD_REQUEST)
        } else {
            response.addHeader(httpHeaderName, bearerPrefix + token)
            ResponseEntity(token, HttpStatus.OK)
        }
    }
}