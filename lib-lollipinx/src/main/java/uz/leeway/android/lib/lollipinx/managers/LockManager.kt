package uz.leeway.android.lib.lollipinx.managers

import android.app.Activity
import android.content.Context
import android.content.Intent
import uz.leeway.android.lib.lollipinx.AbstractLockActivity
import uz.leeway.android.lib.lollipinx.CustomPinCodeActivity

class LockManager private constructor(context: Context) {

    private val _prefUtils = PrefUtils(context)
    private val _ignoredSet = mutableSetOf<String>()
    private var activityCls: Class<out AbstractLockActivity<*>> = CustomPinCodeActivity::class.java


    fun isEnabledLock() = _prefUtils.getIsEnabledAppLock()

    fun enableAppLock() = _prefUtils.setIsEnabledAppLock(true)

    fun disableAppLock() = _prefUtils.setIsEnabledAppLock(false)

    fun refreshActiveMillis() = _prefUtils.setLastActiveMillis()

    fun setCustomLockActivity(cls: Class<out AbstractLockActivity<*>>) {
        this.activityCls = cls
    }

    /**
     * Add an ignored activity to the {@link HashSet}
     */
    fun addIgnoredActivity(cls: Class<out Activity>) {
        this._ignoredSet.add(cls.name)
    }

    /**
     * Remove an ignored activity to the {@link HashSet}
     */
    fun removeIgnoredActivity(cls: Class<out Activity>) {
        this._ignoredSet.remove(cls.name)
    }

    /**
     * Set the timeout used in {@link #shouldLockSceen(Activity)}
     */
    fun setTimeout(millis: Long) = _prefUtils.setTimeout(millis)

    /**
     * Set the passcode (store his SHA1 into {@link android.content.SharedPreferences})
     */
    fun setPassCode(pin: String) = _prefUtils.setPassCode(HashUtils.sha256(pin))

    /**
     * Check the pass code by comparing his SHA256 into {@link android.content.SharedPreferences}
     */
    fun checkPassCode(pin: String): Boolean {
        val stored = if (_prefUtils.containsPassCode()) _prefUtils.getPassCode() else ""
        return stored.equals(HashUtils.sha256(pin), true)
    }

    /**
     * Set the pass code (store his SHA256 into {@link android.content.SharedPreferences})
     * true = enableAppLock
     * false = disableAppLock
     */
    fun setPassCode(pin: String?): Boolean {
        return if (pin == null) {
            _prefUtils.removePassCode()
            false
        } else {
            _prefUtils.setPassCode(HashUtils.sha256(pin))
            true
        }
    }

    fun isPassCodeSet() = _prefUtils.containsPassCode()

    fun setPinChallengeCancelled(cancelled: Boolean) =
        _prefUtils.setPinChallengeCancelled(cancelled)

    /**
     * Check if an activity must be ignored and then don't call the LifeCycleInterface
     */
    private fun isIgnoredActivity(activity: Activity): Boolean =
        _ignoredSet.contains(activity.javaClass.name)


    private fun shouldLockScreen(activity: Activity): Boolean {
        /* Previously backed out of pin screen */
        if (_prefUtils.getPinChallengeCancelled()) return true

        /* Already unlock */
        if (activity is AbstractLockActivity<*>) {
            if (activity.getPinCodeState() == PinCodeState.UNLOCK) return false
        }

        /* No pass code set */
        if (!_prefUtils.containsPassCode()) return false

        /* No enough timeout */
        val lastMillis = _prefUtils.getLastActiveMillis()
        val passed = System.currentTimeMillis() - lastMillis
        val timeout = _prefUtils.getTimeout()
        return !(lastMillis > 0 && passed <= timeout)
    }

    fun onEventResumed(activity: Activity) {
        if (isIgnoredActivity(activity)) return

        if (shouldLockScreen(activity)) {
            val intent = Intent(activity.applicationContext, activityCls).apply {
                putExtra(AppLockConst.Lock.EXTRA_STATE_CODE, PinCodeState.UNLOCK.code)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            activity.application.startActivity(intent)
        }

        if (!shouldLockScreen(activity) && activity !is AbstractLockActivity<*>) {
            refreshActiveMillis()
        }
    }

    fun onEventUserInteraction(activity: Activity) {
        if (_prefUtils.getOnlyBackgroundTimeout() && !shouldLockScreen(activity) && activity !is AbstractLockActivity<*>) {
            refreshActiveMillis()
        }
    }

    fun onEventPaused(activity: Activity) {
        if (isIgnoredActivity(activity)) return
        if (!shouldLockScreen(activity) && activity !is AbstractLockActivity<*>) {
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