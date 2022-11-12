package com.example.console.data.configuration

import lombok.Generated
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@Generated
@ConstructorBinding
@ConfigurationProperties(prefix = "input")
data class InputFileProperties(
    val filePath: String
)
