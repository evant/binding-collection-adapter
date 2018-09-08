@file:Suppress("NOTHING_TO_INLINE")

package me.tatarka.bindingcollectionadapter2

import androidx.annotation.LayoutRes
import me.tatarka.bindingcollectionadapter2.itembindings.OnItemBindClass

/**
 * Maps the given type to the given id and layout.
 *
 * @see OnItemBindClass.map
 */
inline fun <reified T> OnItemBindClass<in T>.map(variableId: Int, @LayoutRes layoutRes: Int) {
    map(T::class.java, variableId, layoutRes)
}

/**
 * Maps the given type to the given callback.
 *
 * @see OnItemBindClass.map
 */
inline fun <reified T> OnItemBindClass<in T>.map(
    noinline onItemBind: (
        @ParameterName("itemBinding") ItemBinding<in T>,
        @ParameterName("position") Int,
        @ParameterName("item") T
    ) -> Unit
) {
    map(T::class.java) { itemBinding, position, item ->
        onItemBind(
            itemBinding as ItemBinding<in T>,
            position,
            item
        )
    }
}
