package commands

import io.arthurkun.parser.model.AutoSkillAction
import io.arthurkun.parser.model.AutoSkillCommand
import io.arthurkun.parser.model.SkillActionsTarget
import io.arthurkun.parser.model.SkillSource
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class SpecialTargetTest {
    /**
     * Servants
     * - Kukulcan
     */
    @Test
    fun `First Skill Slot - Choice 2`() {
        val command = "a[Ch2A]"
        val parsedCommand: AutoSkillCommand = AutoSkillCommand.parse(command)
        val stage1turn1 = parsedCommand[0, 0]

        val action = stage1turn1[0]

        assertEquals(
            expected = command,
            actual = action.codes,
            message = "Action codes should be '$command'",
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

        assertIs<SkillActionsTarget.SpecialSkill.Choice2OptionA>(
            action.targets[0],
            message = "Action target should be Choice2OptionA",
        )
    }

    /**
     * Servants
     * - Van Gogh (Miner)
     */
    @Test
    fun `First Skill Slot - Choice 3`() {
        val command = "a[Ch3A]"
        val parsedCommand: AutoSkillCommand = AutoSkillCommand.parse(command)
        val stage1turn1 = parsedCommand[0, 0]

        val action = stage1turn1[0]

        assertEquals(
            expected = command,
            actual = action.codes,
            message = "Action codes should be '$command'",
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

        assertIs<SkillActionsTarget.SpecialSkill.Choice3OptionA>(
            action.targets[0],
            message = "Action target should be Choice3OptionA",
        )
    }

    /**
     * Servants
     * - Kukulcan 2nd skill Multi-target
     */
    @Test
    fun `Second Skill Slot - Choice 2 Multi Target`() {
        val command = "b([Ch2A]2)"
        val parsedCommand: AutoSkillCommand = AutoSkillCommand.parse(command)
        val stage1turn1 = parsedCommand[0, 0]

        val action = stage1turn1[0]

        assertEquals(
            expected = command,
            actual = action.codes,
            message = "Action codes should be '$command'",
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

        assertIs<SkillActionsTarget.SpecialSkill.Choice2OptionA>(
            action.targets[0],
            message = "Action target should be Choice2OptionA",
        )

        assertIs<SkillActionsTarget.B>(
            action.targets[1],
            message = "Action target should be B",
        )
    }

    /**
     * Servants
     * - Dante Alighieri
     */
    @Test
    fun `Second Skill Slot - Choice 2`() {
        val command = "b[Ch2A]"
        val parsedCommand: AutoSkillCommand = AutoSkillCommand.parse(command)
        val stage1turn1 = parsedCommand[0, 0]

        val action = stage1turn1[0]

        assertEquals(
            expected = command,
            actual = action.codes,
            message = "Action codes should be '$command'",
        )

        assertIs<AutoSkillAction.ServantSkill>(action)

        assertIs<SkillSource.Servant.AS2>(
            action.skillSource,
            message = "Action skill source should be AS2",
        )

        assertEquals(
            1,
            action.targets.size,
            message = "Action should have one target",
        )

        assertIs<SkillActionsTarget.SpecialSkill.Choice2OptionA>(
            action.targets[0],
            message = "Action target should be Choice2OptionA",
        )
    }

    /**
     * Servants
     * - Kukulcan
     * - UDK-Bargest
     */
    @Test
    fun `Third Skill Slot - Choice 2`() {
        val command = "c[Ch2B]"
        val parsedCommand: AutoSkillCommand = AutoSkillCommand.parse(command)
        val stage1turn1 = parsedCommand[0, 0]

        val action = stage1turn1[0]

        assertEquals(
            expected = command,
            actual = action.codes,
            message = "Action codes should be '$command'",
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

        assertIs<SkillActionsTarget.SpecialSkill.Choice2OptionB>(
            action.targets[0],
            message = "Action target should be Choice2OptionB",
        )
    }

    /**
     * Servants
     * - Emiya
     * - BB Dubai
     */
    @Test
    fun `Third Skill Slot - NP Type 2`() {
        val command = "c7"
        val parsedCommand: AutoSkillCommand = AutoSkillCommand.parse(command)
        val stage1turn1 = parsedCommand[0, 0]

        val action = stage1turn1[0]

        assertEquals(
            expected = command,
            actual = action.codes,
            message = "Action codes should be '$command'",
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

        assertIs<SkillActionsTarget.Left>(
            action.targets[0],
            message = "Action target should be Left",
        )
    }

    /**
     * Servants
     * - Hakunon
     * - Soujuurou
     * - Charlotte Corday
     * - Anjra Mainiiu
     */
    @Test
    fun `Third Skill Slot - Choice 3`() {
        val command = "c[Ch3A]"
        val parsedCommand: AutoSkillCommand = AutoSkillCommand.parse(command)
        val stage1turn1 = parsedCommand[0, 0]

        val action = stage1turn1[0]

        assertEquals(
            expected = command,
            actual = action.codes,
            message = "Action codes should be '$command'",
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

        assertIs<SkillActionsTarget.SpecialSkill.Choice3OptionA>(
            action.targets[0],
            message = "Action target should be Choice3OptionA",
        )
    }

    /**
     * Servants
     * - Melusine
     * - Ptolemy
     */
    @Test
    fun `Third Skill Slot - Transform`() {
        val command = "c[Tfrm]"
        val parsedCommand: AutoSkillCommand = AutoSkillCommand.parse(command)
        val stage1turn1 = parsedCommand[0, 0]

        val action = stage1turn1[0]

        assertEquals(
            expected = command,
            actual = action.codes,
            message = "Action codes should be '$command'",
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

        assertIs<SkillActionsTarget.SpecialSkill.Transform>(
            action.targets[0],
            message = "Action target should be Transform",
        )
    }
}
