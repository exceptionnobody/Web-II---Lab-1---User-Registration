package com.group12.server.repository

import com.group12.server.entity.Activation
import org.springframework.data.repository.CrudRepository
import java.util.UUID

interface ActivationRepository : CrudRepository<Activation, UUID>
