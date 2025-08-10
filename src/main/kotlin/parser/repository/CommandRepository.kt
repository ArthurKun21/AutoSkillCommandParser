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
        val allCommands = _internalCommand.value.getAllCommandsAsList.toMutableList()

        // Add the command to the end
        allCommands.add(command)

        // Rebuild the stages structure with corrected wave/turn values
        rebuildStagesFromFlatListWithCorrectWaveTurn(allCommands)
    }

    fun createCommandAtPosition(position: Int, command: AutoSkillAction) {
        val allCommands = _internalCommand.value.getAllCommandsAsList.toMutableList()

        // Validate position
        if (position < 0 || position > allCommands.size) {
            throw IndexOutOfBoundsException("Position $position is out of bounds for list of size ${allCommands.size}")
        }

        // Insert the command at the specified position
        allCommands.add(position, command)

        // Rebuild the stages structure with corrected wave/turn values
        rebuildStagesFromFlatListWithCorrectWaveTurn(allCommands)
    }

    fun updateCommandByPosition(position: Int, command: AutoSkillAction) {
        val allCommands = _internalCommand.value.getAllCommandsAsList.toMutableList()

        // Validate position
        if (position < 0 || position >= allCommands.size) {
            throw IndexOutOfBoundsException("Position $position is out of bounds for list of size ${allCommands.size}")
        }

        // Update the command at the specified position
        allCommands[position] = command

        // Rebuild the stages structure with corrected wave/turn values
        rebuildStagesFromFlatListWithCorrectWaveTurn(allCommands)
    }

    fun deleteCommandByPosition(position: Int) {
        val allCommands = _internalCommand.value.getAllCommandsAsList.toMutableList()

        // Validate position
        if (position < 0 || position >= allCommands.size) {
            throw IndexOutOfBoundsException("Position $position is out of bounds for list of size ${allCommands.size}")
        }

        // Remove the command at the specified position
        allCommands.removeAt(position)

        // Rebuild the stages structure with corrected wave/turn values
        rebuildStagesFromFlatListWithCorrectWaveTurn(allCommands)
    }

    fun deleteLatestCommand() {
        if (_internalCommand.value.stages.isEmpty()) return

        val lastIndex = _internalCommand.value.getAllCommandsAsList.lastIndex
        deleteCommandByPosition(lastIndex)
    }

    fun moveActionByPosition(from: Int, to: Int) {
        val allCommands = _internalCommand.value.getAllCommandsAsList.toMutableList()

        // Validate positions
        if (from < 0 || from >= allCommands.size) {
            throw IndexOutOfBoundsException("From position $from is out of bounds for list of size ${allCommands.size}")
        }
        if (to < 0 || to >= allCommands.size) {
            throw IndexOutOfBoundsException("To position $to is out of bounds for list of size ${allCommands.size}")
        }

        // Move the command from one position to another
        val command = allCommands.removeAt(from)
        allCommands.add(to, command)

        // Rebuild the stages structure with corrected wave/turn values
        rebuildStagesFromFlatListWithCorrectWaveTurn(allCommands)
    }

    /**
     * Rebuilds the stages structure from a flat list of commands with proper wave/turn correction.
     * This is needed when commands are inserted/moved and their wave/turn values may be incorrect.
     */
    private fun rebuildStagesFromFlatListWithCorrectWaveTurn(commands: List<AutoSkillAction>) {
        if (commands.isEmpty()) {
            _internalCommand.update { AutoSkillCommand.parse("") }
            return
        }

        // Create command string from the codes and re-parse to get correct wave/turn values
        val commandString = commands.joinToString("") {
            if (it is AutoSkillAction.Atk) {
                // For Atk actions, we need to ensure the wave and turn are set correctly
                "${it.codes}${it.stageMarker.code}"
            } else {
                it.codes
            }
        }
        _internalCommand.update { AutoSkillCommand.parse(commandString) }
    }

    /**
     * Builds a command string from the stages structure.
     * This reconstructs the command string from the actions' codes.
     */
    private fun buildCommandString(stages: StageCommandList): String {
        if (stages.isEmpty()) return ""

        return stages.joinToString(StageMarker.Wave.code) { turns ->
            turns.joinToString(StageMarker.Turn.code) { actions ->
                actions.joinToString("") { it.codes }
            }
        }
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
