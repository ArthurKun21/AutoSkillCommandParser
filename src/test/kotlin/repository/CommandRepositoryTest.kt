package repository

import io.arthurkun.parser.model.AutoSkillAction
import io.arthurkun.parser.repository.CommandRepository
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
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
    fun `Test move commands`() {
        val command = "i6,#,gf5,#,4"
        commandRepository.setCommand(command)

        val commands = commandRepository.internalCommand.value.getAllCommandsAsList
        val action1 = commands[0]
        assertIs<AutoSkillAction.ServantSkill>(action1)

        val action2 = commands[1]
        assertIs<AutoSkillAction.Atk>(action2)

        commandRepository.moveActionByPosition(0, 1)

        val movedCommands = commandRepository.internalCommand.value.getAllCommandsAsList

        val movedAction1 = movedCommands[1]
        assertIs<AutoSkillAction.ServantSkill>(movedAction1)

        val movedAction2 = movedCommands[0]
        assertIs<AutoSkillAction.Atk>(movedAction2)

        val retrievedCommand = commandRepository.getCommandString()

        assertEquals("6,#,igf5,#,4", retrievedCommand)
    }

    @Test
    fun `Test move commands with multiple turns inside`() {
        val command = "abc56,645,4,g45,#,iie45,#,o245"
        commandRepository.setCommand(command)

        val initialCommand = commandRepository.getCommandString()
        println("Initial command: $initialCommand")

        val commands = commandRepository.internalCommand.value.getAllCommandsAsList
        val action6 = commands[6]
        assertIs<AutoSkillAction.ServantSkill>(action6)
        assertEquals("g", action6.codes)
        val action7 = commands[7]
        assertIs<AutoSkillAction.Atk>(action7)
        assertEquals("45", action7.codes)

        commandRepository.moveActionByPosition(6, 7)

        println("----- After moving actions -----")

        val movedCommands = commandRepository.internalCommand.value.getAllCommandsAsList
        val movedAction6 = movedCommands[6]
        assertIs<AutoSkillAction.Atk>(movedAction6)
        assertEquals("45", movedAction6.codes)
        val movedAction7 = movedCommands[7]
        assertIs<AutoSkillAction.ServantSkill>(movedAction7)
        assertEquals("g", movedAction7.codes)

        val retrievedCommand = commandRepository.getCommandString()
        println("Retrieved command: $retrievedCommand")

        assertEquals("abc56,645,4,45,#,giie45,#,o245", retrievedCommand)
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
