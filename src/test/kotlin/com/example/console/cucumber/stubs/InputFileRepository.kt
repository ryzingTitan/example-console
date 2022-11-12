package com.example.console.cucumber.stubs

import com.example.console.data.file.IInputFileRepository
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Repository
import java.io.File

@Repository
@Profile("cucumber-test")
class InputFileRepository : IInputFileRepository {
    override fun getFile(): File {
        return File("testFile.txt")
    }
}
