package io.arthurkun.parser.model

sealed class SpecialCommand(
    val autoSkillCode: Char,
) {
    /**
     * The "t" character that represents the enemy target.
     * This followed by a number that represents the enemy target.
     * @see io.github.fga.models.model.EnemyTarget
     */
    data object EnemyTarget : SpecialCommand('t')

    /**
     * The "x" character that represents the order change.
     * This followed by two characters that represent the starting and sub members.
     * @see OrderChangeMember
     */
    data object OrderChange : SpecialCommand('x')

    /**
     * The "n" character that represents the number of cards before NP.
     * This followed by a number that represents the number of cards before NP.
     */
    data object CardsBeforeNP : SpecialCommand('n')

    /**
     * The "0" character that represents a no-op.
     * No operation this turn is performed.
     */
    data object NoOp : SpecialCommand('0')

    /**
     * The "(" character that starts a multi-target.
     */
    data object StartMultiTarget : SpecialCommand('(')

    /**
     * The ")" character that ends a multi-target.
     */
    data object EndMultiTarget : SpecialCommand(')')

    /**
     * The "[" character that starts a special
     * @see SkillActionsTarget.SpecialSkill
     */
    data object StartSpecialTarget : SpecialCommand('[')

    /**
     * The "]" character that ends a special target code.
     * @see SkillActionsTarget.SpecialSkill
     */
    data object EndSpecialTarget : SpecialCommand(']')
}
