package app.e_20.core.logic.typedId.impl

import app.e_20.core.logic.typedId.Id
import app.e_20.core.logic.typedId.IdGenerator
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single
import java.util.*
import kotlin.reflect.KClass


/**
 * Generator of [IxId] based on [UUID].
 */
@Single(createdAtStart = true)
@Named("uuid")
class IxIdGenerator : IdGenerator {

    override val idClass: KClass<out Id<*>> = IxId::class

    override val wrappedIdClass: KClass<out Any> = UUID::class

    override fun <T> generateNewId(): Id<T> = IxId(UUID.randomUUID())
}