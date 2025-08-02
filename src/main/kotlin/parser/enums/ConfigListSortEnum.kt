package io.arthurkun.parser.enums

enum class ConfigListSortEnum {
    SORT_BY_NAME,
    SORT_BY_USAGE_COUNT,
    SORT_BY_LAST_USAGE_TIME,
    ;

    fun next(): ConfigListSortEnum {
        val values = entries.toTypedArray()
        val nextOrdinal = (ordinal + 1) % values.size
        return values[nextOrdinal]
    }
}
