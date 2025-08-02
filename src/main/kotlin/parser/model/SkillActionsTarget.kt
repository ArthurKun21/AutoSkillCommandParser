package io.arthurkun.parser.model

/**
 * Represents a target for a skill action.
 * Whether the target is servant or another servant, or a button for special effects.
 */
sealed class SkillActionsTarget(
    val autoSkillCode: Char,
    val specialTarget: String = "",
) {
    /**
     * The "1" character that represents the first servant.
     * @see FieldSlot.A
     */
    data object A : SkillActionsTarget('1')

    /**
     * The "2" character that represents the second servant.
     * @see FieldSlot.B
     */
    data object B : SkillActionsTarget('2')

    /**
     * The "3" character that represents the third servant.
     * @see FieldSlot.C
     */
    data object C : SkillActionsTarget('3')

    /**
     * The "7" character that represents the left button.
     * This is what happens when there is only two servants on the field.
     */
    data object Left : SkillActionsTarget('7')

    /**
     * The "8" character that represents the right button.
     * This is what happens when there is only two servants on the field.
     */
    data object Right : SkillActionsTarget('8')

    // Kukulcan - keeping this for legacy purposes
    @Deprecated(
        message = "Use SpecialSkill.Choice2OptionA instead",
        level = DeprecationLevel.WARNING,
    )
    data object Option1 : SkillActionsTarget('K')

    @Deprecated(
        message = "Use SpecialSkill.Choice2OptionB instead",
        level = DeprecationLevel.WARNING,
    )
    data object Option2 : SkillActionsTarget('U')

    @Deprecated(
        message = "Use SpecialSkill.Transform instead",
        level = DeprecationLevel.WARNING,
    )
    data object Transform : SkillActionsTarget('M')

    sealed class SpecialSkill(
        targetCode: String,
    ) : SkillActionsTarget(
        autoSkillCode = SpecialCommand.StartSpecialTarget.autoSkillCode,
        specialTarget = targetCode,
    ) {
        companion object {
            private val codes = mutableSetOf<String>()

            private fun validateString(targetCode: String) {
                for (existingCode in codes) {
                    require(
                        !(
                            targetCode.startsWith(existingCode) ||
                                existingCode.startsWith(
                                    targetCode,
                                )
                                ),
                    ) {
                        "Special target code " +
                            "$targetCode conflicts with existing code $existingCode"
                    }
                }
                codes.add(targetCode)
            }
        }

        init {
            validateString(targetCode)
        }

        // Kukulcan
        data object Choice2OptionA : SpecialSkill("Ch2A")

        data object Choice2OptionB : SpecialSkill("Ch2B")

        // Soujuurou/Charlotte/Hakunon
        data object Choice3OptionA : SpecialSkill("Ch3A")

        data object Choice3OptionB : SpecialSkill("Ch3B")

        data object Choice3OptionC : SpecialSkill("Ch3C")

        data object Transform : SpecialSkill("Tfrm")
    }

    companion object {
        @Suppress("DEPRECATION")
        val list by lazy {
            listOf(
                A,
                B,
                C,
                Left,
                Right,
                Option1,
                Option2,
                Transform,
                SpecialSkill.Choice3OptionA,
                SpecialSkill.Choice3OptionB,
                SpecialSkill.Choice3OptionC,
                SpecialSkill.Choice2OptionA,
                SpecialSkill.Choice2OptionB,
                SpecialSkill.Transform,
            )
        }

        val fieldList by lazy {
            listOf(
                A,
                B,
                C,
                Left,
                Right,
            )
        }
    }
}
