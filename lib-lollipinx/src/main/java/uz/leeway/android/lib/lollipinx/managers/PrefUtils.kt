package uz.leeway.android.lib.lollipinx.managers

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.preference.PreferenceManager

class PrefUtils(private val context: Context) {

    private val pref: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(context)
    }

    fun getIsEnabledAppLock(): Boolean =
        pref.getBoolean(AppLockConst.PrefKeys.KEY_IS_ENABLED_APP_LOCK, true)

    fun setIsEnabledAppLock(isEnable: Boolean) =
        pref.edit { putBoolean(AppLockConst.PrefKeys.KEY_IS_ENABLED_APP_LOCK, isEnable) }

    fun getTimeout(): Long = pref.getLong(
        AppLockConst.PrefKeys.KEY_TIMEOUT_MILLIS,
        AppLockConst.PrefKeys.DEFAULT_TIMEOUT
    )

    fun setTimeout(timeout: Long) =
        pref.edit { putLong(AppLockConst.PrefKeys.KEY_TIMEOUT_MILLIS, timeout) }

    fun getLogoId(): Int = pref.getInt(
        AppLockConst.PrefKeys.KEY_LOGO_ID,
        AppLockConst.PrefKeys.LOGO_ID_NONE
    )

    fun setLogoId(logoId: Int) = pref.edit { putInt(AppLockConst.PrefKeys.KEY_LOGO_ID, logoId) }

    fun getPinChallengeCancelled(): Boolean =
        pref.getBoolean(AppLockConst.PrefKeys.KEY_PIN_CHALLENGE_CANCELLED, false)

    fun setPinChallengeCancelled(backedOut: Boolean) =
        pref.edit { putBoolean(AppLockConst.PrefKeys.KEY_PIN_CHALLENGE_CANCELLED, backedOut) }

    fun getShouldShowForgot(appLockType: Int): Boolean =
        pref.getBoolean(AppLockConst.PrefKeys.KEY_SHOW_FORGOT, true)
                && appLockType != AppLockConst.Pin.ENABLE
                && appLockType != AppLockConst.Pin.CONFIRM

    fun setShouldShowForgot(showForgot: Boolean) =
        pref.edit { putBoolean(AppLockConst.PrefKeys.KEY_SHOW_FORGOT, showForgot) }

    fun getOnlyBackgroundTimeout(): Boolean =
        pref.getBoolean(AppLockConst.PrefKeys.KEY_ONLY_BACKGROUND_TIMEOUT, false)

    fun setOnlyBackgroundTimeout(background: Boolean) =
        pref.edit { putBoolean(AppLockConst.PrefKeys.KEY_ONLY_BACKGROUND_TIMEOUT, background) }

    fun getBiometricEnabled(): Boolean =
        pref.getBoolean(AppLockConst.PrefKeys.KEY_BIOMETRIC_ENABLED, true)

    fun setBiometricEnabled(enabled: Boolean) =
        pref.edit { putBoolean(AppLockConst.PrefKeys.KEY_BIOMETRIC_ENABLED, enabled) }

    fun getLastActiveMillis(): Long = pref.getLong(AppLockConst.PrefKeys.KEY_LAST_ACTIVE_MILLIS, 0)

    fun setLastActiveMillis() = pref.edit {
        putLong(
            AppLockConst.PrefKeys.KEY_LAST_ACTIVE_MILLIS,
            System.currentTimeMillis()
        )
    }

    fun getPassCode(): String =
        pref.getString(AppLockConst.PrefKeys.KEY_PASS_CODE, "") ?: ""

    fun setPassCode(pin: String) =
        pref.edit { putString(AppLockConst.PrefKeys.KEY_PASS_CODE, pin) }

    fun containsPassCode(): Boolean = pref.getString(AppLockConst.PrefKeys.KEY_PASS_CODE, "").isNullOrEmpty().not()

    fun removePassCode() = pref.edit { remove(AppLockConst.PrefKeys.KEY_PASS_CODE) }


    fun clear() = pref.edit {
        remove(AppLockConst.PrefKeys.KEY_TIMEOUT_MILLIS)
        remove(AppLockConst.PrefKeys.KEY_LOGO_ID)
        remove(AppLockConst.PrefKeys.KEY_SHOW_FORGOT)
        remove(AppLockConst.PrefKeys.KEY_ONLY_BACKGROUND_TIMEOUT)
        remove(AppLockConst.PrefKeys.KEY_LAST_ACTIVE_MILLIS)
        remove(AppLockConst.PrefKeys.KEY_BIOMETRIC_ENABLED)
        remove(AppLockConst.PrefKeys.KEY_PASS_CODE)
    }
}