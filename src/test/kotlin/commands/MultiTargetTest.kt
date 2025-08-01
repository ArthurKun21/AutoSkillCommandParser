import io.arthurkun.parser.model.AutoSkillAction
import io.arthurkun.parser.model.AutoSkillCommand
import io.arthurkun.parser.model.SkillActionsTarget
import io.arthurkun.parser.model.SkillSource
import kotlin.test.Test
import kotlin.test.assertFails

class MultiTargetTest {
    companion object Companion {
        private const val COMMAND = "b([Ch2B]2)4"
    }

    val parsedCommand: AutoSkillCommand = AutoSkillCommand.parse(COMMAND)

    @Test
    fun `commands should not be empty`() {
        assert(parsedCommand.stages.isNotEmpty()) {
            "Parsed command should not be empty"
        }
    }

    @Test
    fun `Kukulcan 2nd skill Multi-target`() {
        val stage1turn1 = parsedCommand[0, 0]

        assert(stage1turn1.isNotEmpty()) {
            "First stage and turn should have actions"
        }

        val action1 = stage1turn1[0]
        assert(action1.codes == "b([Ch2B]2)") {
            "First action should have correct codes"
        }

        assert(
            action1 is AutoSkillAction.ServantSkill &&
                    action1.skillSource is SkillSource.Servant.AS2
        ) {
            "First action should be AS2 skill"
        }

        assert(
            action1 is AutoSkillAction.ServantSkill &&
                    action1.targets.size == 2
        ) {
            "First action should have 2 targets"
        }

        assert(
            action1 is AutoSkillAction.ServantSkill &&
                    action1.targets[0] is SkillActionsTarget.SpecialSkill.Choice2OptionB
        ) {
            "First target should be Choice2OptionB"
        }

        assert(
            action1 is AutoSkillAction.ServantSkill &&
                    action1.targets[1] is SkillActionsTarget.B
        ) {
            "Second target should be B"
        }

        assertFails(
            "There's no 3rd target in this action",
        ) {
            val currentAction = action1 as AutoSkillAction.ServantSkill
            currentAction.targets[2]
        }
    }
}