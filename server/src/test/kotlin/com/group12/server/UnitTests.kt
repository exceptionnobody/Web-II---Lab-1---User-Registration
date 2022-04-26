package com.group12.server

import com.group12.server.entity.Activation
import com.group12.server.entity.User
import com.group12.server.entity.toDTO
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions
import org.springframework.boot.test.context.SpringBootTest
import java.util.Calendar
import java.util.UUID

@SpringBootTest
class UnitTests {

    @Test
    fun userToDTOTest() {
        val user = User(1L,"test@test.com","test","password")
        val userDTO = user.toDTO()
        Assertions.assertEquals(1,userDTO.userId)
        Assertions.assertEquals("test@test.com",userDTO.email)
        Assertions.assertEquals("test",userDTO.nickname)
    }

    @Test
    fun activationToDTOTest() {
        val user = User(1L,"test@test.com","test","password")
        val deadline = Calendar.getInstance().time
        val activation = Activation(UUID.fromString("eda6bff4-cc1e-46be-80fe-b5a59fcc75e3"),user, "test@test.com","123456",5,deadline)
        val activationDTO = activation.toDTO()
        Assertions.assertEquals("eda6bff4-cc1e-46be-80fe-b5a59fcc75e3",activationDTO.provisional_id)
        Assertions.assertEquals("test@test.com",activationDTO.email)
    }
}