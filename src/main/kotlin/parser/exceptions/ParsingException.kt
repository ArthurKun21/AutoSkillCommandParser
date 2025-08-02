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
    /**
     * Indicates that a required end target character is missing.
     *
     * @param char The expected end target character that is missing
     */
    class MissingEndTarget(
        char: Char,
    ) : ParsingReason("Missing end target: $char")

    /**
     * Indicates that the command string is empty when it should contain content.
     */
    object EmptyCommand : ParsingReason("Command cannot be empty")

    /**
     * Indicates that a special target string is not recognized.
     *
     * @param target The unrecognized special target string
     *
     * @see io.arthurkun.parser.model.SkillActionsTarget.specialTarget
     */
    class UnknownSpecialTarget(
        target: String,
    ) : ParsingReason("Special target \"$target\" not found")

    /**
     * Indicates that a character in the command is not recognized.
     *
     * @param char The unrecognized character
     */
    class UnknownCommand(
        char: Char,
    ) : ParsingReason("Unknown character: $char")

    /**
     * Indicates that a character cannot be parsed as a valid number.
     *
     * @param char The invalid number character
     */
    class InvalidNumber(
        char: Char,
    ) : ParsingReason("Invalid number character: $char")

    /**
     * Indicates that an enemy target character is not recognized.
     *
     * @param char The unrecognized enemy target character
     *
     * @see io.arthurkun.parser.model.EnemyTarget
     */
    class UnknownEnemyTarget(
        char: Char,
    ) : ParsingReason("Unknown enemy target: $char")

    /**
     * Indicates that a servant target is required but missing.
     *
     * @see io.arthurkun.parser.model.SkillActionsTarget.fieldList
     */
    object MissingServantTarget : ParsingReason("Missing servant target")

    /**
     * Indicates that a servant target string is not recognized.
     *
     * @param target The unrecognized servant target string
     */
    class UnknownServantTarget(
        target: String,
    ) : ParsingReason("Unknown servant target: $target")

    /**
     * Indicates that all available command spells have already been used.
     *
     * Max command spells are 3
     */
    object AllCommandSpellsAlreadyUsed : ParsingReason("All command spells already used")

    /**
     * Indicates that the command is incomplete and missing required parts.
     */
    object IncompleteCommand : ParsingReason("Incomplete command")

    /**
     * Indicates that an error occurred while parsing a skill command.
     *
     * @param exception The underlying exception that caused the parsing error
     */
    class SkillCommandParseError(
        exception: Exception,
    ) : ParsingReason("Error parsing skill command: ${exception.message ?: exception.toString()}")

    /**
     * Indicates that an error occurred while polling from a queue.
     */
    object QueuePollError : ParsingReason("Error polling from queue")
}