package commands

import io.arthurkun.parser.model.AutoSkillAction
import io.arthurkun.parser.model.AutoSkillCommand
import io.arthurkun.parser.model.SkillActionsTarget
import io.arthurkun.parser.model.SkillSource
import kotlin.test.Test


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

        assert(
            action is AutoSkillAction.ServantSkill &&
                    action.skillSource is SkillSource.Servant.AS1
        ) {
            "Action should be AS1 skill"
        }

        assert(
            action is AutoSkillAction.ServantSkill &&
                    action.codes == "a[Ch2A]"
        ) {
            "Skill slot should be 'a[Ch2A]', but was '${action.codes}'"
        }
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

        assert(
            action is AutoSkillAction.ServantSkill &&
                    action.skillSource is SkillSource.Servant.AS1
        ) {
            "Action should be AS1 skill"
        }

        assert(
            action is AutoSkillAction.ServantSkill &&
                    action.codes == "a[Ch3A]"
        ) {
            "Skill slot should be 'a[Ch3A]', but was '${action.codes}'"
        }

        assert(
            action is AutoSkillAction.ServantSkill &&
                    action.targets[0] is SkillActionsTarget.SpecialSkill.Choice3OptionA
        ) {
            "Skill slot should be '[Ch3A]', but was '${action.codes}'"
        }
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

        assert(
            action is AutoSkillAction.ServantSkill &&
                    action.skillSource is SkillSource.Servant.AS2
        ) {
            "Action should be AS2 skill"
        }

        assert(
            action is AutoSkillAction.ServantSkill &&
                    action.codes == "b([Ch2A]2)"
        ) {
            "Skill slot should be 'b([Ch2A]2)', but was '${action.codes}'"
        }

        assert(
            action is AutoSkillAction.ServantSkill &&
                    action.targets[0] is SkillActionsTarget.SpecialSkill.Choice2OptionA &&
                    action.targets[1] is SkillActionsTarget.B
        ) {
            "Skill slot should be '[Ch2A]' and '2', but was '${action.codes}'"
        }
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

        assert(
            action is AutoSkillAction.ServantSkill &&
                    action.skillSource is SkillSource.Servant.AS2
        ) {
            "Action should be AS2 skill"
        }

        assert(
            action is AutoSkillAction.ServantSkill &&
                    action.codes == "b[Ch2A]"
        ) {
            "Skill slot should be 'b[Ch2A]', but was '${action.codes}'"
        }

        assert(
            action is AutoSkillAction.ServantSkill &&
                    action.targets[0] is SkillActionsTarget.SpecialSkill.Choice2OptionA
        ) {
            "Skill slot should be '[Ch2A]', but was '${action.codes}'"
        }
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

        assert(
            action is AutoSkillAction.ServantSkill &&
                    action.skillSource is SkillSource.Servant.AS3
        ) {
            "Action should be AS3 skill"
        }

        assert(
            action is AutoSkillAction.ServantSkill &&
                    action.codes == "c[Ch2B]"
        ) {
            "Skill slot should be 'c[Ch2B]', but was '${action.codes}'"
        }

        assert(
            action is AutoSkillAction.ServantSkill &&
                    action.targets[0] is SkillActionsTarget.SpecialSkill.Choice2OptionB
        ) {
            "Skill slot should be '[Ch2B]', but was '${action.codes}'"
        }
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

        assert(
            action is AutoSkillAction.ServantSkill &&
                    action.skillSource is SkillSource.Servant.AS3
        ) {
            "Action should be AS3 skill"
        }

        assert(
            action is AutoSkillAction.ServantSkill &&
                    action.codes == "c7"
        ) {
            "Skill slot should be 'c7', but was '${action.codes}'"
        }

        assert(
            action is AutoSkillAction.ServantSkill &&
                    action.targets[0] is SkillActionsTarget.Left
        ) {
            "Skill slot should be '7', but was '${action.codes}'"
        }
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

        assert(
            action is AutoSkillAction.ServantSkill &&
                    action.skillSource is SkillSource.Servant.AS3
        ) {
            "Action should be AS3 skill"
        }

        assert(
            action is AutoSkillAction.ServantSkill &&
                    action.codes == "c[Ch3A]"
        ) {
            "Skill slot should be 'c[Ch3A]', but was '${action.codes}'"
        }

        assert(
            action is AutoSkillAction.ServantSkill &&
                    action.targets[0] is SkillActionsTarget.SpecialSkill.Choice3OptionA
        ) {
            "Skill slot should be '[Ch3A]', but was '${action.codes}'"
        }
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

        assert(
            action is AutoSkillAction.ServantSkill &&
                    action.skillSource is SkillSource.Servant.AS3
        ) {
            "Action should be AS3 skill"
        }

        assert(
            action is AutoSkillAction.ServantSkill &&
                    action.codes == "c[Tfrm]"
        ) {
            "Skill slot should be 'c[Tfrm]', but was '${action.codes}'"
        }

        assert(
            action is AutoSkillAction.ServantSkill &&
                    action.targets[0] is SkillActionsTarget.SpecialSkill.Transform
        ) {
            "Skill slot should be '[Tfrm]', but was '${action.codes}'"
        }
    }
}