package com.group12.server.entity

import javax.persistence.*

@Entity
@Table(name = "users")
class User(
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    var UserID:Long?,
    @Column(nullable = false, unique = true)
    var email : String,
    @Column(nullable = false, unique = true)
    var nickname : String,

) {
}