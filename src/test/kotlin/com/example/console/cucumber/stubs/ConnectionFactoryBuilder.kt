package com.example.console.cucumber.stubs

import com.example.console.data.builders.IConnectionFactoryBuilder
import io.r2dbc.spi.ConnectionFactories
import io.r2dbc.spi.ConnectionFactory
import io.r2dbc.spi.ConnectionFactoryOptions
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile("cucumber-test")
class ConnectionFactoryBuilder : IConnectionFactoryBuilder {
    override fun build(): ConnectionFactory {
        return ConnectionFactories.get(
            ConnectionFactoryOptions.builder()
                .option(ConnectionFactoryOptions.DRIVER, type)
                .option(ConnectionFactoryOptions.USER, "test")
                .option(ConnectionFactoryOptions.PASSWORD, password)
                .option(ConnectionFactoryOptions.HOST, "localhost")
                .option(ConnectionFactoryOptions.PORT, portNumber)
                .option(ConnectionFactoryOptions.DATABASE, "test")
                .build(),
        )
    }

    companion object TestDatabaseSettings {
        var type = "postgresql"
        var password = "test"
        var portNumber = 5432
    }
}
