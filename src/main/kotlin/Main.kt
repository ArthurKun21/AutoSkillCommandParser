package io.arthurkun

import io.arthurkun.parser.model.AutoSkillCommand

fun main() {
    val commands = listOf(
        "",
        "ax11abcdf[Tfrm]gt2j456,i[Tfrm]6",
        "i6,#,gf5,#,4",
    )
    commands.forEachIndexed { index, command ->
        println("Command $index: $command\n")
        try {
            parseCommand(command)
        } catch (e: Exception) {
            println("Error parsing command: ${e.message}")
        }
        if (index != commands.lastIndex) {
            println("--------------------------------------------------")
        }
    }
}

private fun parseCommand(command: String) {
    val stageCommands = AutoSkillCommand.parse(command).stages

    val groupStageCommands = stageCommands
        .flatMap { it }
        .flatMap { it }
        .groupBy {
            Pair(it.wave, it.turn)
        }

    groupStageCommands.forEach { (wave, turn), commands ->
        val parseCommandString = commands.joinToString(separator = "\n") { it.codes }
        val commandString = "Wave:${wave}\t" +
                "Turn:${turn}\n" +
                parseCommandString
        println(commandString)
    }

//    stageCommands.forEach { stageCommand ->
//        stageCommand.forEach { waveCommands ->
//            waveCommands.forEach { commandAction ->
//                val commandString = "Wave:${commandAction.wave}\t" +
//                        "Turn:${commandAction.turn}\t" +
//                        "${commandAction.codes}\n" +
//                        "$commandAction"
//                println(commandString)
//            }
//        }
//    }
}
