package io.arthurkun.parser.model

sealed class StageMarker(val code: String) {
	/**
	 * The ",#," string that represents the next wave.
	 */
	data object Wave : StageMarker(",#,")

	/**
	 * The "," string that represents the end of the turn.
	 */
	data object Turn : StageMarker(",")
}
