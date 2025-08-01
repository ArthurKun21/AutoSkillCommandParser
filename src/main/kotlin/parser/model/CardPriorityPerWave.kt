package io.arthurkun.parser.model

import io.arthurkun.parser.enums.CardAffinityEnum
import io.arthurkun.parser.enums.CardTypeEnum

class CardPriorityPerWave private constructor(
    private val scoresPerWave: List<CardPriority>,
) : List<CardPriority> by scoresPerWave {
    fun atWave(wave: Int) =
        scoresPerWave[wave.coerceIn(scoresPerWave.indices)]
            .plus(
                // Give minimum priority to unknown cards
                CardScore(
                    CardTypeEnum.Unknown,
                    CardAffinityEnum.Normal,
                ),
            )

    override fun toString() = scoresPerWave.joinToString(STAGE_SEPARATOR)

    companion object {
        private const val DEFAULT_CARD_PRIORITY =
            "WBC, WAC, WQC, WB, WA, WQ, BC, AC, QC, B, A, Q, RB, RA, RQ"
        private const val STAGE_SEPARATOR = "\n"

        val default
            get() =
                of(DEFAULT_CARD_PRIORITY)

        fun from(scoresPerWave: List<CardPriority>) = CardPriorityPerWave(scoresPerWave)

        fun of(priority: String): CardPriorityPerWave =
            if (priority.isBlank()) {
                default
            } else {
                CardPriorityPerWave(
                    priority
                        .split(STAGE_SEPARATOR)
                        .map { CardPriority.of(it) },
                )
            }
    }
}
