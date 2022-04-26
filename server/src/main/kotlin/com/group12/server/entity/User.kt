package com.group12.server.entity

import com.group12.server.dto.UserDTO
import javax.persistence.*

@Entity
@Table(name = "users")
class User(
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    var userId:Long?,
    @Column(nullable = false, unique = true)
    var email : String,
    @Column(nullable = false, unique = true)
    var nickname : String,
    @Column(nullable = false)
    var password : String,
    @Column(nullable = false)
    var validated: Boolean=false,
)
fun User.toDTO() = UserDTO(userId!!,nickname,email)