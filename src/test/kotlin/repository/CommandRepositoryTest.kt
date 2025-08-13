package repository

import io.arthurkun.parser.model.AutoSkillAction
import io.arthurkun.parser.model.CommandCard
import io.arthurkun.parser.model.StageMarker
import io.arthurkun.parser.repository.CommandRepository
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
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
    fun `Test invalid NP code`() {
        val command = "4a"
        assertFails {
            commandRepository.setCommand(command)
        }
    }

    @Test
    fun `Test two turns`() {
        val command = "a,b"
        commandRepository.setCommand(command)

        val retrievedCommand = commandRepository.getCommandString()

        assertEquals("a,b", retrievedCommand)
    }

//    @Test
//    fun `Test valid NP code`() {
//        val command = "4"
//        commandRepository.setCommand(command)
//
//        val retrievedCommand = commandRepository.getCommandString()
//        assertEquals(
//            expected = "4,#,",
//            actual = retrievedCommand,
//            message = "The command should be correctly parsed and retrieved",
//        )
//    }

    @Test
    fun `Test create latest command`() {
        val command = "i6,#,gf5"
        commandRepository.setCommand(command)

        val newCommand = AutoSkillAction.Atk.np(
            nps = setOf(CommandCard.NP.A),
            codes = "${CommandCard.NP.A.autoSkillCode}",
        )
        commandRepository.createCommand(newCommand)

        val retrievedCommand = commandRepository.getCommandString()

        assertEquals("i6,#,gf5,#,4,", retrievedCommand)
    }

    @Test
    fun `Test create latest command with last is a turn`() {
        val command = "i6,#,gf5,"
        commandRepository.setCommand(command)

        val newCommand = AutoSkillAction.Atk.np(
            nps = setOf(CommandCard.NP.A),
            codes = "${CommandCard.NP.A.autoSkillCode}",
        )
        commandRepository.createCommand(newCommand)

        val retrievedCommand = commandRepository.getCommandString()

        assertEquals("i6,#,gf5,4,", retrievedCommand)
    }

    @Test
    fun `Test create command at position`() {
        val command = "igf5,#,4"
        commandRepository.setCommand(command)

        val newCommand = AutoSkillAction.Atk.np(
            nps = setOf(CommandCard.NP.C),
            codes = "${CommandCard.NP.C.autoSkillCode}",
            stageMarker = StageMarker.Wave,
        )
        commandRepository.createCommandAtPosition(1, newCommand)

        val retrievedCommand = commandRepository.getCommandString()

        assertEquals("i6,#,gf5,#,4,#,", retrievedCommand)
    }

    @Test
    fun `Test update command at position`() {
        val command = "i45,#,gf5,#,4"
        commandRepository.setCommand(command)

        val newCommand = AutoSkillAction.Atk.np(
            nps = setOf(CommandCard.NP.C),
            codes = "${CommandCard.NP.C.autoSkillCode}",
            stageMarker = StageMarker.Wave,
        )
        commandRepository.updateCommandByPosition(1, newCommand)

        val retrievedCommand = commandRepository.getCommandString()

        assertEquals("i6,#,gf5,#,4,#,", retrievedCommand)
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

        val movedAction1 = movedCommands[0]
        assertIs<AutoSkillAction.Atk>(movedAction1)

        val movedAction2 = movedCommands[1]
        assertIs<AutoSkillAction.ServantSkill>(movedAction2)

        val retrievedCommand = commandRepository.getCommandString()

        assertEquals("6,#,igf5,#,4,#,", retrievedCommand)
    }

    @Test
    fun `Test move commands with multiple turns inside`() {
        val command = "abc56,645,4,g45,#,iie45,#,o245"
        commandRepository.setCommand(command)

        val commands = commandRepository.internalCommand.value.getAllCommandsAsList
        val action6 = commands[6]
        assertIs<AutoSkillAction.ServantSkill>(action6)
        assertEquals("g", action6.codes)
        val action7 = commands[7]
        assertIs<AutoSkillAction.Atk>(action7)
        assertEquals("45", action7.codes)

        commandRepository.moveActionByPosition(6, 7)

        val movedCommands = commandRepository.internalCommand.value.getAllCommandsAsList
        val movedAction6 = movedCommands[6]
        assertIs<AutoSkillAction.Atk>(movedAction6)
        assertEquals("45", movedAction6.codes)
        val movedAction7 = movedCommands[7]
        assertIs<AutoSkillAction.ServantSkill>(movedAction7)
        assertEquals("g", movedAction7.codes)

        val retrievedCommand = commandRepository.getCommandString()

        assertEquals("abc56,645,4,45,#,giie45,#,o245,#,", retrievedCommand)
    }

    @Test
    fun `Test delete command at position`() {
        val command = "i6,#,gf5,#,4"
        commandRepository.setCommand(command)

        val commands = commandRepository.internalCommand.value.getAllCommandsAsList
        val action1 = commands[0]
        assertIs<AutoSkillAction.ServantSkill>(action1)

        val action2 = commands[1]
        assertIs<AutoSkillAction.Atk>(action2)

        commandRepository.deleteCommandByPosition(0)

        val deletedCommands = commandRepository.internalCommand.value.getAllCommandsAsList

        val deletedAction2 = deletedCommands[0]
        assertIs<AutoSkillAction.Atk>(deletedAction2)

        val retrievedCommand = commandRepository.getCommandString()

        assertEquals("6,#,gf5,#,4,#,", retrievedCommand)
    }

    @Test
    fun `Test delete latest command`() {
        val command = "i6,#,gf5,#,4"
        commandRepository.setCommand(command)

        commandRepository.deleteLatestCommand()
        val retrievedCommand = commandRepository.getCommandString()

        assertEquals("i6,#,gf5,#,", retrievedCommand)
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
