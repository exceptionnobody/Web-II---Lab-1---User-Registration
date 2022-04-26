package com.group12.server.entity

import com.group12.server.dto.ActivationDTO
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
    @OneToOne
    var user: User,
    @Column(unique = true , nullable = false)
    var email : String,
    @Column
    var activationCode: String,
    @Column
    var attemptCounter:Int = 5,
    @Column(nullable = false)
    @Temporal(value = TemporalType.TIMESTAMP)
    var deadline: Date,
)
fun Activation.toDTO() = ActivationDTO(provisional_id?.toString()!!,email)