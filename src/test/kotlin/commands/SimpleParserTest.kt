import io.arthurkun.parser.model.AutoSkillAction
import io.arthurkun.parser.model.AutoSkillCommand
import io.arthurkun.parser.model.CommandCard
import io.arthurkun.parser.model.SkillSource
import kotlin.test.*


class SimpleParserTest {
    companion object Companion {
        private const val COMMAND = "i6,#,gf5,#,4"
    }

    val parsedCommand: AutoSkillCommand = AutoSkillCommand.parse(COMMAND)

    @Test
    fun `commands should not be empty`() {
        assertTrue("Parsed command should not be empty") {
            parsedCommand.stages.isNotEmpty()
        }
    }

    @Test
    fun `First stage, first wave`() {
        val stage1turn1 = parsedCommand[0, 0]

        assertTrue("First stage and turn should have actions") {
            stage1turn1.isNotEmpty()
        }

        val action1 = stage1turn1[0]

        assertIs<AutoSkillAction.ServantSkill>(action1)

        assertIs<SkillSource.Servant.CS3>(
            action1.skillSource,
            message = "Action skill source should be CS3"
        )
        assertEquals(
            expected = "i",
            actual = action1.codes,
            message = "Action codes should be 'i'"
        )

        val action2 = stage1turn1[1]
        assertIs<AutoSkillAction.Atk>(action2)

        assertEquals(
            expected = setOf(CommandCard.NP.C),
            actual = action2.nps,
            message = "Action NP should be C"
        )

        assertEquals(
            expected = 0,
            actual = action2.numberOfCardsBeforeNP,
            message = "Action should have 0 cards before NP"
        )

        assertEquals(
            expected = 1,
            actual = action2.wave,
            message = "Action wave should be 1"
        )

        assertEquals(
            expected = 1,
            actual = action2.turn,
            message = "Action wave should be 1"
        )

        assertEquals(
            expected = "6",
            actual = action2.codes,
            message = "Action codes should be '6'"
        )

        assertFails("Accessing non-existent action should fail") {
            stage1turn1[2]
        }
    }

    @Test
    fun `First stage, second wave`() {
        val stage1turn2 = parsedCommand[0, 1]

        assertTrue("First stage, second wave should have no actions") {
            stage1turn2.isEmpty()
        }

        assertFails("Accessing non-existent action should fail") {
            stage1turn2[0]
        }
    }

    @Test
    fun `Second stage, first wave`() {
        val stage2turn1 = parsedCommand[1, 0]

        assertTrue("Second stage, first wave should have actions") {
            stage2turn1.isNotEmpty()
        }

        val action1 = stage2turn1[0]
        assertIs<AutoSkillAction.ServantSkill>(action1)

        assertIs<SkillSource.Servant.CS1>(
            action1.skillSource,
            message = "Action skill source should be CS1"
        )
        assertEquals(
            expected = "g",
            actual = action1.codes,
            message = "Action codes should be 'g'"
        )

        val action2 = stage2turn1[1]
        assertIs<AutoSkillAction.ServantSkill>(action2)

        assertIs<SkillSource.Servant.BS3>(
            action2.skillSource,
            message = "Action skill source should be BS3"
        )
        assertEquals(
            expected = "f",
            actual = action2.codes,
            message = "Action codes should be 'f'"
        )

        val action3 = stage2turn1[2]

        assertIs<AutoSkillAction.Atk>(action3)

        assertEquals(
            expected = setOf(CommandCard.NP.B),
            actual = action3.nps,
            message = "Action NP should be B"
        )

        assertEquals(
            expected = 0,
            actual = action3.numberOfCardsBeforeNP,
            message = "Action should have 0 cards before NP"
        )

        assertEquals(
            expected = 2,
            actual = action3.wave,
            message = "Action wave should be 2"
        )

        assertEquals(
            expected = 2,
            actual = action3.turn,
            message = "Action wave should be 2"
        )

        assertEquals(
            expected = "5",
            actual = action3.codes,
            message = "Action codes should be '5'"
        )
    }

    @Test
    fun `Second stage, second wave`() {
        val stage2turn2 = parsedCommand[1, 1]

        assertTrue("Second stage, second wave should have no actions") {
            stage2turn2.isEmpty()
        }

        assertFails("Accessing non-existent action should fail") {
            stage2turn2[0]
        }
    }

    @Test
    fun `Third stage, first wave`() {
        val stage3turn1 = parsedCommand[2, 0]

        assertTrue("Third stage, first wave should have actions") {
            stage3turn1.isNotEmpty()
        }

        val action1 = stage3turn1[0]

        assertIs<AutoSkillAction.Atk>(action1)

        assertEquals(
            expected = setOf(CommandCard.NP.A),
            actual = action1.nps,
            message = "Action NP should be B"
        )

        assertEquals(
            expected = 0,
            actual = action1.numberOfCardsBeforeNP,
            message = "Action should have 0 cards before NP"
        )

        assertEquals(
            expected = 3,
            actual = action1.wave,
            message = "Action wave should be 2"
        )

        assertEquals(
            expected = 3,
            actual = action1.turn,
            message = "Action wave should be 2"
        )

        assertEquals(
            expected = "4",
            actual = action1.codes,
            message = "Action codes should be '5'"
        )
    }
}