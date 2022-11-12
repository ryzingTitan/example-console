package com.example.console.data.user.entities

import lombok.Generated
import org.springframework.data.relational.core.mapping.Table

@Generated
@Table
data class UserEntity(
    val id: Int,
    val username: String,
    val firstName: String,
    val lastName: String
)
