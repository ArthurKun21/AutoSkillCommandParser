package io.arthurkun.parser.enums

enum class RefillResourceEnum {
	Copper,
	Bronze,
	Silver,
	Gold,
	SQ,
	;

	/**
	 * Returns true if the resource can be used for refilling.
	 * Gold and SQ are not allowed.
	 */
	fun overRechargeAvailable(): Boolean = this !in listOf(Gold, SQ)
}
