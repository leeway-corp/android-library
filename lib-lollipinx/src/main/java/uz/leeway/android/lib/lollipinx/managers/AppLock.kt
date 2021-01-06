package uz.leeway.android.lib.lollipinx.managers

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import uz.leeway.android.lib.lollipinx.PinCompatActivity

class AppLockObserver(val activity: PinCompatActivity) : LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onActivityResumed() {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onActivityPaused() {

    }


}