package uz.leeway.android.lib.lollipinx.managers

enum class PinCodeState(val code: Int) {

    DISABLE(1),
    CREATE(2),
    CHANGE(3),
    UNLOCK(4),
    CONFIRM(5)

}