import io.arthurkun.parser.model.AutoSkillCommand
import kotlin.test.Test

class EmptyCommandTest {
    @Test
    fun `commands should be empty`() {
        val parsedCommand: AutoSkillCommand = AutoSkillCommand.parse("")
        assert(parsedCommand.stages.isEmpty()) {
            "Parsed command should be empty"
        }
    }

    @Test
    fun `commands with whitespace should be empty`() {
        val parsedCommand: AutoSkillCommand = AutoSkillCommand.parse("  ")
        assert(parsedCommand.stages.isEmpty()) {
            "Parsed command should be empty"
        }
    }
}