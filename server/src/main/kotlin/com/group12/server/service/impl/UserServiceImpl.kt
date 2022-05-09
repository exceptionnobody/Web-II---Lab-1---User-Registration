package com.group12.server.service.impl

import com.group12.server.dto.*
import com.group12.server.entity.Activation
import com.group12.server.entity.User
import com.group12.server.entity.toDTO
import com.group12.server.repository.ActivationRepository
import com.group12.server.repository.UserRepository
import com.group12.server.security.Role
import com.group12.server.service.UserService
import io.jsonwebtoken.Jwts
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.util.*
import javax.crypto.SecretKey

@Service
class UserServiceImpl: UserService {

    private val activationCodeSize = 6
    private val charRange : CharRange = '0'..'9'
    @Autowired
    lateinit var secretKey: SecretKey
    @Autowired
    lateinit var passwordEncoder : BCryptPasswordEncoder
    @Autowired
    lateinit var emailService: EmailServiceImpl
    @Autowired
    lateinit var userRepository: UserRepository
    @Autowired
    lateinit var activationRepository: ActivationRepository

    // Returns true if the password is valid, false otherwise
    override fun isValidPwd(pwd: String) : Boolean{
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

    override fun isValidEmail(email: String) : Boolean {
        return !userRepository.existsByEmail(email)
    }

    override fun isValidNickname(nickname: String) : Boolean {
        return !userRepository.existsByNickname(nickname)
    }

    override fun isValidProvisionalId(provisionalId: String): Boolean {
        val reg = Regex("^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$")
        if(!reg.matches(provisionalId))
            return false
        return true
    }

    override fun isValidActivationCode(activationCode: String) : Boolean {
        if (activationCode.length != 6)
            return false
        try {
            activationCode.toLong()
        } catch (e: NumberFormatException) {
            return false
        }
        return true
    }

    override fun newActivationCode() : String {
        return (1..activationCodeSize)
            .map { _ -> kotlin.random.Random.nextInt(0, 10) }
            .map(charRange::elementAt)
            .joinToString("")
    }

    override fun userReg(newUser: RegistrationDTO): ActivationDTO {
        val password = passwordEncoder.encode(newUser.password)
        val tempUser = User(newUser.email, newUser.nickname, password, false,Role.CUSTOMER)
        val savedUser = userRepository.save(tempUser)
        val activationCode = newActivationCode()
        val tempActivation = Activation(savedUser, newUser.email, activationCode)
        val savedActivation = activationRepository.save(tempActivation)
        emailService.sendEmail(newUser.email, newUser.nickname, activationCode)
        return savedActivation.toDTO()
    }

    override fun completedReg(token: TokenDTO) : UserDTO? {
        if( !isValidProvisionalId(token.provisional_id) ) {
            return null
        }
        val activation = activationRepository.findById(UUID.fromString(token.provisional_id)).orElse(null)
        activation ?: return null
        if (activation.deadline.before(Date())) {
            activationRepository.deleteById(UUID.fromString(token.provisional_id))
            userRepository.deleteById(activation.user.userId!!)
            return null
        }
        if (!isValidActivationCode(token.activation_code) ||
            activation.activationCode != token.activation_code) {
            if (activation.attemptCounter == 1) {
                val userId = activation.user.userId
                activationRepository.deleteById(UUID.fromString(token.provisional_id))
                userRepository.deleteById(userId!!)
            } else {
                activation.attemptCounter--
                activationRepository.save(activation)
            }
            return null
        }
        val user = activation.user
        user.validated = true
        userRepository.save(user)
        activationRepository.deleteById(UUID.fromString(token.provisional_id))
        return user.toDTO()
    }

    override fun login(credentials: LoginDTO): String? {
        val user = userRepository.findByNickname(credentials.username)
        if(user!=null && passwordEncoder.matches(credentials.password,user.password)){
            val now = Calendar.getInstance()
            val exp = Calendar.getInstance()
            exp.add(Calendar.HOUR,1)
            val claims = mapOf<String,Any>("sub" to user.nickname, "exp" to exp.time,"iat" to now.time, "roles" to user.role)
            return Jwts.builder().setClaims(claims).signWith(secretKey).compact()
        }
        return null
    }
}
