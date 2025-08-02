package io.arthurkun.parser.exceptions

class ParsingException(val reason: ParsingReason) : Exception(reason.message)

sealed class ParsingReason(val message: String) {
    class MissingEndTarget(
        char: Char,
    ) : ParsingReason("Missing end target: $char")

    object EmptyCommand : ParsingReason("Command cannot be empty")

    class UnknownSpecialTarget(
        target: String,
    ) : ParsingReason("Special target \"$target\" not found")

    class UnknownCommand(
        char: Char,
    ) : ParsingReason("Unknown character: $char")

    class InvalidNumber(
        char: Char,
    ) : ParsingReason("Invalid number char: $char")

    class UnknownEnemyTarget(
        char: Char,
    ) : ParsingReason("Unknown enemy target: $char")

    object MissingServantTarget : ParsingReason("Missing servant target")

    class UnknownServantTarget(
        target: String,
    ) : ParsingReason("Unknown servant target: $target")

    object AllCommandSpellsAlreadyUsed : ParsingReason("All command spells already used")

    object IncompleteCommand : ParsingReason("Incomplete command")

    class SkillCommandParseError(
        exception: Exception,
    ) : ParsingReason("Error parsing skill command: ${exception.message ?: exception.toString()}")

    object QueuePollError : ParsingReason("Error polling from queue")
}