@file:Suppress("RedundantVisibilityModifier", "unused")
@file:JvmName("ReflectionViewGroupBindings")

package uz.leeway.android.lib.vdbpd

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import uz.leeway.android.lib.vdbpd.internal.ViewDataBindingCache

/**
 * Create new [ViewDataBinding] associated with the [ViewGroup]
 *
 * @param T Class of expected [ViewDataBinding] result class
 * @param createMethod Way of how create [ViewDataBinding]
 */
@JvmName("viewBindingFragment")
public inline fun <reified T : ViewDataBinding> ViewGroup.viewDataBinding(createMethod: CreateMethod = CreateMethod.BIND) =
    viewDataBinding(T::class.java, createMethod)

/**
 * Create new [ViewDataBinding] associated with the [ViewGroup]
 *
 * @param viewBindingClass Class of expected [ViewDataBinding] result class
 * @param createMethod Way of how create [ViewDataBinding]
 */
@JvmName("viewBindingFragment")
public fun <T : ViewDataBinding> ViewGroup.viewDataBinding(
    viewBindingClass: Class<T>,
    createMethod: CreateMethod = CreateMethod.BIND,
): ViewDataBindingProperty<ViewGroup, T> = when (createMethod) {
    CreateMethod.BIND -> viewDataBinding { viewGroup -> ViewDataBindingCache.getBind(viewBindingClass).bind(viewGroup) }
    CreateMethod.INFLATE -> viewDataBinding(viewBindingClass, attachToRoot = true)
}

/**
 * Inflate new [ViewDataBinding] with the [ViewGroup][this] as parent
 */
@JvmName("viewBindingFragment")
public inline fun <reified T : ViewDataBinding> ViewGroup.viewDataBinding(attachToRoot: Boolean) =
    viewDataBinding(T::class.java, attachToRoot)

/**
 * Inflate new [ViewDataBinding] with the [ViewGroup][this] as parent
 */
@JvmName("viewBindingFragment")
public fun <T : ViewDataBinding> ViewGroup.viewDataBinding(
    viewBindingClass: Class<T>,
    attachToRoot: Boolean
): ViewDataBindingProperty<ViewGroup, T> {
    return viewDataBinding { viewGroup ->
        ViewDataBindingCache.getInflateWithLayoutInflater(viewBindingClass)
            .inflate(LayoutInflater.from(context), viewGroup, attachToRoot)
    }
}
