package io.arthurkun.parser.exceptions

/**
 * Exception thrown when parsing fails due to various reasons.
 *
 * @param reason The specific reason for the parsing failure
 */
class ParsingException(
    val reason: ParsingReason,
    cause: Throwable? = null,
) : Exception(reason.message, cause)

/**
 * Sealed class representing different reasons why parsing might fail.
 * Each reason contains a descriptive error message.
 *
 * @property message The error message describing the parsing failure
 */
sealed class ParsingReason(val message: String) {

    data class NPCodeNotFollowedByWaveOrTurnOrNPCode(
        val char: Char,
        val nextChar: Char? = null,
    ) : ParsingReason(
        (
            "NP code \"$char\" must be followed by a wave, " +
                "turn, another NP code or None ${nextChar ?: ""}"
            ).trim(),
    )

    /**
     * Indicates that a required end target character is missing.
     *
     * This typically occurs when parsing special targets that should be
     * enclosed in square brackets but the closing bracket is missing.
     *
     * @param char The expected end target character that is missing
     */
    class MissingEndTarget(
        char: Char,
    ) : ParsingReason("Missing expected end target character: '$char'")

    /**
     * Indicates that the command string is empty when it should contain content.
     *
     * This occurs when trying to process an empty or whitespace-only command string
     * in contexts where a valid command is required.
     */
    object EmptyCommand : ParsingReason("Command cannot be empty")

    /**
     * Indicates that a special target string is not recognized.
     *
     * Special targets are enclosed in square brackets (e.g., [Ch2A], [Tfrm])
     * and are used for specific servant abilities that require special handling.
     *
     * @param target The unrecognized special target string
     *
     * @see io.arthurkun.parser.model.SkillActionsTarget.SpecialSkill
     */
    class UnknownSpecialTarget(
        target: String,
    ) : ParsingReason("Special target '$target' is not recognized")

    /**
     * Indicates that a character in the command is not recognized.
     *
     * This occurs when the parser encounters a character that doesn't
     * correspond to any valid auto skill command.
     *
     * @param char The unrecognized character
     */
    class UnknownCommand(
        char: Char,
    ) : ParsingReason("Unknown command character: '$char'")

    /**
     * Indicates that a character cannot be parsed as a valid number.
     *
     * This occurs when expecting a numeric character (0-9) but
     * encountering a non-numeric character instead.
     *
     * @param char The invalid number character
     */
    class InvalidNumber(
        char: Char,
    ) : ParsingReason("Expected number but found: '$char'")

    /**
     * Indicates that an enemy target character is not recognized.
     *
     * Valid enemy targets include positions 1-3 for standard formations,
     * positions 4-9 for six-enemy formations, and special raid targets (R, X, Y, Z).
     *
     * @param char The unrecognized enemy target character
     *
     * @see io.arthurkun.parser.model.EnemyTarget
     */
    class UnknownEnemyTarget(
        char: Char,
    ) : ParsingReason("Unknown enemy target: '$char'")

    /**
     * Indicates that a servant target is required but missing.
     *
     * This occurs when parsing skill actions that require a target servant
     * but no valid target is found in the command string.
     *
     * @see io.arthurkun.parser.model.SkillActionsTarget.fieldList
     */
    object MissingServantTarget : ParsingReason("Missing servant target")

    /**
     * Indicates that a servant target string is not recognized.
     *
     * Valid servant targets include field positions (1, 2, 3) and
     * special positions (7, 8) for two-servant formations.
     *
     * @param target The unrecognized servant target string
     */
    class UnknownServantTarget(
        target: String,
    ) : ParsingReason("Unknown servant target: '$target'")

    /**
     * Indicates that all available command spells have already been used.
     *
     * The maximum number of command spells that can be used is 3.
     */
    object AllCommandSpellsAlreadyUsed : ParsingReason("All command spells already used")

    /**
     * Indicates that the command is incomplete and missing required parts.
     *
     * This occurs when the parser reaches the end of the command string
     * while still expecting additional characters to complete the command.
     */
    object IncompleteCommand : ParsingReason("Incomplete command")

    /**
     * Indicates that an error occurred while parsing a skill command.
     *
     * @param exception The underlying exception that caused the parsing error
     */
    class SkillCommandParseError(
        val exception: Exception,
    ) : ParsingReason("Error parsing skill command: ${exception.message ?: exception.toString()}")
}
