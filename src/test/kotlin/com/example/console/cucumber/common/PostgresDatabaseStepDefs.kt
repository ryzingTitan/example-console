package com.example.console.cucumber.common

import com.example.console.cucumber.stubs.ConnectionFactoryBuilder.TestDatabaseSettings.password
import com.example.console.cucumber.stubs.ConnectionFactoryBuilder.TestDatabaseSettings.portNumber
import com.example.console.cucumber.stubs.ConnectionFactoryBuilder.TestDatabaseSettings.type
import io.cucumber.java.en.Given
import org.testcontainers.containers.GenericContainer
import org.testcontainers.images.builder.ImageFromDockerfile

class PostgresDatabaseStepDefs {
    @Given("a Postgres database with users")
    fun aPostgresDatabaseWithUsers() {
        val postgresContainer =
            GenericContainer<Nothing>(
                ImageFromDockerfile()
                    .withFileFromClasspath("db.sql", "integration/postgres/db.sql")
                    .withFileFromClasspath("Dockerfile", "integration/postgres/Dockerfile"),
            )
        postgresContainer.exposedPorts = listOf(5432)
        postgresContainer.start()

        portNumber = postgresContainer.getMappedPort(5432)
        type = "postgresql"
        password = "test"
    }
}
