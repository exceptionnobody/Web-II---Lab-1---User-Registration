package com.group12.server.entity

import org.hibernate.annotations.GenericGenerator
import java.util.*
import javax.persistence.*

@Entity
@Table(name="activations")
class Activation(
    @Id
    @Column
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    var provisionalID: UUID?,
    @Column
    @OneToOne(mappedBy = "users")
    var user: User,
    @Column
    var attemptCounter:Int = 5,
    @Column(unique = true , nullable = false)
    var email : String,
    @Column
    var activationCode: Int,
) {
}