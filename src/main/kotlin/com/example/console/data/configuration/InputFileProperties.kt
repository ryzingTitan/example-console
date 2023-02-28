package com.example.console.data.configuration

import lombok.Generated
import org.springframework.boot.context.properties.ConfigurationProperties

@Generated
@ConfigurationProperties(prefix = "input")
data class InputFileProperties(
    val filePath: String,
)
