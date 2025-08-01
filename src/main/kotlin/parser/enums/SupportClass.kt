package io.arthurkun.parser.enums

enum class SupportClass {
    None,
    All,
    Saber,
    Archer,
    Lancer,
    Rider,
    Caster,
    Assassin,
    Berserker,
    Extra,
    Mix,
    ;

    companion object {
        val validEntries
            get() = entries.filterNot { it == None }
    }
}

val SupportClass.canAlsoCheckAll
    get() =
        this !in listOf(SupportClass.None, SupportClass.All, SupportClass.Mix)
