package io.arthurkun.parser.model

sealed class BoostItem {
    data object Disabled : BoostItem()

    sealed class Enabled : BoostItem() {
        data object Skip : Enabled()

        data object BoostItem1 : Enabled()

        data object BoostItem2 : Enabled()

        data object BoostItem3 : Enabled()
    }

    companion object {
        fun of(value: Int): BoostItem =
            when (value) {
                0 -> Enabled.Skip
                1 -> Enabled.BoostItem1
                2 -> Enabled.BoostItem2
                3 -> Enabled.BoostItem3
                else -> Disabled
            }
    }
}
