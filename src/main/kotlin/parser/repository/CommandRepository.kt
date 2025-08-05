package io.arthurkun.parser.repository

import io.arthurkun.parser.model.AutoSkillAction
import io.arthurkun.parser.model.AutoSkillCommand
import io.arthurkun.parser.model.CommandsList
import io.arthurkun.parser.model.StageMarker
import io.arthurkun.parser.utils.StageCommandList
import io.arthurkun.parser.utils.Turn
import io.arthurkun.parser.utils.Wave
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class CommandRepository {

    private var _internalCommand = MutableStateFlow(AutoSkillCommand.parse(""))

    val internalCommand: StateFlow<AutoSkillCommand>
        get() = _internalCommand.asStateFlow()

    val commandListByWaveTurn = internalCommand
        .map { skillCommand ->
            skillCommand.stages
                .flatMap { it }
                .flatMap { it }
                .groupBy { command ->
                    Pair(command.wave, command.turn)
                }
                .map { (waveTurn, commands) ->
                    StageCommandListItem(
                        waveTurn = waveTurn,
                        commandsList = commands,
                    )
                }
        }

    /**
     * Gets the raw command string representation of the current state.
     * @return The command string that would generate the current AutoSkillCommand
     */
    fun getCommandString(): String = buildCommandString(internalCommand.value.stages)

    fun setCommand(command: String) {
        _internalCommand.update { AutoSkillCommand.parse(command) }
    }

    fun clearCommand() {
        _internalCommand.update { AutoSkillCommand.parse("") }
    }

    fun createCommand(command: AutoSkillAction) {
    }

    fun updateCommandByPosition(position: Int, command: AutoSkillAction) {

    }

    fun deleteCommandByPosition(position: Int) {

    }

    fun deleteLatestCommand() {

    }

    fun moveActionByPosition(from: Int, to: Int) {

    }

    /**
     * Ensures that the stages structure can accommodate the specified wave and turn.
     * Expands the structure if necessary.
     */
    private fun ensureStageExists(wave: Int, turn: Int): StageCommandList {
        val stages = _internalCommand.value.stages.toMutableList()

        // Ensure we have enough waves
        while (stages.size <= wave) {
            stages.add(emptyList())
        }

        // Ensure we have enough turns in the specified wave
        val currentWave = stages[wave].toMutableList()
        while (currentWave.size <= turn) {
            currentWave.add(emptyList())
        }
        stages[wave] = currentWave

        return stages
    }

    /**
     * Builds a command string from the stages structure.
     * This reconstructs the command string from the actions' codes.
     */
    private fun buildCommandString(stages: StageCommandList): String {
        if (stages.isEmpty()) return ""

        return stages.joinToString(StageMarker.Wave.code) { turns ->
            if (turns.isEmpty()) ""
            else turns.joinToString(StageMarker.Turn.code) { actions ->
                if (actions.isEmpty()) ""
                else actions.joinToString("") { it.codes }
            }
        }.removeSuffix(StageMarker.Wave.code)
            .removeSuffix(StageMarker.Turn.code)
    }
}

data class StageCommandListItem(
    val waveTurn: Pair<Wave, Turn>,
    val commandsList: CommandsList,
) {
    val wave: Wave
        get() = waveTurn.first

    val turn: Turn
        get() = waveTurn.second
}
