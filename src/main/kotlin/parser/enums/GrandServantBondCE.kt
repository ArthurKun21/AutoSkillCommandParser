package io.arthurkun.parser.enums

enum class GrandServantBondCE {
    SKIP,
    ANY,
    BOND,
    CHARGE,
    ;

    val isUsed: Boolean
        get() = this != SKIP
}
