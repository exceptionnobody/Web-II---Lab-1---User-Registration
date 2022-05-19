package com.group12.server

import com.group12.server.entity.RoleEntity
import com.group12.server.entity.User
import com.group12.server.repository.RoleRepository
import com.group12.server.repository.UserRepository
import com.group12.server.security.Role
import com.group12.server.service.impl.UserServiceImpl
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.event.EventListener
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.sql.Connection
import java.sql.DriverManager
import javax.annotation.PostConstruct
import javax.crypto.SecretKey


@SpringBootApplication
class ServerApplication {
	@Value("\${key}")
	lateinit var stringKey: String

	@Bean
	fun secretKey(): SecretKey {
		return Keys.hmacShaKeyFor(Decoders.BASE64.decode(stringKey))
	}

}

fun main(args: Array<String>) {
	runApplication<ServerApplication>(*args)

}
