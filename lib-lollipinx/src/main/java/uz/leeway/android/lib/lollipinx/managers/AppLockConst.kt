package uz.leeway.android.lib.lollipinx.managers

object AppLockConst {
    private const val PACKAGE_NAME = "uz.leeway.android.lib.lollipinx"

    object Actions {
        const val CANCEL = "$PACKAGE_NAME.actionCancelled"
    }

    object Lock {
        const val DEFAULT_PIN_LENGTH = 4
        const val EXTRA_STATE_CODE = "extra_state_code"
    }

    object Pin {
        const val ENABLE = 0
        const val DISABLE = 1
        const val CHANGE = 2
        const val CONFIRM = 3
        const val UNLOCK = 4
    }


    object PrefKeys {
        const val DEFAULT_TIMEOUT: Long = 1000 * 10 // 10sec
        const val LOGO_ID_NONE: Int = -1


        const val KEY_TIMEOUT_MILLIS = "KEY_TIMEOUT_MILLIS"
        const val KEY_LOGO_ID = "KEY_LOGO_ID"
        const val KEY_SHOW_FORGOT = "KEY_SHOW_FORGOT"
        const val KEY_PIN_CHALLENGE_CANCELLED = "KEY_PIN_CHALLENGE_CANCELLED"
        const val KEY_ONLY_BACKGROUND_TIMEOUT = "KEY_ONLY_BACKGROUND_TIMEOUT"
        const val KEY_LAST_ACTIVE_MILLIS = "KEY_LAST_ACTIVE_MILLIS"
        const val KEY_BIOMETRIC_ENABLED = "KEY_BIOMETRIC_ENABLED"
        const val KEY_PASS_CODE = "KEY_PASS_CODE"
    }

}