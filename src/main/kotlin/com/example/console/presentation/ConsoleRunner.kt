package com.example.console.presentation

import com.example.console.domain.services.ReportingService
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

@Component
class ConsoleRunner(private val reportingService: ReportingService) : CommandLineRunner {
    override fun run(vararg args: String?) {
        reportingService.create()
    }
}
