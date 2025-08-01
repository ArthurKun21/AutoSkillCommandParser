package io.arthurkun.parser.model

sealed class SkillSource(
    val autoSkillCode: Char,
) {
    /**
     * Skill is casted by a servant in the field.
     */
    sealed class Servant(
        autoSkillCode: Char,
    ) : SkillSource(autoSkillCode) {
        /**
         * First Servant's first skill.
         */
        data object AS1 : Servant('a')

        /**
         * First Servant's second skill.
         */
        data object AS2 : Servant('b')

        /**
         * First Servant's third skill.
         */
        data object AS3 : Servant('c')

        /**
         * Second Servant's first skill.
         */
        data object BS1 : Servant('d')

        /**
         * Second Servant's second skill.
         */
        data object BS2 : Servant('e')

        /**
         * Second Servant's third skill.
         */
        data object BS3 : Servant('f')

        /**
         * Third Servant's first skill.
         */
        data object CS1 : Servant('g')

        /**
         * Third Servant's second skill.
         */
        data object CS2 : Servant('h')

        /**
         * Third Servant's third skill.
         */
        data object CS3 : Servant('i')

        companion object {
            val list by lazy {
                listOf(
                    AS1,
                    AS2,
                    AS3,
                    BS1,
                    BS2,
                    BS3,
                    CS1,
                    CS2,
                    CS3,
                )
            }
            val skill1 by lazy {
                listOf(AS1, BS1, CS1)
            }
            val skill2 by lazy {
                listOf(AS2, BS2, CS2)
            }
            val skill3 by lazy {
                listOf(AS3, BS3, CS3)
            }
        }
    }

    /**
     * Skill is casted by a master using a mystic code.
     */
    sealed class Master(
        autoSkillCode: Char,
    ) : SkillSource(autoSkillCode) {
        /**
         * * Mystic Code's first skill.
         */
        data object MC1 : Master('j')

        /**
         * * Mystic Code's second skill.
         */
        data object MC2 : Master('k')

        /**
         * * Mystic Code's third skill.
         */
        data object MC3 : Master('l')

        companion object {
            val list by lazy { listOf(MC1, MC2, MC3) }
        }
    }

    /**
     * Skill is casted by a master using a command spell.
     */
    sealed class CommandSpell(
        autoSkillCode: Char,
    ) : SkillSource(autoSkillCode) {
        /**
         * Command Spell's Full NP
         */
        data object CS1 : CommandSpell('o')

        /**
         * Command Spell's Full HP
         */
        data object CS2 : CommandSpell('p')

        companion object {
            val list by lazy { listOf(CS1, CS2) }
        }
    }
}
