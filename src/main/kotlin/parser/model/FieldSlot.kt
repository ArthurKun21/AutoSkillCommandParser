package io.arthurkun.parser.model

sealed class FieldSlot(
	val position: Int,
) {
	data object A : FieldSlot(1)

	data object B : FieldSlot(2)

	data object C : FieldSlot(3)

	companion object {
		val list by lazy { listOf(A, B, C) }
	}

	override fun toString() = "[$position]"
}

fun FieldSlot.skill1() = when (this) {
	FieldSlot.A -> SkillSource.Servant.AS1
	FieldSlot.B -> SkillSource.Servant.BS1
	FieldSlot.C -> SkillSource.Servant.CS1
}

fun FieldSlot.skill2() = when (this) {
	FieldSlot.A -> SkillSource.Servant.AS2
	FieldSlot.B -> SkillSource.Servant.BS2
	FieldSlot.C -> SkillSource.Servant.CS2
}

fun FieldSlot.skill3() = when (this) {
	FieldSlot.A -> SkillSource.Servant.AS3
	FieldSlot.B -> SkillSource.Servant.BS3
	FieldSlot.C -> SkillSource.Servant.CS3
}

fun FieldSlot.skills() = listOf(skill1(), skill2(), skill3())
