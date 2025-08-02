package commands

import io.arthurkun.parser.model.AutoSkillAction
import io.arthurkun.parser.model.AutoSkillCommand
import io.arthurkun.parser.model.SkillActionsTarget
import io.arthurkun.parser.model.SkillSource
import org.junit.jupiter.api.BeforeAll
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertIs

class CommandSpellTest {

	companion object {
		private lateinit var parsedCommand: AutoSkillCommand

		@BeforeAll
		@JvmStatic
		fun setup() {
			val command = "o2p2"
			parsedCommand = AutoSkillCommand.parse(command)
		}
	}

	@Test
	fun `Command Spell Full HP`() {
		val currentStage = parsedCommand[0, 0]

		val action = currentStage[0]
		assertIs<AutoSkillAction.CommandSpell>(action)

		assertIs<SkillSource.CommandSpell.CS1>(action.skillSource)

		assertEquals(
			expected = "o2",
			actual = action.codes,
			message = "Action codes should be 'o2'",
		)

		assertEquals(
			expected = SkillActionsTarget.B,
			actual = action.target,
			message = "Action target should be B",
		)
	}

	@Test
	fun `Command Spell Full NP`() {
		val currentStage = parsedCommand[0, 0]

		val action = currentStage[1]
		assertIs<AutoSkillAction.CommandSpell>(action)

		assertIs<SkillSource.CommandSpell.CS2>(action.skillSource)

		assertEquals(
			expected = "p2",
			actual = action.codes,
			message = "Action codes should be 'p2'",
		)

		assertEquals(
			expected = SkillActionsTarget.B,
			actual = action.target,
			message = "Action target should be B",
		)
	}

	@Test
	fun `Incomplete Command Spell should fail`() {
		assertFails {
			val failCommand = "op2"
			AutoSkillCommand.parse(failCommand)
		}
	}

	@Test
	fun `Command Spell target should be field servant`() {
		assertFails {
			val failCommand = "p[Ch2A]"
			AutoSkillCommand.parse(failCommand)
		}
	}

	@Test
	fun `Max Command Spell is 3`() {
		assertFails {
			val failCommand = "o2p2o2p2"
			AutoSkillCommand.parse(failCommand)
		}
	}
}
