package io.arthurkun.parser.model

import io.arthurkun.parser.enums.CardAffinityEnum
import io.arthurkun.parser.enums.CardTypeEnum

data class ParsedCommandCard(
	val card: CommandCard.Face,
	val servant: TeamSlot,
	val fieldSlot: FieldSlot?,
	val type: CardTypeEnum,
	val affinity: CardAffinityEnum = CardAffinityEnum.Normal,
	val isStunned: Boolean = false,
	val criticalPercentage: Int = 0,
) {
	override fun equals(other: Any?) = other is ParsedCommandCard && card == other.card

	override fun hashCode() = card.hashCode()
}
