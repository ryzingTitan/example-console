package com.example.console.domain.services

import com.example.console.data.user.entities.UserEntity
import com.example.console.data.user.repositories.UserRepository
import com.example.console.domain.dtos.User
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.reset
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate

class UserServiceTests {
    @BeforeEach
    fun setup() {
        userService = UserService(mockUserRepository)
        reset(mockUserRepository)
    }

    @Nested
    inner class FindAllUsersByFirstName {
        @Test
        fun `returns all users with the first name that is provided`() = runTest {
            whenever(mockUserRepository.findByFirstName(mockR2dbcEntityTemplate, userEntity.firstName)).thenReturn(
                flowOf(userEntity)
            )

            val users = userService.findAllUsersByFirstName(mockR2dbcEntityTemplate, userEntity.firstName)

            Assertions.assertEquals(listOf(user), users.toList())

            verify(mockUserRepository, times(1))
                .findByFirstName(mockR2dbcEntityTemplate, userEntity.firstName)
        }
    }

    @Nested
    inner class GetUsernameById {
        @Test
        fun `returns the username for the user if the user id exists`() = runTest {
            whenever(mockUserRepository.findById(mockR2dbcEntityTemplate, userEntity.id)).thenReturn(userEntity)

            val username = userService.getUsernameById(mockR2dbcEntityTemplate, userEntity.id)

            Assertions.assertEquals(userEntity.username, username)

            verify(mockUserRepository, times(1)).findById(mockR2dbcEntityTemplate, userEntity.id)
        }

        @Test
        fun `returns an empty string if the user id doesn't exist`() = runTest {
            whenever(mockUserRepository.findById(mockR2dbcEntityTemplate, userEntity.id)).thenReturn(null)

            val username = userService.getUsernameById(mockR2dbcEntityTemplate, userEntity.id)

            Assertions.assertEquals("", username)

            verify(mockUserRepository, times(1)).findById(mockR2dbcEntityTemplate, userEntity.id)
        }
    }

    private lateinit var userService: UserService

    private val mockUserRepository = mock<UserRepository>()
    private val mockR2dbcEntityTemplate = mock<R2dbcEntityTemplate>()

    private val userEntity = UserEntity(
        id = 1,
        username = "testUser",
        firstName = "test",
        lastName = "user"
    )

    private val user = User(
        id = 1,
        firstName = "test",
        lastName = "user",
        fullName = "test user"
    )
}
