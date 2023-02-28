package com.example.console.domain.services

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.read.ListAppender
import com.example.console.data.builders.R2dbcEntityTemplateBuilder
import com.example.console.data.file.InputFileRepository
import com.example.console.domain.dtos.User
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.slf4j.LoggerFactory
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import java.io.File

@ExperimentalCoroutinesApi
class ReportingServiceTests {
    @BeforeEach
    fun setup() {
        reportingService = ReportingService(
            mockUserService,
            mockR2dbcEntityTemplateBuilder,
            mockInputFileRepository,
        )

        logger = LoggerFactory.getLogger(ReportingService::class.java) as Logger
        appender = ListAppender()
        logger.addAppender(appender)
        appender.start()
    }

    @Nested
    inner class RunJob {
        @Test
        fun `returns correct data from the job`() = runTest {
            setupMockResponses()

            reportingService.create()

            assertEquals(1, appender.list.size)
            assertEquals(Level.INFO, appender.list[0].level)
            assertEquals(
                "Usernames found: [$FIRST_USERNAME, $SECOND_USERNAME, $THIRD_USERNAME]",
                appender.list[0].message,
            )

            verify(mockInputFileRepository, times(1)).getFile()
            verify(mockR2dbcEntityTemplateBuilder, times(1)).build()
            verify(mockUserService, times(1)).findAllUsersByFirstName(mockR2dbcEntityTemplate, firstUser.firstName)
            verify(mockUserService, times(1)).findAllUsersByFirstName(mockR2dbcEntityTemplate, secondUser.firstName)
            verify(mockUserService, times(1)).getUsernameById(mockR2dbcEntityTemplate, firstUser.id)
            verify(mockUserService, times(1)).getUsernameById(mockR2dbcEntityTemplate, secondUser.id)
            verify(mockUserService, times(1)).getUsernameById(mockR2dbcEntityTemplate, thirdUser.id)
        }

        private suspend fun setupMockResponses() {
            whenever(mockInputFileRepository.getFile()).thenReturn(testFile)
            whenever(mockR2dbcEntityTemplateBuilder.build()).thenReturn(mockR2dbcEntityTemplate)
            whenever(mockUserService.findAllUsersByFirstName(mockR2dbcEntityTemplate, firstUser.firstName)).thenReturn(
                flowOf(firstUser),
            )
            whenever(mockUserService.findAllUsersByFirstName(mockR2dbcEntityTemplate, secondUser.firstName)).thenReturn(
                flowOf(secondUser, thirdUser),
            )
            whenever(mockUserService.getUsernameById(mockR2dbcEntityTemplate, firstUser.id)).thenReturn(FIRST_USERNAME)
            whenever(mockUserService.getUsernameById(mockR2dbcEntityTemplate, secondUser.id))
                .thenReturn(SECOND_USERNAME)
            whenever(mockUserService.getUsernameById(mockR2dbcEntityTemplate, thirdUser.id)).thenReturn(THIRD_USERNAME)
        }
    }

    private lateinit var reportingService: ReportingService
    private lateinit var logger: Logger
    private lateinit var appender: ListAppender<ILoggingEvent>

    private val mockUserService = mock<UserService>()
    private val mockR2dbcEntityTemplate = mock<R2dbcEntityTemplate>()
    private val mockInputFileRepository = mock<InputFileRepository>()
    private val mockR2dbcEntityTemplateBuilder = mock<R2dbcEntityTemplateBuilder>()
    private val testFile = File("testFile.txt")

    private val firstUser = User(
        id = 1,
        firstName = "distracted",
        lastName = "wright",
        fullName = "distracted wright",
    )

    private val secondUser = User(
        id = 2,
        firstName = "focused",
        lastName = "euclid",
        fullName = "focused euclid",
    )

    private val thirdUser = User(
        id = 3,
        firstName = "focused",
        lastName = "austin",
        fullName = "focused austin",
    )

    companion object ReportingServiceTestConstants {
        private const val FIRST_USERNAME = "user1"
        private const val SECOND_USERNAME = "user2"
        private const val THIRD_USERNAME = "user3"
    }
}
