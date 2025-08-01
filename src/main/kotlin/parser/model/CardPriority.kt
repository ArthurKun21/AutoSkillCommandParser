package io.arthurkun.parser.model

import io.arthurkun.parser.enums.CardAffinityEnum
import io.arthurkun.parser.enums.CardTypeEnum

class CardPriority private constructor(
    scores: List<CardScore>,
) : List<CardScore> by scores {
    override fun toString() = joinToString()

    companion object {
        private const val CARD_PRIORITY_ERROR_MSG = "Battle_CardPriority Error at '"
        private const val DUMMY_NORMAL_CRIT_CHAR = 'X'

        private const val WEAK_AFFINITY_CHAR = 'W'
        private const val DUMMY_NORMAL_AFFINITY_CHAR = 'X'
        private const val RESIST_AFFINITY_CHAR = 'R'

        private const val CRITICAL_AFFINITY_CHAR = 'C'

        /**
         * 0 - The position of the affinity in the card string.
         */
        private const val AFFINITY_POSITION = 0

        /**
         * 1 - The position of the card type in the card string.
         */
        private const val TYPE_POSITION = 1

        /**
         * 2 - The position of the critical affinity in the card string.
         */
        private const val CRITICAL_POSITION = 2

        fun from(scores: List<CardScore>) = CardPriority(scores)

        private fun raiseParseError(msg: String): Nothing = throw Exception("Unable to parse cards $msg")

        fun of(priority: String): CardPriority {
            val scores =
                priority
                    .splitToSequence(',')
                    .map { it.trim().uppercase() }
                    .map { formatCardString(it) }
                    .map { cardString ->
                        parseCards(cardString)
                    }.toList()
                    .let { validateAndFinalizeScores(it) }

            return CardPriority(scores)
        }

        private fun validateAndFinalizeScores(scores: List<CardScore>): List<CardScore> {
            if (scores.size < 9 || scores.size > 15) {
                raiseParseError(
                    "$CARD_PRIORITY_ERROR_MSG': Expected at least 9 but not more than 15 cards, " +
                        "but ${scores.size} found.",
                )
            }
            return when (scores.size) {
                9 -> {
                    expandScoresWithCriticalCards(scores)
                }

                15 -> scores
                else -> raiseParseError("$CARD_PRIORITY_ERROR_MSG': Expected 9 or 15 cards, but ${scores.size} found.")
            }
        }

        /**
         * Expands the scores with critical cards.
         * For example, if a card has a normal affinity, it will be expanded to have a normal critical affinity as well.
         * If a card has a weak affinity, it will be expanded to have a weak critical affinity as well.
         *
         * It will be placed before the original card.
         * @param scores The scores to expand.
         * @return The expanded scores.
         */
        private fun expandScoresWithCriticalCards(scores: List<CardScore>) =
            scores.flatMap { score ->
                listOfNotNull(
                    when (score.affinity) {
                        CardAffinityEnum.Normal ->
                            CardScore(
                                score.type,
                                CardAffinityEnum.NormalCritical,
                            )

                        CardAffinityEnum.Weak ->
                            CardScore(
                                score.type,
                                CardAffinityEnum.WeakCritical,
                            )
                        // Resist cards don't need to be checked.
                        else -> null
                    },
                    score,
                )
            }

        /**
         * Parses the card string into a [CardScore] object.
         * @param cardString The card string to parse.
         * @return The parsed [CardScore] object.
         */
        private fun parseCards(cardString: String): CardScore {
            val cardType = parseCardType(cardString)

            val cardAffinity = parseCardAffinity(cardString)
            val cardCriticalAffinity = parseCardCriticalAffinity(cardString, cardAffinity)

            return CardScore(
                type = cardType,
                affinity = cardCriticalAffinity,
            )
        }

        /**
         * Parses the critical affinity of the card.
         * if it doesn't have 'C' in the third position, it will return the card affinity as is.
         * Otherwise, it will return the critical affinity of the card if the card affinity is normal or weak.
         *
         * @param cardString The card string to parse.
         * @param cardAffinity The affinity of the card.
         */
        private fun parseCardCriticalAffinity(
            cardString: String,
            cardAffinity: CardAffinityEnum,
        ) = when (cardString[CRITICAL_POSITION]) {
            CRITICAL_AFFINITY_CHAR -> {
                when (cardAffinity) {
                    CardAffinityEnum.Normal -> CardAffinityEnum.NormalCritical
                    CardAffinityEnum.Weak -> CardAffinityEnum.WeakCritical
                    else -> CardAffinityEnum.Resist
                }
            }

            else -> cardAffinity
        }

        /**
         * Parses the affinity of the card.
         * @param cardString The card string to parse.
         *
         * @see CardAffinityEnum
         */
        private fun parseCardAffinity(cardString: String) =
            when (cardString[AFFINITY_POSITION]) {
                WEAK_AFFINITY_CHAR -> CardAffinityEnum.Weak
                RESIST_AFFINITY_CHAR -> CardAffinityEnum.Resist
                DUMMY_NORMAL_AFFINITY_CHAR -> CardAffinityEnum.Normal
                else ->
                    raiseParseError(
                        "$CARD_PRIORITY_ERROR_MSG${cardString[AFFINITY_POSITION]}': Only 'W', and 'R' are valid card affinities.",
                    )
            }

        /**
         * Parses the type of the card.
         *
         * @param cardString The card string to parse.
         * @see CardTypeEnum
         */
        private fun parseCardType(cardString: String) =
            try {
                CardTypeEnum.from(char = cardString[TYPE_POSITION])
            } catch (e: Exception) {
                val errorMsg =
                    "$CARD_PRIORITY_ERROR_MSG${cardString[TYPE_POSITION]}': Only 'B', 'A' and 'Q' are valid card types."
                raiseParseError(e.message ?: errorMsg)
            }

        private fun formatCardString(cardString: String) =
            when (cardString.length) {
                1 -> "$DUMMY_NORMAL_AFFINITY_CHAR$cardString$DUMMY_NORMAL_CRIT_CHAR"
                2 -> {
                    if (cardString[AFFINITY_POSITION] == WEAK_AFFINITY_CHAR || cardString[AFFINITY_POSITION] == RESIST_AFFINITY_CHAR) {
                        "$cardString$DUMMY_NORMAL_CRIT_CHAR"
                    } else {
                        "$DUMMY_NORMAL_AFFINITY_CHAR$cardString"
                    }
                }

                3 -> cardString
                else -> raiseParseError("$CARD_PRIORITY_ERROR_MSG$cardString': Invalid card length.")
            }
    }
}
