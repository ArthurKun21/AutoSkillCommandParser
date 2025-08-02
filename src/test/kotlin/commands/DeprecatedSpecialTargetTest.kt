package commands

import io.arthurkun.parser.model.AutoSkillAction
import io.arthurkun.parser.model.AutoSkillCommand
import io.arthurkun.parser.model.SkillActionsTarget
import io.arthurkun.parser.model.SkillSource
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

/**
 * Deprecated Special Target Commands should still be recognized
 * for backward compatibility, but they are not used in the current codebase.
 */
class DeprecatedSpecialTargetTest {

	/**
	 * Servants
	 * - Kukulcan
	 */
	@Test
	fun `First Skill Slot - Choice 2 Option 1`() {
		val command = "aK"
		val parsedCommand: AutoSkillCommand = AutoSkillCommand.parse(command)
		val stage1turn1 = parsedCommand[0, 0]

		val action = stage1turn1[0]

		assertEquals(
			expected = "aK",
			actual = action.codes,
			message = "Action codes should be 'aK'",
		)

		assertIs<AutoSkillAction.ServantSkill>(action)

		assertIs<SkillSource.Servant.AS1>(
			action.skillSource,
			message = "Action skill source should be AS1",
		)

		assertEquals(
			1,
			action.targets.size,
			message = "Action should have one target",
		)

		@Suppress("DEPRECATION")
		assertIs<SkillActionsTarget.Option1>(
			action.targets[0],
			message = "Action target should be Option1",
		)
	}

	/**
	 * Servants
	 * - Kukulcan
	 */
	@Test
	fun `First Skill Slot - Choice 2 Option 2`() {
		val command = "aU"
		val parsedCommand: AutoSkillCommand = AutoSkillCommand.parse(command)
		val stage1turn1 = parsedCommand[0, 0]

		val action = stage1turn1[0]

		assertEquals(
			expected = "aU",
			actual = action.codes,
			message = "Action codes should be 'aU'",
		)

		assertIs<AutoSkillAction.ServantSkill>(action)

		assertIs<SkillSource.Servant.AS1>(
			action.skillSource,
			message = "Action skill source should be AS1",
		)

		assertEquals(
			1,
			action.targets.size,
			message = "Action should have one target",
		)

		@Suppress("DEPRECATION")
		assertIs<SkillActionsTarget.Option2>(
			action.targets[0],
			message = "Action target should be Option2",
		)
	}

	/**
	 * Servants
	 * - Kukulcan - Multi Target Skill
	 */
	@Test
	fun `Second Skill Slot - Choice 2 Option 2 Multi Target`() {
		val command = "b(U2)"
		val parsedCommand: AutoSkillCommand = AutoSkillCommand.parse(command)
		val stage1turn1 = parsedCommand[0, 0]

		val action = stage1turn1[0]

		assertEquals(
			expected = "b(U2)",
			actual = action.codes,
			message = "Action codes should be 'b(U2)'",
		)

		assertIs<AutoSkillAction.ServantSkill>(action)

		assertIs<SkillSource.Servant.AS2>(
			action.skillSource,
			message = "Action skill source should be AS2",
		)

		assertEquals(
			2,
			action.targets.size,
			message = "Action should have two targets",
		)

		@Suppress("DEPRECATION")
		assertIs<SkillActionsTarget.Option2>(
			action.targets[0],
			message = "Action target should be Option2",
		)

		assertIs<SkillActionsTarget.B>(
			action.targets[1],
			message = "Action target should be B",
		)
	}

	/**
	 * Servants
	 * - Melusine
	 * - Ptolemy
	 */
	@Test
	fun `Third Skill Slot - Transform`() {
		val command = "cM"
		val parsedCommand: AutoSkillCommand = AutoSkillCommand.parse(command)
		val stage1turn1 = parsedCommand[0, 0]

		val action = stage1turn1[0]

		assertEquals(
			expected = "cM",
			actual = action.codes,
			message = "Action codes should be 'cM'",
		)

		assertIs<AutoSkillAction.ServantSkill>(action)

		assertIs<SkillSource.Servant.AS3>(
			action.skillSource,
			message = "Action skill source should be AS3",
		)

		assertEquals(
			1,
			action.targets.size,
			message = "Action should have one target",
		)

		@Suppress("DEPRECATION")
		assertIs<SkillActionsTarget.Transform>(
			action.targets[0],
			message = "Action target should be Transform",
		)
	}
}
