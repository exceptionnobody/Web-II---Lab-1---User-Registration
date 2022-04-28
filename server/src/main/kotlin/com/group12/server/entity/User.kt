package com.group12.server.entity

import com.group12.server.dto.UserDTO
import javax.persistence.*

@Entity
@Table(name = "users")
class User(
    @Column(nullable = false, unique = true)
    var email: String,
    @Column(nullable = false, unique = true)
    var nickname: String,
    @Column(nullable = false)
    var password: String,
    @Column(nullable = false)
    var validated: Boolean = false,
) {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    var userId: Long? = null
}
fun User.toDTO() = UserDTO(userId!!, nickname, email)
