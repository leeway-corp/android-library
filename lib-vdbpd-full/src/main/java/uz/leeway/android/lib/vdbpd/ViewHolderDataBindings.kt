@file:Suppress("RedundantVisibilityModifier", "unused")
@file:JvmName("ReflectionViewHolderBindings")

package uz.leeway.android.lib.vdbpd

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import uz.leeway.android.lib.vdbpd.internal.ViewDataBindingCache

/**
 * Create new [ViewDataBinding] associated with the [ViewHolder]
 *
 * @param T Class of expected [ViewDataBinding] result class
 */
@JvmName("viewBindingFragment")
public inline fun <reified T : ViewDataBinding> ViewHolder.viewDataBinding() = viewDataBinding(T::class.java)

/**
 * Create new [ViewDataBinding] associated with the [ViewHolder]
 *
 * @param viewBindingClass Class of expected [ViewDataBinding] result class
 */
@JvmName("viewBindingFragment")
public fun <T : ViewDataBinding> ViewHolder.viewDataBinding(
    viewBindingClass: Class<T>,
): ViewDataBindingProperty<ViewHolder, T> {
    return viewDataBinding { ViewDataBindingCache.getBind(viewBindingClass).bind(itemView) }
}
