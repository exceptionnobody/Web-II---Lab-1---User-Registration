package com.group12.server.service

import com.group12.server.dto.*

interface UserService {
    fun isValidPwd(pwd: String) : Boolean
    fun isValidEmail(email: String) : Boolean
    fun isValidNickname(nickname: String) : Boolean
    fun isValidProvisionalId(provisionalId: String): Boolean
    fun isValidActivationCode(activationCode: String) : Boolean
    fun newActivationCode() : String
    fun userReg(newUser: RegistrationDTO): ActivationDTO
    fun completedReg(token: TokenDTO) : UserDTO?
    fun login(username:String, password:String) : String?
}
