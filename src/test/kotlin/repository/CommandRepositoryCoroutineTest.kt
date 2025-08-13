package repository

import io.arthurkun.parser.model.AutoSkillAction
import io.arthurkun.parser.repository.CommandRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class CommandRepositoryCoroutineTest {

    private lateinit var commandRepository: CommandRepository
    private val testDispatcher = UnconfinedTestDispatcher()

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        commandRepository = CommandRepository()
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `Test commandListByWaveTurn`() = runBlocking {
        val command = "a,b"
        commandRepository.setCommand(command)
        val commandListByWaveTurn = commandRepository.commandListByWaveTurn.first()

        assertTrue("Command list should not be empty") {
            commandListByWaveTurn.isNotEmpty()
        }

        assertEquals(
            expected = 2,
            actual = commandListByWaveTurn.size,
            message = "There are two turn groups in the command",
        )

        val firstTurn = 0

        assertIs<AutoSkillAction.ServantSkill>(commandListByWaveTurn[firstTurn].commandsList[0])

        assertEquals(
            expected = 0,
            actual = commandListByWaveTurn[firstTurn].waveTurn.first,
            message = "The first command should be in wave 0",
        )

        assertEquals(
            expected = 0,
            actual = commandListByWaveTurn[firstTurn].waveTurn.second,
            message = "The first command should be in turn 0",
        )

        val secondTurn = 1

        assertIs<AutoSkillAction.ServantSkill>(commandListByWaveTurn[secondTurn].commandsList[0])

        assertEquals(
            expected = 0,
            actual = commandListByWaveTurn[secondTurn].waveTurn.first,
            message = "The second command should be in wave 0",
        )

        assertEquals(
            expected = 1,
            actual = commandListByWaveTurn[secondTurn].waveTurn.second,
            message = "The second command should be in turn 1",
        )
    }
}
