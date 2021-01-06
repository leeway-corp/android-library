package uz.leeway.android.lib.lollipinx.managers

object AppLockConst {
    private const val PACKAGE_NAME = "uz.leeway.android.lib.lollipinx"

    object Actions {
        const val CANCEL = "$PACKAGE_NAME.actionCancelled"
    }

    object Lock {
        const val DEFAULT_PIN_LENGTH = 4
        const val EXTRA_TYPE = "type"
    }

    object Pin {
        const val ENABLE = 0
        const val DISABLE = 1
        const val CHANGE = 2
        const val CONFIRM = 3
        const val UNLOCK = 4
    }


}