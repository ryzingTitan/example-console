package com.example.console.cucumber.common

import com.example.console.cucumber.stubs.ConnectionFactoryBuilder.TestDatabaseSettings.password
import com.example.console.cucumber.stubs.ConnectionFactoryBuilder.TestDatabaseSettings.portNumber
import com.example.console.cucumber.stubs.ConnectionFactoryBuilder.TestDatabaseSettings.type
import io.cucumber.java.en.Given
import org.testcontainers.containers.MSSQLServerContainer
import org.testcontainers.utility.DockerImageName

class MssqlDatabaseStepDefs {
    @Given("an MSSQL database with users")
    fun anMssqlDatabaseWithUsers() {
        val mssqlDatabaseContainer =
            MSSQLServerContainer<Nothing>(DockerImageName.parse("mcr.microsoft.com/mssql/server:2022-latest"))
                .apply {
                    withExposedPorts(1433)
                    withInitScript("integration/mssql/db.sql")
                    acceptLicense()
                }

        mssqlDatabaseContainer.start()

        portNumber = mssqlDatabaseContainer.getMappedPort(1433)
        type = "sqlserver"
        password = "SecurePassw0rd"
    }
}
