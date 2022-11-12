package com.example.console.data.builders

import com.example.console.data.configuration.DatabaseProperties
import io.r2dbc.spi.ConnectionFactories
import io.r2dbc.spi.ConnectionFactory
import io.r2dbc.spi.ConnectionFactoryOptions
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile("!cucumber-test")
class ConnectionFactoryBuilder(private val databaseProperties: DatabaseProperties) : IConnectionFactoryBuilder {
    override fun build(): ConnectionFactory {
        return ConnectionFactories.get(
            ConnectionFactoryOptions.builder()
                .option(ConnectionFactoryOptions.DRIVER, databaseProperties.type)
                .option(ConnectionFactoryOptions.USER, databaseProperties.user)
                .option(ConnectionFactoryOptions.PASSWORD, databaseProperties.password)
                .option(ConnectionFactoryOptions.HOST, databaseProperties.server)
                .option(ConnectionFactoryOptions.PORT, databaseProperties.port)
                .option(ConnectionFactoryOptions.DATABASE, databaseProperties.name)
                .build()
        )
    }
}
