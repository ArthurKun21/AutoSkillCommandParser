package commands

import io.arthurkun.parser.model.AutoSkillCommand
import kotlin.test.Test
import kotlin.test.assertTrue

class EmptyCommandTest {
	@Test
	fun `commands should be empty`() {
		val command = ""
		val parsedCommand: AutoSkillCommand = AutoSkillCommand.parse(command)
		assertTrue("Parsed command should be empty") {
			parsedCommand.stages.isEmpty()
		}
	}

	@Test
	fun `commands with whitespace should be empty`() {
		val whitespaceCommand = "   "
		val parsedCommand: AutoSkillCommand = AutoSkillCommand.parse(whitespaceCommand)
		assertTrue("Parsed command should be empty") {
			parsedCommand.stages.isEmpty()
		}
	}
}
