package com.group12.server.dto

import java.util.UUID

data class TokenDTO(val provisional_id: UUID, val activation_code: String)
