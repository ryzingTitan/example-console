package com.example.console.data.user.repositories

import com.example.console.data.user.entities.UserEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.data.r2dbc.core.awaitOneOrNull
import org.springframework.data.relational.core.query.Criteria.where
import org.springframework.data.relational.core.query.Query
import org.springframework.data.relational.core.query.isEqual
import org.springframework.stereotype.Repository

@Repository
class UserRepository {
    fun findByFirstName(r2dbcEntityTemplate: R2dbcEntityTemplate, firstName: String): Flow<UserEntity> {
        return r2dbcEntityTemplate
            .select(UserEntity::class.java)
            .from(getTableName(r2dbcEntityTemplate))
            .matching(Query.query(where("first_name").isEqual(firstName)))
            .all()
            .asFlow()
    }

    suspend fun findById(r2dbcEntityTemplate: R2dbcEntityTemplate, id: Int): UserEntity? {
        return r2dbcEntityTemplate
            .select(UserEntity::class.java)
            .from(getTableName(r2dbcEntityTemplate))
            .matching(Query.query(where("id").isEqual(id)))
            .awaitOneOrNull()
    }

    private fun getTableName(r2dbcEntityTemplate: R2dbcEntityTemplate): String {
        return when (r2dbcEntityTemplate.databaseClient.connectionFactory.metadata.name) {
            "PostgreSQL" -> POSTGRES_TABLE_NAME
            "Microsoft SQL Server" -> MSSQL_TABLE_NAME
            else -> POSTGRES_TABLE_NAME
        }
    }

    companion object UserTableNameConstants {
        private const val POSTGRES_TABLE_NAME = "test_schema.user"
        private const val MSSQL_TABLE_NAME = "[test_schema].[user]"
    }
}
