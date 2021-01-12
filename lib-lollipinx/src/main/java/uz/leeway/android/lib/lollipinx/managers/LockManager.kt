package uz.leeway.android.lib.lollipinx.managers

import android.app.Activity
import android.content.Context
import uz.leeway.android.lib.lollipinx.BaseAbstractLockActivity

class LockManager private constructor(context: Context) {

    private val prefUtils = PrefUtils(context)
    private val ignoredSet = mutableSetOf<String>()

    fun refreshActiveMillis() = prefUtils.setLastActiveMillis()

    /**
     * Add an ignored activity to the {@link HashSet}
     */
    fun addIgnoredActivity(cls: Class<out Activity>) {
        this.ignoredSet.add(cls.name)
    }

    /**
     * Remove an ignored activity to the {@link HashSet}
     */
    fun removeIgnoredActivity(cls: Class<out Activity>) {
        this.ignoredSet.remove(cls.name)
    }

    /**
     * Set the timeout used in {@link #shouldLockSceen(Activity)}
     */
    fun setTimeout(millis: Long) = prefUtils.setTimeout(millis)

    /**
     * Set the passcode (store his SHA1 into {@link android.content.SharedPreferences})
     */
    fun setPassCode(pin: String) = prefUtils.setPassCode(HashUtils.sha256(pin))

    /**
     * Check the pass code by comparing his SHA256 into {@link android.content.SharedPreferences}
     */
    fun checkPassCode(pin: String): Boolean {
        val stored = if (prefUtils.containsPassCode()) prefUtils.getPassCode() else ""
        return stored.equals(HashUtils.sha256(pin), true)
    }

    /**
     * Set the pass code (store his SHA256 into {@link android.content.SharedPreferences})
     * true = enableAppLock
     * false = disableAppLock
     */
    fun setPassCode(pin: String?): Boolean {
        return if (pin == null) {
            prefUtils.removePassCode()
            false
        } else {
            prefUtils.setPassCode(HashUtils.sha256(pin))
            true
        }
    }

    fun setPinChallengeCancelled(cancelled: Boolean) = prefUtils.setPinChallengeCancelled(cancelled)

    /**
     * Check if an activity must be ignored and then don't call the LifeCycleInterface
     */
    private fun isIgnoredActivity(activity: Activity): Boolean =
        ignoredSet.contains(activity.javaClass.name)


    private fun shouldLockScreen(activity: Activity): Boolean {
        if (activity !is BaseAbstractLockActivity<*>) return false

        /* Previously backed out of pin screen */
        if (prefUtils.getPinChallengeCancelled()) return true

        /* Already unlock */
        if (activity.getPinCodeState() == PinCodeState.UNLOCK) return false

        /* No pass code set */
        if (!prefUtils.containsPassCode()) return false

        /* No enough timeout */
        val lastMillis = prefUtils.getLastActiveMillis()
        val passed = System.currentTimeMillis() - lastMillis
        val timeout = prefUtils.getTimeout()
        return !(lastMillis > 0 && passed <= timeout)
    }

    fun onEventUserInteraction(activity: Activity) {
        if (activity !is BaseAbstractLockActivity<*>) return
        if (prefUtils.getOnlyBackgroundTimeout() && !shouldLockScreen(activity)) {
            refreshActiveMillis()
        }
    }

    fun onEventResumed(activity: Activity) = refreshLastActiveMillis(activity)

    fun onEventPaused(activity: Activity) = refreshLastActiveMillis(activity)

    private fun refreshLastActiveMillis(activity: Activity) {
        if (activity !is BaseAbstractLockActivity<*>) return
        if (isIgnoredActivity(activity)) return
        if (!shouldLockScreen(activity)) {
            refreshActiveMillis()
        }
    }

    companion object {

        @Volatile
        var INSTANCE: LockManager? = null

        @JvmStatic
        fun getInstance(context: Context): LockManager {
            return INSTANCE ?: synchronized(this) {
                val instance = LockManager(context)
                INSTANCE = instance
                //return instance
                instance
            }
        }
    }

}