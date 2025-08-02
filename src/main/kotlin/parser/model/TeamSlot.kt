package io.arthurkun.parser.model

sealed class TeamSlot(
	val position: Int,
) {
	data object A : TeamSlot(1)

	data object B : TeamSlot(2)

	data object C : TeamSlot(3)

	data object D : TeamSlot(4)

	data object E : TeamSlot(5)

	data object F : TeamSlot(6)

	data object Unknown : TeamSlot(0)

	override fun toString() = "[$position]"

	companion object {
		val list by lazy {
			listOf(A, B, C, D, E, F)
		}
	}
}
