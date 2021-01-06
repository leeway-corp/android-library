@file:Suppress("RedundantVisibilityModifier", "unused")
@file:JvmName("ReflectionActivityViewBindings")

package uz.leeway.android.lib.vdbpd

import android.app.Activity
import android.view.View
import androidx.annotation.IdRes
import androidx.core.app.ActivityCompat
import androidx.core.app.ComponentActivity
import androidx.databinding.ViewDataBinding
import uz.leeway.android.lib.vdbpd.internal.DefaultActivityViewDataBingingRootProvider
import uz.leeway.android.lib.vdbpd.internal.ViewDataBindingCache

/**
 * Create new [ViewDataBinding] associated with the [Activity][ComponentActivity]
 *
 * @param T Class of expected [ViewDataBinding] result class
 * @param viewBindingRootId Root view's id that will be used as root for the view binding
 */
@JvmName("viewBindingActivity")
public inline fun <reified T : ViewDataBinding> ComponentActivity.viewDataBinding(@IdRes viewBindingRootId: Int) =
    viewDataBinding(T::class.java, viewBindingRootId)

/**
 * Create new [ViewDataBinding] associated with the [Activity][ComponentActivity]
 *
 * @param viewBindingClass Class of expected [ViewDataBinding] result class
 * @param viewBindingRootId Root view's id that will be used as root for the view binding
 */
@JvmName("viewBindingActivity")
public fun <T : ViewDataBinding> ComponentActivity.viewDataBinding(
    viewBindingClass: Class<T>,
    @IdRes viewBindingRootId: Int
): ViewDataBindingProperty<ComponentActivity, T> {
    return viewDataBinding { activity ->
        val rootView = ActivityCompat.requireViewById<View>(activity, viewBindingRootId)
        ViewDataBindingCache.getBind(viewBindingClass).bind(rootView)
    }
}

/**
 * Create new [ViewDataBinding] associated with the [Activity]
 *
 * @param viewBindingClass Class of expected [ViewDataBinding] result class
 * @param rootViewProvider Provider of root view for the [ViewDataBinding] from the [Activity][this]
 */
@JvmName("viewBindingActivity")
public fun <T : ViewDataBinding> ComponentActivity.viewDataBinding(
    viewBindingClass: Class<T>,
    rootViewProvider: (ComponentActivity) -> View
): ViewDataBindingProperty<ComponentActivity, T> {
    return viewDataBinding { activity ->
        ViewDataBindingCache.getBind(viewBindingClass).bind(rootViewProvider(activity))
    }
}

/**
 * Create new [ViewDataBinding] associated with the [Activity][ComponentActivity].
 * You need to set [ViewDataBinding.getRoot] as content view using [Activity.setContentView].
 *
 * @param T Class of expected [ViewDataBinding] result class
 */
@JvmName("inflateViewBindingActivity")
public inline fun <reified T : ViewDataBinding> ComponentActivity.viewDataBinding(
    createMethod: CreateMethod = CreateMethod.BIND
) = viewDataBinding(T::class.java, createMethod)

@JvmName("inflateViewBindingActivity")
public fun <T : ViewDataBinding> ComponentActivity.viewDataBinding(
    viewBindingClass: Class<T>,
    createMethod: CreateMethod = CreateMethod.BIND
): ViewDataBindingProperty<ComponentActivity, T> = when (createMethod) {
    CreateMethod.BIND -> viewDataBinding(
        viewBindingClass,
        DefaultActivityViewDataBingingRootProvider::findRootView
    )
    CreateMethod.INFLATE -> viewDataBinding {
        ViewDataBindingCache.getInflateWithLayoutInflater(viewBindingClass)
            .inflate(layoutInflater, null, false)
    }
}
