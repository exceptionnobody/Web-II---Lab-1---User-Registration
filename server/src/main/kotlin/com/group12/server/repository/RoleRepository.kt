package com.group12.server.repository

import com.group12.server.entity.RoleEntity
import com.group12.server.security.Role
import org.springframework.data.repository.CrudRepository

interface RoleRepository  : CrudRepository<RoleEntity, Long> {
    fun existsByRole(role: Role) : Boolean
    fun findByRole(role: Role) : RoleEntity?
}