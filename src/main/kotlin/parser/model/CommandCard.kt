package io.arthurkun.parser.model

sealed class CommandCard {
    sealed class Face(
        val index: Int,
    ) : CommandCard() {
        data object A : Face(1)

        data object B : Face(2)

        data object C : Face(3)

        data object D : Face(4)

        data object E : Face(5)

        companion object {
            val list by lazy { listOf(A, B, C, D, E) }
        }

        override fun toString() = "$index"
    }

    sealed class NP(
        val autoSkillCode: Char,
    ) : CommandCard() {
        data object A : NP('4')

        data object B : NP('5')

        data object C : NP('6')

        companion object {
            val list by lazy { listOf(A, B, C) }
        }

        override fun toString() = "$autoSkillCode"
    }
}

fun CommandCard.NP.toFieldSlot() =
    when (this) {
        CommandCard.NP.A -> FieldSlot.A
        CommandCard.NP.B -> FieldSlot.B
        CommandCard.NP.C -> FieldSlot.C
    }
