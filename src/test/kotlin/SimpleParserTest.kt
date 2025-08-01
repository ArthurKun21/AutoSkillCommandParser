import io.arthurkun.parser.model.AutoSkillAction
import io.arthurkun.parser.model.AutoSkillCommand
import io.arthurkun.parser.model.CommandCard
import io.arthurkun.parser.model.SkillSource
import kotlin.test.Test
import kotlin.test.assertFails


class SimpleParserTest {
    companion object Companion {
        private const val COMMAND = "i6,#,gf5,#,4"
    }

    val parsedCommand: AutoSkillCommand = AutoSkillCommand.parse(COMMAND)

    @Test
    fun `commands should not be empty`() {
        assert(parsedCommand.stages.isNotEmpty()) {
            "Parsed command should not be empty"
        }
    }

    @Test
    fun `First stage, first wave`() {
        val stage1turn1 = parsedCommand[0, 0]

        assert(stage1turn1.isNotEmpty()) {
            "First stage and turn should have actions"
        }

        val action1 = stage1turn1[0]
        assert(action1 is AutoSkillAction.ServantSkill) {
            "First action should be a ServantSkill"
        }
        assert(
            action1 is AutoSkillAction.ServantSkill &&
                    action1.skillSource is SkillSource.Servant.CS3
        ) {
            "First action should be CS3 skill"
        }
        assert(
            action1.codes == "i"
        ) {
            "First action should have correct codes"
        }
        val action2 = stage1turn1[1]
        assert(action2 is AutoSkillAction.Atk) {
            "Second action should be an Atk action"
        }
        assert(
            action2 is AutoSkillAction.Atk &&
                    action2.nps == setOf(CommandCard.NP.C) &&
                    action2.numberOfCardsBeforeNP == 0 &&
                    action2.wave == 1 &&
                    action2.turn == 1
        ) {
            "Second action should be Atk with correct parameters"
        }
        assert(
            action2.codes == "6"
        ) {
            "Second action should have correct codes"
        }
        assertFails(
            "Accessing non-existent action should fail"
        ) {
            stage1turn1[2]
        }
    }

    @Test
    fun `First stage, second wave`() {
        val stage1turn2 = parsedCommand[0, 1]

        assert(stage1turn2.isEmpty()) {
            "First stage, second wave should have no actions"
        }

        assertFails(
            "Accessing non-existent action should fail"
        ) {
            stage1turn2[0]
        }
    }

    @Test
    fun `Second stage, first wave`() {
        val stage2turn1 = parsedCommand[1, 0]

        assert(stage2turn1.isNotEmpty()) {
            "Second stage, first wave should have actions"
        }

        val action1 = stage2turn1[0]
        assert(action1 is AutoSkillAction.ServantSkill) {
            "First action should be a ServantSkill"
        }
        assert(
            action1 is AutoSkillAction.ServantSkill &&
                    action1.skillSource is SkillSource.Servant.CS1
        ) {
            "First action should be CS1 skill"
        }
        assert(
            action1.codes == "g"
        ) {
            "First action should have correct codes"
        }

        val action2 = stage2turn1[1]
        assert(action2 is AutoSkillAction.ServantSkill) {
            "Second action should be a ServantSkill"
        }
        assert(
            action2 is AutoSkillAction.ServantSkill &&
                    action2.skillSource is SkillSource.Servant.BS3
        ) {
            "First action should be BS3 skill"
        }
        assert(
            action2.codes == "f"
        ) {
            "Second action should have correct codes"
        }


        val action3 = stage2turn1[2]
        assert(
            action3 is AutoSkillAction.Atk &&
                    action3.nps == setOf(CommandCard.NP.B) &&
                    action3.numberOfCardsBeforeNP == 0 &&
                    action3.wave == 2 &&
                    action3.turn == 2
        ) {
            "Second action should be Atk with correct parameters"
        }

        assert(
            action3.codes == "5"
        ) {
            "Third action should have correct codes"
        }
    }

    @Test
    fun `Second stage, second wave`() {
        val stage2turn2 = parsedCommand[1, 1]

        assert(stage2turn2.isEmpty()) {
            "Second stage, second wave should have no actions"
        }

        assertFails(
            "Accessing non-existent action should fail"
        ) {
            stage2turn2[0]
        }
    }

    @Test
    fun `Third stage, first wave`() {
        val stage3turn1 = parsedCommand[2, 0]

        assert(stage3turn1.isNotEmpty()) {
            "Third stage, first wave should have actions"
        }

        val action1 = stage3turn1[0]
        assert(action1 is AutoSkillAction.Atk) {
            "Second action should be an Atk action"
        }
        assert(
            action1 is AutoSkillAction.Atk &&
                    action1.nps == setOf(CommandCard.NP.A) &&
                    action1.numberOfCardsBeforeNP == 0 &&
                    action1.wave == 3 &&
                    action1.turn == 3
        ) {
            "First action should be Atk with correct parameters"
        }

        assert(
            action1.codes == "4"
        ) {
            "First action should have correct codes"
        }
    }
}