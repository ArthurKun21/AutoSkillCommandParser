package commands

import io.arthurkun.parser.model.AutoSkillAction
import io.arthurkun.parser.model.AutoSkillCommand
import io.arthurkun.parser.model.OrderChangeMember
import io.arthurkun.parser.model.SkillActionsTarget
import io.arthurkun.parser.model.SkillSource
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertIs
import kotlin.test.assertTrue

class MasterSkillTest {

    @Test
    fun `Master Skill without target`() {
        val command = "jkl"
        val parsedCommand: AutoSkillCommand = AutoSkillCommand.parse(command)
        val currentStage = parsedCommand[0, 0]

        assertTrue {
            currentStage.isNotEmpty()
        }

        val action1 = currentStage[0]
        verifyMasterSkill(
            action = action1,
            command = "j",
            target = null,
            targetMessage = "Action should not have a target",
            skillSource = SkillSource.Master.MC1,
        )

        val action2 = currentStage[1]

        verifyMasterSkill(
            action = action2,
            command = "k",
            target = null,
            targetMessage = "Action should not have a target",
            skillSource = SkillSource.Master.MC2,
        )

        val action3 = currentStage[2]

        verifyMasterSkill(
            action = action3,
            command = "l",
            target = null,
            targetMessage = "Action should not have a target",
            skillSource = SkillSource.Master.MC3,
        )
    }

    @Test
    fun `Master Skill with target`() {
        val command = "j1k1l1"
        val parsedCommand: AutoSkillCommand = AutoSkillCommand.parse(command)
        val currentStage = parsedCommand[0, 0]

        assertTrue {
            currentStage.isNotEmpty()
        }

        val action1 = currentStage[0]
        verifyMasterSkill(
            action = action1,
            command = "j1",
            target = SkillActionsTarget.A,
            targetMessage = "Action should target A",
            skillSource = SkillSource.Master.MC1,
        )

        val action2 = currentStage[1]

        verifyMasterSkill(
            action = action2,
            command = "k1",
            target = SkillActionsTarget.A,
            targetMessage = "Action should target A",
            skillSource = SkillSource.Master.MC2,
        )

        val action3 = currentStage[2]

        verifyMasterSkill(
            action = action3,
            command = "l1",
            target = SkillActionsTarget.A,
            targetMessage = "Action should target A",
            skillSource = SkillSource.Master.MC3,
        )
    }

    private fun verifyMasterSkill(
        action: AutoSkillAction,
        command: String,
        target: SkillActionsTarget?,
        targetMessage: String,
        skillSource: SkillSource.Master,
    ) {
        assertIs<AutoSkillAction.MasterSkill>(action)

        assertEquals(
            expected = command,
            actual = action.codes,
            message = "Action codes should be '$command'",
        )

        assertEquals(
            expected = target,
            actual = action.target,
            message = targetMessage,
        )

        assertEquals(skillSource, action.skillSource)
    }

    @Test
    fun `Master Skill's Order Change`() {
        val command = "x11"
        val parsedCommand: AutoSkillCommand = AutoSkillCommand.parse(command)
        val currentStage = parsedCommand[0, 0]

        assertTrue {
            currentStage.isNotEmpty()
        }

        val action = currentStage[0]

        assertIs<AutoSkillAction.OrderChange>(action)

        assertEquals(
            expected = command,
            actual = action.codes,
            message = "Action codes should be '$command'",
        )

        assertEquals(
            expected = OrderChangeMember.Starting.A,
            actual = action.starting,
            message = "Action starting-member should be A",
        )

        assertEquals(
            expected = OrderChangeMember.Sub.A,
            actual = action.sub,
            message = "Action sub-member should be A",
        )
    }

    @Test
    fun `Incomplete Order Change fail`() {
        val command = "x1"

        assertFails {
            AutoSkillCommand.parse(command)
        }
    }
}
