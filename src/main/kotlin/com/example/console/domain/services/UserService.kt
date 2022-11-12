package com.example.console.domain.services

import com.example.console.data.user.repositories.UserRepository
import com.example.console.domain.dtos.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.stereotype.Service

@Service
class UserService(private val userRepository: UserRepository) {
    fun findAllUsersByFirstName(r2dbcEntityTemplate: R2dbcEntityTemplate, firstName: String): Flow<User> {
        return userRepository
            .findByFirstName(r2dbcEntityTemplate, firstName)
            .map { userEntity ->
                User(
                    id = userEntity.id,
                    firstName = userEntity.firstName,
                    lastName = userEntity.lastName,
                    fullName = "${userEntity.firstName} ${userEntity.lastName}"
                )
            }
    }

    suspend fun getUsernameById(r2dbcEntityTemplate: R2dbcEntityTemplate, id: Int): String {
        return userRepository.findById(r2dbcEntityTemplate, id)?.username.orEmpty()
    }
}
