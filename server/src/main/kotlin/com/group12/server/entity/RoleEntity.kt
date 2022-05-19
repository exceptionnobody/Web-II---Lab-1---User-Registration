package com.group12.server.entity

import com.group12.server.security.Role
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import javax.persistence.*

@Entity
@Table(name ="roles")
class RoleEntity (
    @ManyToMany(fetch = FetchType.EAGER,cascade = [CascadeType.PERSIST])
    @OnDelete(action = OnDeleteAction.CASCADE)
    var users : MutableSet<User>,
    @Column(unique = true)
    var role: Role,

)
{
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    var roleId: Long? = null
}