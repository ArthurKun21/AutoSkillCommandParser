package repository

import io.arthurkun.parser.repository.CommandRepository
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CommandRepositoryTest {

    private lateinit var commandRepository: CommandRepository

    @BeforeEach
    fun setUp() {
        commandRepository = CommandRepository()
    }

    @Test
    fun `is Empty command`() {
        assertTrue(commandRepository.autoSkillCommand.value.stages.isEmpty())
    }

    @Test
    fun `set new command`() {
        commandRepository.setCommand("i6,#,gf5,#,4")
        assertTrue(commandRepository.autoSkillCommand.value.stages.isNotEmpty())
        assertEquals(3, commandRepository.autoSkillCommand.value.stages.size)
    }
}
