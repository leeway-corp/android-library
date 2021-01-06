@file:Suppress("RedundantVisibilityModifier", "unused")
@file:JvmName("ReflectionDialogFragmentViewBindings")

package uz.leeway.android.lib.vdbpd

import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.RestrictTo
import androidx.annotation.RestrictTo.Scope.LIBRARY
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import uz.leeway.android.lib.vdbpd.internal.ViewDataBindingCache

/**
 * Create new [ViewDataBinding] associated with the [DialogFragment]'s view
 *
 * @param T Class of expected [ViewDataBinding] result class
 * @param viewBindingRootId Id of the root view from your custom view
 */
@JvmName("viewBindingDialogFragment")
public inline fun <reified T : ViewDataBinding> DialogFragment.dialogViewDataBinding(@IdRes viewBindingRootId: Int) =
    dialogViewDataBinding(T::class.java, viewBindingRootId)

/**
 * Create new [ViewDataBinding] associated with the [DialogFragment]'s view
 *
 * @param viewBindingClass Class of expected [ViewDataBinding] result class
 * @param viewBindingRootId Id of the root view from your custom view
 */
@JvmName("viewBindingDialogFragment")
public fun <T : ViewDataBinding> DialogFragment.dialogViewDataBinding(
    viewBindingClass: Class<T>,
    @IdRes viewBindingRootId: Int
): ViewDataBindingProperty<DialogFragment, T> {
    return dialogViewDataBinding { dialogFragment ->
        ViewDataBindingCache.getBind(viewBindingClass).bind(dialogFragment.getRootView(viewBindingRootId))
    }
}

@RestrictTo(LIBRARY)
private fun DialogFragment.getRootView(viewBindingRootId: Int): View {
    val dialog = checkNotNull(dialog) { "Dialog hasn't been created yet" }
    val window = checkNotNull(dialog.window) { "Dialog has no window" }
    return with(window.decorView) { if (viewBindingRootId != 0) findViewById(viewBindingRootId) else this }
}
