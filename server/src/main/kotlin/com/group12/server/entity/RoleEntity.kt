package com.group12.server.entity

import com.group12.server.security.Role
import javax.persistence.*

@Entity
@Table(name ="roles")
class RoleEntity (
    @ManyToMany
    var users : MutableSet<User>,
    @Column
    var role: Role,

)
{
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    var roleId: Long? = null
}