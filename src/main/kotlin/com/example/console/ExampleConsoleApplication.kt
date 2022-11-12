package com.example.console

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class ExampleConsoleApplication

fun main(args: Array<String>) {
    runApplication<ExampleConsoleApplication>(arrayOf(args).toString())
}
