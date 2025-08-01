package io.arthurkun.parser.model

sealed class AutoSkillAction(
    open val wave: Int,
    open val turn: Int,
    open val codes: String,
) {
    /**
     * Represents a set of NPs and the number of cards to use before the NP in a single turn.
     * @param nps The set of NPs to use
     * @param numberOfCardsBeforeNP The number of cards to use before the NP
     * @param wave Wave this action is in
     * @param turn Turn this action is in
     */
    data class Atk(
        val nps: Set<CommandCard.NP>,
        val numberOfCardsBeforeNP: Int,
        override val wave: Int,
        override val turn: Int,
        override val codes: String,
    ) : AutoSkillAction(
        wave = wave,
        turn = turn,
        codes = codes
    ) {
        init {
            require(numberOfCardsBeforeNP in 0..2) { "Only 0, 1 or 2 cards can be used before NP" }
        }

        operator fun plus(other: Atk) =
            Atk(
                nps = nps + other.nps,
                numberOfCardsBeforeNP = numberOfCardsBeforeNP + other.numberOfCardsBeforeNP,
                wave = wave,
                turn = turn,
                codes = codes + other.codes
            )

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is Atk) return false

            return nps == other.nps && numberOfCardsBeforeNP == other.numberOfCardsBeforeNP
        }

        override fun hashCode(): Int {
            var result = nps.hashCode()
            result = 31 * result + numberOfCardsBeforeNP
            return result
        }

        fun toNPUsage() = NPUsage(nps, numberOfCardsBeforeNP)

        companion object {
            fun noOp(
                wave: Int = 0,
                turn: Int = 0,
            ) = Atk(
                nps = emptySet(),
                numberOfCardsBeforeNP = 0,
                wave = wave,
                turn = turn,
                codes = ""
            )

            fun np(
                nps: Set<CommandCard.NP>,
                wave: Int = 0,
                turn: Int = 0,
                codes: String,
            ) = Atk(
                nps = nps,
                numberOfCardsBeforeNP = 0,
                wave = wave,
                turn = turn,
                codes = codes
            )

            fun cardsBeforeNP(
                numberOfCardsBeforeNP: Int,
                wave: Int = 0,
                turn: Int = 0,
                codes: String,
            ) = Atk(
                nps = emptySet(),
                numberOfCardsBeforeNP = numberOfCardsBeforeNP,
                wave = wave,
                turn = turn,
                codes = codes
            )
        }
    }

    data class ServantSkill(
        val skillSource: SkillSource.Servant,
        val targets: List<SkillActionsTarget>,
        override val wave: Int,
        override val turn: Int,
        override val codes: String,
    ) : AutoSkillAction(
        wave = wave,
        turn = turn,
        codes = codes
    )

    data class MasterSkill(
        val skillSource: SkillSource.Master,
        val target: SkillActionsTarget?,
        override val wave: Int,
        override val turn: Int,
        override val codes: String,
    ) : AutoSkillAction(
        wave = wave,
        turn = turn,
        codes = codes
    )

    data class CommandSpell(
        val skillSource: SkillSource.CommandSpell,
        val target: SkillActionsTarget?,
        override val wave: Int,
        override val turn: Int,
        override val codes: String,
    ) : AutoSkillAction(
        wave = wave,
        turn = turn,
        codes = codes
    )

    data class TargetEnemy(
        val enemy: EnemyTarget,
        override val wave: Int,
        override val turn: Int,
        override val codes: String,
    ) : AutoSkillAction(
        wave = wave,
        turn = turn,
        codes = codes
    )

    data class OrderChange(
        val starting: OrderChangeMember.Starting,
        val sub: OrderChangeMember.Sub,
        override val wave: Int,
        override val turn: Int,
        override val codes: String,
    ) : AutoSkillAction(
        wave = wave,
        turn = turn,
        codes = codes
    )
}
