package io.arthurkun.parser.repository

import io.arthurkun.parser.model.AutoSkillAction
import io.arthurkun.parser.model.AutoSkillCommand
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CommandRepository {

    private var _autoSkillCommand = MutableStateFlow(AutoSkillCommand.parse(""))

    val autoSkillCommand: StateFlow<AutoSkillCommand>
        get() = _autoSkillCommand.asStateFlow()

    fun setCommand(command: String) {
        _autoSkillCommand.update { AutoSkillCommand.parse(command) }
    }

    fun clearCommand() {
        _autoSkillCommand.update { AutoSkillCommand.parse("") }
    }

    fun createCommand(command: AutoSkillAction) {
    }
}
