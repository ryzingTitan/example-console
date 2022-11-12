package com.example.console.data.builders

import io.r2dbc.spi.ConnectionFactory

interface IConnectionFactoryBuilder {
    fun build(): ConnectionFactory
}
