@file:Suppress("RedundantVisibilityModifier")

package uz.leeway.android.lib.vdbpd

import android.os.Handler
import android.os.Looper
import androidx.annotation.MainThread
import androidx.annotation.RestrictTo
import androidx.annotation.RestrictTo.Scope.LIBRARY_GROUP
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty


interface ViewDataBindingProperty<in R : Any, T : ViewDataBinding> : ReadOnlyProperty<R, T> {

    @MainThread
    fun clear()
}

@RestrictTo(LIBRARY_GROUP)
public open class LazyViewDataBindingProperty<in R : Any, T : ViewDataBinding>(
    protected val viewBinder: (R) -> T
) : ViewDataBindingProperty<R, T> {

    protected var viewBinding: T? = null

    @MainThread
    public override fun getValue(thisRef: R, property: KProperty<*>): T {
        return viewBinding ?: viewBinder(thisRef).also { viewBinding ->
            this.viewBinding = viewBinding
        }
    }

    @MainThread
    public override fun clear() {
        viewBinding = null
    }
}

@RestrictTo(LIBRARY_GROUP)
public abstract class LifecycleViewDataBindingProperty<in R : Any, T : ViewDataBinding>(
    private val viewBinder: (R) -> T
) : ViewDataBindingProperty<R, T> {

    private var viewBinding: T? = null
    private val lifecycleObserver = ClearOnDestroyLifecycleObserver()
    private var thisRef: R? = null

    protected abstract fun getLifecycleOwner(thisRef: R): LifecycleOwner

    @MainThread
    public override fun getValue(thisRef: R, property: KProperty<*>): T {
        viewBinding?.let { return it }

        this.thisRef = thisRef
        val lifecycle = getLifecycleOwner(thisRef).lifecycle
        val viewBinding = viewBinder(thisRef)
        if (lifecycle.currentState == Lifecycle.State.DESTROYED) {
            // We can access to ViewBinding after on destroy, but don't save to prevent memory leak
        } else {
            lifecycle.addObserver(lifecycleObserver)
            this.viewBinding = viewBinding
        }
        return viewBinding
    }

    @MainThread
    public override fun clear() {
        val thisRef = thisRef ?: return
        this.thisRef = null
        getLifecycleOwner(thisRef).lifecycle.removeObserver(lifecycleObserver)
        mainHandler.post { viewBinding = null }
    }

    private inner class ClearOnDestroyLifecycleObserver : DefaultLifecycleObserver {

        @MainThread
        override fun onDestroy(owner: LifecycleOwner): Unit = clear()
    }

    private companion object {

        private val mainHandler = Handler(Looper.getMainLooper())
    }
}
