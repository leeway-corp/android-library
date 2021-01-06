@file:Suppress("RedundantVisibilityModifier", "unused")
@file:JvmName("FragmentViewBindings")

package uz.leeway.android.lib.vdbpd

import android.view.View
import androidx.annotation.IdRes
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

private class FragmentViewDataBindingProperty<F : Fragment, T : ViewDataBinding>(
    viewBinder: (F) -> T
) : LifecycleViewDataBindingProperty<F, T>(viewBinder) {

    override fun getLifecycleOwner(thisRef: F) = thisRef.viewLifecycleOwner
}

/**
 * Create new [ViewDataBinding] associated with the [Fragment]
 */
@JvmName("viewBindingFragment")
public fun <F : Fragment, T : ViewDataBinding> Fragment.viewDataBinding(viewBinder: (F) -> T): ViewDataBindingProperty<F, T> {
    return FragmentViewDataBindingProperty(viewBinder)
}

/**
 * Create new [ViewDataBinding] associated with the [Fragment]
 *
 * @param vbFactory Function that create new instance of [ViewDataBinding]. `MyViewBinding::bind` can be used
 * @param viewProvider Provide a [View] from the Fragment. By default call [Fragment.requireView]
 */
@JvmName("viewBindingFragment")
public inline fun <F : Fragment, T : ViewDataBinding> Fragment.viewDataBinding(
    crossinline vbFactory: (View) -> T,
    crossinline viewProvider: (F) -> View = Fragment::requireView
): ViewDataBindingProperty<F, T> {
    return viewDataBinding { fragment: F -> vbFactory(viewProvider(fragment)) }
}

/**
 * Create new [ViewDataBinding] associated with the [Fragment]
 *
 * @param vbFactory Function that create new instance of [ViewDataBinding]. `MyViewBinding::bind` can be used
 * @param viewBindingRootId Root view's id that will be used as root for the view binding
 */
@JvmName("viewBindingFragment")
public inline fun <T : ViewDataBinding> Fragment.viewDataBinding(
    crossinline vbFactory: (View) -> T,
    @IdRes viewBindingRootId: Int
): ViewDataBindingProperty<Fragment, T> {
    return viewDataBinding(vbFactory) { fragment: Fragment -> fragment.requireView().findViewById(viewBindingRootId) }
}
