package com.example.console.data.configuration

import lombok.Generated
import org.springframework.boot.context.properties.ConfigurationProperties

@Generated
@ConfigurationProperties(prefix = "database")
data class DatabaseProperties(
    val server: String,
    val user: String,
    val name: String,
    val password: String,
    val type: String,
    val port: Int,
)
