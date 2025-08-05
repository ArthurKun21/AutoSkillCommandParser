package repository

import io.arthurkun.parser.repository.CommandRepository
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CommandRepositoryTest {

    private lateinit var commandRepository: CommandRepository

    @BeforeTest
    fun setup() {
        commandRepository = CommandRepository()
    }

    @Test
    fun `Test initial command is empty`() {
        assertTrue(commandRepository.internalCommand.value.stages.isEmpty())
    }

    @Test
    fun `Test set command is the same as get command`() {
        val command = "i6,#,gf5,#,4"
        commandRepository.setCommand(command)

        val retrievedCommand = commandRepository.getCommandString()

        assertEquals(command, retrievedCommand, "The set command should match the retrieved command")
    }

    @Test
    fun `Test clear command`() {
        commandRepository.setCommand("i6,#,gf5,#,4")

        assertEquals(3, commandRepository.internalCommand.value.stages.size)

        commandRepository.clearCommand()

        assertTrue(commandRepository.internalCommand.value.stages.isEmpty())
    }
}
