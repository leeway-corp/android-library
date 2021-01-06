package uz.leeway.android.lib.lollipinx.interfaces

import uz.leeway.android.lib.lollipinx.PinCompatActivity

interface LifeCycleInterface {

    fun onEventResumed(activity: PinCompatActivity)

    fun onEventUserInteraction(activity: PinCompatActivity)

    fun onEventPaused(activity: PinCompatActivity)
}