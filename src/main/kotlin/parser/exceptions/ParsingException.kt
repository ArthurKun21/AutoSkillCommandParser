package io.arthurkun.parser.exceptions

sealed class ParsingException(
    message: String,
) : Exception(message) {
    class MissingEndTarget(
        char: Char,
    ) : ParsingException("Missing end target: $char")

    class EmptyCommand : ParsingException("Command cannot be empty")

    class UnknownSpecialTarget(
        target: String,
    ) : ParsingException("Special target \"$target\" not found")

    class UnknownCommand(
        char: Char,
    ) : ParsingException("Unknown character: $char")

    class InvalidNumber(
        char: Char,
    ) : ParsingException("Invalid number char: $char")

    class UnknownEnemyTarget(
        char: Char,
    ) : ParsingException("Unknown enemy target: $char")

    class IncompleteCommand : ParsingException("Incomplete command")

    class SkillCommandParseError(
        exception: Exception,
    ) : ParsingException("Error parsing skill command: ${exception.message ?: exception.toString()}")

    class QueuePollError() : ParsingException("Error polling from queue")
}