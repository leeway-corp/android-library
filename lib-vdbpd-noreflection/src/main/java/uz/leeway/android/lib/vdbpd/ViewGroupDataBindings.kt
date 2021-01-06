@file:Suppress("unused")

package uz.leeway.android.lib.vdbpd

import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.core.view.ViewCompat
import androidx.databinding.ViewDataBinding

/**
 * Create new [ViewDataBinding] associated with the [ViewGroup]
 *
 * @param vbFactory Function that create new instance of [ViewDataBinding]. `MyViewBinding::bind` can be used
 */
inline fun <T : ViewDataBinding> ViewGroup.viewDataBinding(
    crossinline vbFactory: (ViewGroup) -> T,
): ViewDataBindingProperty<ViewGroup, T> {
    return LazyViewDataBindingProperty { vbFactory(it) }
}

/**
 * Create new [ViewDataBinding] associated with the [ViewGroup]
 *
 * @param vbFactory Function that create new instance of [ViewDataBinding]. `MyViewBinding::bind` can be used
 * @param viewBindingRootId Root view's id that will be used as root for the view binding
 */
inline fun <T : ViewDataBinding> ViewGroup.viewDataBinding(
    crossinline vbFactory: (View) -> T,
    @IdRes viewBindingRootId: Int,
): ViewDataBindingProperty<ViewGroup, T> {
    return LazyViewDataBindingProperty { viewGroup: ViewGroup ->
        ViewCompat.requireViewById<View>(viewGroup, viewBindingRootId).let(vbFactory)
    }
}
