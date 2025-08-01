package io.arthurkun.parser.model

import io.arthurkun.parser.enums.CardAffinityEnum
import io.arthurkun.parser.enums.CardTypeEnum


data class CardScore(
    val type: CardTypeEnum,
    val affinity: CardAffinityEnum,
) {
    companion object {
        private const val CRITICAL_CHAR = 'C'
    }

    private fun String.filterCapitals(): String =
        this
            .asSequence()
            .filter { it.isUpperCase() }
            .joinToString(separator = "")

    override fun toString(): String {
        var result = ""

        if (affinity != CardAffinityEnum.Normal && affinity != CardAffinityEnum.NormalCritical) {
            result += "$affinity "
        }

        var criticalExist = false
        if ("$affinity".contains(CRITICAL_CHAR, ignoreCase = true)) {
            criticalExist = true
            result =
                result.filterNot {
                    it.equals(CRITICAL_CHAR, ignoreCase = true)
                }
        }

        result += type

        if (criticalExist) {
            result += CRITICAL_CHAR
        }

        return result.filterCapitals()
    }
}
