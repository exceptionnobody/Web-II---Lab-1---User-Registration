package com.group12.server.service

import com.group12.server.dto.ActivationDTO
import com.group12.server.dto.RegistrationDTO
import com.group12.server.dto.TokenDTO
import com.group12.server.dto.UserDTO

interface UserService {
    fun isValidPwd(pwd: String) : Boolean
    fun isValidEmail(email: String) : Boolean
    fun isValidNickname(nickname: String) : Boolean
    fun isValidProvisionalId(provisionalId: String): Boolean
    fun isValidActivationCode(activationCode: String) : Boolean
    fun newActivationCode() : String
    fun userReg(newUser: RegistrationDTO): ActivationDTO
    fun completedReg(token: TokenDTO) : UserDTO?
}
