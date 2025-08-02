package commands

import io.arthurkun.parser.model.AutoSkillAction
import io.arthurkun.parser.model.AutoSkillCommand
import io.arthurkun.parser.model.EnemyTarget
import io.arthurkun.parser.model.SpecialCommand
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertIs

class EnemyTargetTest {

    @Test
    fun `Enemy Target`() {
        val targetsMap = EnemyTarget.list.associateWith {
            "${SpecialCommand.EnemyTarget.autoSkillCode}${it.autoSkillCode}"
        }

        targetsMap.forEach { (target, command) ->
            testEnemyTargetEachCode(
                command = command,
                enemyTarget = target,
            )
        }
    }

    private fun testEnemyTargetEachCode(
        command: String,
        enemyTarget: EnemyTarget,
    ) {
        val parsedCommand = AutoSkillCommand.parse(command)
        val currentStage = parsedCommand[0, 0]

        val action = currentStage[0]

        assertIs<AutoSkillAction.TargetEnemy>(action)

        assertEquals(enemyTarget, action.enemy)

        assertEquals(
            expected = command,
            actual = action.codes,
            message = "Action codes should be '$command'",
        )
    }

    @Test
    fun `Incomplete Enemy Target should fail`() {
        assertFails {
            AutoSkillCommand.parse("${SpecialCommand.EnemyTarget.autoSkillCode}")
        }
    }
}
