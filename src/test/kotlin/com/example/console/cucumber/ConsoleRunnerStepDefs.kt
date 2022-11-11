package com.example.console.cucumber

import com.example.console.domain.services.ReportingService
import io.cucumber.java.en.When

class ConsoleRunnerStepDefs(private val reportingService: ReportingService) {
    @When("a user runs the console application")
    fun aUserRunsTheConsoleApplication() {
        reportingService.create()
    }
}
