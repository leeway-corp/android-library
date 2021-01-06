@file:Suppress("RedundantVisibilityModifier", "unused")
@file:JvmName("ActivityViewBindings")

package uz.leeway.android.lib.vdbpd

import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.RestrictTo
import androidx.annotation.RestrictTo.Scope.LIBRARY
import androidx.core.app.ComponentActivity
import androidx.databinding.ViewDataBinding
import uz.leeway.android.lib.vdbpd.internal.DefaultActivityViewDataBingingRootProvider

@RestrictTo(LIBRARY)
private class ActivityViewDataDataBindingProperty<A : ComponentActivity, T : ViewDataBinding>(
    viewBinder: (A) -> T
) : LifecycleViewDataBindingProperty<A, T>(viewBinder) {

    override fun getLifecycleOwner(thisRef: A) = thisRef
}

/**
 * Create new [ViewDataBinding] associated with the [Activity][ComponentActivity] and allow customize how
 * a [View] will be bounded to the view binding.
 */
@JvmName("viewBindingActivity")
public fun <A : ComponentActivity, T : ViewDataBinding> ComponentActivity.viewDataBinding(
    viewBinder: (A) -> T
): ViewDataBindingProperty<A, T> {
    return ActivityViewDataDataBindingProperty(viewBinder)
}

/**
 * Create new [ViewDataBinding] associated with the [Activity][ComponentActivity] and allow customize how
 * a [View] will be bounded to the view binding.
 */
@JvmName("viewBindingActivity")
public inline fun <A : ComponentActivity, T : ViewDataBinding> ComponentActivity.viewDataBinding(
    crossinline vbFactory: (View) -> T,
    crossinline viewProvider: (A) -> View = DefaultActivityViewDataBingingRootProvider::findRootView
): ViewDataBindingProperty<A, T> {
    return viewDataBinding { activity -> vbFactory(viewProvider(activity)) }
}

/**
 * Create new [ViewDataBinding] associated with the [Activity][this] and allow customize how
 * a [View] will be bounded to the view binding.
 *
 * @param vbFactory Function that create new instance of [ViewDataBinding]. `MyViewBinding::bind` can be used
 * @param viewBindingRootId Root view's id that will be used as root for the view binding
 */
@Suppress("unused")
@JvmName("viewBindingActivity")
public inline fun <T : ViewDataBinding> ComponentActivity.viewDataBinding(
    crossinline vbFactory: (View) -> T,
    @IdRes viewBindingRootId: Int
): ViewDataBindingProperty<ComponentActivity, T> {
    return viewDataBinding { activity -> vbFactory(activity.findViewById(viewBindingRootId)) }
}
