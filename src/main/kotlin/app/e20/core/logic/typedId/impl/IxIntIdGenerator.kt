package app.e20.core.logic.typedId.impl

import app.e20.core.logic.typedId.Id
import app.e20.core.logic.typedId.IdGenerator
import com.google.errorprone.annotations.DoNotCall
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single
import kotlin.reflect.KClass

/**
 * Generator of [IxIntId] based on [Int].
 */
@Single(createdAtStart = true)
@Named("int")
class IxIntIdGenerator : IdGenerator {
    override val idClass: KClass<out Id<*>> = IxIntId::class

    override val wrappedIdClass: KClass<out Any> = Int::class

    @DoNotCall("This doesn't generate a safe int id, it always uses 0!")
    override fun <T> generateNewId(): Id<T> = IxIntId(0)
}