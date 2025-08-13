package io.arthurkun.parser.utils

import io.arthurkun.parser.model.CommandsList
import io.arthurkun.parser.model.SkillActionsTarget

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

typealias Wave = Int
typealias Turn = Int
