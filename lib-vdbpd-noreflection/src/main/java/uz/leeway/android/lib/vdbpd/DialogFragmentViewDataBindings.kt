@file:Suppress("RedundantVisibilityModifier", "unused")
@file:JvmName("DialogFragmentViewBindings")

package uz.leeway.android.lib.vdbpd

import android.view.View
import androidx.annotation.IdRes
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.LifecycleOwner

private class DialogFragmentViewDataBindingProperty<F : DialogFragment, T : ViewDataBinding>(
    viewBinder: (F) -> T
) : LifecycleViewDataBindingProperty<F, T>(viewBinder) {

    override fun getLifecycleOwner(thisRef: F): LifecycleOwner {
        return if (thisRef.view != null) thisRef.viewLifecycleOwner else thisRef
    }
}

/**
 * Create new [ViewDataBinding] associated with the [DialogFragment]
 */
@JvmName("viewBindingDialogFragment")
public fun <F : DialogFragment, T : ViewDataBinding> DialogFragment.dialogViewDataBinding(
    viewBinder: (F) -> T
): ViewDataBindingProperty<F, T> {
    return DialogFragmentViewDataBindingProperty(viewBinder)
}

/**
 * Create new [ViewDataBinding] associated with the [DialogFragment]
 *
 * @param vbFactory Function that create new instance of [ViewDataBinding]. `MyViewBinding::bind` can be used
 */
@JvmName("viewBindingDialogFragment")
public inline fun <F : DialogFragment, T : ViewDataBinding> DialogFragment.dialogViewDataBinding(
    crossinline vbFactory: (View) -> T,
    crossinline viewProvider: (F) -> View
): ViewDataBindingProperty<F, T> {
    return dialogViewDataBinding { fragment -> vbFactory(viewProvider(fragment)) }
}

/**
 * Create new [ViewDataBinding] associated with the [DialogFragment][this]
 *
 * @param vbFactory Function that create new instance of [ViewDataBinding]. `MyViewBinding::bind` can be used
 * @param viewBindingRootId Id of the root view from your custom view
 */
@Suppress("unused")
@JvmName("viewBindingDialogFragment")
public inline fun <T : ViewDataBinding> DialogFragment.dialogViewDataBinding(
    crossinline vbFactory: (View) -> T,
    @IdRes viewBindingRootId: Int
): ViewDataBindingProperty<DialogFragment, T> {
    return dialogViewDataBinding(vbFactory) { fragment: DialogFragment ->
        fragment.dialog!!.window!!.decorView.findViewById(viewBindingRootId)
    }
}
