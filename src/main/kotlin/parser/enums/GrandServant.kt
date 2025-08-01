package io.arthurkun.parser.enums

enum class GrandServant {
    ANY,
    TARGET_LEVEL_100,
    TARGET_ABOVE_LEVEL_100,
    ;

    val isGrand: Boolean
        get() = this != ANY
}
