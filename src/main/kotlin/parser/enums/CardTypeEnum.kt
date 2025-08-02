package io.arthurkun.parser.enums

enum class CardTypeEnum {
    Buster,
    Arts,
    Quick,

    /**
     * Couldn't detect card type.
     * Can be because Attack screen didn't open up or because the servant is stunned/charmed.
     */
    Unknown,

    ;

    companion object {
        fun from(char: Char) = when (char) {
            'B' -> Buster
            'A' -> Arts
            'Q' -> Quick
            else -> throw Exception("Unknown card type: $char. Only 'B', 'A' and 'Q' are valid card types.")
        }
    }
}
