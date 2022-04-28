package com.group12.server

import com.group12.server.dto.ActivationDTO
import com.group12.server.dto.RegistrationDTO
import com.group12.server.dto.TokenDTO
import com.group12.server.dto.UserDTO
import com.group12.server.repository.ActivationRepository
import com.group12.server.repository.UserRepository
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.postForEntity
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpEntity
import org.springframework.http.HttpStatus
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.util.concurrent.atomic.AtomicInteger

@Testcontainers
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
class IntegrationTests {

    companion object {
        @Container
        val postgres = PostgreSQLContainer("postgres:latest")

        @JvmStatic
        @DynamicPropertySource
        fun properties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", postgres::getJdbcUrl)
            registry.add("spring.datasource.username", postgres::getUsername)
            registry.add("spring.datasource.password", postgres::getPassword)
            registry.add("spring.jpa.hibernate.ddl-auto") { "create-drop" }
        }
    }

    @LocalServerPort
    protected var port: Int = 0
    @Autowired
    lateinit var restTemplate: TestRestTemplate
    @Autowired
    lateinit var userRepository: UserRepository
    @Autowired
    lateinit var activationRepository: ActivationRepository

    @Test
    fun registerUserTest() {
        val baseUrl = "http://localhost:$port"

        // sends a request with a weak password
        val wrongReq = HttpEntity(RegistrationDTO("somename", "1234","me@email.com"))
        val wrongRes = restTemplate.postForEntity<ActivationDTO>("$baseUrl/user/register", wrongReq)
        assert(wrongRes.statusCode == HttpStatus.BAD_REQUEST)

        // sends a request with a wrong email
        val wrongReq1 = HttpEntity(RegistrationDTO("somename", "1234","meemail"))
        val wrongRes1 = restTemplate.postForEntity<Unit>("$baseUrl/user/register", wrongReq1)
        assert(wrongRes1.statusCode == HttpStatus.BAD_REQUEST)


        // sends a request with a empty nickname
        val wrongReq2 = HttpEntity(RegistrationDTO("", "1234","meemail"))
        val wrongRes2 = restTemplate.postForEntity<Unit>("$baseUrl/user/register", wrongReq2)
        assert(wrongRes2.statusCode == HttpStatus.BAD_REQUEST)

        // sends a request with a strong password
        val rightReq = HttpEntity(RegistrationDTO("somename", "Secret!Password1","me@email.com"))
        val rightRes = restTemplate.postForEntity<ActivationDTO>("$baseUrl/user/register", rightReq)
        assert(rightRes.statusCode == HttpStatus.OK)

        // deletes created records from the db
        val act = activationRepository.findById(rightRes.body!!.provisional_id).get()
        val userId = act.user.userId!!
        activationRepository.deleteById(act.provisionalId!!)
        userRepository.deleteById(userId)
    }

    @Test
    fun activateUserTest() {
        val baseUrl = "http://localhost:$port"

        // sends a request with wrong provisional id and activation code
        val wrongReq = HttpEntity(TokenDTO("1234", "1234"))
        val wrongRes = restTemplate.postForEntity<UserDTO>("$baseUrl/user/validate", wrongReq)
        assert(wrongRes.statusCode == HttpStatus.NOT_FOUND)

        // registers a user in the db
        val registerReq = HttpEntity(RegistrationDTO("somename", "Secret!Password1","me@email.com"))
        val registerRes = restTemplate.postForEntity<ActivationDTO>("$baseUrl/user/register", registerReq)
        assert(registerRes.statusCode == HttpStatus.OK)

        // sends a request with correct provisional id and activation code
        val activationCode = activationRepository.findById(registerRes.body!!.provisional_id).get().activationCode
        val rightReq = HttpEntity(TokenDTO(registerRes.body!!.provisional_id.toString(), activationCode))
        val rightRes = restTemplate.postForEntity<UserDTO>("$baseUrl/user/validate", rightReq)
        assert(rightRes.statusCode == HttpStatus.CREATED)

        // deletes the one remaining record from the db
        userRepository.deleteById(rightRes.body!!.userId)
    }

    @Test
    fun rateLimiterTests() {
        val baseUrl = "http://localhost:$port"
        val countWrong = AtomicInteger()
        val count  = AtomicInteger()
        // Testing wrong request because it is faster
        val wrongReq = HttpEntity(RegistrationDTO("somename", "1234","me@email.com"))
        val tl = mutableListOf<Thread>()
        for(i in 1..16) {
            tl.add(Thread{
                val wrongRes = restTemplate.postForEntity<Unit>("$baseUrl/user/register", wrongReq)
                if(wrongRes.statusCode == HttpStatus.TOO_MANY_REQUESTS)
                    countWrong.incrementAndGet()
                else
                    count.incrementAndGet()
            })
        }
        tl.forEach { it.start() }
        tl.forEach { it.join() }
        assert(count.get()==10)
        assert(countWrong.get()==6)

    }
}