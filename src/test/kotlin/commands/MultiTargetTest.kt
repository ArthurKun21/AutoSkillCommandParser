package commands

import io.arthurkun.parser.model.AutoSkillAction
import io.arthurkun.parser.model.AutoSkillCommand
import io.arthurkun.parser.model.SkillActionsTarget
import io.arthurkun.parser.model.SkillSource
import kotlin.test.*

class MultiTargetTest {

    @Test
    fun `Kukulcan 2nd skill Multi-target`() {
        val command = "b([Ch2B]2)"
        val parsedCommand: AutoSkillCommand = AutoSkillCommand.parse(command)
        val stage1turn1 = parsedCommand[0, 0]

        assertTrue {
            stage1turn1.isNotEmpty()
        }

        val action1 = stage1turn1[0]
        assertIs<AutoSkillAction.ServantSkill>(action1)

        assertEquals(
            expected = command,
            actual = action1.codes,
            message = "Action codes should be '$command'"
        )

        assertEquals(
            expected = 2,
            actual = action1.targets.size,
            message = "Action should have two targets"
        )
        assertIs<SkillSource.Servant.AS2>(action1.skillSource)

        assertIs<SkillActionsTarget.SpecialSkill.Choice2OptionB>(
            action1.targets[0],
            message = "Action target should be Choice2OptionB"
        )

        assertIs<SkillActionsTarget.B>(
            action1.targets[1],
            message = "Action target should be B"
        )

        assertFails("There's no 3rd target in this action") {
            action1.targets[2]
        }
    }
}