package com.group12.server.repository

import com.group12.server.entity.User
import org.springframework.data.repository.CrudRepository

interface UserRepository : CrudRepository<User, Long> {
    fun findByEmail(email: String) : User
}
