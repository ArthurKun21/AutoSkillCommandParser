package io.arthurkun.parser.model

import io.arthurkun.parser.exceptions.ParsingException
import io.arthurkun.parser.exceptions.ParsingReason
import java.util.*

/**
 * Represents list of commands for a single turn in an auto skill command.
 */
typealias CommandsList = List<AutoSkillAction>
/**
 * Represents a list of [CommandsList] for each turn in a stage of an auto skill command.
 */
typealias TurnCommandsList = List<CommandsList>
/**
 * Represents a list of [TurnCommandsList] for each stage in an auto skill command.
 * This is the top-level structure for an auto skill command.
 */
typealias StageCommandList = List<TurnCommandsList>

typealias ActionTargetListAndCodes = Pair<List<SkillActionsTarget>, String>
typealias ActionTargetAndCode = Pair<SkillActionsTarget?, String>

class AutoSkillCommand private constructor(val stages: StageCommandList) {
    operator fun get(
        stage: Int,
        turn: Int,
    ): CommandsList = stages.getOrNull(stage)?.getOrNull(turn) ?: emptyList()

    val getTotalCommandTurns
        get() = stages.flatten().size

    fun commandTurnsUntilStage(stage: Int): Int =
        when (stage) {
            0 -> stages[stage].flatten().size
            else -> stages.take(stage).flatten().size
        }

    fun commandTurnsAtStage(stage: Int): Int = stages.getOrNull(stage)?.flatten()?.size ?: 0

    private class CommandParser {

        companion object {
            private const val MAX_COMMAND_SPELLS = 3
        }

        fun parseCommand(command: String): AutoSkillCommand {
            val trimmedCommand = command.trim()
            if (trimmedCommand.isEmpty()) return AutoSkillCommand(emptyList())

            var currentWave = 0
            var currentTurn = 0

            val commandTable =
                trimmedCommand
                    .split(StageMarker.Wave.code)
                    .map { waveCommandList ->
                        currentWave++
                        waveCommandList
                            .split(StageMarker.Turn.code)
                            .map { cmd ->
                                currentTurn++
                                parseActions(cmd = cmd, wave = currentWave, turn = currentTurn)
                            }
                    }

            return AutoSkillCommand(commandTable)
        }

        private fun parseActions(
            cmd: String,
            wave: Int,
            turn: Int,
        ): CommandsList {
            val queue: Deque<Char> = ArrayDeque(cmd.length)
            queue.addAll(cmd.asIterable())

            var commandSpellUsed = 0

            return buildList {
                while (queue.isNotEmpty()) {
                    val action = parseAction(queue = queue, wave = wave, turn = turn)

                    // track the number of command spells used
                    if (action is AutoSkillAction.CommandSpell) {
                        commandSpellUsed++
                        if (commandSpellUsed > MAX_COMMAND_SPELLS) {
                            throw ParsingException(ParsingReason.AllCommandSpellsAlreadyUsed)
                        }
                    }

                    // merge NPs and cards before NPs
                    if (isNotEmpty() && action is AutoSkillAction.Atk) {
                        val last = last()
                        if (last is AutoSkillAction.Atk) {
                            this[lastIndex] = last + action
                            continue
                        }
                    }

                    add(action)
                }
            }
        }

        private fun parseAction(
            queue: Queue<Char>,
            wave: Int = 0,
            turn: Int = 0,
        ): AutoSkillAction {
            try {
                val currentChar = queue.poll() ?: throw ParsingException(ParsingReason.QueuePollError)
                return when (currentChar) {
                    in servantSkillSourceCodes -> {
                        val skillSource = SkillSource.Servant.list
                            .first { it.autoSkillCode == currentChar }
                        val (actionTargets, targetCodes) = getTargets(queue)

                        val codes = "$currentChar$targetCodes"

                        AutoSkillAction.ServantSkill(
                            skillSource = skillSource,
                            targets = actionTargets,
                            wave = wave,
                            turn = turn,
                            codes = codes
                        )
                    }

                    in masterSkillSourceCodes -> {
                        val skillSource = SkillSource.Master.list
                            .first { it.autoSkillCode == currentChar }
                        val (actionTarget, targetCodes) = getTarget(queue)

                        val codes = "$currentChar$targetCodes"

                        AutoSkillAction.MasterSkill(
                            skillSource = skillSource,
                            target = actionTarget,
                            wave = wave,
                            turn = turn,
                            codes = codes
                        )
                    }

                    in commandSpellCodes -> {
                        val spell = SkillSource.CommandSpell.list.first { it.autoSkillCode == currentChar }
                        val (actionTarget, targetCodes) = getTarget(queue)

                        val target = actionTarget ?: throw ParsingException(ParsingReason.MissingServantTarget)

                        if (target !in SkillActionsTarget.fieldList) {
                            throw ParsingException(ParsingReason.UnknownServantTarget(targetCodes))
                        }

                        val codes = "$currentChar$targetCodes"

                        AutoSkillAction.CommandSpell(
                            skillSource = spell,
                            target = actionTarget,
                            wave = wave,
                            turn = turn,
                            codes = codes
                        )
                    }

                    in npCodes -> {
                        val np = CommandCard.NP.list.first { it.autoSkillCode == currentChar }

                        val codes = "$currentChar"

                        AutoSkillAction.Atk.np(
                            nps = setOf(np),
                            wave = wave,
                            turn = turn,
                            codes = codes
                        )
                    }

                    SpecialCommand.EnemyTarget.autoSkillCode -> {
                        checkIfQueueIsEmpty(queue)
                        val targetCode = queue.poll() ?: throw ParsingException(ParsingReason.QueuePollError)

                        val target = EnemyTarget.list
                            .firstOrNull { it.autoSkillCode == targetCode }
                            ?: throw ParsingException(ParsingReason.UnknownEnemyTarget(char = targetCode))

                        val codes = "$currentChar$targetCode"

                        AutoSkillAction.TargetEnemy(
                            target,
                            wave,
                            turn,
                            codes = codes
                        )
                    }

                    SpecialCommand.CardsBeforeNP.autoSkillCode -> {
                        checkIfQueueIsEmpty(queue)
                        val targetCode = queue.poll() ?: throw ParsingException(ParsingReason.QueuePollError)

                        if (!Character.isDigit(targetCode)) {
                            throw ParsingException(ParsingReason.InvalidNumber(char = targetCode))
                        }
                        val count = Character.getNumericValue(targetCode)

                        val codes = "$currentChar$targetCode"

                        AutoSkillAction.Atk.cardsBeforeNP(
                            numberOfCardsBeforeNP = count,
                            wave = wave,
                            turn = turn,
                            codes = codes
                        )
                    }

                    SpecialCommand.OrderChange.autoSkillCode -> {
                        checkIfQueueIsEmpty(queue)
                        val startingCode = queue.poll() ?: throw ParsingException(ParsingReason.QueuePollError)

                        val starting =
                            OrderChangeMember.Starting.list
                                .firstOrNull { it.autoSkillCode == startingCode }
                                ?: throw ParsingException(ParsingReason.UnknownCommand(char = startingCode))

                        checkIfQueueIsEmpty(queue)
                        val subCode = queue.poll() ?: throw ParsingException(ParsingReason.QueuePollError)

                        val sub =
                            OrderChangeMember.Sub.list
                                .firstOrNull { it.autoSkillCode == subCode }
                                ?: throw ParsingException(ParsingReason.UnknownCommand(char = subCode))

                        val codes = "$currentChar$startingCode$subCode"

                        AutoSkillAction.OrderChange(
                            starting = starting,
                            sub = sub,
                            wave = wave,
                            turn = turn,
                            codes = codes
                        )
                    }

                    SpecialCommand.NoOp.autoSkillCode ->
                        AutoSkillAction.Atk.noOp(
                            wave = wave,
                            turn = turn,
                        )

                    else -> throw ParsingException(ParsingReason.UnknownCommand(char = currentChar))
                }
            } catch (e: Exception) {
                if (e is ParsingException) throw e
                throw ParsingException(ParsingReason.SkillCommandParseError(e), e)
            }
        }

        private fun checkIfQueueIsEmpty(queue: Queue<Char>) {
            if (queue.isEmpty()) {
                throw ParsingException(ParsingReason.IncompleteCommand)
            }
        }

        private fun getTarget(queue: Queue<Char>): ActionTargetAndCode {
            val peekTarget = queue.peek() ?: return null to ""

            var actionTarget: SkillActionsTarget? = null
            val targetCodes = StringBuilder()

            if (peekTarget == SpecialCommand.StartSpecialTarget.autoSkillCode) {
                // remove initial [
                val code = queue.poll() ?: throw ParsingException(ParsingReason.QueuePollError)
                // Append the starting special target code
                targetCodes.append(code)

                val special = StringBuilder()
                var char: Char? = null

                while (queue.isNotEmpty()) {
                    char = queue.poll() ?: throw ParsingException(ParsingReason.QueuePollError)

                    if (char == SpecialCommand.EndSpecialTarget.autoSkillCode) {
                        // Append the ending special target code
                        targetCodes.append(char)
                        break
                    }

                    if (char != SpecialCommand.StartSpecialTarget.autoSkillCode) {
                        special.append(char)
                    }

                    specialTargetList
                        .firstOrNull {
                            it.specialTarget == special.toString()
                        }?.let {
                            targetCodes.append(special.toString())
                            actionTarget = it
                        }
                }
                if (char != SpecialCommand.EndSpecialTarget.autoSkillCode) {
                    throw ParsingException(ParsingReason.MissingEndTarget(SpecialCommand.EndSpecialTarget.autoSkillCode))
                }
                if (special.isEmpty()) throw ParsingException(ParsingReason.EmptyCommand)

                if (actionTarget == null) throw ParsingException(ParsingReason.UnknownSpecialTarget(target = special.toString()))
            } else {
                SkillActionsTarget
                    .list
                    .firstOrNull { it.autoSkillCode == peekTarget }
                    ?.let {
                        val char = queue.poll() ?: throw ParsingException(ParsingReason.QueuePollError)
                        targetCodes.append(char)
                        actionTarget = it
                    }
            }
            return actionTarget to targetCodes.toString()
        }

        /**
         * Parses multiple targets from the command queue.
         * Handles both multi-target syntax (targets) and single target syntax.
         *
         * Multi-target format: (target1target2[special1][SkillActionsTarget.specialTarget]...)
         * Single target format: target or [SkillActionsTarget.specialTarget]
         *
         * @param queue The character queue to parse from
         * @return List of SkillActionsTarget objects and a string for target codes
         * @throws ParsingException for various parsing errors related to multi-targets
         */
        private fun getTargets(queue: Queue<Char>): ActionTargetListAndCodes {
            val actionTargets = mutableListOf<SkillActionsTarget>()
            val targetCodes = StringBuilder()

            val nextChar = queue.peek()
            if (nextChar == SpecialCommand.StartMultiTarget.autoSkillCode) {
                // Parse multi-target: (target1target2...)
                val startMultiTargetChar = queue.poll() ?: throw ParsingException(ParsingReason.QueuePollError)
                targetCodes.append(startMultiTargetChar)

                var char: Char? = null
                var specialFound = false

                val special = StringBuilder()

                while (queue.isNotEmpty()) {
                    char = queue.poll() ?: throw ParsingException(ParsingReason.QueuePollError)

                    if (char == SpecialCommand.EndMultiTarget.autoSkillCode) {
                        targetCodes.append(char)
                        break
                    }

                    if (char == SpecialCommand.StartSpecialTarget.autoSkillCode) {
                        targetCodes.append(char)
                        specialFound = true
                    } else if (char == SpecialCommand.EndSpecialTarget.autoSkillCode) {
                        specialTargetList
                            .firstOrNull {
                                it.specialTarget == special.toString()
                            }
                            ?.let { target ->
                                actionTargets.add(target)
                                // Append to overall target code
                                targetCodes.append(special.toString())
                                targetCodes.append(char)

                                special.clear() // Reset StringBuilder efficiently
                            } ?: run {

                            if (special.isEmpty()) {
                                throw ParsingException(ParsingReason.EmptyCommand)
                            } else {
                                throw ParsingException(ParsingReason.UnknownSpecialTarget(target = special.toString()))
                            }
                        }

                        specialFound = false
                    }

                    if (specialFound) {
                        if (char != SpecialCommand.StartSpecialTarget.autoSkillCode) {
                            special.append(char)
                        }
                    } else {
                        SkillActionsTarget
                            .list
                            .firstOrNull { it.autoSkillCode == char }
                            ?.let { target ->
                                actionTargets.add(target)
                                targetCodes.append(char)
                            }
                    }
                }
                if (char != SpecialCommand.EndMultiTarget.autoSkillCode) {
                    throw ParsingException(
                        ParsingReason
                            .MissingEndTarget(SpecialCommand.EndMultiTarget.autoSkillCode)
                    )
                }
                if (specialFound) {
                    throw ParsingException(
                        ParsingReason
                            .MissingEndTarget(SpecialCommand.EndSpecialTarget.autoSkillCode)
                    )
                }
            } else {
                val (target, code) = getTarget(queue)
                target?.let {
                    actionTargets.add(it)
                    targetCodes.append(code)
                }
            }

            return actionTargets to targetCodes.toString()
        }
    }

    companion object {
        // Cache frequently accessed lists
        private val specialTargetList =
            SkillActionsTarget.list.filter { it.specialTarget.isNotEmpty() }
        private val servantSkillSourceCodes =
            SkillSource.Servant.list
                .map { it.autoSkillCode }
                .toSet()
        private val masterSkillSourceCodes =
            SkillSource.Master.list
                .map { it.autoSkillCode }
                .toSet()
        private val commandSpellCodes =
            SkillSource.CommandSpell.list
                .map { it.autoSkillCode }
                .toSet()
        private val npCodes =
            CommandCard.NP.list
                .map { it.autoSkillCode }
                .toSet()

        fun parse(command: String): AutoSkillCommand = CommandParser().parseCommand(command)
    }
}
