package com.example.console.domain.services

import com.example.console.data.builders.R2dbcEntityTemplateBuilder
import com.example.console.data.file.InputFileRepository
import kotlinx.coroutines.runBlocking
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.stereotype.Service

@Service
class ReportingService(
    private val userService: UserService,
    private val r2dbcEntityTemplateBuilder: R2dbcEntityTemplateBuilder,
    private val inputFileRepository: InputFileRepository
) {
    private val logger: Logger = LoggerFactory.getLogger(ReportingService::class.java)

    fun create() {
        val inputFile = inputFileRepository.getFile()
        val r2dbcEntityTemplate = r2dbcEntityTemplateBuilder.build()

        val usernames = mutableListOf<String>()
        inputFile.forEachLine { line ->
            runBlocking {
                usernames.addAll(getReportData(line, r2dbcEntityTemplate))
            }
        }

        logger.info("Usernames found: ${usernames.sorted()}")
    }

    private suspend fun getReportData(line: String, r2dbcEntityTemplate: R2dbcEntityTemplate): List<String> {
        val users = userService.findAllUsersByFirstName(r2dbcEntityTemplate, line)

        val usernames = mutableListOf<String>()
        users.collect { user ->
            usernames.add(userService.getUsernameById(r2dbcEntityTemplate, user.id))
        }

        return usernames
    }
}
