@file:Suppress("unused")

package uz.leeway.android.lib.vdbpd.internal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RestrictTo
import androidx.annotation.RestrictTo.Scope.LIBRARY
import androidx.databinding.ViewDataBinding
import java.lang.reflect.Method

object ViewDataBindingCache {

    private val inflateCache = mutableMapOf<Class<out ViewDataBinding>, InflateViewDataBinding<ViewDataBinding>>()
    private val bindCache = mutableMapOf<Class<out ViewDataBinding>, BindViewDataBinding<ViewDataBinding>>()

    @Suppress("UNCHECKED_CAST")
    @RestrictTo(LIBRARY)
    internal fun <T : ViewDataBinding> getInflateWithLayoutInflater(viewBindingClass: Class<T>): InflateViewDataBinding<T> {
        return inflateCache.getOrPut(viewBindingClass) { InflateViewBinding(viewBindingClass) } as InflateViewDataBinding<T>
    }

    @Suppress("UNCHECKED_CAST")
    @RestrictTo(LIBRARY)
    internal fun <T : ViewDataBinding> getBind(viewBindingClass: Class<T>): BindViewDataBinding<T> {
        return bindCache.getOrPut(viewBindingClass) { BindViewDataBinding(viewBindingClass) } as BindViewDataBinding<T>
    }

    /**
     * Reset all cached data
     */
    fun clear() {
        inflateCache.clear()
        bindCache.clear()
    }
}

/**
 * Wrapper of ViewDataBinding.inflate(LayoutInflater, ViewGroup, Boolean)
 */
@RestrictTo(LIBRARY)
internal abstract class InflateViewDataBinding<out VB : ViewDataBinding>(
    private val inflateViewBinding: Method
) {

    @Suppress("UNCHECKED_CAST")
    abstract fun inflate(layoutInflater: LayoutInflater, parent: ViewGroup?, attachToParent: Boolean): VB
}

@RestrictTo(LIBRARY)
@Suppress("FunctionName")
internal fun <VB : ViewDataBinding> InflateViewBinding(viewBindingClass: Class<VB>): InflateViewDataBinding<VB> {
    try {
        val method = viewBindingClass.getMethod(
            "inflate", LayoutInflater::class.java, ViewGroup::class.java, Boolean::class.java
        )
        return FullInflateViewDataBinding(method)
    } catch (e: NoSuchMethodException) {
        val method = viewBindingClass.getMethod("inflate", LayoutInflater::class.java, ViewGroup::class.java)
        return MergeInflateViewDataBinding(method)
    }
}

@RestrictTo(LIBRARY)
internal class FullInflateViewDataBinding<out VB : ViewDataBinding>(
    private val inflateViewBinding: Method
) : InflateViewDataBinding<VB>(inflateViewBinding) {

    @Suppress("UNCHECKED_CAST")
    override fun inflate(layoutInflater: LayoutInflater, parent: ViewGroup?, attachToParent: Boolean): VB {
        return inflateViewBinding(null, layoutInflater, parent, attachToParent) as VB
    }
}


@RestrictTo(LIBRARY)
internal class MergeInflateViewDataBinding<out VB : ViewDataBinding>(
    private val inflateViewBinding: Method
) : InflateViewDataBinding<VB>(inflateViewBinding) {

    @Suppress("UNCHECKED_CAST")
    override fun inflate(layoutInflater: LayoutInflater, parent: ViewGroup?, attachToParent: Boolean): VB {
        require(attachToParent) {
            "${InflateViewDataBinding::class.java.simpleName} supports inflate only with attachToParent=true"
        }
        return inflateViewBinding(null, layoutInflater, parent) as VB
    }
}

/**
 * Wrapper of ViewDataBinding.bind(View)
 */
@RestrictTo(LIBRARY)
internal class BindViewDataBinding<out VB : ViewDataBinding>(viewBindingClass: Class<VB>) {

    private val bindViewBinding = viewBindingClass.getMethod("bind", View::class.java)

    @Suppress("UNCHECKED_CAST")
    fun bind(view: View): VB {
        return bindViewBinding(null, view) as VB
    }
}
