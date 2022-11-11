package com.example.console.cucumber

import com.example.console.ExampleConsoleApplication
import io.cucumber.spring.CucumberContextConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@CucumberContextConfiguration
@ActiveProfiles("cucumber-test")
@SpringBootTest(
    classes = [ExampleConsoleApplication::class]
)
class CucumberContextConfiguration
