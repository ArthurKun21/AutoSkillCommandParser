package io.arthurkun.parser.model

data class NPUsage(
	val nps: Set<CommandCard.NP>,
	val cardsBeforeNP: Int,
) {
	companion object {
		val none = NPUsage(emptySet(), 0)
	}
}
