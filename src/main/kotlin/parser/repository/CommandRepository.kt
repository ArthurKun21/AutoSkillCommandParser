package io.arthurkun.parser.repository

import io.arthurkun.parser.model.AutoSkillAction
import io.arthurkun.parser.model.AutoSkillCommand
import io.arthurkun.parser.model.CommandsList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

typealias Wave = Int
typealias Turn = Int

class CommandRepository {

    private var _autoSkillCommand = MutableStateFlow(AutoSkillCommand.parse(""))

    val autoSkillCommand: StateFlow<AutoSkillCommand>
        get() = _autoSkillCommand.asStateFlow()

    val commandListByWaveTurn = autoSkillCommand
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

    fun setCommand(command: String) {
        _autoSkillCommand.update { AutoSkillCommand.parse(command) }
    }

    fun clearCommand() {
        _autoSkillCommand.update { AutoSkillCommand.parse("") }
    }

    fun createCommand(command: AutoSkillAction) {
    }
}

data class StageCommandListItem(
    val waveTurn: Pair<Wave, Turn>,
    val commandsList: CommandsList,
)
