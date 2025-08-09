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

    /**
     * Combines two StageMarkers into one.
     * If both are Wave, returns Wave.
     * If both are Turn, returns Turn.
     * If one is Wave and the other is Turn, returns Wave.
     */
    operator fun plus(other: StageMarker): StageMarker {
        return if (this is Wave || other is Wave) Wave else Turn
    }
}
