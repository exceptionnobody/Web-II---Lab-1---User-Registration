package com.group12.server.entity

import com.group12.server.dto.ActivationDTO
import com.group12.server.dto.UserDTO
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
    var provisional_id: UUID?,
    @Column
    @OneToOne(mappedBy = "users")
    var user: User,
    @Column
    var attemptCounter:Int = 5,
    @Column(unique = true , nullable = false)
    var email : String,
    @Column
    var activationCode: Int,
)
fun Activation.toDTO() = ActivationDTO(provisional_id?.toString()!!,email)