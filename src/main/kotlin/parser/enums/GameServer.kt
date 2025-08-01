package io.arthurkun.parser.enums

sealed class GameServer(
    val simple: String,
    val betterFgo: Boolean = false,
) {
    sealed class En(
        betterFgo: Boolean = false,
    ) : GameServer("En", betterFgo) {
        object Original : En()

        object BetterFGO : En(true)
    }

    sealed class Jp(
        betterFgo: Boolean = false,
    ) : GameServer("Jp", betterFgo) {
        object Original : Jp()

        object BetterFGO : Jp(true)
    }

    data object Cn : GameServer("Cn")

    data object Tw : GameServer("Tw")

    data object Kr : GameServer("Kr")

    fun serialize(): String = simple + (if (betterFgo) BETTER_FGO_SUFFIX else "")

    override fun toString(): String = serialize()

    companion object {
        val default = En.Original as GameServer

        private const val BETTER_FGO_SUFFIX = " BFGO"

        /**
         * Maps an APK package name to the corresponding [GameServer].
         */
        fun fromPackageName(packageName: String): GameServer? = packageNames.get(packageName)

        val values =
            listOf(
                En.Original,
                En.BetterFGO,
                Jp.Original,
                Jp.BetterFGO,
                Cn,
                Tw,
                Kr,
            )

        val valuesNoBetterFGO = values.filter { !it.betterFgo }

        val entries: List<GameServer?> = listOf(null) + valuesNoBetterFGO

        val packageNames =
            mapOf(
                "com.aniplex.fategrandorder.en" to En.Original,
                "io.rayshift.betterfgo.en" to En.BetterFGO,
                "com.aniplex.fategrandorder" to Jp.Original,
                "io.rayshift.betterfgo" to Jp.BetterFGO,
                "com.bilibili.fatego" to Cn,
                "com.bilibili.fatego.sharejoy" to Cn,
                "com.komoe.fgomycard" to Tw,
                "com.xiaomeng.fategrandorder" to Tw,
                "com.netmarble.fgok" to Kr,
            )

        private val serializedValues by lazy {
            values.associateBy { it.serialize() }
        }

        fun deserialize(value: String): GameServer? = serializedValues[value]
    }
}
