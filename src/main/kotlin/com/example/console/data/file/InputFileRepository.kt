package com.example.console.data.file

import com.example.console.data.configuration.InputFileProperties
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Repository
import java.io.File

@Repository
@Profile("!cucumber-test")
class InputFileRepository(private val inputFileProperties: InputFileProperties) : IInputFileRepository {
    override fun getFile(): File {
        return File(inputFileProperties.filePath)
    }
}
