@file:Suppress("RedundantVisibilityModifier", "unused")
@file:JvmName("ReflectionFragmentViewBindings")

package uz.leeway.android.lib.vdbpd

import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import uz.leeway.android.lib.vdbpd.internal.ViewDataBindingCache

/**
 * Create new [ViewDataBinding] associated with the [Fragment]
 *
 * @param T Class of expected [ViewDataBinding] result class
 */
@JvmName("viewBindingFragment")
public inline fun <reified T : ViewDataBinding> Fragment.viewDataBinding(createMethod: CreateMethod = CreateMethod.BIND) =
    viewDataBinding(T::class.java, createMethod)

/**
 * Create new [ViewDataBinding] associated with the [Fragment]
 *
 * @param viewBindingClass Class of expected [ViewDataBinding] result class
 */
@JvmName("viewBindingFragment")
public fun <T : ViewDataBinding> Fragment.viewDataBinding(
    viewBindingClass: Class<T>,
    createMethod: CreateMethod = CreateMethod.BIND
): ViewDataBindingProperty<Fragment, T> = when (createMethod) {
    CreateMethod.BIND -> viewDataBinding {
        ViewDataBindingCache.getBind(viewBindingClass).bind(requireView())
    }
    CreateMethod.INFLATE -> viewDataBinding {
        ViewDataBindingCache.getInflateWithLayoutInflater(viewBindingClass).inflate(layoutInflater, null, false)
    }
}
