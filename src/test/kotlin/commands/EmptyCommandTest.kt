import io.arthurkun.parser.model.AutoSkillCommand
import kotlin.test.Test

class EmptyCommandTest {
    companion object Companion {
        private const val COMMAND = ""
    }

    val parsedCommand: AutoSkillCommand = AutoSkillCommand.parse(COMMAND)

    @Test
    fun `commands should be empty`() {
        assert(parsedCommand.stages.isEmpty()) {
            "Parsed command should be empty"
        }
    }

    @Test
    fun `should not throw on empty command`() {
        assert(parsedCommand.stages.isEmpty()) {
            "Parsed command should be empty"
        }
    }
}