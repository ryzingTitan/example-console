package com.example.console.data.builders

import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.stereotype.Component

@Component
class R2dbcEntityTemplateBuilder(private val connectionFactoryBuilder: ConnectionFactoryBuilder) {
    fun build(): R2dbcEntityTemplate {
        return R2dbcEntityTemplate(connectionFactoryBuilder.build())
    }
}
