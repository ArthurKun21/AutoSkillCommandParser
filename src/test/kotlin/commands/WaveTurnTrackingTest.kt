package commands

import io.arthurkun.parser.model.AutoSkillCommand
import org.junit.jupiter.api.BeforeAll
import kotlin.test.Test
import kotlin.test.assertEquals

class WaveTurnTrackingTest {

    companion object {
        private lateinit var parsedCommand: AutoSkillCommand

        @BeforeAll
        @JvmStatic
        fun setup() {
            val command = "abcdefg2h2i2x314,#,gj5,#,h1i2o254"
            parsedCommand = AutoSkillCommand.parse(command)
        }
    }

    @Test
    fun `should have correct Waves`() {
        val firstStage = parsedCommand[0, 0]

        for (i in 0..10) {
            assertEquals(1, firstStage[i].wave)
        }

        val secondStage = parsedCommand[1, 0]
        for (i in 0..2) {
            assertEquals(2, secondStage[i].wave)
        }

        val thirdStage = parsedCommand[2, 0]
        for (i in 0..3) {
            assertEquals(3, thirdStage[i].wave)
        }
    }

    @Test
    fun `should have correct Turns`() {
        val firstStage = parsedCommand[0, 0]

        for (i in 0..10) {
            assertEquals(1, firstStage[i].turn)
        }

        val secondStage = parsedCommand[1, 0]
        for (i in 0..2) {
            assertEquals(2, secondStage[i].turn)
        }

        val thirdStage = parsedCommand[2, 0]
        for (i in 0..3) {
            assertEquals(3, thirdStage[i].turn)
        }
    }
}