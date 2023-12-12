package app.e_20.core.logic.typedId

import app.e_20.core.logic.typedId.impl.IxId
import app.e_20.core.logic.typedId.impl.IxIntId
import java.util.*

fun <T> String.toIxId() = IxId<T>(this)
fun <T> Int.toIxIntId() = IxIntId<T>(this)
fun <T> newIxId() = IxId<T>(UUID.randomUUID())
fun <T> newIxIntId() = IxIntId<T>((1..100).random())