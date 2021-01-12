package uz.leeway.android.lib.lollipinx.managers

import android.content.Context
import androidx.annotation.StringRes
import uz.leeway.android.lib.lollipinx.R

enum class PinCodeState(val code: Int, @StringRes val resId: Int) {

    /**
     * DISABLE_PINLOCK type, uses to disable the system by asking the current password
     */
    DISABLE(1, R.string.pin_lock_step_disable),

    /**
     * ENABLE_PINLOCK type, uses at firt to define the password
     */
    CREATE(2, R.string.pin_lock_step_create),

    /**
     * CHANGE_PIN type, uses to change the current password
     */
    CHANGE(3, R.string.pin_lock_step_change),

    /**
     * UNLOCK_PIN type, uses to ask the password to the user, in order to unlock the app
     */
    UNLOCK(4, R.string.pin_lock_step_unlock),

    /**
     * CONFIRM_PIN type, used to confirm the new password
     */
    CONFIRM(5, R.string.pin_lock_step_confirm);

    fun canBackPrevious() = this == CHANGE || this == DISABLE

    fun getResName(context: Context, pinLength: Int): String = context.getString(resId, pinLength)

    companion object {
        fun getByCode(code: Int): PinCodeState = values().findLast { it.code == code } ?: DISABLE
    }

}